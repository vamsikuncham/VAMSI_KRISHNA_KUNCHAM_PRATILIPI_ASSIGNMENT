package com.pratilipi.dao.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Table
@Entity(name = "Users")
@Access(AccessType.FIELD)
@Cacheable(false)
@Data
public class Users {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;

    @NotNull
    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "created_at", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
}
