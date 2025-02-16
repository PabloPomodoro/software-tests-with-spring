package com.demo.softwaretests.service;

import com.demo.softwaretests.entity.Person;
import com.demo.softwaretests.exception.PersonCreationException;
import com.demo.softwaretests.repository.PersonRepository;
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

    public void createPerson(String firstName, String lastName, String email, LocalDate dateOfBirth) {
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

        if (personRepository.findByEmail(emailAddress)) {
            throw new PersonCreationException(
                    String.format("The email address: %s is already in use.", emailAddress)
            );
        }
    }
}
