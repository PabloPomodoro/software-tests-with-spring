package com.demo.softwaretests.person.controller;

import com.demo.softwaretests.person.entity.Person;
import com.demo.softwaretests.person.exception.PersonCreationErrorResponse;
import com.demo.softwaretests.person.exception.PersonCreationException;
import com.demo.softwaretests.person.service.PersonService;
import com.demo.softwaretests.person.util.TestDataUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonControllerTest {

    @Mock
    private PersonService personService;

    @InjectMocks
    private PersonController personController;

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
        String firstName = "Lilliane";
        String lastName = "Langdorf";
        String email = "lilliane.langdorf@icloud.com";
        LocalDate dateOfBirth = LocalDate.of(1995, 3, 20);

        doNothing().when(personService).createPerson(firstName, lastName, email, dateOfBirth);

        // Act
        personController.createPerson(firstName, lastName, email, dateOfBirth);

        // Assert
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
