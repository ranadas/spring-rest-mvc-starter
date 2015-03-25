package com.rdas.controller;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by rdas on 23/03/2015.
 */
@RestController
@RequestMapping("/api")
public class RestServiceController {
    private static final Logger log = Logger.getLogger(RestServiceController.class);

    @RequestMapping(value = "/sampleJson", method = RequestMethod.GET)
    public String returnIndexJson() {
        log.debug("returning from RestServiceController:/sampleJson");
        return "Hello world (json)";
    }
}