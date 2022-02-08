package sdmx.repository.entities;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import sdmx.repository.entities.Annotation;
import sdmx.repository.entities.Dataflow;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2022-02-08T20:48:49", comments="EclipseLink-2.7.7.v20200504-rNA")
@StaticMetamodel(Annotated.class)
public class Annotated_ { 

    public static volatile ListAttribute<Annotated, Annotation> annotationList;
    public static volatile ListAttribute<Annotated, Dataflow> dataflowList;
    public static volatile SingularAttribute<Annotated, String> padfield;
    public static volatile SingularAttribute<Annotated, Long> id;

}