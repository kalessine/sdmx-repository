/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdmx.repository.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
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
@Table(name = "ComponentValue", catalog = "repository", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ComponentValue.findAll", query = "SELECT c FROM ComponentValue c"),
    @NamedQuery(name = "ComponentValue.findByDataflow", query = "SELECT c FROM ComponentValue c WHERE c.componentValuePK.dataflow = :dataflow"),
    @NamedQuery(name = "ComponentValue.findByColumnid", query = "SELECT c FROM ComponentValue c WHERE c.componentValuePK.columnid = :columnid"),
    @NamedQuery(name = "ComponentValue.findByValue", query = "SELECT c FROM ComponentValue c WHERE c.value = :value")})
public class ComponentValue implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ComponentValuePK componentValuePK;
    @Basic(optional = false)
    @Column(name = "value", nullable = false, length = 300)
    private String value;
    @JoinColumns({
        @JoinColumn(name = "dataflow", referencedColumnName = "dataflow", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "columnid", referencedColumnName = "columnid", nullable = false, insertable = false, updatable = false)})
    @OneToOne(optional = false)
    private DataflowComponent dataflowComponent;

    public ComponentValue() {
    }

    public ComponentValue(ComponentValuePK componentValuePK) {
        this.componentValuePK = componentValuePK;
    }

    public ComponentValue(ComponentValuePK componentValuePK, String value) {
        this.componentValuePK = componentValuePK;
        this.value = value;
    }

    public ComponentValue(long dataflow, String columnid) {
        this.componentValuePK = new ComponentValuePK(dataflow, columnid);
    }

    public ComponentValuePK getComponentValuePK() {
        return componentValuePK;
    }

    public void setComponentValuePK(ComponentValuePK componentValuePK) {
        this.componentValuePK = componentValuePK;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public DataflowComponent getDataflowComponent() {
        return dataflowComponent;
    }

    public void setDataflowComponent(DataflowComponent dataflowComponent) {
        this.dataflowComponent = dataflowComponent;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (componentValuePK != null ? componentValuePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ComponentValue)) {
            return false;
        }
        ComponentValue other = (ComponentValue) object;
        if ((this.componentValuePK == null && other.componentValuePK != null) || (this.componentValuePK != null && !this.componentValuePK.equals(other.componentValuePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sdmx.repository.entities.ComponentValue[ componentValuePK=" + componentValuePK + " ]";
    }
    
}
