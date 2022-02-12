/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdmx.repository;

import fr.insee.vtl.model.Dataset;
import fr.insee.vtl.model.Structured;
import sdmx.repository.WritableRepository;
import sdmx.repository.exception.RepositoryException;

public interface VTLRepository {
public boolean hasDataset(String name);
public void createDataset(String name, Dataset s) throws RepositoryException;
public void deleteDataset(String name) throws RepositoryException;
public void appendDataset(String name, fr.insee.vtl.model.Dataset ds) throws RepositoryException;
public void replaceDataset(String name, fr.insee.vtl.model.Dataset ds) throws RepositoryException;
public Dataset getDataset(String name) throws RepositoryException;
}
