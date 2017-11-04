package com.dvdstore.model;

import javax.persistence.*;

@Entity
@Table(name = "disc")
public class Disc {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "disc_id")
    private int id;

    @Column(name = "title")
    private String title;

    @OneToOne
    @JoinColumn(name="user_id")
    private User owner;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

}
