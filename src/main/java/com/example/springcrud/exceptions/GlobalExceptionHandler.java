package com.example.springcrud.exceptions;

import java.util.NoSuchElementException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public String handleDataIntegrityException(DataIntegrityViolationException e, Model model) {

        model.addAttribute("error", e.getMessage());

        return "pages/error";

    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleDataIntegrityException(IllegalArgumentException e, Model model) {

        model.addAttribute("error", e.getMessage());

        return "pages/error";

    }

    @ExceptionHandler(NoSuchElementException.class)
    public String handleDataIntegrityException(NoSuchElementException e, Model model) {

        model.addAttribute("error", e.getMessage());

        return "pages/error";

    }

}
