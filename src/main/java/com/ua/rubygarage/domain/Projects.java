package com.ua.rubygarage.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class Projects implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    @OneToMany(mappedBy = "projects",cascade = CascadeType.REMOVE)
    private List<Tasks> tasksList;

    @ManyToOne()
    private UserProjects userProjects;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Tasks> getTasksList() {
        return tasksList;
    }

    public void setTasksList(List<Tasks> tasksList) {
        this.tasksList = tasksList;
    }

    public UserProjects getUserProjects() {
        return userProjects;
    }

    public void setUserProjects(UserProjects userProjects) {
        this.userProjects = userProjects;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Projects)) return false;

        Projects projects = (Projects) o;

        if (id != projects.id) return false;
        return name != null ? name.equals(projects.name) : projects.name == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Projects{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
