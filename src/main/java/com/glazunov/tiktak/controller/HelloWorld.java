package com.glazunov.tiktak.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

public class HelloWorld {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    ModelAndView doHello(){
        return new ModelAndView("index");
    }
}
