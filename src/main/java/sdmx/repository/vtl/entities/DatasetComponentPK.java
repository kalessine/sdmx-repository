/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdmx.repository.vtl.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author jsg
 */
@Embeddable
public class DatasetComponentPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "dataset", nullable = false)
    private long dataset;
    @Basic(optional = false)
    @Column(name = "columnid", nullable = false, length = 100)
    private String columnid;

    public DatasetComponentPK() {
    }

    public DatasetComponentPK(long dataset, String columnid) {
        this.dataset = dataset;
        this.columnid = columnid;
    }

    public long getDataset() {
        return dataset;
    }

    public void setDataset(long dataset) {
        this.dataset = dataset;
    }

    public String getColumnid() {
        return columnid;
    }

    public void setColumnid(String columnid) {
        this.columnid = columnid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) dataset;
        hash += (columnid != null ? columnid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DatasetComponentPK)) {
            return false;
        }
        DatasetComponentPK other = (DatasetComponentPK) object;
        if (this.dataset != other.dataset) {
            return false;
        }
        if ((this.columnid == null && other.columnid != null) || (this.columnid != null && !this.columnid.equals(other.columnid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sdmx.repository.vtl.entities.DatasetComponentPK[ dataset=" + dataset + ", columnid=" + columnid + " ]";
    }
    
}
