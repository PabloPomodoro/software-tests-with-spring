package com.demo.softwaretests.repository;

import com.demo.softwaretests.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    List<Person> findAllByAgeBetween(int fromAge, int toAge);
    List<Person> findAllByEmailEndsWith(String emailDomain);
}
