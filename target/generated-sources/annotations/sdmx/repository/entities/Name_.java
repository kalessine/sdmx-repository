package sdmx.repository.entities;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import sdmx.repository.entities.Dataflow;
import sdmx.repository.entities.NameText;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2022-02-09T05:52:35", comments="EclipseLink-2.7.7.v20200504-rNA")
@StaticMetamodel(Name.class)
public class Name_ { 

    public static volatile ListAttribute<Name, NameText> nameTextList;
    public static volatile ListAttribute<Name, Dataflow> dataflowList;
    public static volatile SingularAttribute<Name, String> en;
    public static volatile SingularAttribute<Name, Long> id;

}