package com.demo.softwaretests.person.repository;

import com.demo.softwaretests.person.entity.Person;
import com.demo.softwaretests.person.util.TestDataUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    @BeforeEach
    void setUp() {
        personRepository.saveAll(
                TestDataUtil.listOfRichardAndGuentherAndLilliane()
        );
    }

    @Test
    void givenAllPersons_whenFindAllByAgeBetween_thenReturnPersonsInAgeRange() {
        // Arrange
        int fromAge = 25;
        int toAge = 45;

        // Act
        List<Person> persons = personRepository.findAllByAgeBetween(fromAge, toAge);

        // Assert
        assertThat(persons).hasSize(2);
        assertThat(persons).extracting(Person::getFullName)
                .containsExactlyInAnyOrder(
                        "Richard Rüdiger",
                        "Lilliane Langdorf"
                );
    }

    @Test
    void givenPersonsWithDifferentEmailAddresses_whenFindAllByEmailAddressEndsWith_thenReturnPersonsWithSameDomains() {
        // Arrange
        String emailDomain = "@gmail.com";

        // Act
        List<Person> persons = personRepository.findAllByEmailAddressEndsWith(emailDomain);

        // Assert
        assertThat(persons).hasSize(2);
        assertThat(persons).extracting(Person::getEmailAddress)
                .containsExactlyInAnyOrder(
                        "richard.ruediger@gmail.com",
                        "guenther.grandiger@gmail.com"
                );
    }

    @Test
    void givenEmailAddressInDb_whenExistsByEmailAddress_thenReturnTrue() {
        // Arrange
        String emailAddress = "lilliane.langdorf@icloud.com";

        // Act
        boolean existsByEmailAddress = personRepository.existsByEmailAddress(emailAddress);

        // Assert
        assertThat(existsByEmailAddress).isTrue();
    }

    @Test
    void givenEmailAddressNotInDb_whenExistsByEmailAddress_thenReturnFalse() {
        // Arrange
        String emailAddress = "example@gmail.com";

        // Act
        boolean existsByEmailAddress = personRepository.existsByEmailAddress(emailAddress);

        // Assert
        assertThat(existsByEmailAddress).isFalse();
    }
}
