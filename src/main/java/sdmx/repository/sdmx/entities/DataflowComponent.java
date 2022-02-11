/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdmx.repository.sdmx.entities;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jsg
 */
@Entity
@Table(name = "DataflowComponent", catalog = "repository", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DataflowComponent.findAll", query = "SELECT d FROM DataflowComponent d"),
    @NamedQuery(name = "DataflowComponent.findByDataflow", query = "SELECT d FROM DataflowComponent d WHERE d.dataflowComponentPK.dataflow = :dataflow"),
    @NamedQuery(name = "DataflowComponent.findByColumnid", query = "SELECT d FROM DataflowComponent d WHERE d.dataflowComponentPK.columnid = :columnid"),
    @NamedQuery(name = "DataflowComponent.findByType", query = "SELECT d FROM DataflowComponent d WHERE d.type = :type"),
    @NamedQuery(name = "DataflowComponent.findByPosition", query = "SELECT d FROM DataflowComponent d WHERE d.position = :position")})
public class DataflowComponent implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DataflowComponentPK dataflowComponentPK;
    @Column(name = "type")
    private Integer type;
    @Column(name = "position")
    private Integer position;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "dataflowComponent")
    private ComponentValue componentValue;
    @JoinColumn(name = "dataflow", referencedColumnName = "dataflow", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Dataflow dataflow1;

    public DataflowComponent() {
    }

    public DataflowComponent(DataflowComponentPK dataflowComponentPK) {
        this.dataflowComponentPK = dataflowComponentPK;
    }

    public DataflowComponent(long dataflow, String columnid) {
        this.dataflowComponentPK = new DataflowComponentPK(dataflow, columnid);
    }

    public DataflowComponentPK getDataflowComponentPK() {
        return dataflowComponentPK;
    }

    public void setDataflowComponentPK(DataflowComponentPK dataflowComponentPK) {
        this.dataflowComponentPK = dataflowComponentPK;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public ComponentValue getComponentValue() {
        return componentValue;
    }

    public void setComponentValue(ComponentValue componentValue) {
        this.componentValue = componentValue;
    }

    public Dataflow getDataflow1() {
        return dataflow1;
    }

    public void setDataflow1(Dataflow dataflow1) {
        this.dataflow1 = dataflow1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dataflowComponentPK != null ? dataflowComponentPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DataflowComponent)) {
            return false;
        }
        DataflowComponent other = (DataflowComponent) object;
        if ((this.dataflowComponentPK == null && other.dataflowComponentPK != null) || (this.dataflowComponentPK != null && !this.dataflowComponentPK.equals(other.dataflowComponentPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sdmx.repository.sdmx.entities.DataflowComponent[ dataflowComponentPK=" + dataflowComponentPK + " ]";
    }
    
}
