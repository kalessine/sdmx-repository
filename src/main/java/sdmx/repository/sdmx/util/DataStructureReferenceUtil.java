/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdmx.repository.util;

import javax.persistence.EntityManager;
import sdmx.commonreferences.ConceptReference;
import sdmx.commonreferences.ConceptSchemeReference;
import sdmx.commonreferences.DataStructureReference;
import sdmx.commonreferences.IDType;
import sdmx.commonreferences.ItemSchemeReferenceBase;
import sdmx.commonreferences.NestedNCNameID;
import sdmx.commonreferences.Version;

/**
 *
 * @author James
 */
public class DataStructureReferenceUtil {

    public static sdmx.repository.sdmx.entities.DataStructureReference toDatabaseDataStructureReference(EntityManager em, DataStructureReference reference) {
        reference.dump();
        sdmx.repository.sdmx.entities.DataStructureReference ref = new sdmx.repository.sdmx.entities.DataStructureReference();
        ref.setAgencyid(reference.getAgencyId().toString());
        ref.setId(reference.getMaintainableParentId().toString());
        ref.setVersion(reference.getVersion().toString());
        return ref;
        /*sdmx.repository.sdmx.entities.DataStructure ds = DataStructureUtil.findDataStructure(em, reference.getAgencyId().toString(),reference.getMaintainableParentId().toString(),reference.getVersion().toString());
        if (ds != null) {
            ref.setDataStructure(ds);
            ds.getDataStructureReferenceList().add(ref);
            em.persist(ref);
            em.merge(ds);
            em.flush();
            em.refresh(ref);
            return ref;
        } else {
            System.out.println("Data Structure doesn't exist!!!");
            reference.dump();
            return null;
        }
        */
    }

    public static DataStructureReference toSDMXDataStructureReference(sdmx.repository.sdmx.entities.DataStructureReference ref) {
        return DataStructureReference.create(new NestedNCNameID(ref.getAgencyid()),new IDType(ref.getId()), new Version(ref.getVersion()));
    }
}
