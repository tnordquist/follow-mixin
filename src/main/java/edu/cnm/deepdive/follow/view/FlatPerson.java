package edu.cnm.deepdive.follow.view;

import java.net.URI;
import java.util.UUID;

/**
 * Declares the getters (and thus the JSON properties) of a source for serialization, excluding
 * references to other objects that could result in stack or buffer overflow on serialization.
 */
public interface FlatPerson {

  /**
   * Universally unique ID (UUID) of a person resource.
   */
  UUID getId();

  /**
   * URL of the person resource.
   */
  URI getHref();

  /**
   * First name of the person.
   */
  String getFirstName();

  /**
   * Last name of the person.
   */
  String getLastName();


}
