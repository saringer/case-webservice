package de.agdb.backend.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
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
    private String title;
    @NotNull
    private String description;
    private String shortCut;
    private String shortCutColorCss;
    private int shortCutColorRGB;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = Categories.class)
    @JoinTable(name = "CATEGORY_CONTACTS", joinColumns = { @JoinColumn(name = "CATEGORY_ID") }, inverseJoinColumns = { @JoinColumn(name = "CONTACT_ID") })
    private Set<Contact> contacts;


    public Set<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(Set<Contact> contacts) {
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
        this.contacts.add(contact);
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
