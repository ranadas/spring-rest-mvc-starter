package com.rdas.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 */
@Controller
class IndexController {

    private static final Logger log = Logger.getLogger(IndexController.class);

    /**
     * http://localhost:8080/ http://localhost:8080/index
     */
    @RequestMapping(value = { "/", "/index**" }, method = RequestMethod.GET)
    public ModelAndView showIndex(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView model = new ModelAndView("index");

        model.addObject("title", "Spring Hello World Application [public access] !");
        model.addObject("message", "hello world!");
        return model;
    }

    /**
     * http://localhost:8080/protected
     */
    @RequestMapping(value = "/protected**", method = RequestMethod.GET)
    public ModelAndView protectedPage() {

        ModelAndView model = new ModelAndView();
        model.addObject("title", "Spring Hello World Application [protected access] !");
        model.addObject("message", "This is protected page - Only for Administrators !");
        model.setViewName("protected");
        return model;

    }

    /**
     * http://localhost:8080/confidential
     */
    @RequestMapping(value = "/confidential**", method = RequestMethod.GET)
    public ModelAndView superAdminPage() {

        ModelAndView model = new ModelAndView();
        model.addObject("title", "Spring Hello World Application [private access] !");
        model.addObject("message", "This is confidential page - Need Super Admin Role !");
        model.setViewName("confidential");

        return model;
    }
}
