package com.ua.rubygarage;

import com.ua.rubygarage.domain.Projects;
import com.ua.rubygarage.domain.Tasks;
import com.ua.rubygarage.ejb.ProjectsManagerBean;
import com.ua.rubygarage.ejb.TasksManagerBean;
import com.ua.rubygarage.ejb.UserProjectsManagerBean;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.ReorderEvent;
import org.primefaces.event.SelectEvent;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@ManagedBean
@Named("tasksBean")
@SessionScoped
public class TasksBean implements Serializable {
    private boolean status;
    private String name;
    private String tempName;
    private RequestContext requestContext;
    private FacesMessage msg;
    private Date date;
    private Tasks selectedTask;
    private long id;
    private int j=0;

    @EJB
    private UserProjectsManagerBean userProjectsManagerBean;
    @EJB
    private ProjectsManagerBean projectsManagerBean;
    @EJB
    private TasksManagerBean tasksManagerBean;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
            this.name = name;
    }

    public List<Tasks> getTaskList(long idProject){
         return tasksManagerBean.getTaskProject(idProject);
    }

    public Tasks getSelectedTask() {
        return selectedTask;
    }

    public void setSelectedTask(Tasks selectedTask) {
        this.selectedTask = selectedTask;
    }

    public void init(){
        name=null;
        msg=null;
        if(UserBean.isLogged()) {
            for (Projects p : UserBean.getProjectsList()) {
                requestContext = RequestContext.getCurrentInstance();
                requestContext.execute("PF('tableWidget" + p.getId() + "').filter();");
            }
        }
    }

    public void createTask(Projects projects){
        if(!StringUtils.isEmpty(tempName) && projects!=null) {
            tasksManagerBean.createTask(tempName, date, projects);
            requestContext = RequestContext.getCurrentInstance();
            requestContext.update(":form_center:msgs");
            requestContext.execute("PF('tableWidget" + projects.getId() + "').filter();");
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Information", "Project '"+tempName+"' successfully created");
            name=null;
            tempName=null;
            date=null;
        }else{
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning", "Enter a name for the task and select deadline");
        }
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void deleteTask(long idTask,long idP){
        tasksManagerBean.deleteTask(idTask);
        name=null;
        requestContext = RequestContext.getCurrentInstance();
        requestContext.update(":form_center:msgs");
        requestContext.execute("PF('tableWidget" + idP + "').filter();PF('tableWidget" + idP + "').clearFilters();");
    }

    public void editReadyTask(Tasks tasks,boolean flag){
        tasksManagerBean.updateStatusTask(tasks,flag);
        name=null;
    }

    public UserProjectsManagerBean getUserProjectsManagerBean() {
        return userProjectsManagerBean;
    }

    public void setUserProjectsManagerBean(UserProjectsManagerBean userProjectsManagerBean) {
        this.userProjectsManagerBean = userProjectsManagerBean;
    }

    public ProjectsManagerBean getProjectsManagerBean() {
        return projectsManagerBean;
    }

    public void setProjectsManagerBean(ProjectsManagerBean projectsManagerBean) {
        this.projectsManagerBean = projectsManagerBean;
    }

    public TasksManagerBean getTasksManagerBean() {
        return tasksManagerBean;
    }

    public void setTasksManagerBean(TasksManagerBean tasksManagerBean) {
        this.tasksManagerBean = tasksManagerBean;
    }

    public void wtireTask(){
        if(!StringUtils.isEmpty(name)) {
            tempName = name;
        }else{tempName=null;}
    }

    public void onDateSelect(SelectEvent event) {
        date = (Date) event.getObject();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Date Selected", format.format(event.getObject())));
    }

    public void updateTasks(Tasks task){
        if(!task.getName().isEmpty()) {
             if(task.getRating()==null){
                 task.setRating(0);
             }
            tasksManagerBean.updateTask(task);
            date = null;
            selectedTask = null;
            requestContext = RequestContext.getCurrentInstance();
            requestContext.update("form_center");
            requestContext.execute("PF('tableWidget" + task.getProjects().getId() + "').filter();PF('taskDialog').hide()");
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Information", "Task update");
        }else{
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning", "All fields are required");
        }
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void closeDialog(long idP){
        date=null;
        selectedTask=null;
        requestContext = RequestContext.getCurrentInstance();
        requestContext.execute("PF('tableWidget" + idP + "').filter();PF('taskDialog').hide()");
    }

    public String formatDate(Date date){
        if(date!=null) {
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
            return format.format(date);
        }
        return null;
    }

    public void onRowReorder(ReorderEvent event) {
        if(j==0) {
            int from = event.getFromIndex();
            int to = event.getToIndex();
            if(id>0) {
                List<Tasks> tasks = tasksManagerBean.getTaskProject(id);
                Tasks t = tasks.get(from);
                tasks.remove(from);
                tasks.add(to, t);
                double maxRating = 5.0;
                double minRating = roundingOff(maxRating / (double) tasks.size());
                if(from>to){ to=from; }
                for(int i=0;i<=to;i++){
                    tasks.get(i).setReorder(maxRating);
                    maxRating = roundingOff(maxRating - minRating);
                    tasksManagerBean.updateReorderTask(tasks.get(i));
                }
                j++;
                id=0;
            }
        }else{j=0;}
    }

    public void onRowSelect(long idP) {
        id=idP;
    }

    private double roundingOff(double d){
      return new BigDecimal(d).setScale(3, RoundingMode.UP).doubleValue();
    }

}
