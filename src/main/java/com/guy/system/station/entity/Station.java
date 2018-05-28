package com.guy.system.station.entity;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.guy.system.user.entity.User;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.HashSet;
import java.util.Set;

/**
 * 类名称：StationEntity
 * 创建人：blank
 * 创建时间：2018-01-19
 */
@Entity
@Table(name = "sys_station")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DynamicUpdate
@DynamicInsert
public class Station implements java.io.Serializable {

    // Fields
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String name;
    private String code;
    @JsonIgnore
    private Set<User> users = new HashSet<>(0);


    /**
     * default constructor
     */
    public Station() {
    }

    // Property accessors
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "CODE")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "station",fetch=FetchType.LAZY)
    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}