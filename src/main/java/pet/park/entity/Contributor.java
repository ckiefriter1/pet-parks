// Copyright (c) 2023 by Promineo Tech.

package pet.park.entity;

import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
 *         snake case (so the Contributor class becomes the contributor table).
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
public class Contributor {
  /**
   * @Id This annotation tells Spring JPA that the instance variable annotated
   *     (<em>amenityId</em>) is the table identity (primary key) value.
   * 
   * @GeneratedValue This annotation tells JPA what type of generation strategy
   *                 is used to generate primary key values. In this case,
   *                 IDENTITY tells Spring JPA that MySQL will maintain the
   *                 primary key values.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long contributorId;

  private String contributorName;

  /**
   * The @Column annotation is used to tell Spring JPA to create a unique index
   * on the contributor email column.
   */
  @Column(unique = true)
  private String contributorEmail;

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
   * @OneToMany Indicates the secondary class in a bidirectional one-to-many
   *            relationship. The primary class is indicated by the variable
   *            data type. The mappedBy attribute tells JPA which Java variable
   *            in the PetPark class has the other end of the one-to-many
   *            relationship. So, PetPark.contributor contains the primary
   *            relationship definition.
   */
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  @OneToMany(mappedBy = "contributor", cascade = CascadeType.ALL)
  private Set<PetPark> petParks = new HashSet<>();
}
