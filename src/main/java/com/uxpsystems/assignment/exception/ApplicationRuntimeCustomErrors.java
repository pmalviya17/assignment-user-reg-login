package com.uxpsystems.assignment.exception;

import com.uxpsystems.assignment.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The class {@code MortgageApplicationException}  represent runtime Exception for User Registration and Login Application.
 *
 * Author: Pritesh Malviya
 */
@AllArgsConstructor
@Getter
@ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "This is runtime errors class for User Registration and Login Application")
public class ApplicationRuntimeCustomErrors extends User {
    private String errorMessage;
}
