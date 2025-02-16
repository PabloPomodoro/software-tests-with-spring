package com.demo.softwaretests.service;

import com.demo.softwaretests.entity.Person;
import com.demo.softwaretests.exception.PersonCreationException;
import com.demo.softwaretests.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonService personService;

    private Person richard;
    private Person guenther;

    @BeforeEach
    public void setUp() {
        richard = new Person();
        richard.setFullName("Richard Rüdiger");
        richard.setEmailAddress("richard.ruediger@gmail.com");
        richard.setAge(41);

        guenther = new Person();
        guenther.setFullName("Günther Grandiger");
        guenther.setEmailAddress("guenther.grandiger@yahoo.com");
        guenther.setAge(55);
    }

    @Test
    public void givenEmailDomain_whenGetAllPersonsByEmailDomain_thenReturnPersons() {
        // Arrange
        String domain = "@gmail.com";
        when(personRepository.findAllByEmailAddressEndsWith(domain)).thenReturn(List.of(richard));

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
        when(personRepository.findAllByAgeBetween(fromAge, toAge)).thenReturn(List.of(richard));

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
        String firstName = "Lilliane";
        String lastName = "Langdorf";
        String email = "lilliane.langdorf@icloud.com";
        LocalDate dateOfBirth = LocalDate.of(1995, 3, 20);

        // Act
        personService.createPerson(firstName, lastName, email, dateOfBirth);

        // Assert
        verify(personRepository, times(1)).save(any(Person.class));
    }

    @Test
    public void givenBirthDate_whenCalculateAge_thenReturnCorrectAge() {
        // Arrange
        LocalDate birthDate = LocalDate.of(1994, 7, 30);
        LocalDate thirtiethBirthDay = LocalDate.of(2024, 7, 30);

        // Act
        int age = personService.calculateAge(birthDate, thirtiethBirthDay);

        // Assert
        assertEquals(30, age);
    }

    @Test
    public void givenValidParameters_whenValidateParameters_thenDoNotThrowException() {
        // Arrange
        String email = "example@gmail.com";
        LocalDate dateOfBirth = LocalDate.of(2000, 1, 1);

        // Act & Assert
        assertDoesNotThrow(() -> personService.validateParameters(dateOfBirth, email));
    }

    @Test
    public void givenTooYoungPerson_whenValidateParameters_thenThrowPersonCreationException() {
        // Arrange
        String email = "example@gmail.com";
        LocalDate dateOfBirth = LocalDate.of(2010, 1, 1);

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
        String email = "example@gmail.com";
        LocalDate dateOfBirth = LocalDate.of(1990, 1, 1);
        when(personRepository.existsByEmailAddress(email)).thenReturn(true);

        // Act & Assert
        PersonCreationException exception = assertThrows(
                PersonCreationException.class,
                () -> personService.validateParameters(dateOfBirth, email)
        );
        assertEquals("The email address: example@gmail.com is already in use.", exception.getMessage());
    }
}
