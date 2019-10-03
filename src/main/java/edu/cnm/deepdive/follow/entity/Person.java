package edu.cnm.deepdive.follow.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import edu.cnm.deepdive.follow.view.FlatPerson;
import java.net.URI;
import java.util.Objects;
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

/**
 * Defines a database entity and REST resource representing the person, and its relationships to
 * zero or more {@link Person} resources.
 */
@Entity
@Component
@JsonIgnoreProperties(
    value = {"created", "updated", "follows", "followers", "id",
        "href"}, allowGetters = true, ignoreUnknown = true)
public class Person implements Comparable<Person>, FlatPerson {

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


  public String getFirstName() {
    return firstName;
  }

  /**
   * Sets the first name of this <code>Person</code> instance.
   *
   * @param firstName person first name.
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  /**
   * Sets the last name of this <code>Person</code> instance.
   *
   * @param lastName person last name.
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public UUID getId() {
    return id;
  }

  /**
   * Returns a {@link Set} of the {@link Person} instances that are followers of this instance of
   * {@code Person}.
   *
   * @return {@link Person} set.
   */
  public Set<Person> getFollowers() {
    return followers;
  }

  /**
   * Returns a {@link Set} of the {@link Person} instances that this instance of {@code Person}
   * follows.
   *
   * @return {@link Person} set.
   */
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

  @Override
  public int compareTo(Person other) {
    int comparison = lastName.compareTo(other.lastName);
    if (comparison == 0) {
      comparison = firstName.compareTo(other.firstName);
    }
    return comparison;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Person) {
      Person person = (Person) obj;
      return (getFirstName().equals(person.firstName)
          && (getLastName().equals(person.lastName)));
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash(firstName,lastName);
  }

  @Override
  public String toString() {
    return getFirstName() + getLastName();
  }
}
