package com.ua.rubygarage.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class UserProjects implements Serializable{
    @Id
    private String login;
    private String password;

    @OneToMany(mappedBy = "userProjects")
    private List<Projects> projectsList;


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Projects> getProjectsList() {
        return projectsList;
    }

    public void setProjectsList(List<Projects> projectsList) {
        this.projectsList = projectsList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserProjects)) return false;

        UserProjects that = (UserProjects) o;

        if (login != null ? !login.equals(that.login) : that.login != null) return false;
        return password != null ? password.equals(that.password) : that.password == null;
    }

    @Override
    public int hashCode() {
        int result = login != null ? login.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserProjects{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
