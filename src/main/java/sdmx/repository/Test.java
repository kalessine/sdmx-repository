/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdmx.repository;

import fr.insee.vtl.model.Dataset;
import fr.insee.vtl.model.InMemoryDataset;
import java.util.Arrays;
import java.util.Collections;
import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleBindings;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptException;
import sdmx.repository.exception.RepositoryException;
import sdmx.repository.service.VTLDatabaseRepository;
/**
 *
 * @author jsg
 */
public class Test {
    public static void main(String args[]) {
        try {
            InMemoryDataset dataset = new InMemoryDataset(
                    List.of(
                            Map.of("var1", "x", "var2", "y", "var3", 5l),
                            Map.of("var1", "xx", "var2", "yy", "var3", 10l)
                    ),
                    Map.of("var1", String.class, "var2", String.class, "var3", Long.class),
                    Map.of("var1",Dataset.Role.IDENTIFIER,"var2",Dataset.Role.IDENTIFIER,"var3",Dataset.Role.MEASURE)
            );
            VTLRepository vtl = new VTLDatabaseRepository();
            vtl.createDataset("test", dataset);
            vtl.appendDataset("test", dataset);
            
        } catch (RepositoryException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
