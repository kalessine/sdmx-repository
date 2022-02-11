/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdmx.repository.sdmx.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jsg
 */
@Entity
@Table(name = "Dataflow", catalog = "repository", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Dataflow.findAll", query = "SELECT d FROM Dataflow d"),
    @NamedQuery(name = "Dataflow.findByDataflow", query = "SELECT d FROM Dataflow d WHERE d.dataflow = :dataflow"),
    @NamedQuery(name = "Dataflow.findByAgencyid", query = "SELECT d FROM Dataflow d WHERE d.agencyid = :agencyid"),
    @NamedQuery(name = "Dataflow.findById", query = "SELECT d FROM Dataflow d WHERE d.id = :id"),
    @NamedQuery(name = "Dataflow.findByVersion", query = "SELECT d FROM Dataflow d WHERE d.version = :version")})
public class Dataflow implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "dataflow", nullable = false)
    private Long dataflow;
    @Column(name = "agencyid", length = 100)
    private String agencyid;
    @Column(name = "id", length = 100)
    private String id;
    @Column(name = "version", length = 50)
    private String version;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dataflow1")
    private List<DataflowComponent> dataflowComponentList;
    @JoinColumn(name = "annotations", referencedColumnName = "id")
    @ManyToOne
    private Annotated annotations;
    @JoinColumn(name = "structure", referencedColumnName = "refid", nullable = false)
    @ManyToOne(optional = false)
    private DataStructureReference structure;
    @JoinColumn(name = "name", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Name name;

    public Dataflow() {
    }

    public Dataflow(Long dataflow) {
        this.dataflow = dataflow;
    }

    public Long getDataflow() {
        return dataflow;
    }

    public void setDataflow(Long dataflow) {
        this.dataflow = dataflow;
    }

    public String getAgencyid() {
        return agencyid;
    }

    public void setAgencyid(String agencyid) {
        this.agencyid = agencyid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @XmlTransient
    public List<DataflowComponent> getDataflowComponentList() {
        return dataflowComponentList;
    }

    public void setDataflowComponentList(List<DataflowComponent> dataflowComponentList) {
        this.dataflowComponentList = dataflowComponentList;
    }

    public Annotated getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Annotated annotations) {
        this.annotations = annotations;
    }

    public DataStructureReference getStructure() {
        return structure;
    }

    public void setStructure(DataStructureReference structure) {
        this.structure = structure;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dataflow != null ? dataflow.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Dataflow)) {
            return false;
        }
        Dataflow other = (Dataflow) object;
        if ((this.dataflow == null && other.dataflow != null) || (this.dataflow != null && !this.dataflow.equals(other.dataflow))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sdmx.repository.sdmx.entities.Dataflow[ dataflow=" + dataflow + " ]";
    }
    
}
