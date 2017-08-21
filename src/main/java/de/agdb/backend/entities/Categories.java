package de.agdb.backend.entities;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.jdo.annotations.Unique;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Created by Riva on 22.03.2017.
 */
@Entity
@Table(name = "category")
public class Categories implements Serializable {

    // An autogenerated id (unique for each category in the db)
    @Id
    @Column(name = "CATEGORY_ID", updatable=false, nullable=false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull
    @Unique
    private String title;
    @NotNull
    private String description;
    @NotNull
    @Unique
    private String shortCut;
    private String shortCutColorCss;
    private int shortCutColorRGB;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = Contact.class)
    @JoinTable(name = "CATEGORY_CONTACTS", joinColumns = { @JoinColumn(name = "CATEGORY_ID") }, inverseJoinColumns = { @JoinColumn(name = "CONTACT_ID") })
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Contact> contacts;


    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public String getShortCut() {
        return shortCut;
    }

    public void setShortCut(String shortCut) {
        this.shortCut = shortCut;
    }



    public Categories() {

    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void addContact(Contact contact) {
        boolean flag = true;
        for (int i=0;i<this.contacts.size();i++) {
            if (contact.getEmail().equals(this.contacts.get(i).getEmail())) {
                flag = false;
                break;
            }
        }
        if (flag) {
            this.contacts.add(contact);

        }
    }

    public String getShortCutColorCss() {
        return shortCutColorCss;
    }

    public void setShortCutColorCss(String shortCutColorCss) {
        this.shortCutColorCss = shortCutColorCss;
    }

    public int getShortCutColorRGB() {
        return shortCutColorRGB;
    }

    public void setShortCutColorRGB(int shortCutColorRGB) {
        this.shortCutColorRGB = shortCutColorRGB;
    }
}
