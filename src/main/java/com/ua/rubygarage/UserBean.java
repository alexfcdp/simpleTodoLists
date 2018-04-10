package com.ua.rubygarage;

import com.ua.rubygarage.domain.Projects;
import com.ua.rubygarage.ejb.ProjectsManagerBean;
import org.apache.commons.lang3.StringUtils;
import com.ua.rubygarage.ejb.UserProjectsManagerBean;
import org.primefaces.context.RequestContext;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

@ManagedBean
@Named("userBean")
@SessionScoped
public class UserBean implements Serializable {
    private String login;
    private String password;
    private static boolean logged;
    private static String tmpLogin;
    private static List<Projects> projectsList;
    private FacesContext context;

    @EJB
    private UserProjectsManagerBean userProjectsManagerBean;
    @EJB
    private ProjectsManagerBean projectsManagerBean;

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

    public static boolean isLogged() {
        return logged;
    }

    public static void setLogged(boolean logged) {
        UserBean.logged = logged;
    }

    public static List<Projects> getProjectsList() {
        return projectsList;
    }

    public static String getTmpLogin() {
        return tmpLogin;
    }

    public static void setTmpLogin(String tmpLogin) {
        UserBean.tmpLogin = tmpLogin;
    }

    public static void setProjectsList(List<Projects> projectsList) {
        UserBean.projectsList = projectsList;
    }

    public void doLogin(){
        context=FacesContext.getCurrentInstance();
        if(StringUtils.isEmpty(login) || StringUtils.isEmpty(password)){
            context.addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "All fields are required",""));
            logged = false;
            return;
        }
        logged = userProjectsManagerBean.isLoggedIn(login,password);

        if(logged){
            try {
                tmpLogin = login;
                projectsList = projectsManagerBean.getProjectsUser(login);
                password=null;
                FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() +"/todoLists.xhtml");
            } catch (IOException e) {

            }
        }else {
            context.addMessage(null,new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Authentication failed. Check the username or password",""));
        }
    }

    public void logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        this.login=null;
        this.password=null;
        this.logged=false;
        this.tmpLogin=null;
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() +"/login.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createUser() {
        context = FacesContext.getCurrentInstance();
        if(!StringUtils.isEmpty(login) && !StringUtils.isEmpty(password)){
            boolean isDuplicate  = userProjectsManagerBean.createUser(login,password);
            if(isDuplicate){
                context.addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "User successfully created",""));
                this.login=null;
                this.password=null;
            return;}
            else { context.addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Login already in use",""));  }
        }else { context.addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "All fields are required",""));  }
    }
}
