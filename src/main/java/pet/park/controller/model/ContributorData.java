// Copyright (c) 2023 by Promineo Tech.

package pet.park.controller.model;

import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import lombok.NoArgsConstructor;
import pet.park.entity.Amenity;
import pet.park.entity.Contributor;
import pet.park.entity.GeoLocation;
import pet.park.entity.PetPark;

/**
 * This class is a Data Transfer Object (DTO). It is used to shuttle data from
 * one application layer to another. It is essentially the same as the
 * {@link Contributor} entity without the recursion needed to manage Java
 * Persistence API (JPA) relationships.
 * 
 * Here are explanations of the class-level annotations:
 * 
 * @Data This is a Lombok annotation. When @Data is present, Lombok adds getters
 *       and setters for all instance variables. It also adds a no-argument
 *       constructor along with .hashCode(), .equals(), and a readable
 *       .toString() method.
 * 
 * @NoArgsConstructor An additional {@link #ContributorData(Contributor)
 *                    constructor} was added to convert from a
 *                    {@link Contributor} entity to a ContributorData object.
 *                    When this was done, Lombok removed the no-args constructor
 *                    provided by the @Data annotation. However, the Jackson
 *                    library needs a no-argument constructor to convert to and
 *                    from JSON. This annotation adds it back.
 * 
 * @author Promineo
 *
 */
@Data
@NoArgsConstructor
public class ContributorData {
  private Long contributorId;
  private String contributorName;
  private String contributorEmail;
  private Set<PetParkResponse> petParks = new HashSet<>();

  /**
   * This constructor converts a Contributor object to a ContributorData object.
   * It is needed because the DAO methods return the entity classes, of which
   * {@link Contributor} is one. However, the entity has a recursive nature due
   * to the way that JPA manages relationships. The ContributorData class has
   * the same variables but without the recursion.
   */
  public ContributorData(Contributor contributor) {
    contributorId = contributor.getContributorId();
    contributorName = contributor.getContributorName();
    contributorEmail = contributor.getContributorEmail();

    for(PetPark petPark : contributor.getPetParks()) {
      petParks.add(new PetParkResponse(petPark));
    }
  }

  /**
   * This inner class contains the same data as the PetPark entity but without
   * the recursion. As such it is Jackson friendly.
   * 
   * @author Promineo
   *
   */
  @Data
  @NoArgsConstructor
  static class PetParkResponse {
    private Long petParkId;
    private String parkName;
    private String directions;
    private String stateOrProvince;
    private String country;
    private GeoLocation geoLocation;
    private Set<String> amenities = new HashSet<>();

    /**
     * Like the ContributorData constructor that converts an entity object, this
     * constructor takes the PetPark entity as a parameter and converts it to a
     * PetParkResponse object.
     * 
     * @param petPark
     */
    PetParkResponse(PetPark petPark) {
      petParkId = petPark.getPetParkId();
      parkName = petPark.getParkName();
      directions = petPark.getDirections();
      stateOrProvince = petPark.getStateOrProvince();
      country = petPark.getCountry();
      geoLocation = new GeoLocation(petPark.getGeoLocation());

      for(Amenity amenity : petPark.getAmenities()) {
        amenities.add(amenity.getAmenity());
      }
    }
  }
}
