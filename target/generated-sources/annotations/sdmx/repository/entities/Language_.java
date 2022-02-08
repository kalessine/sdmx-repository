package sdmx.repository.entities;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import sdmx.repository.entities.NameText;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2022-02-08T20:48:49", comments="EclipseLink-2.7.7.v20200504-rNA")
@StaticMetamodel(Language.class)
public class Language_ { 

    public static volatile ListAttribute<Language, NameText> nameTextList;
    public static volatile SingularAttribute<Language, String> name;
    public static volatile SingularAttribute<Language, String> lang;

}