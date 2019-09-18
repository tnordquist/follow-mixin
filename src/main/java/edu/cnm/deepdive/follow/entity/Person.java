package edu.cnm.deepdive.follow.entity;

import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Person implements Comparable {


  @Override
  public int compareTo(Object o) {
    return 0;
  }

  @Id
  @GeneratedValue
  private UUID id;

  //  @Column(name = "followers_id", columnDefinition = "CHAR(16) FOR BIT DATA",
  //      nullable = false, updatable = false)
  //  private UUID id;

  private String firstName;
  private String lastName;

//  @OneToMany(mappedBy = "followers", fetch = FetchType.EAGER)
//  private Set<Person> followers = new TreeSet<>();
//
  @ManyToMany(mappedBy = "follows", fetch = FetchType.EAGER)
  private Set<Person> follows = new TreeSet<>();

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

  public void setId(UUID id) {
    this.id = id;
  }
}
