// Copyright (c) 2023 by Promineo Tech.

package pet.park.entity;

import java.math.BigDecimal;
import java.util.Objects;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class embeds geolocation data into another class ({@link PetPark}). This
 * class simply contains the latitude and longitude of a pet park.
 * 
 * Class-level annotations:
 * 
 * @Embeddable Tells Spring JPA that this class can be embedded into a JPA
 *             entity.
 * 
 * @Data This is a Lombok annotation that creates getters and setters for all
 *       instance variables. It also creates .hashCode(), .equals, and
 *       .toString() methods.
 * 
 * @NoArgsConstructor Because a copy constructor is defined, Lombok will remove
 *                    the no-argument constructor created by the @Data
 *                    annotation. Since the Jackson library requires a
 *                    no-argument constructor, one must be created. This
 *                    instructs Lombok to add the no-argument constructor back
 *                    into the class.
 * 
 * @author Promineo
 *
 */
@Embeddable
@Data
@NoArgsConstructor
public class GeoLocation {
  private BigDecimal latitude;
  private BigDecimal longitude;

  /**
   * Copy constructor. This takes a GeoLocation object and copies it. It creates
   * new BigDecimal objects and does not simply copy the references.
   * 
   * @param geoLocation
   */
  public GeoLocation(GeoLocation geoLocation) {
    if(Objects.nonNull(geoLocation)) {
      if(Objects.nonNull(geoLocation.latitude)) {
        this.latitude = new BigDecimal(geoLocation.latitude.toString());
      }

      if(Objects.nonNull(geoLocation.longitude)) {
        this.longitude = new BigDecimal(geoLocation.longitude.toString());
      }
    }
  }
}
