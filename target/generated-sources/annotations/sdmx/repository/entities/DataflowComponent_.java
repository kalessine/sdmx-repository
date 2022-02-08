package sdmx.repository.entities;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import sdmx.repository.entities.ComponentValue;
import sdmx.repository.entities.Dataflow;
import sdmx.repository.entities.DataflowComponentPK;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2022-02-09T05:52:35", comments="EclipseLink-2.7.7.v20200504-rNA")
@StaticMetamodel(DataflowComponent.class)
public class DataflowComponent_ { 

    public static volatile SingularAttribute<DataflowComponent, ComponentValue> componentValue;
    public static volatile SingularAttribute<DataflowComponent, DataflowComponentPK> dataflowComponentPK;
    public static volatile SingularAttribute<DataflowComponent, Integer> position;
    public static volatile SingularAttribute<DataflowComponent, Integer> type;
    public static volatile SingularAttribute<DataflowComponent, Dataflow> dataflow1;

}