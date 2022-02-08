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
@Table(name = "Annotation", catalog = "repository", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Annotation.findAll", query = "SELECT a FROM Annotation a"),
    @NamedQuery(name = "Annotation.findById", query = "SELECT a FROM Annotation a WHERE a.id = :id"),
    @NamedQuery(name = "Annotation.findByTitle", query = "SELECT a FROM Annotation a WHERE a.title = :title"),
    @NamedQuery(name = "Annotation.findByUrl", query = "SELECT a FROM Annotation a WHERE a.url = :url"),
    @NamedQuery(name = "Annotation.findByType", query = "SELECT a FROM Annotation a WHERE a.type = :type"),
    @NamedQuery(name = "Annotation.findByAnnotationid", query = "SELECT a FROM Annotation a WHERE a.annotationid = :annotationid")})
public class Annotation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "title", length = 500)
    private String title;
    @Column(name = "url", length = 1000)
    private String url;
    @Column(name = "type", length = 500)
    private String type;
    @Column(name = "annotationid", length = 500)
    private String annotationid;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "annotation")
    private List<AnnotationText> annotationTextList;
    @JoinColumn(name = "annotated", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Annotated annotated;

    public Annotation() {
    }

    public Annotation(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAnnotationid() {
        return annotationid;
    }

    public void setAnnotationid(String annotationid) {
        this.annotationid = annotationid;
    }

    @XmlTransient
    public List<AnnotationText> getAnnotationTextList() {
        return annotationTextList;
    }

    public void setAnnotationTextList(List<AnnotationText> annotationTextList) {
        this.annotationTextList = annotationTextList;
    }

    public Annotated getAnnotated() {
        return annotated;
    }

    public void setAnnotated(Annotated annotated) {
        this.annotated = annotated;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Annotation)) {
            return false;
        }
        Annotation other = (Annotation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sdmx.repository.entities.Annotation[ id=" + id + " ]";
    }
    
}
