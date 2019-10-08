package edu.cnm.deepdive.follow.repository;

import edu.cnm.deepdive.follow.entity.Person;
import java.util.List;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

/**
 * Declares database operations that can be performed on {@link Person} entity instances.
 */
public interface PersonRepository extends CrudRepository<Person, UUID> {

  /**
   * Selects and returns all {@link Person} instances, sorted in alphabetical order by last name.
   *
   * @return {@link Person} list of {@link Person} instances.
   */
  List<Person> findAllByOrderByLastName();

  /**
   * Returns a {@link Person} instance.
   *
   * @return a {@link Person} instance.
   */
  Person findByLastName(Person lastName);

}
