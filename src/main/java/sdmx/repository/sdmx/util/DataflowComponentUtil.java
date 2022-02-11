/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdmx.repository.util;

import fr.insee.vtl.model.Dataset.Role;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import sdmx.Registry;
import sdmx.commonreferences.IDType;
import sdmx.commonreferences.NestedNCNameID;
import sdmx.commonreferences.Version;
import sdmx.message.DataStructure;
import sdmx.repository.sdmx.entities.Dataflow;
import sdmx.repository.sdmx.entities.DataflowComponentPK;
import sdmx.structure.base.Component;
import sdmx.structure.datastructure.AttributeListType;
import sdmx.structure.datastructure.AttributeType;
import sdmx.structure.datastructure.DataStructureComponents;
import sdmx.structure.datastructure.DataStructureType;
import sdmx.structure.datastructure.DimensionListType;
import sdmx.structure.datastructure.DimensionType;
import sdmx.structure.datastructure.MeasureDimensionType;
import sdmx.structure.datastructure.MeasureListType;
import sdmx.structure.datastructure.PrimaryMeasure;
import sdmx.structure.datastructure.TimeDimensionType;
import sdmx.structure.dataflow.DataflowType;

/**
 *
 * @author James
 */
public class DataflowComponentUtil {

    public static final int TYPE_DIMENSION = 0;
    public static final int TYPE_TIMEDIMENSION = 1;
    public static final int TYPE_MEASURE = 2;
    public static final int TYPE_PRIMARYMEASURE = 3;
    public static final int TYPE_ATTRIBUTE = 4;

    public static sdmx.repository.sdmx.entities.DataflowComponent createDatabaseDataflowComponent(EntityManager em, Dataflow df, Component comp, int pos) {
        sdmx.repository.sdmx.entities.DataflowComponent dfc = new sdmx.repository.sdmx.entities.DataflowComponent();
        if (comp instanceof MeasureDimensionType) {
            dfc.setType(TYPE_MEASURE);
        } else if (comp instanceof DimensionType) {
            dfc.setType(TYPE_DIMENSION);
        } else if (comp instanceof TimeDimensionType) {
            dfc.setType(TYPE_TIMEDIMENSION);
        } else if (comp instanceof PrimaryMeasure) {
            dfc.setType(TYPE_PRIMARYMEASURE);
        } else if (comp instanceof AttributeType) {
            dfc.setType(TYPE_ATTRIBUTE);
        }
        dfc.setPosition(pos);
        DataflowComponentPK pk = new DataflowComponentPK();
        pk.setDataflow(df.getDataflow());
        pk.setColumnid(comp.getId().toString());
        dfc.setDataflowComponentPK(pk);
        return dfc;
    }

    public static List<sdmx.repository.sdmx.entities.DataflowComponent> searchDataflowComponent(EntityManager em, Long df) {
        try {
            Query q = em.createQuery("select d from DataflowComponent d where d.dataflowComponentPK.dataflow=:df");
            q.setParameter("df", df);
            sdmx.repository.sdmx.entities.DataflowComponent dfc = null;
            List<sdmx.repository.sdmx.entities.DataflowComponent> list = q.getResultList();
            return list;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static List<sdmx.repository.sdmx.entities.DataflowComponent> searchDataflowComponent(EntityManager em, Long df, int type) {
        try {
            Query q = em.createQuery("select d from DataflowComponent d where d.dataflowComponentPK.dataflow=:df and d.type=:type");
            q.setParameter("df", df);
            q.setParameter("type", type);
            sdmx.repository.sdmx.entities.DataflowComponent dfc = null;
            List<sdmx.repository.sdmx.entities.DataflowComponent> list = q.getResultList();
            return list;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static sdmx.repository.sdmx.entities.DataflowComponent findDataflowComponent(EntityManager em, Long df, String id) {
        try {
            Query q = em.createQuery("select d from DataflowComponent d where d.dataflowComponentPK.dataflow=:df and d.dataflowComponentPK.columnId=:id");
            q.setParameter("df", df);
            q.setParameter("id", id);
            sdmx.repository.sdmx.entities.DataflowComponent dfc = null;
            List<sdmx.repository.sdmx.entities.DataflowComponent> list = q.getResultList();
            if (list.size() == 0) {
                return null;
            }
            return list.get(0);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static sdmx.repository.sdmx.entities.DataflowComponent findDataflowComponent(EntityManager em, sdmx.repository.sdmx.entities.Dataflow df, String id) {
        try {
            Query q = em.createQuery("select d from DataflowComponent d where d.dataflowComponentPK.dataflow=:df and d.dataflowComponentPK.columnId=:id");
            q.setParameter("df", df);
            q.setParameter("id", id);
            sdmx.repository.sdmx.entities.DataflowComponent dfc = null;
            List<sdmx.repository.sdmx.entities.DataflowComponent> list = q.getResultList();
            if (list.size() == 0) {
                return null;
            }
            return list.get(0);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
