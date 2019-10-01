package edu.cnm.deepdive.follow.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import edu.cnm.deepdive.follow.view.FlatPerson;
import java.net.URI;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.stereotype.Component;


@Component
@JsonIgnoreProperties(
    value = {"created", "updated", "follows", "followers", "id",
        "href"}, allowGetters = true, ignoreUnknown = true)
@Entity
public class Person implements Comparable, FlatPerson {

  private static EntityLinks entityLinks;
  @Id
  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  @Column(name = "person_id", columnDefinition = "CHAR(16) FOR BIT DATA",
      nullable = false, updatable = false)
  private UUID id;
  private String firstName;
  private String lastName;

  @JsonSerialize(contentAs = FlatPerson.class)
  @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE,
      CascadeType.PERSIST, CascadeType.REFRESH})
  @JoinTable(
      name = "follow",
      joinColumns = @JoinColumn(name = "follower_id"), inverseJoinColumns = @JoinColumn(name = "followed_id"))
  private Set<Person> followers = new TreeSet<>();

  @JsonSerialize(contentAs = FlatPerson.class)
  @ManyToMany(mappedBy = "followers", fetch = FetchType.LAZY)
  private Set<Person> follows = new TreeSet<>();

  @Override
  public int compareTo(Object o) {
    return 0;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public UUID getId() {
    return id;
  }

  public Set<Person> getFollowers() {
    return followers;
  }

  public Set<Person> getFollows() {
    return follows;
  }

  public URI getHref() {
    return entityLinks.linkForSingleResource(Person.class, id).toUri();
  }

  @PostConstruct
  private void init() {
    String ignore = entityLinks.toString(); // Deliberately ignored.
  }

  @Autowired
  private void setEntityLinks(EntityLinks entityLinks) {
    Person.entityLinks = entityLinks;
  }

}
