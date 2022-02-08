/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdmx.repository.service;

import com.google.common.collect.Collections2;
import java.awt.Dimension;
import java.io.IOException;
import java.sql.Connection;
import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.paukov.combinatorics3.Generator;
import sdmx.Registry;
import sdmx.Repository;
import sdmx.SdmxIO;
import sdmx.common.PayloadStructureType;
import sdmx.commonreferences.DataflowReference;
import sdmx.commonreferences.IDType;
import sdmx.commonreferences.NestedNCNameID;
import sdmx.commonreferences.Version;
import sdmx.data.ColumnMapper;
import sdmx.data.DataSet;
import sdmx.data.DataSetWriter;
import sdmx.data.DefaultParseDataCallbackHandler;
import sdmx.data.flat.FlatDataSet;
import sdmx.data.flat.FlatDataSetWriter;
import sdmx.data.flat.FlatObs;
import sdmx.exception.ParseException;
import sdmx.message.BaseHeaderType;
import sdmx.message.DataMessage;
import sdmx.message.DataQueryMessage;
import sdmx.message.DataStructure;
import sdmx.querykey.Query;
import sdmx.repository.entities.Dataflow;
import sdmx.repository.entities.DataflowComponent;
import sdmx.repository.util.DataflowComponentUtil;
import sdmx.repository.util.DataflowUtil;
import sdmx.repository.util.LanguageUtil;
import sdmx.structure.dataflow.DataflowType;
import sdmx.structure.datastructure.AttributeType;
import sdmx.structure.datastructure.DataStructureType;
import sdmx.structure.datastructure.DimensionType;
import sdmx.structure.datastructure.MeasureDimensionType;
import sdmx.structure.datastructure.PrimaryMeasure;
import sdmx.structure.datastructure.TimeDimensionType;
import sdmx.util.PostParseUtilities;
import sdmx.version.common.ParseDataCallbackHandler;
import sdmx.version.common.ParseParams;

/**
 *
 * @author James
 */
public class SingleTableDatabaseRepository {

    public static final EntityManagerFactory EMF = Persistence.createEntityManagerFactory("sdmxrepositoryPU");
    EntityManager em = EMF.createEntityManager();

    PoolingDataSource<Connection> pool;

    public SingleTableDatabaseRepository() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            ConnectionFactory connectionFactory = new DriverManagerConnectionFactory("jdbc:mysql://localhost:3306/repository", "root", "Possumfeb182");
            PoolableConnectionFactory poolableConnectionFactory;
            poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory, null);
            GenericObjectPool connectionPool = new GenericObjectPool(poolableConnectionFactory);
            pool = new PoolingDataSource(connectionPool);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SingleTableDatabaseRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        LanguageUtil.init(em);
    }

    public Connection getConnection() throws SQLException {
        return pool.getConnection();
    }

    public void returnConnection(Connection c) throws SQLException {
        if (!c.isClosed()) {
            c.close();
        }
    }

    public void deleteDataflow(DataflowType df) throws SQLException {
        if (!this.hasDataflow(df)) {
            return;
        }
        Dataflow d = DataflowUtil.findDataflow(em, df.getAgencyID().toString(), df.getId().toString(), df.getVersion().toString());
        Long id = d.getDataflow();
        em.getTransaction().begin();
        em.remove(d);
        em.getTransaction().commit();
        String drop = "drop table if exists flow_" + id + ";";
        Connection con = pool.getConnection();
        PreparedStatement pst = con.prepareStatement(drop);
        pst.executeUpdate();
        returnConnection(con);
    }

    public void createDataflow(Registry reg, DataflowType df) throws SQLException {
        if (this.hasDataflow(df)) {
            return;
        }
        em.getTransaction().begin();
        Dataflow d = DataflowUtil.createDatabaseDataflow(em, reg, df);
        em.persist(d);
        em.getTransaction().commit();
        String id = d.getDataflow().toString();
        DataStructureType struct = reg.find(df.getStructure());
        ArrayList<String> pks = new ArrayList<String>();
        String create = "create table if not exists flow_" + id + " (";
        for (int i = 0; i < struct.getDataStructureComponents().getDimensionList().size(); i++) {
            DimensionType dim = struct.getDataStructureComponents().getDimensionList().getDimension(i);
            create += "" + dim.getId().toString() + " varchar(30)";
            if (struct.getDataStructureComponents().getDimensionList().size() - 1 > i) {
                create += ",";
            }
            pks.add("`" + dim.getId().toString() + "`");
        }
        if (struct.getDataStructureComponents().getDimensionList().getTimeDimension() != null) {
            TimeDimensionType dim = struct.getDataStructureComponents().getDimensionList().getTimeDimension();
            create += ",";
            create += "" + dim.getId().toString() + " varchar(30)";
            pks.add("`" + dim.getId().toString() + "`");
        }
        if (struct.getDataStructureComponents().getDimensionList().getMeasureDimension() != null) {
            MeasureDimensionType dim = struct.getDataStructureComponents().getDimensionList().getMeasureDimension();
            create += ",";
            create += "" + dim.getId().toString() + " varchar(30)";
            pks.add("`" + dim.getId().toString() + "`");
        }
        for (int i = 0; i < struct.getDataStructureComponents().getAttributeList().size(); i++) {
            create += ",";
            AttributeType att = struct.getDataStructureComponents().getAttributeList().getAttribute(i);
            create += "" + att.getId().toString() + " text";
        }
        create += "," + "revision" + " INTEGER";
        create += ",";
        create += "" + "updatedat" + " TIMESTAMP";
        create += ",";
        PrimaryMeasure pm = struct.getDataStructureComponents().getMeasureList().getPrimaryMeasure();
        create += "" + pm.getId().toString() + " double precision";
        String pk = ",PRIMARY KEY (";
        String indexes = "";
        pks.add("revision");
        for (int i = 0; i < pks.size(); i++) {
            pk += pks.get(i);
            if (i < pks.size() - 1) {
                pk += ",";
            }
        }
        pk += ")";

        String ixs = "";
        int count = 0;
        int index_size = 3;
        if (pks.size() < 3) {
            index_size = pks.size();
        }
        for (int i = 1; i < index_size; i++) {
            Iterator it = Generator.combination(pks)
                    .simple(i).iterator();
            String s = "";
            while (it.hasNext()) {
                Collection c = (Collection) it.next();
                s += "INDEX (";
                Iterator it2 = c.iterator();
                while (it2.hasNext()) {
                    s += it2.next().toString();
                    if (it2.hasNext()) {
                        s += ",";
                    }
                }
                s += ")";
                if (it.hasNext()) {
                    s += ",";
                }
            }
            ixs += s;
            if (i < index_size - 1) {
                ixs += ",";
            }
        }
        create += pk + "," + ixs + ");";
        Connection con = pool.getConnection();
        System.out.println(create);
        PreparedStatement pst = con.prepareStatement(create);
        pst.executeUpdate();
        returnConnection(con);
    }

    /**
     *
     * @param query
     * @return
     * @throws SQLException
     */
    public DataMessage query(Registry reg, Query query) {
        try {
            List<FlatObs> result = new ArrayList<FlatObs>();
            Connection con = pool.getConnection();
            String select = "select * from \"flow_" + query.getFlowRef() + "\"";
            int count = 0;
            if (query.getQuerySize() > 0) {
                select += " where ";
                for (int i = 0; i < query.size() && query.getQueryDimension(i).size() > 0; i++) {
                    if (count != 0 && count < query.size() - 1) {
                        select += " and ";
                    }
                    count += query.getQueryDimension(i).size();
                    select += query.getQueryDimension(i).getConcept() + " in ";
                    select += "(";
                    for (int j = 0; j < query.getQueryDimension(i).size(); j++) {
                        select += "'" + query.getQueryDimension(i).getValues().get(j) + "'";
                        if (j < query.getQueryDimension(i).size() - 1) {
                            select += ",";
                        }
                    }
                    select += ")";

                }
            }
            select += ";";
            System.out.println("Query:" + select);
            PreparedStatement pst = con.prepareStatement(select);
            ResultSet rst = pst.executeQuery();
            ParseDataCallbackHandler handler = new DefaultParseDataCallbackHandler();
            handler.headerParsed(SdmxIO.getBaseHeader());
            DataSetWriter w = handler.getDataSetWriter();
            w.newDataSet();
            while (rst.next()) {
                w.newObservation();
                for (int i = 1; i <= rst.getMetaData().getColumnCount(); i++) {
                    String n = rst.getMetaData().getColumnName(i);
                    String s = rst.getString(i);
                    if (s != null && !"".equals(s) && !"".equals(n) && n != null) {
                        w.writeObservationComponent(n, s);
                    }
                }
                w.finishObservation();
            }
            returnConnection(con);
            // streaming output writers return null at w.finishDataSet();
            DataSet ds = w.finishDataSet();
            DataMessage dm = new DataMessage();
            ArrayList list = new ArrayList<DataSet>();
            list.add(ds);
            dm.setDataSets(list);
            dm.setHeader(SdmxIO.getBaseHeader());
            DataflowReference ref = DataflowReference.create(new NestedNCNameID(query.getProviderRef()), new IDType(query.getFlowRef()), Version.ONE);
            DataflowType flow = reg.find(ref);
            PostParseUtilities.setStructureReference(dm, flow.getStructure());
            handler.footerParsed(null);
            handler.documentFinished();
            return dm;
        } catch (SQLException sql) {
            throw new Error(sql);
        }
    }

    public void query(Registry reg, Query query, ParseDataCallbackHandler handler) {
        try {
            List<FlatObs> result = new ArrayList<FlatObs>();
            Connection con = pool.getConnection();
            String select = "select * from \"flow_" + query.getFlowRef() + "\"";
            int count = 0;
            if (query.getQuerySize() > 0) {
                select += " where ";
                for (int i = 0; i < query.size() && query.getQueryDimension(i).size() > 0; i++) {
                    if (count != 0 && count < query.size() - 1) {
                        select += " and ";
                    }
                    count += query.getQueryDimension(i).size();
                    select += query.getQueryDimension(i).getConcept() + " in ";
                    select += "(";
                    for (int j = 0; j < query.getQueryDimension(i).size(); j++) {
                        select += "'" + query.getQueryDimension(i).getValues().get(j) + "'";
                        if (j < query.getQueryDimension(i).size() - 1) {
                            select += ",";
                        }
                    }
                    select += ")";

                }
            }
            select += ";";
            System.out.println("Query:" + select);
            PreparedStatement pst = con.prepareStatement(select);
            ResultSet rst = pst.executeQuery();
            BaseHeaderType header = SdmxIO.getBaseHeader();
            DataflowReference ref = DataflowReference.create(new NestedNCNameID(query.getProviderRef()), new IDType(query.getFlowRef()), Version.ONE);
            DataflowType flow = reg.find(ref);
            PayloadStructureType payload = new PayloadStructureType();
            payload.setStructure(flow.getStructure());
            header.setStructures(new ArrayList<PayloadStructureType>());
            header.getStructures().add(payload);
            handler.headerParsed(header);
            DataSetWriter w = handler.getDataSetWriter();
            w.newDataSet();
            while (rst.next()) {
                w.newObservation();
                for (int i = 1; i <= rst.getMetaData().getColumnCount(); i++) {
                    String n = rst.getMetaData().getColumnName(i);
                    String s = rst.getString(i);
                    //System.out.println("n="+n+":s="+s);
                    if (s != null && !"".equals(s) && !"".equals(n) && n != null) {
                        w.writeObservationComponent(rst.getMetaData().getColumnName(i), s);
                    }
                }
                w.finishObservation();
            }
            w.finishDataSet();
            returnConnection(con);
            // streaming output writers return null at w.finishDataSet();
            handler.footerParsed(null);
            handler.documentFinished();
        } catch (SQLException sql) {
            throw new Error(sql);
        }
    }

    public void appendDataSet(DataSet ds, DataflowType df) throws SQLException {
        if (!this.hasDataflow(df)) {
            return;
        }
        Dataflow d = DataflowUtil.findDataflow(em, df.getAgencyID().toString(), df.getId().toString(), df.getVersion().toString());
        String flow = d.getDataflow().toString();
        Connection con = pool.getConnection();
        con.setAutoCommit(false);
        List<DataflowComponent> dimensions = DataflowUtil.findAllDimensions(d);
        DataflowComponent time = DataflowUtil.findTimeDimension(d);
        List<DataflowComponent> attributes = DataflowUtil.findAllDimensions(d);
        DataflowComponent measure = DataflowUtil.findMeasureDimension(d);
        DataflowComponent pm = DataflowUtil.findPrimaryMeasure(d);
        ColumnMapper mapper = ds.getColumnMapper();
        for (int i = 0; i < ds.size(); i++) {
            String find2 = "select " + pm.getDataflowComponentPK().getColumnid() + " from flow_" + flow + " WHERE ";
            String update1 = "update flow_" + flow;
            update1 += " SET revision=revision+1 WHERE ";
            FlatObs obs = ds.getFlatObs(i);
            List<String> whereParams = new ArrayList<String>();
            for (DataflowComponent dfc : dimensions) {
                whereParams.add(dfc.getDataflowComponentPK().getColumnid() + "='" + obs.getValue(mapper.getColumnIndex(dfc.getDataflowComponentPK().getColumnid())) + "'");
            }
            if (time != null) {
                whereParams.add(time.getDataflowComponentPK().getColumnid() + "='" + obs.getValue(mapper.getColumnIndex(time.getDataflowComponentPK().getColumnid())) + "'");
            }
            if (measure != null) {
                whereParams.add(measure.getDataflowComponentPK().getColumnid() + "='" + obs.getValue(mapper.getColumnIndex(measure.getDataflowComponentPK().getColumnid())) + "'");
            }
            for (int j = 0; j < whereParams.size(); j++) {
                find2 += whereParams.get(j);
                update1 += whereParams.get(j);
                if (j < whereParams.size() - 1) {
                    find2 += " and ";
                    update1 += " and ";
                }
            }
            find2 += " and revision=0";
            find2 += ";";

            update1 += " order by revision DESC;";
            PreparedStatement pst2 = con.prepareStatement(find2);
            ResultSet resultSet2 = pst2.executeQuery();
            if (!resultSet2.next()) {
                continue;
            }
            Double value = resultSet2.getDouble(1);
            if (value != null) {
                System.out.println("This value is already in the DB");
                continue;
            }
            Double obsValue = Double.parseDouble(obs.getValue(mapper.getColumnIndex(pm.getDataflowComponentPK().getColumnid())));
            PreparedStatement pst3 = con.prepareStatement(update1);
            pst3.executeUpdate();
            String insert = "insert into flow_" + flow;
            String values = "(";
            String params = "(";
            for (int j = 0; j < mapper.size(); j++) {
                values += "" + mapper.getColumnName(j) + "";
                params += "?";
                if (mapper.size() - 1 > j) {
                    values += ",";
                    params += ",";
                }
            }
            values += ",revision,updatedat";
            values += ")";
            params += ",0,NOW()";
            params += ")";
            insert += " " + values + " values " + params + ";";
            PreparedStatement pst4 = con.prepareStatement(insert);
            for (int j = 0; j < mapper.size(); j++) {
                if (mapper.getColumnName(j).equals(pm.getDataflowComponentPK().getColumnid())) {
                    pst4.setDouble(j + 1, Double.parseDouble(obs.getValue(j)));
                } else {
                    pst4.setString(j + 1, obs.getValue(j));
                }
            }
            pst4.executeUpdate();
        }
        con.commit();
        returnConnection(con);
    }

    /*

        if (!this.hasDataflow(df)) {
            return;
        }
        Dataflow d = DataflowUtil.findDataflow(em, df.getAgencyID().toString(), df.getId().toString(), df.getVersion().toString());
        String flow = d.getDataflow().toString();
        Connection con = pool.getConnection();
        DataflowComponent pm = DataflowUtil.findPrimaryMeasure(d);
        String insert = "insert into flow_" + flow + "";
        ColumnMapper mapper = ds.getColumnMapper();
        String values = "(";
        String params = "(";
        for (int i = 0; i < mapper.size(); i++) {
            values += "" + mapper.getColumnName(i) + "";
            params += "?";
            if (mapper.size() - 1 > i) {
                values += ",";
                params += ",";
            }
        }
        values += ",revision,updatedat";
        values += ")";
        params += ",0,NOW()";
        params += ")";
        insert += " " + values + " values " + params + ";";
        PreparedStatement pst = con.prepareStatement(insert);
        for (int i = 0; i < ds.size(); i++) {
            for (int j = 0; j < mapper.size(); j++) {
                if (mapper.getColumnName(j).equals(pm.getDataflowComponentPK().getColumnid())) {
                    pst.setDouble(j + 1, Double.parseDouble(ds.getFlatObs(i).getValue(j)));
                } else {
                    pst.setString(j + 1, ds.getFlatObs(i).getValue(j));
                }
            }
            pst.addBatch();
        }
        pst.executeBatch();
        returnConnection(con);
    }
     */
    public void replaceDataSet(DataSet ds, DataflowType df) throws SQLException {
        if (!this.hasDataflow(df)) {
            return;
        }
        Dataflow d = DataflowUtil.findDataflow(em, df.getAgencyID().toString(), df.getId().toString(), df.getVersion().toString());
        String flow = d.getDataflow().toString();
        Connection con = pool.getConnection();
        con.setAutoCommit(false);
        List<DataflowComponent> dimensions = DataflowUtil.findAllDimensions(d);
        DataflowComponent time = DataflowUtil.findTimeDimension(d);
        List<DataflowComponent> attributes = DataflowUtil.findAllDimensions(d);
        DataflowComponent measure = DataflowUtil.findMeasureDimension(d);
        DataflowComponent pm = DataflowUtil.findPrimaryMeasure(d);
        ColumnMapper mapper = ds.getColumnMapper();
        for (int i = 0; i < ds.size(); i++) {
            String find1 = "select " + pm.getDataflowComponentPK().getColumnid() + " from flow_" + flow + " WHERE ";
            String update1 = "update flow_" + flow;
            update1 += " SET revision=revision+1 WHERE ";
            FlatObs obs = ds.getFlatObs(i);
            List<String> whereParams = new ArrayList<String>();
            for (DataflowComponent dfc : dimensions) {
                whereParams.add(dfc.getDataflowComponentPK().getColumnid() + "='" + obs.getValue(mapper.getColumnIndex(dfc.getDataflowComponentPK().getColumnid())) + "'");
            }
            if (time != null) {
                whereParams.add(time.getDataflowComponentPK().getColumnid() + "='" + obs.getValue(mapper.getColumnIndex(time.getDataflowComponentPK().getColumnid())) + "'");
            }
            if (measure != null) {
                whereParams.add(measure.getDataflowComponentPK().getColumnid() + "='" + obs.getValue(mapper.getColumnIndex(measure.getDataflowComponentPK().getColumnid())) + "'");
            }
            for (int j = 0; j < whereParams.size(); j++) {
                find1 += whereParams.get(j);
                update1 += whereParams.get(j);
                if (j < whereParams.size() - 1) {
                    find1 += " and ";
                    update1 += " and ";
                }
            }
            find1 += " and revision=0";
            find1 += ";";

            update1 += " order by revision DESC;";
            PreparedStatement pst2 = con.prepareStatement(find1);
            ResultSet resultSet2 = pst2.executeQuery();
            if (!resultSet2.next()) {
                continue;
            }
            Double value = resultSet2.getDouble(1);
            if (value == null) {
                System.out.println("This value doesn't exist in this dataset.(nulled)");
                continue;
            }
            Double obsValue = Double.parseDouble(obs.getValue(mapper.getColumnIndex(pm.getDataflowComponentPK().getColumnid())));
            if (value.doubleValue() == obsValue.doubleValue()) {
                // Nothing To Update
                // System.out.println("This observation is already set to this value!");
                continue;
            }
            PreparedStatement pst3 = con.prepareStatement(update1);
            pst3.executeUpdate();
            String insert = "insert into flow_" + flow;
            String values = "(";
            String params = "(";
            for (int j = 0; j < mapper.size(); j++) {
                values += "" + mapper.getColumnName(j) + "";
                params += "?";
                if (mapper.size() - 1 > j) {
                    values += ",";
                    params += ",";
                }
            }
            values += ",revision,updatedat";
            values += ")";
            params += ",0,NOW()";
            params += ")";
            insert += " " + values + " values " + params + ";";
            PreparedStatement pst4 = con.prepareStatement(insert);
            for (int j = 0; j < mapper.size(); j++) {
                if (mapper.getColumnName(j).equals(pm.getDataflowComponentPK().getColumnid())) {
                    pst4.setDouble(j + 1, Double.parseDouble(obs.getValue(j)));
                } else {
                    pst4.setString(j + 1, obs.getValue(j));
                }
            }
            pst4.executeUpdate();
        }
        con.commit();
        returnConnection(con);
    }

    public void deleteDataSet(DataSet ds, DataflowType df) throws SQLException {
        if (!this.hasDataflow(df)) {
            System.out.println("no dataflow");
            return;
        }
        Dataflow d = DataflowUtil.findDataflow(em, df.getAgencyID().toString(), df.getId().toString(), df.getVersion().toString());
        String flow = d.getDataflow().toString();
        Connection con = pool.getConnection();
        con.setAutoCommit(false);
        List<DataflowComponent> dimensions = DataflowUtil.findAllDimensions(d);
        DataflowComponent time = DataflowUtil.findTimeDimension(d);
        List<DataflowComponent> attributes = DataflowUtil.findAllDimensions(d);
        DataflowComponent measure = DataflowUtil.findMeasureDimension(d);
        DataflowComponent pm = DataflowUtil.findPrimaryMeasure(d);
        ColumnMapper mapper = ds.getColumnMapper();
        for (int i = 0; i < ds.size(); i++) {
            String find1 = "select " + pm.getDataflowComponentPK().getColumnid() + " from flow_" + flow + " WHERE revision=0 and ";
            String update1 = "update flow_" + flow + " SET revision=revision+1 WHERE ";
            FlatObs obs = ds.getFlatObs(i);
            List<String> whereParams = new ArrayList<String>();
            for (DataflowComponent dfc : dimensions) {
                whereParams.add(dfc.getDataflowComponentPK().getColumnid() + "='" + obs.getValue(mapper.getColumnIndex(dfc.getDataflowComponentPK().getColumnid())) + "'");
            }
            if (time != null) {
                whereParams.add(time.getDataflowComponentPK().getColumnid() + "='" + obs.getValue(mapper.getColumnIndex(time.getDataflowComponentPK().getColumnid())) + "'");
            }
            if (measure != null) {
                whereParams.add(measure.getDataflowComponentPK().getColumnid() + "='" + obs.getValue(mapper.getColumnIndex(measure.getDataflowComponentPK().getColumnid())) + "'");
            }
            for (int j = 0; j < whereParams.size(); j++) {
                find1 += whereParams.get(j);
                update1 += whereParams.get(j);
                if (j < whereParams.size() - 1) {
                    find1 += " and ";
                    update1 += " and ";
                }
            }
            find1 += ";";
            update1 += " order by revision desc;";
            PreparedStatement pst = con.prepareStatement(find1);
            ResultSet resultSet = pst.executeQuery();
            if (!resultSet.next()) {
                // Nothing to delete
                continue;
            }
            Double value = resultSet.getDouble(1);
            Double obsValue = Double.parseDouble(obs.getValue(mapper.getColumnIndex(pm.getDataflowComponentPK().getColumnid())));
            if (value.doubleValue() == obsValue.doubleValue()) {
                String insert = "insert into flow_" + flow;
                String values = "(";
                String params = "(";
                for (int j = 0; j < mapper.size(); j++) {
                    values += "" + mapper.getColumnName(j) + "";
                    params += "?";
                    if (mapper.size() - 1 > j) {
                        values += ",";
                        params += ",";
                    }
                }
                values += ",revision,updatedat";
                values += ")";
                params += ",0,NOW()";
                params += ")";
                insert += " " + values + " values " + params + ";";
                PreparedStatement pst3 = con.prepareStatement(update1);
                pst3.executeUpdate();
                PreparedStatement pst4 = con.prepareStatement(insert);
                for (int j = 0; j < mapper.size(); j++) {
                    if (mapper.getColumnName(j).equals(pm.getDataflowComponentPK().getColumnid())) {
                        pst4.setNull(j + 1, JDBCType.DOUBLE.getVendorTypeNumber());
                    } else {
                        pst4.setString(j + 1, obs.getValue(j));
                    }
                }
                pst4.executeUpdate();
            }
        }
        con.commit();
        returnConnection(con);
    }

    public boolean hasDataflow(DataflowType df) {
        Dataflow d = DataflowUtil.findDataflow(em, df.getAgencyID().toString(), df.getId().toString(), df.getVersion().toString());
        return d != null;
    }
}
