package com.demo.softwaretests.person.controller;

import com.demo.softwaretests.person.entity.Person;
import com.demo.softwaretests.person.exception.PersonCreationErrorResponse;
import com.demo.softwaretests.person.exception.PersonCreationException;
import com.demo.softwaretests.person.service.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/persons")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/email-domain/{domain}")
    public ResponseEntity<List<Person>> getAllPersonsByEmailDomain(@PathVariable String domain) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(personService.getAllPersonsByEmailDomain(domain));
    }

    @GetMapping("/age-range")
    public ResponseEntity<List<Person>> getAllPersonsByAgeRange(
            @RequestParam("fromAge") int fromAge,
            @RequestParam("toAge") int toAge
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(personService.getAllPersonsByAgeRange(fromAge, toAge));
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
    public ResponseEntity<PersonCreationErrorResponse> handleUserCreationException(
            PersonCreationException personCreationException
    ) {
        return new ResponseEntity<>(
                new PersonCreationErrorResponse("Person could not be created!", personCreationException.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }
}
