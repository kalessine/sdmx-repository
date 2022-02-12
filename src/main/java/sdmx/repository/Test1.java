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
public class Test1 {

    public static void main(String args[]) {
        try {
            VTLRepository vtl = new VTLDatabaseRepository();
            ScriptEngine engine = new ScriptEngineManager().getEngineByName("vtl");
            Bindings bindings = new SimpleBindings();
// By default, if a variable role is not defined, the `MEASURE` will be affected.
            Dataset dataset = vtl.getDataset("test");
            bindings.put("ds", dataset);
            System.out.println(dataset.getDataPoints());
            ScriptContext context = engine.getContext();
            context.setBindings(bindings, ScriptContext.ENGINE_SCOPE);
            String script = "res:=ds[filter var2=\"y\"];";
            try {
                engine.eval(script);
            } catch (ScriptException e) {
                e.printStackTrace();
            }
            Bindings outputBindings = engine.getContext().getBindings(ScriptContext.ENGINE_SCOPE);
            Dataset res = (Dataset) outputBindings.get("res");
            vtl.createDataset("res", res);
            vtl.appendDataset("res", res);
        } catch (RepositoryException ex) {
            Logger.getLogger(Test1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
