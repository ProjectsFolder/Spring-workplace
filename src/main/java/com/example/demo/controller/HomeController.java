package com.example.demo.controller;

import com.example.demo.service.BillingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    Environment environment;

    @Autowired
    HttpServletRequest request;

    @Autowired
    BillingService billingService;

    @GetMapping(value = "/billing/{id}", name = "billing")
    public @ResponseBody ArrayList<Map<String, Object>> billing(
            @PathVariable(name = "id") Integer id
    ) {
        return this.billingService.getContract(id);
    }

    @GetMapping(value = "/", name = "index")
    public String index() {
        return "test/index";
    }

    @GetMapping(value = "/test/{name2}/{name1}", name = "test")
    public String test1(
            @RequestParam(name="test", required = false, defaultValue = "World") String value, // name1
            @PathVariable(name = "name2") String name2, // name2
            @PathVariable(name = "name1") String name1, // test
            Model model
    ) {
        model.addAttribute("name1", name1);
        model.addAttribute("name2", name2);
        model.addAttribute("value", value);
        model.addAttribute("configValue", this.environment.getProperty("app.test"));
        return "test/test";
    }

    @Secured("ROLE_USER")
    @GetMapping(value = "/test2", name = "test2")
    public String test2(Model model) {
        var name1 = request.getParameter("name1");
        var name2 = request.getParameter("name2");
        var value = request.getParameter("value");
        model.addAttribute("name1", name1);
        model.addAttribute("name2", name2);
        model.addAttribute("value", value);
        model.addAttribute("configValue", this.environment.getProperty("app.test"));
        return "test/test";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/test3", name = "test3")
    public @ResponseBody String test3() {
        return "test_3";
    }
}
