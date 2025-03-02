package com.demo.softwaretests.person;

import com.demo.softwaretests.person.controller.PersonController;
import com.demo.softwaretests.person.entity.Person;
import com.demo.softwaretests.person.exception.PersonCreationException;
import com.demo.softwaretests.person.service.PersonService;
import com.demo.softwaretests.person.util.TestDataUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class PersonE2ETest {

    private MockMvc mockMvc;

    @Mock
    private PersonService personService;

    @InjectMocks
    private PersonController personController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(personController).build();
    }

    @Test
    void givenPersonWithEmailDomain_whenGetAllPersonsByEmailDomain_thenReturnRichard() throws Exception {
        // Arrange
        String domain = "gmail.com";

        List<Person> persons = TestDataUtil.listOfRichard();
        when(personService.getAllPersonsByEmailDomain(domain)).thenReturn(persons);

        // Act & Assert
        mockMvc.perform(get("/persons/email-domain/{domain}", domain))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fullName").value("Richard Rüdiger"))
                .andExpect(jsonPath("$[0].emailAddress").value("richard.ruediger@gmail.com"));
    }

    @Test
    void givenAgeRange_whenGetAllPersonsByAgeRange_thenReturnBianca() throws Exception {
        // Arrange
        int fromAge = 20;
        int toAge = 30;

        List<Person> persons = TestDataUtil.listOfBianca();
        when(personService.getAllPersonsByAgeRange(fromAge, toAge)).thenReturn(persons);

        // Act & Assert
        mockMvc.perform(get("/persons/age-range")
                        .param("fromAge", String.valueOf(fromAge))
                        .param("toAge", String.valueOf(toAge)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fullName").value("Bianca Bambus"))
                .andExpect(jsonPath("$[0].emailAddress").value("bianca.bambus@yahoo.com"));
    }

    @Test
    void givenPersonDetails_whenCreatePerson_thenReturnCreated() throws Exception {
        // Arrange
        String firstName = "Emma";
        String lastName = "Eidhoven";
        String email = "emma.eidhoven@hotmail.com";
        LocalDate dateOfBirth = LocalDate.of(1990, 1, 1);

        // Act & Assert
        mockMvc.perform(post("/persons/create")
                        .param("firstName", firstName)
                        .param("lastName", lastName)
                        .param("email", email)
                        .param("dateOfBirth", dateOfBirth.toString()))
                .andExpect(status().isCreated());
    }

    @Test
    void givenInvalidAge_whenCreatePerson_thenReturnBadRequest() throws Exception {
        // Arrange
        String firstName = "Junior";
        String lastName = "Jamestown";
        String email = "junior.jamestown@hotmail.com";
        LocalDate dateOfBirth = LocalDate.of(2010, 1, 1);

        doThrow(new PersonCreationException("The minimum required age is 18."))
                .when(personService)
                .validateParameters(dateOfBirth, email);

        // Act & Assert
        mockMvc.perform(post("/persons/create")
                        .param("firstName", firstName)
                        .param("lastName", lastName)
                        .param("email", email)
                        .param("dateOfBirth", dateOfBirth.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("Person could not be created!"))
                .andExpect(jsonPath("$.exceptionReason").value("The minimum required age is 18."));
    }

    @Test
    void givenEmailAlreadyInUse_whenCreatePerson_thenReturnBadRequest() throws Exception {
        // Arrange
        String firstName = "Fred";
        String lastName = "Faker";
        String email = "richard.ruediger@gmail.com";
        LocalDate dateOfBirth = LocalDate.of(1990, 1, 1);

        doThrow(new PersonCreationException("The email address: " + email + " is already in use."))
                .when(personService)
                .validateParameters(dateOfBirth, email);

        // Act & Assert
        mockMvc.perform(post("/persons/create")
                        .param("firstName", firstName)
                        .param("lastName", lastName)
                        .param("email", email)
                        .param("dateOfBirth", dateOfBirth.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Person could not be created!"))
                .andExpect(jsonPath("$.details").value("The email address: " + email + " is already in use."));
    }

    @Test
    void givenValidParameters_whenCreatePerson_thenReturnCreated() throws Exception {
        // Arrange
        String firstName = "Emma";
        String lastName = "Eidhoven";
        String email = "emma.eidhoven@hotmail.com";
        LocalDate dateOfBirth = LocalDate.of(1990, 1, 1);

        doNothing().when(personService).validateParameters(dateOfBirth, email);

        // Act & Assert
        mockMvc.perform(post("/persons/create")
                        .param("firstName", firstName)
                        .param("lastName", lastName)
                        .param("email", email)
                        .param("dateOfBirth", dateOfBirth.toString()))
                .andExpect(status().isCreated());
    }
}
