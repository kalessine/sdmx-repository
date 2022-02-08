package sdmx.repository.entities;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import sdmx.repository.entities.Annotated;
import sdmx.repository.entities.DataStructureReference;
import sdmx.repository.entities.DataflowComponent;
import sdmx.repository.entities.Name;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2022-02-09T05:52:35", comments="EclipseLink-2.7.7.v20200504-rNA")
@StaticMetamodel(Dataflow.class)
public class Dataflow_ { 

    public static volatile ListAttribute<Dataflow, DataflowComponent> dataflowComponentList;
    public static volatile SingularAttribute<Dataflow, Long> dataflow;
    public static volatile SingularAttribute<Dataflow, Name> name;
    public static volatile SingularAttribute<Dataflow, Annotated> annotations;
    public static volatile SingularAttribute<Dataflow, String> agencyid;
    public static volatile SingularAttribute<Dataflow, String> id;
    public static volatile SingularAttribute<Dataflow, String> version;
    public static volatile SingularAttribute<Dataflow, DataStructureReference> structure;

}