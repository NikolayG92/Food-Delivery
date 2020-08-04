package softuni.delivery.web.controllers;

import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

public abstract class BaseController {

    protected BaseController(){

    }

    public ModelAndView view(String viewName, ModelAndView modelAndView){
        modelAndView.setViewName(viewName);
        return modelAndView;
    }

    protected ModelAndView view(String viewName){
        return this.view(viewName, new ModelAndView());
    }

    protected ModelAndView redirect (String url){
        return this.view("redirect:" + url);
    }

    protected String getUsername(Principal principal){

        return principal.getName();
    }
}
