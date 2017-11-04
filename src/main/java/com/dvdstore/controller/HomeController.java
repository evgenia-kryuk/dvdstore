package com.dvdstore.controller;

import com.dvdstore.model.Disc;
import com.dvdstore.model.User;
import com.dvdstore.repository.DiscRepository;
import com.dvdstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private UserService userService;

    private final DiscRepository discs;


    public HomeController(DiscRepository discRepository) {
        this.discs = discRepository;
    }

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        List<Disc> currentUserDiscsList = discs.findCurrentUserOwnDiscs(user.getId());
        modelAndView.addObject("currentUserDiscsList", currentUserDiscsList);
        modelAndView.setViewName("user/home");
        return modelAndView;
    }

    @RequestMapping(value = "/taken", method = RequestMethod.GET)
    public ModelAndView showDiscsTakenByCurrentUser() {
        ModelAndView modelAndView = new ModelAndView();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        List<Disc> currentUserDiscsList = user.getTakenDiscList();
        modelAndView.addObject("currentUserDiscsList", currentUserDiscsList);
        modelAndView.setViewName("user/taken");
        return modelAndView;
    }

    @RequestMapping(value = "/available", method = RequestMethod.GET)
    public ModelAndView showCurrentlyAvailableDiscs() {
        ModelAndView modelAndView = new ModelAndView();

        List<Disc> allDiscsList = discs.findAll();
        allDiscsList.removeAll(findAllDiscsOnHands());

        modelAndView.addObject("available", allDiscsList);
        modelAndView.setViewName("user/available");

        return modelAndView;
    }

    @RequestMapping(value = "/thirdparty", method = RequestMethod.GET)
    public ModelAndView showThirdpartyDiscs() {
        ModelAndView modelAndView = new ModelAndView();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        List<Disc> currentUserDiscsList = discs.findCurrentUserOwnDiscs(user.getId());
        List<User> userList = userService.findDiscHolders();

        for(User usr: userList){
            user.getTakenDiscList().retainAll(currentUserDiscsList);
        }
        Iterator<User> it = userList.iterator();
        while (it.hasNext()) {
            User disc = it.next();
            if (disc.getTakenDiscList().isEmpty()) {
                it.remove();
            }
        }

        modelAndView.addObject("userList", userList);
        modelAndView.setViewName("user/thirdparty");

        return modelAndView;
    }

    @RequestMapping(value = "/return/{id}", method = RequestMethod.PUT)
    public ModelAndView returnDisc(@PathVariable("id") int id) {
        ModelAndView modelAndView = new ModelAndView();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        List<Disc> currentUserDiscsList = user.getTakenDiscList();
        Iterator<Disc> it = currentUserDiscsList.iterator();
        while (it.hasNext()) {
            Disc disc = it.next();
            if (disc.getId() == id) {
                it.remove();
            }
        }
        user.setTakenDiscList(currentUserDiscsList);
        userService.saveUser(user);

        modelAndView.addObject("currentUserDiscsList", currentUserDiscsList);
        modelAndView.setViewName("redirect:/taken");
        return modelAndView;

    }

    @RequestMapping(value = "/take/{id}", method = RequestMethod.PUT)
    public ModelAndView takeDisc(@PathVariable("id") int id) {
        ModelAndView modelAndView = new ModelAndView();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        List<Disc> currentUserDiscsList = user.getTakenDiscList();
        currentUserDiscsList.add(discs.findById(id));

        user.setTakenDiscList(currentUserDiscsList);
        userService.saveUser(user);

        modelAndView.addObject("currentUserDiscsList", currentUserDiscsList);
        modelAndView.setViewName("redirect:/available");
        return modelAndView;
    }

    private List<Disc> findAllDiscsOnHands() {
        List<Disc> takenDiscsList = new ArrayList<>();
        List<User> userList = userService.findDiscHolders();
        for (User usr : userList) {
            if (!usr.getTakenDiscList().isEmpty()) {
                takenDiscsList.addAll(usr.getTakenDiscList());
            }
        }
        return takenDiscsList;
    }
}
