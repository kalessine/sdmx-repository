/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdmx.repository.vtl.util;

import fr.insee.vtl.model.Dataset.Role;
import fr.insee.vtl.model.Structured;
import fr.insee.vtl.model.Structured.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import sdmx.repository.vtl.entities.DatasetComponent;
import sdmx.repository.vtl.entities.Dataset;

/**
 *
 * @author jsg
 */
public class DatasetUtil {
    public static sdmx.repository.vtl.entities.Dataset createVTLDataset(EntityManager em, String name, Structured df) {
        sdmx.repository.vtl.entities.Dataset ds1 = new sdmx.repository.vtl.entities.Dataset();
        ds1.setName(name);
        int position = 0;
        Set<Map.Entry<String, Structured.Component>> comps1 = df.getDataStructure().entrySet();
        List<DatasetComponent> comps = new ArrayList<>();
        Iterator<Map.Entry<String, Structured.Component>> it = comps1.iterator();
        int count = 0;
        em.getTransaction().begin();
        em.persist(ds1);
        em.getTransaction().commit();
        em.refresh(ds1);
        while(it.hasNext()) {
            comps.add(DatasetComponentUtil.createDatabaseVTLComponent(em, ds1, it.next().getValue(), count++));
        }
        ds1.setDatasetComponentList(comps);
        return ds1;
    }
    public static Dataset findDataset(EntityManager em,String name) {
        try {
            Query q = em.createQuery("select d from Dataset d where d.name=:name");
            q.setParameter("name", name);
            return (sdmx.repository.vtl.entities.Dataset) q.getSingleResult();
        } catch (Exception ex) {
            //ex.printStackTrace();
            return null;
        }
    }
    public static fr.insee.vtl.model.Structured.DataStructure getStructure(EntityManager em,String name) {
        Dataset ds = findDataset(em, name);
        List<Component> result = new ArrayList<Component>();
        for(DatasetComponent dsc:ds.getDatasetComponentList()){
            try {
                result.add(DatasetComponentUtil.toComponent(dsc));
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(DatasetUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        fr.insee.vtl.model.Structured.DataStructure struct = new fr.insee.vtl.model.Structured.DataStructure(result);
        return struct;
    }
}
