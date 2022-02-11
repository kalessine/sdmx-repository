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
import sdmx.repository.WritableRepository;
import sdmx.repository.sdmx.entities.Dataflow;
import sdmx.repository.sdmx.entities.DataflowComponent;
import sdmx.repository.exception.RepositoryException;
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
import sdmx.xml.ID;

/**
 *
 * @author James
 */
public class InMemoryRepository implements WritableRepository {

    List<Dataflow> dataflows = new ArrayList<Dataflow>();
    
    public InMemoryRepository() {
    }

    public void deleteDataflow(DataflowType df) throws RepositoryException {
        if (!this.hasDataflow(df)) {
            return;
        }
    }

    @Override
    public void createDataflow(Registry reg, DataflowType df) throws RepositoryException {
        if (this.hasDataflow(df)) {
            return;
        }
    }

    /**
     *
     * @param query
     * @return
     * @throws SQLException
     */
    public DataMessage query(Query query) throws QueryException {
        return null;
    }

    public void query(Query query, ParseDataCallbackHandler handler) throws QueryException {
    }

    public void appendDataSet(DataSet ds, DataflowType df) throws RepositoryException {
    }

    public void replaceDataSet(DataSet ds, DataflowType df) throws RepositoryException {
        if (!this.hasDataflow(df)) {
            return;
        }
    }

    public void deleteDataSet(DataSet ds, DataflowType df) throws RepositoryException {
        if (!this.hasDataflow(df)) {
            System.out.println("no dataflow");
            return;
        }
    }

    public boolean hasDataflow(DataflowType df) {
        for (int i = 0; i < this.dataflows.size(); i++) {
            Dataflow d = this.dataflows.get(i);
            if (d.getAgencyid().equals(df.getAgencyID().toString()) && d.getId().equals(df.getId().toString()) && d.getVersion().equals(df.getVersion().toString())) {
                return true;
            }
        }
        return false;
    }

}
