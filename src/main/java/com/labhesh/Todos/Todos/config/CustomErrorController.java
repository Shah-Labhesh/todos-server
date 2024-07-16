package com.labhesh.Todos.Todos.config;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @GetMapping("/error")
    public String handleError(HttpServletRequest request) {
        // You can add more logic here to handle different types of errors
        // For now, we just return the 404 page for all errors

        return "404";
    }

    // Override method deprecated in Spring Boot 2.3+
    public String getErrorPath() {
        return "/error";
    }
}
