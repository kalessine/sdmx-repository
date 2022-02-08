package sdmx.repository.entities;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import sdmx.repository.entities.Language;
import sdmx.repository.entities.Name;
import sdmx.repository.entities.NameTextPK;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2022-02-09T05:52:35", comments="EclipseLink-2.7.7.v20200504-rNA")
@StaticMetamodel(NameText.class)
public class NameText_ { 

    public static volatile SingularAttribute<NameText, NameTextPK> nameTextPK;
    public static volatile SingularAttribute<NameText, Name> name;
    public static volatile SingularAttribute<NameText, Language> language;
    public static volatile SingularAttribute<NameText, String> text;

}