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

@RestController
@ExposesResourceFor(Person.class)
@RequestMapping("/persons")
public class PersonController {

  private PersonRepository personRepository;

  @Autowired
  public PersonController(PersonRepository personRepository) {
    this.personRepository = personRepository;
  }

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

  @PutMapping(value = "{person_id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public void put(@PathVariable("person_id") UUID person_id, @RequestBody Person update) {
    Person person = personRepository.findById(person_id).get();
    person.setFirstName(update.getFirstName());
    person.setLastName(update.getLastName());
    personRepository.save(person);
  }

  @GetMapping(value = "{person_id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Person get(@PathVariable("person_id") UUID person_id) {
    return personRepository.findById(person_id).get();
  }

//  @GetMapping(value = "{last_name}",produces = MediaType.APPLICATION_JSON_VALUE)
//  public Person get(@PathVariable("last_name") Person lastName){
//    return personRepository.findByLastName(lastName);
//  }

  @PutMapping(value = "{follower_id}/follows/{follows_id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void put(
      @PathVariable("follower_id") UUID followerId, @PathVariable("follows_id") UUID followsId) {
    Person follower = get(followerId);
    Person follows = get(followsId);
    if (!follows.getFollowers().contains(follower)) {
      follows.getFollowers().add(follower);
    }
    personRepository.save(follows);
  }

  @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Person not found")
  @ExceptionHandler(NoSuchElementException.class)
  public void notFound() {
  }

  //  @GetMapping(value = "persons/{person_id}/follows/{person2_id}", produces = MediaType.APPLICATION_JSON_VALUE)
  //  public Person get(@PathVariable("person_id") UUID person_id, @PathVariable("person2_id") UUID person2_id) {
  //    Person person = get(person_id);
  //    Person person2 = personRepository.findById(person_id).get();
  //    if (!source.getPerson().contains(person2)) {
  //      throw new NoSuchElementException();
  //    }
  //    return person;
  //  }
}
