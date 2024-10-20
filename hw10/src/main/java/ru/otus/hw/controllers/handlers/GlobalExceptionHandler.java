package ru.otus.hw.controllers.handlers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import ru.otus.hw.dto.ErrorDto;
import ru.otus.hw.exceptions.NotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handleNotFoundException(NotFoundException ex) {
        ErrorDto errorDto = new ErrorDto(404, ex.getMessage());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorDto", errorDto);
        modelAndView.setViewName("error/error");
        return modelAndView;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception ex) {
        ErrorDto errorDto = new ErrorDto(500, ex.getMessage());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorDto", errorDto);
        modelAndView.setViewName("error/error");
        return modelAndView;
    }
}
