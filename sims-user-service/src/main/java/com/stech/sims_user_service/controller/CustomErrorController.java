package com.stech.sims_user_service.controller;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@RestController
public class CustomErrorController implements ErrorController {

    private final ErrorAttributes errorAttributes;

    public CustomErrorController(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    @RequestMapping("/error")
    public ResponseEntity<Map<String, Object>> handleError(HttpServletRequest request) {
        // Get detailed error attributes
        Map<String, Object> errorDetails = errorAttributes.getErrorAttributes(
                (WebRequest) request, ErrorAttributeOptions.of(ErrorAttributeOptions.Include.MESSAGE, ErrorAttributeOptions.Include.EXCEPTION, ErrorAttributeOptions.Include.STACK_TRACE)
        );

        return ResponseEntity.status((int) errorDetails.getOrDefault("status", 500))
                .body(errorDetails);
    }

    public String getErrorPath() {
        return "/error";
    }
}
