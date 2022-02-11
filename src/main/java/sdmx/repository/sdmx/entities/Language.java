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
import javax.persistence.Id;
import javax.persistence.Lob;
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
@Table(name = "Language", catalog = "repository", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Language.findAll", query = "SELECT l FROM Language l"),
    @NamedQuery(name = "Language.findByLang", query = "SELECT l FROM Language l WHERE l.lang = :lang")})
public class Language implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Lob
    @Column(name = "name", nullable = false, length = 65535)
    private String name;
    @Id
    @Basic(optional = false)
    @Column(name = "lang", nullable = false, length = 10)
    private String lang;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "language")
    private List<NameText> nameTextList;

    public Language() {
    }

    public Language(String lang) {
        this.lang = lang;
    }

    public Language(String lang, String name) {
        this.lang = lang;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    @XmlTransient
    public List<NameText> getNameTextList() {
        return nameTextList;
    }

    public void setNameTextList(List<NameText> nameTextList) {
        this.nameTextList = nameTextList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (lang != null ? lang.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Language)) {
            return false;
        }
        Language other = (Language) object;
        if ((this.lang == null && other.lang != null) || (this.lang != null && !this.lang.equals(other.lang))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sdmx.repository.sdmx.entities.Language[ lang=" + lang + " ]";
    }
    
}
