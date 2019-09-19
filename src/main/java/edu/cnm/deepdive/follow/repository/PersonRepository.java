package edu.cnm.deepdive.follow.repository;

import edu.cnm.deepdive.follow.entity.Person;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person, UUID> {



}
