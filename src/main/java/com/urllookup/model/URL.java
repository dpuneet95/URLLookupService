package com.urllookup.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Getter
@Table(name = "urllookup")
public class URL {
    @Id
    String url;

    public URL(String url) {
        super();
        this.url = url;
    }

    public URL() {
        super();
    }
}
