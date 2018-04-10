package com.ua.rubygarage.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Tasks implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private boolean status;
    private Date date;
    private Integer rating;
    private double reorder;

    @ManyToOne()
    private Projects projects;

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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Projects getProjects() {
        return projects;
    }

    public void setProjects(Projects projects) {
        this.projects = projects;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public double getReorder() {
        return reorder;
    }

    public void setReorder(double reorder) {
        this.reorder = reorder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tasks)) return false;

        Tasks tasks = (Tasks) o;

        if (id != tasks.id) return false;
        if (status != tasks.status) return false;
        if (Double.compare(tasks.reorder, reorder) != 0) return false;
        if (name != null ? !name.equals(tasks.name) : tasks.name != null) return false;
        if (date != null ? !date.equals(tasks.date) : tasks.date != null) return false;
        return rating != null ? rating.equals(tasks.rating) : tasks.rating == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (status ? 1 : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (rating != null ? rating.hashCode() : 0);
        temp = Double.doubleToLongBits(reorder);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Tasks{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", date=" + date +
                ", rating=" + rating +
                ", reorder=" + reorder +
                '}';
    }
}
