/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdmx.repository.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "NameText", catalog = "repository", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NameText.findAll", query = "SELECT n FROM NameText n"),
    @NamedQuery(name = "NameText.findById", query = "SELECT n FROM NameText n WHERE n.nameTextPK.id = :id"),
    @NamedQuery(name = "NameText.findByLang", query = "SELECT n FROM NameText n WHERE n.nameTextPK.lang = :lang")})
public class NameText implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected NameTextPK nameTextPK;
    @Lob
    @Column(name = "text", length = 65535)
    private String text;
    @JoinColumn(name = "lang", referencedColumnName = "lang", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Language language;
    @JoinColumn(name = "id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Name name;

    public NameText() {
    }

    public NameText(NameTextPK nameTextPK) {
        this.nameTextPK = nameTextPK;
    }

    public NameText(long id, String lang) {
        this.nameTextPK = new NameTextPK(id, lang);
    }

    public NameTextPK getNameTextPK() {
        return nameTextPK;
    }

    public void setNameTextPK(NameTextPK nameTextPK) {
        this.nameTextPK = nameTextPK;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
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
        hash += (nameTextPK != null ? nameTextPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NameText)) {
            return false;
        }
        NameText other = (NameText) object;
        if ((this.nameTextPK == null && other.nameTextPK != null) || (this.nameTextPK != null && !this.nameTextPK.equals(other.nameTextPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sdmx.repository.entities.NameText[ nameTextPK=" + nameTextPK + " ]";
    }
    
}
