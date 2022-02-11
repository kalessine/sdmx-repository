/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdmx.repository.vtl;

import fr.insee.vtl.model.Dataset;
import java.util.List;
import sdmx.message.DataMessage;

/**
 *
 * @author jsg
 */
public class DataMessageWrapper implements Dataset {
    private DataMessage msg = null;
    public DataMessageWrapper(DataMessage msg) {
        this.msg=msg;
    }

    @Override
    public List<DataPoint> getDataPoints() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DataStructure getDataStructure() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
