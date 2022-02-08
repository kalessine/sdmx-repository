/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdmx.repository.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
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
@Table(name = "AnnotationText", catalog = "repository", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AnnotationText.findAll", query = "SELECT a FROM AnnotationText a"),
    @NamedQuery(name = "AnnotationText.findById", query = "SELECT a FROM AnnotationText a WHERE a.id = :id"),
    @NamedQuery(name = "AnnotationText.findByLang", query = "SELECT a FROM AnnotationText a WHERE a.lang = :lang")})
public class AnnotationText implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @Lob
    @Column(name = "text", length = 65535)
    private String text;
    @Column(name = "lang", length = 10)
    private String lang;
    @JoinColumn(name = "annotation", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Annotation annotation;

    public AnnotationText() {
    }

    public AnnotationText(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Annotation getAnnotation() {
        return annotation;
    }

    public void setAnnotation(Annotation annotation) {
        this.annotation = annotation;
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
        if (!(object instanceof AnnotationText)) {
            return false;
        }
        AnnotationText other = (AnnotationText) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sdmx.repository.entities.AnnotationText[ id=" + id + " ]";
    }
    
}
