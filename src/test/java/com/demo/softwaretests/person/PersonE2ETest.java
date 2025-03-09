package com.demo.softwaretests.person;

import com.demo.softwaretests.person.controller.PersonController;
import com.demo.softwaretests.person.repository.PersonRepository;
import com.demo.softwaretests.person.service.PersonService;
import com.demo.softwaretests.person.util.TestDataUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PersonE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonService personService;

    @Autowired
    private PersonController personController;

    @Autowired
    private PersonRepository personRepository;

    @BeforeEach
    void setUp() {
        personRepository.deleteAll();
        personRepository.save(TestDataUtil.richard());
        personRepository.save(TestDataUtil.bianca());
    }

    @Test
    void givenPersonWithEmailDomain_whenGetAllPersonsByEmailDomain_thenReturnRichard() throws Exception {
        // Arrange
        String domain = "gmail.com";

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

        // Act & Assert
        mockMvc.perform(post("/persons/create")
                        .param("firstName", firstName)
                        .param("lastName", lastName)
                        .param("email", email)
                        .param("dateOfBirth", dateOfBirth.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("Person could not be created!"))
                .andExpect(jsonPath("$.exceptionReason").value("The email address: " + email + " is already in use."));
    }

    @Test
    void givenValidParameters_whenCreatePerson_thenReturnCreated() throws Exception {
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
}
