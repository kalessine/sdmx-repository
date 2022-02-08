/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdmx.repository;

import sdmx.Registry;
import sdmx.data.DataSet;
import sdmx.structure.dataflow.DataflowType;

/**
 *
 * @author jsg
 */
public interface WritableRepository extends sdmx.Repository {
     public void createDataflow(Registry reg, DataflowType df);
     public void appendDataSet(DataSet ds, DataflowType df);
     public void updateDataSet(DataSet ds, DataflowType df);
     public void deleteDataSet(DataSet ds, DataflowType df);
}
