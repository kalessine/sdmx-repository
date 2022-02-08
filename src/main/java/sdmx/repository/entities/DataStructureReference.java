/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdmx.repository.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "DataStructureReference", catalog = "repository", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DataStructureReference.findAll", query = "SELECT d FROM DataStructureReference d"),
    @NamedQuery(name = "DataStructureReference.findByRefid", query = "SELECT d FROM DataStructureReference d WHERE d.refid = :refid"),
    @NamedQuery(name = "DataStructureReference.findByAgencyid", query = "SELECT d FROM DataStructureReference d WHERE d.agencyid = :agencyid"),
    @NamedQuery(name = "DataStructureReference.findById", query = "SELECT d FROM DataStructureReference d WHERE d.id = :id"),
    @NamedQuery(name = "DataStructureReference.findByVersion", query = "SELECT d FROM DataStructureReference d WHERE d.version = :version")})
public class DataStructureReference implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "refid", nullable = false)
    private Long refid;
    @Column(name = "agencyid", length = 100)
    private String agencyid;
    @Column(name = "id", length = 100)
    private String id;
    @Column(name = "version", length = 50)
    private String version;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "structure")
    private List<Dataflow> dataflowList;

    public DataStructureReference() {
    }

    public DataStructureReference(Long refid) {
        this.refid = refid;
    }

    public Long getRefid() {
        return refid;
    }

    public void setRefid(Long refid) {
        this.refid = refid;
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
    public List<Dataflow> getDataflowList() {
        return dataflowList;
    }

    public void setDataflowList(List<Dataflow> dataflowList) {
        this.dataflowList = dataflowList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (refid != null ? refid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DataStructureReference)) {
            return false;
        }
        DataStructureReference other = (DataStructureReference) object;
        if ((this.refid == null && other.refid != null) || (this.refid != null && !this.refid.equals(other.refid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sdmx.repository.entities.DataStructureReference[ refid=" + refid + " ]";
    }
    
}
