package com.ua.rubygarage;

import com.ua.rubygarage.domain.Projects;
import com.ua.rubygarage.ejb.ProjectsManagerBean;
import com.ua.rubygarage.ejb.TasksManagerBean;
import com.ua.rubygarage.ejb.UserProjectsManagerBean;
import org.primefaces.context.RequestContext;
import org.primefaces.event.DashboardReorderEvent;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;

import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@ManagedBean
@Named("projectsBean")
@SessionScoped
public class ProjectsBean implements Serializable {
    private DashboardModel model;
    private DashboardColumn column1;
    private RequestContext requestContext;
    private FacesMessage msg;
    private Projects selectedProject;
    private String name;

    @EJB
    private UserProjectsManagerBean userProjectsManagerBean;
    @EJB
    private ProjectsManagerBean projectsManagerBean;
    @EJB
    private TasksManagerBean tasksManagerBean;

    public DashboardModel getModel() {
        return model;
    }

    public void setModel(DashboardModel model) {
        this.model = model;
    }

    public Projects getSelectedProject() {
        return selectedProject;
    }

    public void setSelectedProject(Projects selectedProject) {
        this.selectedProject = selectedProject;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Projects> getProjectsList() {
        UserBean.setProjectsList(projectsManagerBean.getProjectsUser(UserBean.getTmpLogin()));
        return UserBean.getProjectsList();
    }

    public void init() {
        if(UserBean.isLogged()) {
            model = new DefaultDashboardModel();
            column1 = new DefaultDashboardColumn();

            for (Projects project : UserBean.getProjectsList()) {
                column1.addWidget("panel" + String.valueOf(project.getId()));
            }
            model.addColumn(column1);
           /* requestContext = RequestContext.getCurrentInstance();
            requestContext.update(":form_center");*/
        }
    }

    public void deleteProject(long idProject){
        projectsManagerBean.deleteProjects(idProject);
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Information", "Project deleted" );
        FacesContext.getCurrentInstance().addMessage(null, message);
        requestContext = RequestContext.getCurrentInstance();
        requestContext.update("form_center");
    }

    public void createProject(){
        if(UserBean.isLogged()) {
            Projects project = projectsManagerBean.createProjects(name, UserBean.getTmpLogin());
            getProjectsList();
            name=null;
            column1.addWidget("panel" + String.valueOf(project.getId()));
            requestContext = RequestContext.getCurrentInstance();
            requestContext.update("form_center");
            requestContext.execute("PF('tableWidget"+project.getId()+"').filter();");
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Information", "Create New_Project");
        }else {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning", "Please sign in first");
        }
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void editProject(Projects projects){
        if(projects!=null && !projects.getName().isEmpty()) {
            projectsManagerBean.updateProjects(projects);
            requestContext = RequestContext.getCurrentInstance();
            requestContext.update("form_center");
            requestContext.execute("PF('tableWidget"+projects.getId()+"').filter();PF('prDial"+projects.getId()+"').hide()");
            selectedProject=null;
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Information", "Project successfully changed");
        }else {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning", "Enter the name of the project");
        }
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void handleReorder(DashboardReorderEvent event) {
        FacesMessage message = new FacesMessage();
        message.setSeverity(FacesMessage.SEVERITY_INFO);
        message.setSummary("Reordered: " + event.getWidgetId());
        message.setDetail("Item index: " + event.getItemIndex() + ", Column index: " + event.getColumnIndex() + ", Sender index: " + event.getSenderColumnIndex());
        addMessage(message);
    }

    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
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

public Projects select(Projects projects){
    selectedProject = projects;
    return  selectedProject;
}

}
