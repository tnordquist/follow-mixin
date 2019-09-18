package edu.cnm.deepdive.follow.controller;

import edu.cnm.deepdive.follow.entity.Person;
import edu.cnm.deepdive.follow.entity.request.AddPersonRequest;
import edu.cnm.deepdive.follow.repository.PersonRepository;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/person/{personId}/follow/{followId}")
public class PersonController {

  private PersonRepository personRepository;

  @Autowired
  public PersonController(PersonRepository personRepository) {
    this.personRepository = personRepository;
  }

  @RequestMapping(method = RequestMethod.GET)
  public Set<Person> findAllPersons() {
    return (Set<Person>) personRepository.findAll();
  }

  @RequestMapping(method = RequestMethod.POST)
  public void addPerson(@RequestBody AddPersonRequest addPersonRequest){
    Person person = new Person();
    person.setFirstName(addPersonRequest.getFirstName());
    person.setLastName(addPersonRequest.getLastName());
    personRepository.save(person);
  }


}
