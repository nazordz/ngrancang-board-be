package com.unindra.ngrancang.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "roles")
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Role {
    @Id
    @GeneratedValue(generator = "uuidv4")
    @GenericGenerator(name = "uuidv4", strategy = "org.hibernate.id.UUIDGenerator")
    // @JsonView(Views.Basic.class)
    private UUID id;

    @Column
    // @JsonView(Views.Basic.class)
    private String name;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    @JsonBackReference
    // @JsonView(Views.Nested.class)
    private List<User> users = new ArrayList<User>();

    @Column(name = "created_at")
    @CreationTimestamp
    // @JsonView(Views.Basic.class)
    private Timestamp createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    // @JsonView(Views.Basic.class)
    private Timestamp updatedAt;
}
