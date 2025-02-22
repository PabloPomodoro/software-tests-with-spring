package com.demo.softwaretests.person.repository;

import com.demo.softwaretests.person.entity.Person;
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
        Person richard = new Person();
        richard.setFullName("Richard Rüdiger");
        richard.setEmailAddress("richard.ruediger@gmail.com");
        richard.setAge(41);

        Person guenther = new Person();
        guenther.setFullName("Günther Grandiger");
        guenther.setEmailAddress("guenther.grandiger@gmail.com");
        guenther.setAge(55);

        Person lilliane = new Person();
        lilliane.setFullName("Lilliane Langdorf");
        lilliane.setEmailAddress("lilliane.langdorf@icloud.com");
        lilliane.setAge(29);

        personRepository.saveAll(
                List.of(richard, guenther, lilliane)
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
    void givenPersonsWithDifferentEmailDomains_whenFindAllByEmailAddressEndsWith_thenReturnPersonsWithEmailDomainEmailAddress() {
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
    void givenPersonWithEmail_whenExistsByEmailAddress_thenReturnTrue() {
        // Arrange
        String emailAddress = "lilliane.langdorf@icloud.com";

        // Act
        boolean existsByEmailAddress = personRepository.existsByEmailAddress(emailAddress);

        // Assert
        assertThat(existsByEmailAddress).isTrue();
    }

    @Test
    void givenNoPersonWithEmail_whenExistsByEmailAddress_thenReturnFalse() {
        // Arrange
        String emailAddress = "example@gmail.com";

        // Act
        boolean existsByEmailAddress = personRepository.existsByEmailAddress(emailAddress);

        // Assert
        assertThat(existsByEmailAddress).isFalse();
    }
}
