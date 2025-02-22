package com.demo.softwaretests.person.repository;

import com.demo.softwaretests.person.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    List<Person> findAllByAgeBetween(int fromAge, int toAge);
    List<Person> findAllByEmailAddressEndsWith(String emailDomain);

    boolean existsByEmailAddress(String emailAddress);
}
