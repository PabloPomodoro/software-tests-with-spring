package com.demo.softwaretests.person.service;

import com.demo.softwaretests.person.entity.Person;
import com.demo.softwaretests.person.exception.PersonCreationException;
import com.demo.softwaretests.person.repository.PersonRepository;
import com.demo.softwaretests.person.util.Persons;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonService personService;

    @Test
    public void givenEmailDomain_whenGetAllPersonsByEmailDomain_thenReturnPersons() {
        // Arrange
        var domain = "@gmail.com";
        when(personRepository.findAllByEmailAddressEndsWith(domain)).thenReturn(Persons.listOfRichard());

        // Act
        List<Person> persons = personService.getAllPersonsByEmailDomain(domain);

        // Assert
        assertNotNull(persons);
        assertEquals(1, persons.size());
        assertEquals("richard.ruediger@gmail.com", persons.getFirst().getEmailAddress());
    }

    @Test
    public void givenAgeRange_whenGetAllPersonsByAgeRange_thenReturnPersons() {
        // Arrange
        int fromAge = 30;
        int toAge = 50;
        when(personRepository.findAllByAgeBetween(fromAge, toAge)).thenReturn(Persons.listOfRichard());

        // Act
        List<Person> persons = personService.getAllPersonsByAgeRange(fromAge, toAge);

        // Assert
        assertNotNull(persons);
        assertEquals(1, persons.size());

        Person foundPerson = persons.getFirst();
        assertTrue(foundPerson.getAge() >= fromAge && foundPerson.getAge() <= toAge);
    }

    @Test
    public void givenPersonDetails_whenCreatePerson_thenSavePerson() {
        // Arrange
        var firstName = "Lilliane";
        var lastName = "Langdorf";
        var email = "lilliane.langdorf@icloud.com";
        var dateOfBirth = LocalDate.of(1995, 3, 20);

        // Act
        personService.createPerson(firstName, lastName, email, dateOfBirth);

        // Assert
        verify(personRepository, times(1)).save(any(Person.class));
    }

    @Test
    public void givenBirthDate_whenCalculateAge_thenReturnCorrectAge() {
        // Arrange
        var birthDate = LocalDate.of(1994, 7, 30);
        var thirtiethBirthDay = LocalDate.of(2024, 7, 30);

        // Act
        int age = personService.calculateAge(birthDate, thirtiethBirthDay);

        // Assert
        assertEquals(30, age);
    }

    @Test
    public void givenValidParameters_whenValidateParameters_thenDoNotThrowException() {
        // Arrange
        var email = "example@gmail.com";
        var dateOfBirth = LocalDate.of(2000, 1, 1);

        // Act & Assert
        assertDoesNotThrow(() -> personService.validateParameters(dateOfBirth, email));
    }

    @Test
    public void givenTooYoungPerson_whenValidateParameters_thenThrowPersonCreationException() {
        // Arrange
        var email = "example@gmail.com";
        var dateOfBirth = LocalDate.of(2010, 1, 1);

        // Act & Assert
        PersonCreationException personCreationException = assertThrows(
                PersonCreationException.class,
                () -> personService.validateParameters(dateOfBirth, email)
        );
        assertEquals("The minimum required age is 18.", personCreationException.getMessage());
    }

    @Test
    public void givenDuplicateEmail_whenValidateParameters_thenThrowPersonCreationException() {
        // Arrange
        var email = "example@gmail.com";
        var dateOfBirth = LocalDate.of(1990, 1, 1);
        when(personRepository.existsByEmailAddress(email)).thenReturn(true);

        // Act & Assert
        PersonCreationException exception = assertThrows(
                PersonCreationException.class,
                () -> personService.validateParameters(dateOfBirth, email)
        );
        assertEquals("The email address: example@gmail.com is already in use.", exception.getMessage());
    }
}
