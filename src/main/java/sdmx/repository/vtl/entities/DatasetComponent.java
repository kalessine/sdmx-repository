/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdmx.repository.vtl.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jsg
 */
@Entity
@Table(name = "DatasetComponent", catalog = "vtlrepository", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DatasetComponent.findAll", query = "SELECT d FROM DatasetComponent d"),
    @NamedQuery(name = "DatasetComponent.findByDataset", query = "SELECT d FROM DatasetComponent d WHERE d.datasetComponentPK.dataset = :dataset"),
    @NamedQuery(name = "DatasetComponent.findByColumnid", query = "SELECT d FROM DatasetComponent d WHERE d.datasetComponentPK.columnid = :columnid"),
    @NamedQuery(name = "DatasetComponent.findByType", query = "SELECT d FROM DatasetComponent d WHERE d.type = :type"),
    @NamedQuery(name = "DatasetComponent.findByPosition", query = "SELECT d FROM DatasetComponent d WHERE d.position = :position"),
    @NamedQuery(name = "DatasetComponent.findByRole", query = "SELECT d FROM DatasetComponent d WHERE d.role = :role")})
public class DatasetComponent implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DatasetComponentPK datasetComponentPK;
    @Column(name = "type", length = 300)
    private String type;
    @Column(name = "position")
    private Integer position;
    @Column(name = "role")
    private Integer role;
    @JoinColumn(name = "dataset", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Dataset dataset1;

    public DatasetComponent() {
    }

    public DatasetComponent(DatasetComponentPK datasetComponentPK) {
        this.datasetComponentPK = datasetComponentPK;
    }

    public DatasetComponent(long dataset, String columnid) {
        this.datasetComponentPK = new DatasetComponentPK(dataset, columnid);
    }

    public DatasetComponentPK getDatasetComponentPK() {
        return datasetComponentPK;
    }

    public void setDatasetComponentPK(DatasetComponentPK datasetComponentPK) {
        this.datasetComponentPK = datasetComponentPK;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public Dataset getDataset1() {
        return dataset1;
    }

    public void setDataset1(Dataset dataset1) {
        this.dataset1 = dataset1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (datasetComponentPK != null ? datasetComponentPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DatasetComponent)) {
            return false;
        }
        DatasetComponent other = (DatasetComponent) object;
        if ((this.datasetComponentPK == null && other.datasetComponentPK != null) || (this.datasetComponentPK != null && !this.datasetComponentPK.equals(other.datasetComponentPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sdmx.repository.vtl.entities.DatasetComponent[ datasetComponentPK=" + datasetComponentPK + " ]";
    }
    
}
