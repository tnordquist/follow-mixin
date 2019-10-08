package edu.cnm.deepdive.follow.controller;

import edu.cnm.deepdive.follow.entity.Person;
import edu.cnm.deepdive.follow.repository.PersonRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Defines REST endpoints for servicing requests on {@link Person} resources, invoking {@link
 * PersonRepository} methods to perform the required operations.
 */
@RestController
@ExposesResourceFor(Person.class)
@RequestMapping("/persons")
public class PersonController {

  private PersonRepository personRepository;

  /**
   * Initializes this instance, injecting an instance of {@link PersonRepository}.
   *
   * @param personRepository repository used for operations on {@link Person} entity instances.
   */
  @Autowired
  public PersonController(PersonRepository personRepository) {
    this.personRepository = personRepository;
  }

  /**
   * Returns a sequence of all the {@link Person} resources in the database.
   *
   * @return list of {@link Person} resources.
   */
  @RequestMapping(method = RequestMethod.GET)
  public List<Person> findAllPersons() {
    return personRepository.findAllByOrderByLastName();
  }

  /**
   * Adds the provided {@link Person} resource to the database and returns the completed resource,
   * including timestamp &amp; ID. The provided resource is only required to contain a {@code text}
   * property, with a non-{@code null} value.
   *
   * @param person partial {@link Person} resource.
   * @return completed {@link Person} resource.
   */
  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Person> post(@RequestBody Person person) {
    personRepository.save(person);
    return ResponseEntity.created(person.getHref()).body(person);
  }

  /**
   * Replaces the first and last name of the referenced existing {@link Person} resource with the
   * name from the provided resource.
   *
   * @param personId source {@link UUID}.
   * @param update {@link Person} resource to use to replace contents of existing source.
   */
  @PutMapping(value = "{person_id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public void put(@PathVariable("person_id") UUID personId, @RequestBody Person update) {
    Person person = personRepository.findById(personId).get();
    person.setFirstName(update.getFirstName());
    person.setLastName(update.getLastName());
    personRepository.save(person);
  }

  /**
   * Retrieves and returns the {@link Person} resource with the specified ID.
   *
   * @param personId source {@link UUID}.
   * @return retrieved {@link Person} resource.
   */
  @GetMapping(value = "{person_id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Person get(@PathVariable("person_id") UUID personId) {
    return personRepository.findById(personId).get();
  }

  //  @GetMapping(value = "{last_name}",produces = MediaType.APPLICATION_JSON_VALUE)
  //  public Person get(@PathVariable("last_name") Person lastName){
  //    return personRepository.findByLastName(lastName);
  //  }

  /**
   * Associates the {@link Person} referenced in the path with another {@link Person}, also
   * referenced by a path parameter.
   *
   * @param followerId {@link UUID} of {@link Person} resource.
   * @param followsId {@link UUID} of {@link Person} to be associated with referenced {@link
   * Person}.
   */
  @PutMapping(value = "{follower_id}/follows/{follows_id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void put(
      @PathVariable("follower_id") UUID followerId, @PathVariable("follows_id") UUID followsId) {
    Person follower = get(followerId);
    Person follows = get(followsId);
    follows.getFollowers().add(follower);
    personRepository.save(follows);
  }

  /**
   * Maps (via annotation) a {@link NoSuchElementException} to a response status code of {@link
   * HttpStatus#NOT_FOUND}.
   */
  @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Person not found")
  @ExceptionHandler(NoSuchElementException.class)
  public void notFound() {
  }
}