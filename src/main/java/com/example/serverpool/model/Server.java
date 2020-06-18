package com.example.serverpool.model;

import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "servers")
public class Server {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Size(max = 100)
    private long memory;

    @NotNull
    @Column(name = "is_cloud")
    private boolean isCloud;

    @NotNull
    @Column(name = "is_active")
    private boolean isActive;

    @ManyToOne(cascade={CascadeType.ALL})
    @JoinColumn(name="parent_id")
    private Server parent;

    @OneToMany(mappedBy="parent", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Server> childs = new HashSet<Server>();

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Server() {

    }

    public Server(long memory, boolean isCloud, boolean isActive) {
        this.memory = memory;
        this.isCloud = isCloud;
        this.isActive = isActive;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMemory() {
        return memory;
    }

    public void setMemory(long memory) {
        this.memory = memory;
    }

    public boolean isCloud() {
        return isCloud;
    }

    public void setCloud(boolean cloud) {
        isCloud = cloud;
    }

    public Server getParent() {
        return parent;
    }

    public void setParent(Server parent) {
        this.parent = parent;
    }

    public Set<Server> getChilds() {
        return childs;
    }

    public void setChilds(Set<Server> childs) {
        this.childs = childs;
    }
}
