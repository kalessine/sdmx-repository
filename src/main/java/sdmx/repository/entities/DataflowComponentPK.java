/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdmx.repository.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author jsg
 */
@Embeddable
public class DataflowComponentPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "dataflow", nullable = false)
    private long dataflow;
    @Basic(optional = false)
    @Column(name = "columnid", nullable = false, length = 100)
    private String columnid;

    public DataflowComponentPK() {
    }

    public DataflowComponentPK(long dataflow, String columnid) {
        this.dataflow = dataflow;
        this.columnid = columnid;
    }

    public long getDataflow() {
        return dataflow;
    }

    public void setDataflow(long dataflow) {
        this.dataflow = dataflow;
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
        hash += (int) dataflow;
        hash += (columnid != null ? columnid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DataflowComponentPK)) {
            return false;
        }
        DataflowComponentPK other = (DataflowComponentPK) object;
        if (this.dataflow != other.dataflow) {
            return false;
        }
        if ((this.columnid == null && other.columnid != null) || (this.columnid != null && !this.columnid.equals(other.columnid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sdmx.repository.entities.DataflowComponentPK[ dataflow=" + dataflow + ", columnid=" + columnid + " ]";
    }
    
}
