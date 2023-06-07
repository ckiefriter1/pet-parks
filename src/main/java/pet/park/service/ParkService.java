// Copyright (c) 2023 by Promineo Tech.

package pet.park.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pet.park.controller.model.ContributorData;
import pet.park.controller.model.PetParkData;
import pet.park.dao.AmenityDao;
import pet.park.dao.ContributorDao;
import pet.park.dao.PetParkDao;
import pet.park.entity.Amenity;
import pet.park.entity.Contributor;
import pet.park.entity.PetPark;

/**
 * This class sits between the controller (I/O layer) and the data (DAO) layer.
 * It is responsible for transforming the DTO (Data Transfer Object) data into
 * the JPA entities and vice versa. It is also responsible for managing
 * transactions. Spring uses Aspect-Oriented Programming (AOP) techniques to
 * automatically manage the transaction. A public method is annotated
 * with @Transactional. This allows the method to be surrounded by other code
 * that starts the transaction and manages the error state. If an exception is
 * thrown, the transaction is rolled back. If no exception is thrown the
 * transaction is committed.
 * 
 * Class-level annotations:
 * 
 * @Service Tells Spring that this class is a Managed Bean. With a Managed Bean,
 *          Spring manages the class lifecycle and makes the class eligible for
 *          Dependency Injection (DI).
 * 
 * @author Promineo
 *
 */
@Service
public class ParkService {

  @Autowired
  private AmenityDao amenityDao;

  @Autowired
  private PetParkDao petParkDao;

  /**
   * The @Autowired annotation instructs Spring to inject an object of the
   * required type into the instance variable.
   */
  @Autowired
  private ContributorDao contributorDao;

  /**
   * Save the contributor in the contributor table. If the
   * <em>contributorID</em> is null, the contributor is added to the table. If
   * the <em>contributorID</em> is not null, the contributor is modified.
   * 
   * @param contributorData The contributor information.
   * @return A contributorData object with the primary key value
   *         (<em>contributorId</em>) set.
   * @throws NoSuchElementException Thrown if the contributor with the given ID
   *         is not found.
   */
  @Transactional(readOnly = false)
  public ContributorData saveContributor(ContributorData contributorData) {
    Long contributorId = contributorData.getContributorId();
    Contributor contributor = findOrCreateContributor(contributorId);

    setFieldsInContributor(contributor, contributorData);
    return new ContributorData(contributorDao.save(contributor));
  }

  /**
   * Copy data from the ContributorData object (that came in from the HTTP
   * request payload as JSON) into the Contributor entity that is used in JPA
   * operations.
   * 
   * @param contributor The Contributor target.
   * @param contributorData The ContributorData source.
   */
  private void setFieldsInContributor(Contributor contributor,
      ContributorData contributorData) {
    contributor.setContributorEmail(contributorData.getContributorEmail());
    contributor.setContributorName(contributorData.getContributorName());
  }

  /**
   * If the contributor ID is null, an empty contributor object is returned. If
   * the contributor ID is not null, the database is searched for the object. If
   * not found, an exception is thrown.
   * 
   * @param contributorId The contributor ID (may be {@code null}).
   * @return A Contributor entity object if successful.
   * @throws NoSuchElementException If the contributor ID is not null, an
   *         exception is thrown if the contributor is not in the database.
   */
  private Contributor findOrCreateContributor(Long contributorId) {
    Contributor contributor;

    if(Objects.isNull(contributorId)) {
      contributor = new Contributor();
    }
    else {
      contributor = findContributorById(contributorId);
    }

    return contributor;
  }

  /**
   * Find the contributor with the given ID in the database.
   * 
   * @param contributorId The contributor ID.
   * @return A Contributor entity object if successful.
   * @throws NoSuchElementException Thrown if the contributor with the matching
   *         contributor ID is not found.
   */
  private Contributor findContributorById(Long contributorId) {
    return contributorDao.findById(contributorId)
        .orElseThrow(() -> new NoSuchElementException(
            "Contributor with ID=" + contributorId + " was not found."));
  }

  /**
   * This method returns a list of all the contributors.
   * 
   * @return The list of contributors.
   */
  @Transactional(readOnly = true)
  public List<ContributorData> retrieveAllContributors() {
    /*
     * Uncomment the code below to retrieve all contributors using traditional
     * (non-functional) coding techniques. An enhanced for loop is used to
     * convert from the list of Contributor to the list of ContributorData
     * required by the method return type.
     */
    // List<Contributor> contributors = contributorDao.findAll();
    // List<ContributorData> response = new LinkedList<>();
    //
    // for(Contributor contributor : contributors) {
    // response.add(new ContributorData(contributor));
    // }
    //
    // return response;

    // @formatter:off
    return contributorDao.findAll() // Retrieves a List of Contributor
        .stream()                   // Converts to a Stream of Contributor
        .map(ContributorData::new)  // Converts to a Stream of ContributorData
        .toList();                  // Converts to a List of ContributorData
    // @formatter:on
  }

  /**
   * This method retrieves a specific contributor given the contributor ID
   * (primary key value).
   * 
   * @param contributorId The ID of the contributor to retrieve.
   * @return The contributor data.
   * @throws NoSuchElementException Thrown if the contributor ID is invalid.
   */
  @Transactional(readOnly = true)
  public ContributorData retrieveContributorById(Long contributorId) {
    Contributor contributor = findContributorById(contributorId);
    return new ContributorData(contributor);
  }

  /**
   * Delete a contributor given the contributor ID (primary key value).
   * 
   * @param contributorId The ID of the contributor to delete.
   */
  @Transactional(readOnly = false)
  public void deleteContributorById(Long contributorId) {
    Contributor contributor = findContributorById(contributorId);
    contributorDao.delete(contributor);
  }

  /**
   * Save the pet park data and associate it with a contributor. If the pet park
   * ID is not {@code null} an update operation is performed. If the pet park ID
   * is {@code null} an insert operation is performed.
   * 
   * For JPA to create the table relationships correctly, the relationship
   * variables must be set correctly. The contributor object in the pet park
   * must be set and the pet park must be set in the contributor's set of pet
   * parks.
   * 
   * The pet park object must be set in each amenity object and the amenity must
   * be added to the pet park's set of amenities.
   * 
   * @param contributorId The ID of the contributor to add the pet park to.
   * @param petParkData The pet park data to insert or create.
   * @return The resulting pet park and contributor.
   */
  @Transactional(readOnly = false)
  public PetParkData savePetPark(Long contributorId, PetParkData petParkData) {
    Contributor contributor = findContributorById(contributorId);

    Set<Amenity> amenities =
        amenityDao.findAllByAmenityIn(petParkData.getAmenities());

    PetPark petPark = findOrCreatePetPark(petParkData.getPetParkId());
    setPetParkFields(petPark, petParkData);

    petPark.setContributor(contributor);
    contributor.getPetParks().add(petPark);

    for(Amenity amenity : amenities) {
      amenity.getPetParks().add(petPark);
      petPark.getAmenities().add(amenity);
    }

    PetPark dbPetPark = petParkDao.save(petPark);
    return new PetParkData(dbPetPark);
  }

  /**
   * Set the fields in the PetPark object from the data supplied in the JSON
   * payload.
   * 
   * @param petPark The target fields.
   * @param petParkData The source fields.
   */
  private void setPetParkFields(PetPark petPark, PetParkData petParkData) {
    petPark.setCountry(petParkData.getCountry());
    petPark.setDirections(petParkData.getDirections());
    petPark.setGeoLocation(petParkData.getGeoLocation());
    petPark.setParkName(petParkData.getParkName());
    petPark.setPetParkId(petParkData.getPetParkId());
    petPark.setStateOrProvince(petParkData.getStateOrProvince());
  }

  /**
   * If the pet park ID is not {@code null}, find the pet park object from the
   * pet park table. If the pet park ID is {@code null} return an empty pet park
   * object.
   * 
   * @param petParkId The pet park ID (primary key value). May be {@code null}.
   * @return An empty pet park object or a pet park object found in the pet park
   *         table.
   * @throws NoSuchElementException If the pet park ID is present but does not
   *         match an ID in the pet park table.
   */
  private PetPark findOrCreatePetPark(Long petParkId) {
    PetPark petPark;

    if(Objects.isNull(petParkId)) {
      petPark = new PetPark();
    }
    else {
      petPark = findPetParkById(petParkId);
    }

    return petPark;
  }

  /**
   * Find a pet park given the pet park ID (primary key value).
   * 
   * @param petParkId The pet park ID to search for.
   * @return The pet park data if found.
   * @throws NoSuchElementException If the pet park ID is present but does not
   *         match an ID in the pet park table.
   */
  private PetPark findPetParkById(Long petParkId) {
    return petParkDao.findById(petParkId)
        .orElseThrow(() -> new NoSuchElementException(
            "Pet park with ID=" + petParkId + " does not exist."));
  }

  /**
   * Retrieve a pet park given the park ID (primary key value). If the
   * contributor ID is not associated with the pet park an exception is thrown.
   * 
   * @param contributorId The contributor ID to match.
   * @param parkId The pet park ID of the park to retrieve.
   * @return The pet park data if successful.
   * @throws IllegalStateException Thrown if the contributor ID in the parameter
   *         list is not associated with the pet park.
   */
  @Transactional(readOnly = true)
  public PetParkData retrievePetParkById(Long contributorId, Long parkId) {
    /* Check that the contributor ID exists or throws an exception. */
    findContributorById(contributorId);

    /* Check that the pet park ID exists or throws an exception. */
    PetPark petPark = findPetParkById(parkId);

    /*
     * Throws an exception if the contributor ID of the pet park contributor
     * does not match the expected contributor ID.
     */
    if(petPark.getContributor().getContributorId() != contributorId) {
      throw new IllegalStateException("Pet park with ID=" + parkId
          + " is not owned by contributor with ID=" + contributorId);
    }

    return new PetParkData(petPark);
  }

}
