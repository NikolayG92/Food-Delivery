package softuni.delivery.web.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import softuni.delivery.config.annotations.PageTitle;
import softuni.delivery.model.view.CartViewModel;
import softuni.delivery.service.TownService;
import softuni.delivery.service.UserService;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@Controller
public class HomeController extends BaseController{
    private final TownService townService;
    private final UserService userService;

    public HomeController(TownService townService, UserService userService) {
        this.townService = townService;
        this.userService = userService;
    }

    @GetMapping("/")
    @PreAuthorize("isAnonymous()")
    @PageTitle("Food Delivery")
    public ModelAndView index( ModelAndView modelAndView) {
       return view("index", modelAndView);

    }

    @GetMapping("/home")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Home")
    public ModelAndView home(ModelAndView modelAndView, HttpSession httpSession){

        CartViewModel cartViewModel = new CartViewModel();
        cartViewModel.setProducts(new ArrayList<>());

        if(httpSession.getAttribute("cart") == null){
            httpSession.setAttribute("cart", cartViewModel);
        }
        modelAndView.addObject("towns", this.townService
                .findAllTowns());
        modelAndView.setViewName("home");
        return modelAndView;
    }
}
