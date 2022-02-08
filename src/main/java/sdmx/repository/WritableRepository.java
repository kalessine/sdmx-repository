/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdmx.repository;

import sdmx.Registry;
import sdmx.data.DataSet;
import sdmx.repository.exception.RepositoryException;
import sdmx.structure.dataflow.DataflowType;

/**
 *
 * @author jsg
 */
public interface WritableRepository extends sdmx.Repository {
     public void createDataflow(Registry reg, DataflowType df) throws RepositoryException;
     public void appendDataSet(DataSet ds, DataflowType df) throws RepositoryException;
     public void replaceDataSet(DataSet ds, DataflowType df) throws RepositoryException;
     public void deleteDataSet(DataSet ds, DataflowType df) throws RepositoryException;
}
