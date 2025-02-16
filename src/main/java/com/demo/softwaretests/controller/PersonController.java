package com.demo.softwaretests.controller;

import com.demo.softwaretests.exception.PersonCreationErrorResponse;
import com.demo.softwaretests.exception.PersonCreationException;
import com.demo.softwaretests.service.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/persons")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void createPerson(
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("email") String email,
            @RequestParam("dateOfBirth") LocalDate dateOfBirth
    ) {
        personService.validateParameters(dateOfBirth, email);
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
