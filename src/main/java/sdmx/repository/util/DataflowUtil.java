/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdmx.repository.util;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import sdmx.Registry;
import sdmx.commonreferences.NCNameID;
import sdmx.commonreferences.NestedNCNameID;
import sdmx.commonreferences.Version;
import sdmx.repository.entities.Dataflow;
import sdmx.repository.entities.DataflowComponent;
import sdmx.structure.base.ItemType;
import sdmx.structure.codelist.CodelistType;
import sdmx.structure.dataflow.DataflowType;
import sdmx.structure.datastructure.DataStructureType;

/**
 *
 * @author James
 */
public class DataflowUtil {

    public static final int TYPE_DIMENSION = 0;
    public static final int TYPE_TIMEDIMENSION = 1;
    public static final int TYPE_MEASURE = 2;
    public static final int TYPE_PRIMARYMEASURE = 3;
    public static final int TYPE_ATTRIBUTE = 4;

    public static sdmx.repository.entities.Dataflow createDatabaseDataflow(EntityManager em, Registry reg, DataflowType df) {
        sdmx.repository.entities.DataStructureReference ref = DataStructureReferenceUtil.toDatabaseDataStructureReference(em, df.getStructure());
        em.persist(ref);
        sdmx.repository.entities.Dataflow df1 = new sdmx.repository.entities.Dataflow();
        df1.setAgencyid(df.getAgencyID().toString());
        df1.setId(df.getId().toString());
        df1.setVersion(df.getVersion().toString());
        df1.setAnnotations(AnnotationsUtil.toDatabaseAnnotations(em, df.getAnnotations()));
        NameUtil.setName(em, df1, df);
        df1.setStructure(ref);
        em.persist(df1);
        em.flush();
        em.refresh(df1);
        int position = 0;
        DataStructureType ds = reg.find(df.getStructure());
        List<DataflowComponent> comps = new ArrayList<>();
        for (int i = 0; i < ds.getDataStructureComponents().getDimensionList().getDimensions().size(); i++) {
            comps.add(DataflowComponentUtil.createDatabaseDataflowComponent(em, df1, ds.getDataStructureComponents().getDimensionList().getDimension(i), position++));
        }
        if (ds.getDataStructureComponents().getDimensionList().getTimeDimension() != null) {
            comps.add(DataflowComponentUtil.createDatabaseDataflowComponent(em, df1, ds.getDataStructureComponents().getDimensionList().getTimeDimension(), position++));
        }
        if (ds.getDataStructureComponents().getDimensionList().getMeasureDimension() != null) {
            comps.add(DataflowComponentUtil.createDatabaseDataflowComponent(em, df1, ds.getDataStructureComponents().getDimensionList().getMeasureDimension(), position++));
        }
        for (int i = 0; i < ds.getDataStructureComponents().getAttributeList().size(); i++) {
            comps.add(DataflowComponentUtil.createDatabaseDataflowComponent(em, df1, ds.getDataStructureComponents().getAttributeList().getAttribute(i), position++));
        }
        comps.add(DataflowComponentUtil.createDatabaseDataflowComponent(em, df1, ds.getDataStructureComponents().getMeasureList().getPrimaryMeasure(), position++));
        df1.setDataflowComponentList(comps);
        return df1;
    }

    public static Dataflow findDataflow(EntityManager em, String agency, String id, String version) {
        try {
            Query q = em.createQuery("select d from Dataflow d where d.agencyid=:agency and d.id=:id and d.version=:version");
            q.setParameter("agency", agency);
            q.setParameter("id", id);
            q.setParameter("version", version);
            return (sdmx.repository.entities.Dataflow) q.getSingleResult();
        } catch (Exception ex) {
            //ex.printStackTrace();
            return null;
        }
    }

    public static List<sdmx.repository.entities.Dataflow> searchDataflow(EntityManager em, String agency, String id, String version) {
        if ("*".equals(version) && "all".equals(id) && "all".equals(agency)) {
            Query q = em.createQuery("select d from Dataflow d");
            return (List<sdmx.repository.entities.Dataflow>) q.getResultList();
        } else if ("all".equals(id) && "all".equals(agency)) {
            Query q = em.createQuery("select d from Dataflow d where d.version=:version");
            q.setParameter("version", version);
            return (List<sdmx.repository.entities.Dataflow>) q.getResultList();
        } else if ("*".equals(version) && "all".equals(id)) {
            Query q = em.createQuery("select d from Dataflow d where d.agencyid=:agency");
            q.setParameter("agency", agency);
            return (List<sdmx.repository.entities.Dataflow>) q.getResultList();
        } else if ("*".equals(version) && "all".equals(agency)) {
            Query q = em.createQuery("select d from Dataflow d where d.id=:id");
            q.setParameter("id", id);
            return (List<sdmx.repository.entities.Dataflow>) q.getResultList();
        } else if ("*".equals(version)) {
            Query q = em.createQuery("select d from Dataflow d where d.agencyid=:agency and d.id=:id");
            q.setParameter("agency", agency);
            q.setParameter("id", id);
            return (List<sdmx.repository.entities.Dataflow>) q.getResultList();
        } else if ("all".equals(id)) {
            Query q = em.createQuery("select d from Dataflow d where d.agencyid=:agency and d.version=:version");
            q.setParameter("agency", agency);
            q.setParameter("version", version);
            return (List<sdmx.repository.entities.Dataflow>) q.getResultList();
        } else if ("all".equals(agency)) {
            Query q = em.createQuery("select d from Dataflow d where d.id=:id and d.id=:id");
            q.setParameter("id", id);
            q.setParameter("version", version);
            return (List<sdmx.repository.entities.Dataflow>) q.getResultList();
        } else {
            Query q = em.createQuery("select d from Dataflow d where d.agencyid=:agency and d.id=:id and d.version=:version");
            q.setParameter("agency", agency);
            q.setParameter("id", id);
            q.setParameter("version", version);
            return (List<sdmx.repository.entities.Dataflow>) q.getResultList();
        }
    }

    public static DataflowType toSDMXDataflow(sdmx.repository.entities.Dataflow df) {
        if (df == null) {
            return null;
        }
        DataflowType df1 = new DataflowType();
        df1.setNames(NameUtil.toSDMXName(df.getName()));
        df1.setAgencyID(new NestedNCNameID(df.getAgencyid()));
        df1.setId(new NCNameID(df.getId()));
        df1.setVersion(new Version(df.getVersion()));
        df1.setStructure(DataStructureReferenceUtil.toSDMXDataStructureReference(df.getStructure()));
        return df1;
    }

    public static DataflowComponent findDataflowComponent(Dataflow df, String columnid) {
        for (DataflowComponent dc : df.getDataflowComponentList()) {
            if (dc.getDataflowComponentPK().getColumnid().equals(columnid)) {
                return dc;
            }
        }
        return null;
    }

    public static List<DataflowComponent> findAllDimensions(Dataflow df) {
        List<DataflowComponent> result = new ArrayList<DataflowComponent>();
        for (DataflowComponent dc : df.getDataflowComponentList()) {
            if (dc.getType() == TYPE_DIMENSION) {
                result.add(dc);
            }
        }
        return result;
    }
    public static List<DataflowComponent> findAllAttributes(Dataflow df) {
        List<DataflowComponent> result = new ArrayList<DataflowComponent>();
        for (DataflowComponent dc : df.getDataflowComponentList()) {
            if (dc.getType() == TYPE_ATTRIBUTE) {
                result.add(dc);
            }
        }
        return result;
    }
    public static DataflowComponent findTimeDimension(Dataflow df) {
        for (DataflowComponent dc : df.getDataflowComponentList()) {
            if (dc.getType() == TYPE_TIMEDIMENSION) {
                return dc;
            }
        }
        return null;
    }
    public static DataflowComponent findMeasureDimension(Dataflow df) {
        for (DataflowComponent dc : df.getDataflowComponentList()) {
            if (dc.getType() == TYPE_MEASURE) {
                return dc;
            }
        }
        return null;
    }
    public static DataflowComponent findPrimaryMeasure(Dataflow df) {
        for (DataflowComponent dc : df.getDataflowComponentList()) {
            if (dc.getType() == TYPE_PRIMARYMEASURE) {
                return dc;
            }
        }
        return null;
    }
}
