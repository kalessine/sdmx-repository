/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdmx.repository.service;

import com.google.common.collect.Collections2;
import fr.insee.vtl.model.Dataset;
import fr.insee.vtl.model.Dataset.Role;
import fr.insee.vtl.model.InMemoryDataset;
import fr.insee.vtl.model.Structured;
import fr.insee.vtl.model.Structured.Component;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
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
import sdmx.commonreferences.DataStructureReference;
import sdmx.commonreferences.DataflowReference;
import sdmx.commonreferences.IDType;
import sdmx.commonreferences.NCNameID;
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
import sdmx.exception.QueryException;
import sdmx.message.BaseHeaderType;
import sdmx.message.DataMessage;
import sdmx.message.DataQueryMessage;
import sdmx.message.DataStructure;
import sdmx.querykey.Query;
import sdmx.repository.VTLRepository;
import sdmx.repository.WritableRepository;
import sdmx.repository.sdmx.entities.Dataflow;
import sdmx.repository.sdmx.entities.DataflowComponent;
import sdmx.repository.exception.RepositoryException;
import sdmx.repository.util.DataflowComponentUtil;
import sdmx.repository.util.DataflowUtil;
import sdmx.repository.util.LanguageUtil;
import sdmx.repository.vtl.entities.DatasetComponent;
import sdmx.repository.vtl.util.DatasetUtil;
import sdmx.repository.vtl.util.RoleUtil;
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
import sdmx.xml.ID;

/**
 *
 * @author James
 */
public class VTLDatabaseRepository implements VTLRepository {

    public static final EntityManagerFactory EMF = Persistence.createEntityManagerFactory("sdmxrepositoryPU");
    EntityManager em = EMF.createEntityManager();

    PoolingDataSource<Connection> pool;

    public VTLDatabaseRepository() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            ConnectionFactory connectionFactory = new DriverManagerConnectionFactory("jdbc:mysql://localhost:3306/vtlrepository", "james", "redacted");
            PoolableConnectionFactory poolableConnectionFactory;
            poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory, null);
            GenericObjectPool connectionPool = new GenericObjectPool(poolableConnectionFactory);
            pool = new PoolingDataSource(connectionPool);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DatabaseRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        RoleUtil.init(em);
    }

    public boolean hasDataset(String name) {
        sdmx.repository.vtl.entities.Dataset d = DatasetUtil.findDataset(em, name);
        return d != null;
    }

    public Connection getConnection() throws SQLException {
        return pool.getConnection();
    }

    public void returnConnection(Connection c) throws SQLException {
        if (!c.isClosed()) {
            c.close();
        }
    }

    public void createDataset(String name, Structured s) throws RepositoryException {
        if (this.hasDataset(name)) {
            return;
        }
        try {
            sdmx.repository.vtl.entities.Dataset d2 =  DatasetUtil.createVTLDataset(em, name, s);
            ArrayList<String> pks = new ArrayList<String>();
            String create = "create table if not exists dataset_" + name + " (";
            Map<String,String> types = new HashMap<String,String>();
            types.put("java.lang.String"," varchar(30)");
            types.put("java.lang.Long"," BIGINT");
            types.put("java.lang.Double"," DOUBLE PRECISION");
            types.put("java.lang.Integer"," INT");
            Iterator<Entry<String, Structured.Component>> it = s.getDataStructure().entrySet().iterator();
            int identifiers = 0;
            while (it.hasNext()) {
                Component comp = it.next().getValue();
                types.put("java.lang.String"," varchar(30)");
                if (comp.getRole().name().equals("IDENTIFIER")) {
                    create += "" + comp.getName() + types.get(comp.getType().getName());
                    if (it.hasNext()) {
                        create += ",";
                    }
                    pks.add("`" + comp.getName() + "`");
                    identifiers++;
                }
                if (comp.getRole().name().equals("MEASURE")) {
                    create += "" + comp.getName() + types.get(comp.getType().getName());
                    if (it.hasNext()) {
                        create += ",";
                    }
                }
                if (comp.getRole().name().equals("ATTRIBUTE")) {
                    types.put("java.lang.String"," text");
                    create += "" + comp.getName() + types.get(comp.getType().getName());
                    if (it.hasNext()) {
                        create += ",";
                    }
                }
            }
            String pk = ",PRIMARY KEY (";
            String indexes = "";
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
                Iterator it2 = Generator.combination(pks)
                        .simple(i).iterator();
                String string = "";
                while (it2.hasNext()) {
                    Collection c = (Collection) it2.next();
                    string += "INDEX (";
                    Iterator it3 = c.iterator();
                    while (it3.hasNext()) {
                        string += it3.next().toString();
                        if (it3.hasNext()) {
                            string += ",";
                        }
                    }
                    string += ")";
                    if (it2.hasNext()) {
                        string += ",";
                    }
                }
                ixs += string;
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
        } catch (SQLException sql) {
            throw new RepositoryException(sql.getMessage());
        }
    }
    @Override
    public Dataset getDataset(String name) throws RepositoryException {
        if (!this.hasDataset(name)) {
            return null;
        }
        try {
            sdmx.repository.vtl.entities.Dataset d2 =  DatasetUtil.findDataset(em, name);
            Structured.DataStructure struct = DatasetUtil.getStructure(em, name);
            String select = "select * from dataset_"+name;
            Connection con = pool.getConnection();
            PreparedStatement pst = con.prepareStatement(select);
            ResultSet rs = pst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int numberOfColumns = rsmd.getColumnCount();
            List<List<Object>> obs = new ArrayList<List<Object>>();
            while(rs.next()){
                List<Object> ob = new ArrayList<Object>();
                Iterator<String> it = struct.keySet().iterator();
                while(it.hasNext()){
                    String col = it.next();
                    int idx = struct.indexOfKey(col);
                    Object val = rs.getObject(col, struct.get(col).getType());
                    ob.add(val);
                }
                obs.add(ob);
            }
            InMemoryDataset set = new InMemoryDataset(obs, struct);
            return set;
        } catch (SQLException sql) {
            throw new RepositoryException(sql.getMessage());
        }
    }

    public void appendDataset(String name, fr.insee.vtl.model.Dataset ds) throws RepositoryException {
        if (!this.hasDataset(name)) {
            return;
        }
        try {
            sdmx.repository.vtl.entities.Dataset d = DatasetUtil.findDataset(em, name);
            Connection con = pool.getConnection();
            String insert = "insert into dataset_" + name + "";
            Structured.DataStructure struct = ds.getDataStructure();
            String values = "(";
            String params = "(";
            Iterator<Entry<String, Component>> it = struct.entrySet().iterator();
            int count = 0;
            int colSize = struct.size();
            while (it.hasNext()) {
                Component comp = it.next().getValue();
                values += comp.getName();
                params += "?";
                count++;
                if (count < colSize) {
                    values += ",";
                    params += ",";
                }
            }
            values += ")";
            params += ")";
            insert += " " + values + " values " + params + ";";
            List<List<Object>> list = ds.getDataAsList();
            PreparedStatement pst = con.prepareStatement(insert);
            for (int i = 0; i < list.size(); i++) {
                Iterator<Entry<String, Component>> it2 = struct.entrySet().iterator();
                int pmindex = 0;
                List<Object> obs = list.get(i);
                while (it2.hasNext()) {
                    Entry<String, Component> comp = it2.next();
                    pst.setObject(pmindex+1,obs.get(pmindex++));
                }
                pst.addBatch();
            }
            pst.executeBatch();
            returnConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(VTLDatabaseRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void replaceDataset(String name, fr.insee.vtl.model.Dataset ds) throws RepositoryException {
        if (!this.hasDataset(name)) {
            return;
        }
        try {
            sdmx.repository.vtl.entities.Dataset d = DatasetUtil.findDataset(em, name);
            Connection con = pool.getConnection();
            String delete = "delete from dataset_" + name + ";";
            String insert = "insert into dataset_" + name + "";
            Structured.DataStructure struct = ds.getDataStructure();
            String values = "(";
            String params = "(";
            Iterator<Entry<String, Component>> it = struct.entrySet().iterator();
            int count = 0;
            int colSize = struct.size();
            while (it.hasNext()) {
                Component comp = it.next().getValue();
                values += comp.getName();
                params += "?";
                if (count < colSize) {
                    values += ",";
                    params += ",";
                }
            }
            values += ")";
            params += ")";
            insert += " " + values + " values " + params + ";";
            PreparedStatement pst1 = con.prepareStatement(delete);
            pst1.executeUpdate();
            List<List<Object>> list = ds.getDataAsList();

            PreparedStatement pst = con.prepareStatement(insert);
            for (int i = 0; i < list.size(); i++) {
                Iterator<Entry<String, Component>> it2 = struct.entrySet().iterator();
                int pmindex = 1;
                while (it2.hasNext()) {
                    Entry<String, Component> comp = it2.next();
                    pst.setObject(pmindex++, list.get(i));
                }
                pst.addBatch();
            }
            pst.executeBatch();
            returnConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(VTLDatabaseRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteDataset(String name) throws RepositoryException {

        if (!this.hasDataset(name)) {
            System.out.println("no dataflow");
            return;
        }
        sdmx.repository.vtl.entities.Dataset ds = DatasetUtil.findDataset(em, name);
        em.getTransaction().begin();
        em.remove(ds);
        em.getTransaction().commit();
        try {
            Connection con = pool.getConnection();
            String delete = "drop table dataset_" + name + ";";
            PreparedStatement pst1 = con.prepareStatement(delete);
            pst1.executeUpdate();
            returnConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(VTLDatabaseRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
