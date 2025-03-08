package com.demo.softwaretests.person.service;

import com.demo.softwaretests.person.entity.Person;
import com.demo.softwaretests.person.exception.PersonCreationException;
import com.demo.softwaretests.person.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class PersonService {

    private static final int MINIMUM_AGE = 18;

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> getAllPersonsByEmailDomain(String domain) {
        return personRepository.findAllByEmailAddressEndsWith(domain);
    }

    public List<Person> getAllPersonsByAgeRange(int fromAge, int toAge) {
        return personRepository.findAllByAgeBetween(fromAge, toAge);
    }

    public void createPerson(String firstName, String lastName, String email, LocalDate dateOfBirth) {
        validateParameters(dateOfBirth, email);

        Person person = new Person();
        person.setFullName(firstName + " " + lastName);
        person.setEmailAddress(email);
        person.setAge(calculateAge(dateOfBirth, LocalDate.now()));

        personRepository.save(person);
    }

    public int calculateAge(LocalDate startDate, LocalDate endDate) {

        return Period.between(startDate, endDate).getYears();
    }

    public void validateParameters(LocalDate dateOfBirth, String emailAddress) {

        if (calculateAge(dateOfBirth, LocalDate.now()) < MINIMUM_AGE) {

            throw new PersonCreationException(
                    String.format("The minimum required age is %d.", MINIMUM_AGE)
            );
        }

        if (personRepository.existsByEmailAddress(emailAddress)) {

            throw new PersonCreationException(
                    String.format("The email address: %s is already in use.", emailAddress)
            );
        }
    }
}
