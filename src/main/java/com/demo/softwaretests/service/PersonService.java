package com.demo.softwaretests.service;

import com.demo.softwaretests.entity.Person;
import com.demo.softwaretests.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public void createPerson(String firstName, String lastName, String email, LocalDate dateOfBirth) {
        Person person = new Person();

        person.setFullName(firstName + " " + lastName);
        person.setEmail(email);
        person.setAge(calculateAge(dateOfBirth, LocalDate.now()));

        personRepository.save(person);
    }

    public int calculateAge(LocalDate startDate, LocalDate endDate) {

        return Period.between(startDate, endDate).getYears();
    }
}
