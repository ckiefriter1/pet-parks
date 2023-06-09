// Copyright (c) 2023 by Promineo Tech.

package pet.park.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import pet.park.entity.Contributor;

/**
 * This interface extends {@link JpaRepository}. Both JpaRepository and its
 * parent interface {@link CrudRepository} provide convenient common methods
 * used for CRUD operations on the contributor table. When JPA starts up, it
 * creates backing methods for the method declarations in the interface.
 * 
 * @author Promineo
 *
 */
public interface ContributorDao extends JpaRepository<Contributor, Long> {

}
