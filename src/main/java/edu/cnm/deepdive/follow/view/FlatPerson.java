package edu.cnm.deepdive.follow.view;

import java.net.URI;
import java.util.UUID;

public interface FlatPerson {

  /**
   * Universally unique ID (UUID) of a person resource.
   */
  UUID getId();

  /**
   * URL of the person resource.
   */
  URI getHref();

  String getFirstName();

  String getLastName();


}
