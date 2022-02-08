package sdmx.repository.entities;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import sdmx.repository.entities.ComponentValuePK;
import sdmx.repository.entities.DataflowComponent;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2022-02-09T05:52:35", comments="EclipseLink-2.7.7.v20200504-rNA")
@StaticMetamodel(ComponentValue.class)
public class ComponentValue_ { 

    public static volatile SingularAttribute<ComponentValue, ComponentValuePK> componentValuePK;
    public static volatile SingularAttribute<ComponentValue, String> value;
    public static volatile SingularAttribute<ComponentValue, DataflowComponent> dataflowComponent;

}