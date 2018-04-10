package com.ua.rubygarage.ejb;

import com.ua.rubygarage.domain.UserProjects;
import org.apache.commons.lang3.StringUtils;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@LocalBean
public class UserProjectsManagerBean {
    @PersistenceContext(unitName = "examplePU")
    private EntityManager entityManager;

    public boolean createUser(String login,String pass) {
        UserProjects userProjects = entityManager.find(UserProjects.class,login);
        if(userProjects==null){
            userProjects = new UserProjects();
            userProjects.setLogin(login);
            userProjects.setPassword(pass);
            entityManager.persist(userProjects);
            return true;
        }
        return false;
    }

    public boolean isLoggedIn(String login, String password){
        if(StringUtils.isEmpty(login) || StringUtils.isEmpty(password)) {
            return false;
        }

        UserProjects userProjects = entityManager.find(UserProjects.class,login);
        if(userProjects == null){
            return false;
        }

        if(!password.equals(userProjects.getPassword())){
            return false;
        }

        return true;
    }


}
