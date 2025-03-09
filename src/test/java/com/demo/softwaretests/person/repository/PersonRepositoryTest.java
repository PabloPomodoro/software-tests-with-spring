package com.demo.softwaretests.person.repository;

import com.demo.softwaretests.person.entity.Person;
import com.demo.softwaretests.person.util.TestDataUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonRepositoryTest {

    @Mock
    private PersonRepository personRepository;

    private List<Person> persons;

    @BeforeEach
    void setUp() {
        persons = TestDataUtil.listOfRichardAndGuentherAndLilliane();
    }

    @Test
    void givenAllPersons_whenFindAllByAgeBetween_thenReturnPersonsInAgeRange() {
        // Arrange
        int fromAge = 25;
        int toAge = 45;

        when(personRepository.findAllByAgeBetween(fromAge, toAge)).thenReturn(TestDataUtil.listOfRichardAndLilliane());

        // Act
        List<Person> persons = personRepository.findAllByAgeBetween(fromAge, toAge);

        // Assert
        assertThat(persons).hasSize(2);
        assertThat(persons)
                .extracting(Person::getFullName)
                .containsExactlyInAnyOrder("Richard Rüdiger", "Lilliane Langdorf");
    }

    @Test
    void givenPersonsWithDifferentEmailAddresses_whenFindAllByEmailAddressEndsWith_thenReturnPersonsWithSameDomains() {
        // Arrange
        String emailDomain = "@gmail.com";

        when(personRepository.findAllByEmailAddressEndsWith(emailDomain)).thenReturn(TestDataUtil.listOfRichardAndGuenther());

        // Act
        List<Person> persons = personRepository.findAllByEmailAddressEndsWith(emailDomain);

        // Assert
        assertThat(persons).hasSize(2);
        assertThat(persons)
                .extracting(Person::getEmailAddress)
                .containsExactlyInAnyOrder("richard.ruediger@gmail.com", "guenther.grandiger@gmail.com");
    }

    @Test
    void givenEmailAddressInDb_whenExistsByEmailAddress_thenReturnTrue() {
        // Arrange
        String emailAddress = "lilliane.langdorf@icloud.com";
        when(personRepository.existsByEmailAddress(emailAddress)).thenReturn(true);

        // Act
        boolean existsByEmailAddress = personRepository.existsByEmailAddress(emailAddress);

        // Assert
        assertThat(existsByEmailAddress).isTrue();
    }

    @Test
    void givenEmailAddressNotInDb_whenExistsByEmailAddress_thenReturnFalse() {
        // Arrange
        String emailAddress = "new.address@gmail.com";
        when(personRepository.existsByEmailAddress(emailAddress)).thenReturn(false);

        // Act
        boolean existsByEmailAddress = personRepository.existsByEmailAddress(emailAddress);

        // Assert
        assertThat(existsByEmailAddress).isFalse();
    }
}