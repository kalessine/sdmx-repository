/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdmx.repository.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import sdmx.commonreferences.IDType;
import sdmx.commonreferences.NestedNCNameID;
import sdmx.commonreferences.Version;
import sdmx.repository.sdmx.entities.ComponentValue;
import sdmx.repository.sdmx.entities.ComponentValuePK;
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

/**
 *
 * @author James
 */
public class ComponentValueUtil {

    public static sdmx.repository.sdmx.entities.ComponentValue createDatabaseComponentValue(EntityManager em, sdmx.repository.sdmx.entities.DataflowComponent dfc, String value) {
        sdmx.repository.sdmx.entities.ComponentValue cv = new sdmx.repository.sdmx.entities.ComponentValue();
        ComponentValuePK pk = new ComponentValuePK();
        pk.setDataflow(dfc.getDataflowComponentPK().getDataflow());
        pk.setColumnid(dfc.getDataflowComponentPK().getColumnid());
        cv.setValue(value);
        cv.setComponentValuePK(pk);
        return cv;
    }

    public static List<sdmx.repository.sdmx.entities.ComponentValue> searchComponentValue(EntityManager em, Long df,String id) {
        try {
            Query q = em.createQuery("select c from ComponentValue c where c.componentValuePK.dataflow=:df and c.componentValuePK.columnId=:id");
            q.setParameter("df", df);
            q.setParameter("id", id);
            List<sdmx.repository.sdmx.entities.ComponentValue> list = q.getResultList();
            return list;
        } catch (Exception ex) {
            ex.printStackTrace();
            return Collections.EMPTY_LIST;
        }
    }
    public static sdmx.repository.sdmx.entities.ComponentValue findComponentValue(EntityManager em, Long df,String id,String value) {
        try {
            Query q = em.createQuery("select c from ComponentValue c where c.componentValuePK.dataflow=:df and c.componentValuePK.columnId=:id and c.value=:value");
            q.setParameter("df", df);
            q.setParameter("id", id);
            q.setParameter("value", value);
            List<sdmx.repository.sdmx.entities.ComponentValue> list = q.getResultList();
            if(list.size()>0)return list.get(0);
            else return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
