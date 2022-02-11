/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdmx.repository.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import sdmx.common.TextType;
import sdmx.repository.sdmx.entities.Dataflow;
import sdmx.repository.sdmx.entities.Name;
import sdmx.structure.codelist.CodeType;
import sdmx.structure.codelist.CodelistType;
import sdmx.structure.concept.ConceptSchemeType;
import sdmx.structure.concept.ConceptType;
import sdmx.structure.dataflow.DataflowType;
import sdmx.structure.datastructure.DataStructureType;

/**
 *
 * @author James
 */
public class NameUtil {

    public static void setName(EntityManager em, Dataflow df1, DataflowType df) {
        sdmx.repository.sdmx.entities.Name name = toDatabaseName(em, df.getNames());
        df1.setName(name);
    }
    public static void setName(EntityManager em, Dataflow df1, String nam, String lang) {
        sdmx.repository.sdmx.entities.Name name = toDatabaseName(em, List.of(new sdmx.common.Name(lang,nam)));
        df1.setName(name);
    }

    public static sdmx.repository.sdmx.entities.Name toDatabaseName(EntityManager em, List<sdmx.common.Name> names) {
        sdmx.repository.sdmx.entities.Name nm = new sdmx.repository.sdmx.entities.Name();
        List<sdmx.repository.sdmx.entities.NameText> result = new ArrayList<>();
        em.persist(nm);
        em.flush();
        em.refresh(nm);
        for (int i = 0; i < names.size(); i++) {
            result.add(toDatabaseNameText(nm, names.get(i)));
        }
        nm.setNameTextList(result);
        em.merge(nm);
        return nm;
    }

    public static sdmx.repository.sdmx.entities.NameText toDatabaseNameText(sdmx.repository.sdmx.entities.Name nam, sdmx.common.Name name) {
        sdmx.repository.sdmx.entities.NameText nm = new sdmx.repository.sdmx.entities.NameText();
        sdmx.repository.sdmx.entities.NameTextPK pk = new sdmx.repository.sdmx.entities.NameTextPK();
        pk.setId(nam.getId());
        nm.setText(name.getText());
        if ("en".equals(name.getLang())) {
            nam.setEn(name.getText());
        }
        pk.setLang(name.getLang());
        nm.setNameTextPK(pk);
        return nm;
    }

    public static List<sdmx.common.Name> toSDMXName(sdmx.repository.sdmx.entities.Name name) {
        List<sdmx.common.Name> result = new ArrayList<sdmx.common.Name>();
        for (sdmx.repository.sdmx.entities.NameText nt : name.getNameTextList()) {
            result.add(new sdmx.common.Name(nt.getNameTextPK().getLang(), nt.getText()));
        }
        return result;
    }

}
