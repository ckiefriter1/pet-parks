// Copyright (c) 2023 by Promineo Tech.

package pet.park.entity;

import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * This class is used by JPA to map a Java object to a table. JPA can create the
 * table in the database in addition to managing the data.
 * 
 * Class-level annotations used in this class:
 * 
 * @Entity This tells Spring JPA that this class maps to a data table. The table
 *         name can be used as a parameter to the @Entity annotation. If not
 *         supplied, the table name is the same as the class name converted to
 *         snake case (so the PetPark class becomes the pet_park table).
 * 
 * @Data This is a Lombok annotation that creates getters and setters for all
 *       instance variables. It also creates .hashCode(), .equals, and
 *       .toString() methods.
 * 
 * @author Promineo
 *
 */
@Entity
@Data
public class PetPark {
  /**
   * @Id This annotation tells Spring JPA that the instance variable annotated
   *     (<em>petParkId</em>) is the table identity (primary key) value.
   * 
   * @GeneratedValue This annotation tells JPA what type of generation strategy
   *                 is used to generate primary key values. In this case,
   *                 IDENTITY tells Spring JPA that MySQL will maintain the
   *                 primary key values.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long petParkId;

  private String parkName;
  private String directions;
  private String stateOrProvince;
  private String country;

  /**
   * @Embedded This instructs Spring JPA to include the instance variables in
   *           the {@link GeoLocation} class in the table definition.
   */
  @Embedded
  private GeoLocation geoLocation;

  /**
   * @EqualsAndHashCode.Exclude Instructs Lombok to exclude the instance
   *                            variable from the generated .equals() and
   *                            .hashCode() methods. Without the exclusion, an
   *                            endless recursion occurs when calling .equals()
   *                            or .hashCode().
   * 
   * @ToString.Exclude Instructs Lombok to exclude the instance variable from
   *                   the generated .toString() method. Without the exclusion,
   *                   an endless recursion occurs when calling .toString().
   * 
   * @ManyToOne Indicates the secondary class in a bidirectional one-to-many
   *            relationship. The primary class is indicated by the variable
   *            data type.
   * 
   * @JoinColumn The column name of the one-to-many relationship.
   */
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "contributor_id", nullable = false)
  private Contributor contributor;

  /**
   * @ManyToMany This form (without mappedBy) indicates that this is the primary
   *             end of the many-to-many relationship with the amenity table.
   *             The table is identified by the variable data type.
   * 
   * @JoinTable Instructs Spring JPA to create a join table to manage the
   *            many-to-many relationship between PetPark and Amenity. The table
   *            name is specified and the two join columns names are specified
   *            as well. Note that these are column names and not Java class
   *            variable names. There is no backing Java class for the
   *            pet_park_amenity table.
   */
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  @ManyToMany(cascade = CascadeType.PERSIST)
  @JoinTable(name = "pet_park_amenity",
      joinColumns = @JoinColumn(name = "pet_park_id"),
      inverseJoinColumns = @JoinColumn(name = "amenity_id"))
  private Set<Amenity> amenities = new HashSet<>();
}
