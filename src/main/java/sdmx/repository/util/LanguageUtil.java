/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdmx.repository.util;

import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import sdmx.repository.entities.Language;

/**
 *
 * @author James
 */
public class LanguageUtil {

    public static sdmx.repository.entities.Language EN = new sdmx.repository.entities.Language();

    public static sdmx.repository.entities.Language FR = new sdmx.repository.entities.Language();

    static {
        EN.setLang("en");
        EN.setName("English");
        FR.setLang("fr");
        FR.setName("French");
    }
    public static Language[] LANGS = new Language[]{EN, FR};

    public static Language lookup(String s) {
        for (int i = 0; i < LANGS.length; i++) {
            if (LANGS[i].getLang().equals(s)) {
                return LANGS[i];
            }
        }
        return null;
    }

    public static void init(EntityManager em) {
        try {
            Query query = em.createNamedQuery("Language.findAll");
            LANGS = ((List<Language>) query.getResultList()).toArray(new Language[]{});
            System.out.println(Arrays.toString(LANGS));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if(LANGS.length==0){
             init2(em);
        }
    }
    public static void init2(EntityManager em) {
        try{
            em.getTransaction().begin();
            em.persist(EN);
            em.persist(FR);
        }catch(Exception ex) {}
        finally{
            em.getTransaction().commit();
        }
        try {
            Query query = em.createNamedQuery("Language.findAll");
            LANGS = ((List<Language>) query.getResultList()).toArray(LANGS);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}