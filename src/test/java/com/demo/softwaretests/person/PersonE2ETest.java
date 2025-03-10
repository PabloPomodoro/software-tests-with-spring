package com.demo.softwaretests.person;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class PersonE2ETest {

    private TestRestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        // Initialize TestRestTemplate with the default configuration
        restTemplate = new TestRestTemplate();
        // Set the base URL to the running instance
        restTemplate.getRestTemplate().setUriTemplateHandler(
                new org.springframework.web.util.DefaultUriBuilderFactory("http://localhost:42069")
        );
    }

    @Test
    void givenPersonWithEmailDomain_whenGetAllPersonsByEmailDomain_thenReturnRichard() {
        String domain = "gmail.com";

        ResponseEntity<String> response = restTemplate.getForEntity(
                "/persons/email-domain/{domain}", String.class, domain);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
                .contains("\"fullName\":\"Richard Rüdiger\"")
                .contains("\"emailAddress\":\"richard.ruediger@gmail.com\"");
    }

    @Test
    void givenAgeRange_whenGetAllPersonsByAgeRange_thenReturnBianca() {
        int fromAge = 20;
        int toAge = 30;

        ResponseEntity<String> response = restTemplate.getForEntity(
                "/persons/age-range?fromAge={fromAge}&toAge={toAge}",
                String.class, fromAge, toAge);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
                .contains("\"fullName\":\"Bianca Bambus\"")
                .contains("\"emailAddress\":\"bianca.bambus@yahoo.com\"");
    }

    @Test
    void givenPersonDetails_whenCreatePerson_thenReturnCreated() {
        String firstName = "Emma";
        String lastName = "Eidhoven";
        String email = "emma.eidhoven@hotmail.com";
        LocalDate dateOfBirth = LocalDate.of(1990, 1, 1);

        ResponseEntity<String> response = restTemplate.postForEntity(
                "/persons/create?firstName={firstName}&lastName={lastName}&email={email}&dateOfBirth={dateOfBirth}",
                null, String.class, firstName, lastName, email, dateOfBirth.toString());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void givenInvalidAge_whenCreatePerson_thenReturnBadRequest() {
        String firstName = "Junior";
        String lastName = "Jamestown";
        String email = "junior.jamestown@hotmail.com";
        LocalDate dateOfBirth = LocalDate.of(2010, 1, 1);

        ResponseEntity<String> response = restTemplate.postForEntity(
                "/persons/create?firstName={firstName}&lastName={lastName}&email={email}&dateOfBirth={dateOfBirth}",
                null, String.class, firstName, lastName, email, dateOfBirth.toString());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody())
                .contains("\"errorMessage\":\"Person could not be created!\"")
                .contains("\"exceptionReason\":\"The minimum required age is 18.\"");
    }

    @Test
    void givenEmailAlreadyInUse_whenCreatePerson_thenReturnBadRequest() {
        String firstName = "Fred";
        String lastName = "Faker";
        String email = "richard.ruediger@gmail.com";
        LocalDate dateOfBirth = LocalDate.of(1990, 1, 1);

        ResponseEntity<String> response = restTemplate.postForEntity(
                "/persons/create?firstName={firstName}&lastName={lastName}&email={email}&dateOfBirth={dateOfBirth}",
                null, String.class, firstName, lastName, email, dateOfBirth.toString());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody())
                .contains("\"errorMessage\":\"Person could not be created!\"")
                .contains("\"exceptionReason\":\"The email address: " + email + " is already in use.\"");
    }

    @Test
    void givenValidParameters_whenCreatePerson_thenReturnCreated() {
        String firstName = "Emma";
        String lastName = "Eidhoven";
        String email = "emma.eidhoven@hotmail.com";
        LocalDate dateOfBirth = LocalDate.of(1990, 1, 1);

        ResponseEntity<String> response = restTemplate.postForEntity(
                "/persons/create?firstName={firstName}&lastName={lastName}&email={email}&dateOfBirth={dateOfBirth}",
                null, String.class, firstName, lastName, email, dateOfBirth.toString());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }
}