package com.demo.softwaretests.controller;

import com.demo.softwaretests.exception.PersonCreationErrorResponse;
import com.demo.softwaretests.exception.PersonCreationException;
import com.demo.softwaretests.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/persons")
public class PersonController {

    private static final int MINIMUM_AGE = 18;

    @Autowired
    private PersonService personService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void createPerson(
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("email") String email,
            @RequestParam("dateOfBirth") LocalDate dateOfBirth
    ) {
        if (personService.calculateAge(dateOfBirth, LocalDate.now()) < MINIMUM_AGE) {

            throw new PersonCreationException(
                    String.format("The minimum required age is %d.", MINIMUM_AGE)
            );
        }

        personService.createPerson(firstName, lastName, email, dateOfBirth);
    }

    @ExceptionHandler(PersonCreationException.class)
    public ResponseEntity<PersonCreationErrorResponse> handleUserCreationException(PersonCreationException personCreationException) {

        return new ResponseEntity<>(
                new PersonCreationErrorResponse("Person could not be created!", personCreationException.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }
}
