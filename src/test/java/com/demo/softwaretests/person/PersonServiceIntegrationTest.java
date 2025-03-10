package com.demo.softwaretests.person;

import com.demo.softwaretests.person.entity.Person;
import com.demo.softwaretests.person.exception.PersonCreationException;
import com.demo.softwaretests.person.repository.PersonRepository;
import com.demo.softwaretests.person.service.PersonService;
import com.demo.softwaretests.person.util.TestDataUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PersonServiceIntegrationTest {

    @Autowired
    private PersonService personService;

    @Autowired
    private PersonRepository personRepository;

    @BeforeEach
    void setUp() {
        personRepository.deleteAll();
    }

    @Test
    void givenValidPersonDetails_whenCreatePerson_thenPersonIsSaved() {
        // Arrange
        var firstName = "Richard";
        var lastName = "Rüdiger";
        var email = "richard.ruediger@gmail.com";
        var dateOfBirth = LocalDate.now().minusYears(41);

        // Act
        personService.createPerson(firstName, lastName, email, dateOfBirth);

        // Assert
        List<Person> persons = personRepository.findAll();
        assertEquals(1, persons.size());

        Person savedPerson = persons.getFirst();
        assertEquals("Richard Rüdiger", savedPerson.getFullName());
        assertEquals(email, savedPerson.getEmailAddress());
        assertEquals(41, savedPerson.getAge());
    }

    @Test
    void givenUnderagePerson_whenCreatePerson_thenThrowsException() {
        // Arrange
        var firstName = "Bianca";
        var lastName = "Bambus";
        var email = "bianca.bambus@yahoo.com";
        var underageDateOfBirth = LocalDate.now().minusYears(17);

        // Act & Assert
        PersonCreationException exception = assertThrows(PersonCreationException.class, () -> {
            personService.createPerson(firstName, lastName, email, underageDateOfBirth);
        });

        assertEquals("The minimum required age is 18.", exception.getMessage());
    }

    @Test
    void givenDuplicateEmail_whenCreatePerson_thenThrowsException() {
        // Arrange
        Person richard = TestDataUtil.richard();
        personRepository.save(richard);

        // Act & Assert
        PersonCreationException exception = assertThrows(PersonCreationException.class, () -> {
            personService.createPerson("Fred", "Faker", richard.getEmailAddress(), LocalDate.now().minusYears(25));
        });

        assertEquals(
                "The email address: richard.ruediger@gmail.com is already in use.",
                exception.getMessage()
        );
    }

    @Test
    void givenPersonsWithDifferentAges_whenGetAllPersonsByAgeRange_thenReturnsMatchingPersons() {
        // Arrange
        personRepository.saveAll(TestDataUtil.listOfRichardAndGuentherAndLilliane());

        // Act
        List<Person> persons = personService.getAllPersonsByAgeRange(25, 45);

        // Assert
        assertEquals(2, persons.size());

        List<String> fullNames = persons.stream().map(Person::getFullName).toList();
        assertTrue(fullNames.contains("Richard Rüdiger"));
        assertTrue(fullNames.contains("Lilliane Langdorf"));
        assertFalse(fullNames.contains("Günther Grandiger"));
    }

    @Test
    void givenPersonsWithDifferentEmails_whenGetAllPersonsByEmailDomain_thenReturnsMatchingPersons() {
        // Arrange
        personRepository.saveAll(TestDataUtil.listOfRichardAndGuentherAndLilliane());

        // Act
        List<Person> persons = personService.getAllPersonsByEmailDomain("@gmail.com");

        // Assert
        assertEquals(2, persons.size());

        List<String> fullNames = persons.stream().map(Person::getFullName).toList();
        assertTrue(fullNames.contains("Richard Rüdiger"));
        assertTrue(fullNames.contains("Günther Grandiger"));
        assertFalse(fullNames.contains("Lilliane Langdorf"));
    }
}