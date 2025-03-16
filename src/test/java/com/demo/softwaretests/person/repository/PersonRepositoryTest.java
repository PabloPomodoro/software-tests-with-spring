package com.demo.softwaretests.person.repository;

import com.demo.softwaretests.person.entity.Person;
import com.demo.softwaretests.person.util.Persons;
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

    @Test
    void givenAllPersons_whenFindAllByAgeBetween_thenReturnPersonsInAgeRange() {
        // Arrange
        int fromAge = 25;
        int toAge = 45;

        when(personRepository.findAllByAgeBetween(fromAge, toAge)).thenReturn(Persons.listOfRichardAndLilliane());

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
        var emailDomain = "@gmail.com";

        when(personRepository.findAllByEmailAddressEndsWith(emailDomain)).thenReturn(Persons.listOfRichardAndGuenther());

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
        var emailAddress = "lilliane.langdorf@icloud.com";
        when(personRepository.existsByEmailAddress(emailAddress)).thenReturn(true);

        // Act
        boolean existsByEmailAddress = personRepository.existsByEmailAddress(emailAddress);

        // Assert
        assertThat(existsByEmailAddress).isTrue();
    }

    @Test
    void givenEmailAddressNotInDb_whenExistsByEmailAddress_thenReturnFalse() {
        // Arrange
        var emailAddress = "new.address@gmail.com";
        when(personRepository.existsByEmailAddress(emailAddress)).thenReturn(false);

        // Act
        boolean existsByEmailAddress = personRepository.existsByEmailAddress(emailAddress);

        // Assert
        assertThat(existsByEmailAddress).isFalse();
    }
}