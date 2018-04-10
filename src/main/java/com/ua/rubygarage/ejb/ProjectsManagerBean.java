package com.ua.rubygarage.ejb;

import com.ua.rubygarage.domain.Projects;
import com.ua.rubygarage.domain.UserProjects;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
@LocalBean
public class ProjectsManagerBean {
    @PersistenceContext(unitName = "examplePU")
    private EntityManager entityManager;

    public Projects createProjects(String name, String login) {
        UserProjects user = entityManager.find(UserProjects.class,login);
        Projects projects = new Projects();
        projects.setName(name);
        projects.setUserProjects(user);
        entityManager.persist(projects);
        return projects;
    }

    public boolean updateProjects(Projects project){
        Projects projects = entityManager.find(Projects.class,project.getId());
        if(projects==null){
            return false;
        }
        projects.setName(project.getName());
        entityManager.merge(projects);
        return true;
    }

    public boolean deleteProjects(long projectId){
        Projects projects = entityManager.find(Projects.class,projectId);
        if(projects==null){
            return false;
        }
        entityManager.remove(projects);
        return true;
    }

    public List<Projects> getProjectsUser(String login) {
        TypedQuery<Projects> query = entityManager.createQuery("select c from Projects c where c.userProjects.login =:login ORDER BY c.id",Projects.class).setParameter("login",login);
        return query.getResultList();
    }

}
