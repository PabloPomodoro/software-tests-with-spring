package com.demo.softwaretests.person.controller;

import com.demo.softwaretests.person.util.TestDataUtil;
import com.demo.softwaretests.person.entity.Person;
import com.demo.softwaretests.person.exception.PersonCreationErrorResponse;
import com.demo.softwaretests.person.exception.PersonCreationException;
import com.demo.softwaretests.person.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

public class PersonControllerTest {

    @Mock
    private PersonService personService;

    @InjectMocks
    private PersonController personController;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenValidEmailDomain_whenGetAllPersonsByEmailDomain_thenReturnPersonsList() {
        // Arrange
        String domain = "gmail.com";
        List<Person> persons = TestDataUtil.listOfRichardAndGuenther();
        when(personService.getAllPersonsByEmailDomain(domain)).thenReturn(persons);

        // Act
        ResponseEntity<List<Person>> response = personController.getAllPersonsByEmailDomain(domain);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(persons, response.getBody());
        verify(personService, times(1)).getAllPersonsByEmailDomain(domain);
    }

    @Test
    void givenValidAgeRange_whenGetAllPersonsByAgeRange_thenReturnPersonsList() {
        // Arrange
        int fromAge = 30;
        int toAge = 45;
        List<Person> persons = TestDataUtil.listOfRichardAndLilliane();
        when(personService.getAllPersonsByAgeRange(fromAge, toAge)).thenReturn(persons);

        // Act
        ResponseEntity<List<Person>> response = personController.getAllPersonsByAgeRange(fromAge, toAge);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(persons, response.getBody());
        verify(personService, times(1)).getAllPersonsByAgeRange(fromAge, toAge);
    }

    @Test
    void givenValidPersonData_whenCreatePerson_thenPersonIsCreated() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        LocalDate dateOfBirth = LocalDate.of(1985, 5, 15);

        doNothing().when(personService).validateParameters(dateOfBirth, email);
        doNothing().when(personService).createPerson(firstName, lastName, email, dateOfBirth);

        // Act
        personController.createPerson(firstName, lastName, email, dateOfBirth);

        // Assert
        verify(personService, times(1)).validateParameters(dateOfBirth, email);
        verify(personService, times(1)).createPerson(firstName, lastName, email, dateOfBirth);
    }

    @Test
    void givenPersonCreationException_whenHandleUserCreationException_thenReturnErrorResponse() {
        // Arrange
        PersonCreationException exception = new PersonCreationException("Invalid data");
        PersonCreationErrorResponse expectedResponse =
                new PersonCreationErrorResponse(
                        "Person could not be created!",
                        "Invalid data"
                );

        // Act
        ResponseEntity<PersonCreationErrorResponse> response = personController.handleUserCreationException(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedResponse.getErrorMessage(), response.getBody().getErrorMessage());
        assertEquals(expectedResponse.getExceptionReason(), response.getBody().getExceptionReason());
    }
}
