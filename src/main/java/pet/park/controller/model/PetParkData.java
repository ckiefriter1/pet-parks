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
 * This class is a Data Transfer Object (DTO). It is used to load data from JSON
 * sent in the pet park requests and to convert to JSON in the replies. It is
 * basically the same as the {@link PetPark} entity but without the recursive
 * nature in the instance variables. This keeps the Jackson library that
 * converts to and from JSON from going berserk.
 * 
 * Here are the class-level annotations:
 * 
 * @Data This is a Lombok library annotation. It adds getters and setters for
 *       all the instance variables as well as .toString(), .hashCode() and
 *       .equals() methods. It also adds a no-argument constructor.
 * 
 * @NoArgsConstructor This is also a Lombok annotation. It creates a no-argument
 *                    constructor. This is needed because Jackson requires it.
 *                    When we added code for the {@link #PetParkData(PetPark)
 *                    constructor} that takes a {@link PetPark}, Lombok removed
 *                    the no-argument constructor created with the @ Data
 *                    annotation. This adds it back.
 * 
 * @author Promineo
 *
 */
@Data
@NoArgsConstructor
public class PetParkData {
  private Long petParkId;
  private String parkName;
  private String directions;
  private String stateOrProvince;
  private String country;
  private GeoLocation geoLocation;
  private PetParkContributor contributor;
  private Set<String> amenities = new HashSet<>();

  /**
   * This constructor converts from a {@link PetPark} object to a
   * {@link PetParkData} object.
   * 
   * @param petPark The object to convert.
   */
  public PetParkData(PetPark petPark) {
    petParkId = petPark.getPetParkId();
    parkName = petPark.getParkName();
    directions = petPark.getDirections();
    stateOrProvince = petPark.getStateOrProvince();
    country = petPark.getCountry();
    geoLocation = petPark.getGeoLocation();
    contributor = new PetParkContributor(petPark.getContributor());

    for(Amenity amenity : petPark.getAmenities()) {
      amenities.add(amenity.getAmenity());
    }
  }

  /**
   * This inner class is basically the same as a {@link Contributor} entity
   * class but without recursion in the instance variables. A Contributor object
   * contains a set of PetPark objects and a PetPark contains a Contributor.
   * This is required so that JPA can manage the object relationships correctly,
   * but is not so good for Jackson's conversion to and from JSON.
   * 
   * @author Promineo
   *
   */
  @Data
  @NoArgsConstructor
  public static class PetParkContributor {
    private Long contributorId;
    private String contributorName;
    private String contributorEmail;

    public PetParkContributor(Contributor contributor) {
      contributorId = contributor.getContributorId();
      contributorName = contributor.getContributorName();
      contributorEmail = contributor.getContributorEmail();
    }
  }
}
