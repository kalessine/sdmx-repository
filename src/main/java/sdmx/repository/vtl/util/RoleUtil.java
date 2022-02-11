/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdmx.repository.vtl.util;

import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import sdmx.repository.vtl.entities.Role;

/**
 *
 * @author jsg
 */
public class RoleUtil {
    private static sdmx.repository.vtl.entities.Role IDENTIFIER = new sdmx.repository.vtl.entities.Role(1);
    private static sdmx.repository.vtl.entities.Role MEASURE = new sdmx.repository.vtl.entities.Role(2);
    private static sdmx.repository.vtl.entities.Role ATTRIBUTE = new sdmx.repository.vtl.entities.Role(3);

    static {
        IDENTIFIER.setName("Identifier");
        MEASURE.setName("Measure");
        ATTRIBUTE.setName("Attribute");
    }
    public static Role[] ROLES = new Role[]{IDENTIFIER, MEASURE,ATTRIBUTE};

    public static Role lookup(String s) {
        for (int i = 0; i < ROLES.length; i++) {
            if (ROLES[i].getName().equals(s)) {
                return ROLES[i];
            }
        }
        return null;
    }
    public static fr.insee.vtl.model.Dataset.Role lookup(int i) {
        switch(i){
            case 0:return fr.insee.vtl.model.Dataset.Role.IDENTIFIER;
            case 1:return fr.insee.vtl.model.Dataset.Role.MEASURE;
            case 2:return fr.insee.vtl.model.Dataset.Role.ATTRIBUTE;
        }
        return null;
    }

    public static void init(EntityManager em) {
        try {
            Query query = em.createNamedQuery("Role.findAll");
            ROLES = ((List<Role>) query.getResultList()).toArray(new Role[]{});
            System.out.println(Arrays.toString(ROLES));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if(ROLES.length==0){
             init2(em);
        }
    }
    public static void init2(EntityManager em) {
        try{
            em.getTransaction().begin();
            em.persist(IDENTIFIER);
            em.persist(MEASURE);
            em.persist(ATTRIBUTE);
        }catch(Exception ex) {}
        finally{
            em.getTransaction().commit();
        }
        try {
            Query query = em.createNamedQuery("Role.findAll");
            ROLES = ((List<Role>) query.getResultList()).toArray(ROLES);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
