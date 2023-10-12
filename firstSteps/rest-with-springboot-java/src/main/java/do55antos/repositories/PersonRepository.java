package do55antos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import do55antos.model.Person;

public interface PersonRepository extends JpaRepository<Person, Long>{}
