package com.demo.softwaretests.person;

import com.demo.softwaretests.person.entity.Person;
import com.demo.softwaretests.person.repository.PersonRepository;
import com.demo.softwaretests.person.util.TestDataUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PersonRepositoryIntegrationTest {

    @Autowired
    private PersonRepository personRepository;

    @BeforeEach
    void setUp() {
        personRepository.saveAll(TestDataUtil.listOfRichardAndGuentherAndLilliane());
    }

    @Test
    void givenPersonWithinAgeRange_whenFindAllByAgeBetween_thenReturnCorrectPerson() {
        // Arrange
        int fromAge = 20;
        int toAge = 30;

        // Act
        List<Person> persons = personRepository.findAllByAgeBetween(fromAge, toAge);

        // Assert
        assertThat(persons).hasSize(1);
        assertThat(persons).extracting(Person::getFullName).containsExactly("Lilliane Langdorf");
    }

    @Test
    void givenPersonsWithSpecificEmailDomain_whenFindAllByEmailAddressEndsWith_thenReturnCorrectPersons() {
        // Arrange
        String emailDomain = "gmail.com";

        // Act
        List<Person> persons = personRepository.findAllByEmailAddressEndsWith(emailDomain);

        // Assert
        assertThat(persons).hasSize(2);
        assertThat(persons)
                .extracting(Person::getEmailAddress)
                .containsExactly("richard.ruediger@gmail.com", "guenther.grandiger@gmail.com");
    }

    @Test
    void givenPersonExists_whenExistsByEmailAddress_thenReturnTrue() {
        // Arrange
        String emailAddress = "richard.ruediger@gmail.com";

        // Act
        boolean exists = personRepository.existsByEmailAddress(emailAddress);

        // Assert
        assertThat(exists).isTrue();
    }

    @Test
    void givenPerson_whenSaveAndFindById_thenReturnCorrectPerson() {
        // Arrange
        Person savedPerson = personRepository.save(TestDataUtil.bianca());

        // Act
        Person foundPerson = personRepository.findById(savedPerson.getId()).orElseThrow();

        // Assert
        assertThat(foundPerson).isEqualTo(savedPerson);
        assertThat(foundPerson).hasFieldOrPropertyWithValue("fullName", "Bianca Bambus");
        assertThat(foundPerson).hasFieldOrPropertyWithValue("emailAddress", "bianca.bambus@yahoo.com");
        assertThat(foundPerson).hasFieldOrPropertyWithValue("age", 22);
    }


    @Test
    void givenPerson_whenDelete_thenPersonIsDeleted() {
        // Arrange
        Person bianca = TestDataUtil.bianca();
        personRepository.save(bianca);

        // Act
        personRepository.delete(bianca);

        // Assert
        List<Person> persons = personRepository.findAll();
        assertThat(persons).doesNotContain(bianca);
    }
}
