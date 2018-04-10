package com.ua.rubygarage.ejb;

import com.ua.rubygarage.domain.Projects;
import com.ua.rubygarage.domain.Tasks;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.*;

@Stateless
@LocalBean
public class TasksManagerBean {
    @PersistenceContext(unitName = "examplePU")
    private EntityManager entityManager;

    public List<Tasks> getTaskProject(long idProject) {
        TypedQuery<Tasks> query = entityManager.createQuery("select c from Tasks c where c.projects.id =:idProject ORDER BY c.reorder desc",Tasks.class).setParameter("idProject",idProject);
        return query.getResultList();
    }

    public Tasks createTask(String name, Date date, Projects projects) {
        Tasks task = new Tasks();
        task.setName(name);
        task.setProjects(projects);
        task.setStatus(false);
        task.setDate(date);
        task.setRating(0);
        task.setRating(0);
        entityManager.persist(task);
        return task;
    }

    public boolean deleteTask(long id){
        Tasks task = entityManager.find(Tasks.class,id);
        if(task==null){
            return false;
        }
        entityManager.remove(task);
        return true;
    }

    public boolean updateStatusTask(Tasks task,boolean flag){
        Tasks tasks = entityManager.find(Tasks.class,task.getId());
        if(tasks==null){
            return false;
        }
        tasks.setStatus(flag);
        entityManager.merge(tasks);
        return true;
    }

    public boolean updateTask(Tasks task){
        Tasks tasks = entityManager.find(Tasks.class,task.getId());
        if(tasks==null){
            return false;
        }
        tasks.setName(task.getName());
        tasks.setDate(task.getDate());
        tasks.setRating(task.getRating());
        if((int)Math.round(task.getReorder())!= task.getRating()) {
            tasks.setReorder(task.getRating());
        }
        entityManager.merge(tasks);
        return true;
    }

    public void updateReorderTask(Tasks task){
        //task.setRating((int)Math.round(task.getReorder()));
        entityManager.merge(task);
    }
}
