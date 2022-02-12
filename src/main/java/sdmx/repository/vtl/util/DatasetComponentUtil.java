/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdmx.repository.vtl.util;

import fr.insee.vtl.model.Dataset;
import fr.insee.vtl.model.Structured;
import javax.persistence.EntityManager;
import sdmx.repository.sdmx.entities.Dataflow;
import sdmx.repository.sdmx.entities.DataflowComponentPK;
import static sdmx.repository.util.DataflowComponentUtil.TYPE_DIMENSION;
import static sdmx.repository.util.DataflowComponentUtil.TYPE_PRIMARYMEASURE;
import static sdmx.repository.util.DataflowComponentUtil.TYPE_TIMEDIMENSION;
import sdmx.repository.vtl.entities.DatasetComponent;
import sdmx.repository.vtl.entities.DatasetComponentPK;

/**
 *
 * @author jsg
 */
public class DatasetComponentUtil {
    public static sdmx.repository.vtl.entities.DatasetComponent createDatabaseVTLComponent(EntityManager em, sdmx.repository.vtl.entities.Dataset ds,fr.insee.vtl.model.Structured.Component comp, int pos) {
        sdmx.repository.vtl.entities.DatasetComponent dfc = new sdmx.repository.vtl.entities.DatasetComponent();
        dfc.setPosition(pos);
        DatasetComponentPK pk = new DatasetComponentPK();
        pk.setDataset(ds.getId());
        pk.setColumnid(comp.getName());
        dfc.setDatasetComponentPK(pk);
        dfc.setType(comp.getType().getName());
        if(comp.getRole().equals(Dataset.Role.IDENTIFIER)){
            dfc.setRole(RoleUtil.IDENTIFIER.getId());
        }
        else if(comp.getRole().equals(Dataset.Role.MEASURE)){
            dfc.setRole(RoleUtil.MEASURE.getId());
        }
        else if(comp.getRole().equals(Dataset.Role.ATTRIBUTE)){
            dfc.setRole(RoleUtil.ATTRIBUTE.getId());
        }
        return dfc;
    }
    public static Structured.Component toComponent(DatasetComponent comp) throws ClassNotFoundException {
        return new Structured.Component(comp.getDatasetComponentPK().getColumnid(),Class.forName(comp.getType()),RoleUtil.lookup(comp.getRole()));
    }
}
