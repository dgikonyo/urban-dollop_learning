package com.kcbgroup.learning.jugtours.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

import java.time.Instant;
import java.util.Set;

@Entity
public class Event {
    @Id
    @GeneratedValue
    private Long id;
    private Instant date;
    private String title;
    private String description;

    @ManyToMany
    private Set<User> attendees;

    public Event() {}

    public Event(Instant date, String title, String description) {
        this.date = date;
        this.title = title;
        this.description = description;
    }
}
