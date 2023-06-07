// Copyright (c) 2023 by Promineo Tech.

package pet.park.dao;

import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import pet.park.entity.Amenity;

/**
 * This interface extends {@link JpaRepository}. Both JpaRepository and its
 * parent interface {@link CrudRepository} provide convenient common methods
 * used for CRUD operations on the amenity table. When JPA starts up, it creates
 * backing methods for the method declarations in the interface.
 * 
 * @author Promineo
 *
 */
public interface AmenityDao extends JpaRepository<Amenity, Long> {

  /**
   * This method looks up amenity objects based on a list of amenity names.
   * 
   * This method declaration is implemented by Spring JPA. It generates SQL like
   * this: select a1_0.amenity_id,a1_0.amenity from amenity a1_0 where
   * a1_0.amenity in(?,?,?,?) where the question marks are replaced by the
   * amenity names.
   * 
   * @param amenities The set of amenity names to look up in the amenity table.
   * @return The amenity objects matching the names.
   */
  Set<Amenity> findAllByAmenityIn(Set<String> amenities);

}
