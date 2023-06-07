// Copyright (c) 2023 by Promineo Tech.

package pet.park.controller;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import pet.park.controller.model.ContributorData;
import pet.park.controller.model.PetParkData;
import pet.park.service.ParkService;

/**
 * This class intercepts HTTP requests and formulates appropriate responses. The
 * class is annotated with the following:
 * 
 * @RestController This tells Spring Boot that the class is a REST controller.
 *                 As such, it expects JSON requests and sends JSON responses.
 *                 If you don't specify otherwise, the response status is 200
 *                 (OK). This also tells Spring to map HTTP requests to specific
 *                 methods in this class.
 * 
 * @RequestMapping This specifies the "base" mapping of the request. The
 *                 complete mapping adds any method-level mapping onto the base
 *                 mapping. So, if the base mapping is "/pet_park" and the
 *                 method-level mapping specified in @PostMapping is
 *                 "/contributor", the mapping for that method is a POST request
 *                 to "/pet_park/contributor".
 * 
 * @Slf4j This is a Lombok annotation that sets up an SLF4J logger in an
 *        instance variable named <em>log</em>.
 * 
 * @author Promineo
 *
 */
@RestController
@RequestMapping("/pet_park")
@Slf4j
public class ParkController {
  /*
   * The @Autowired annotation is used to ask Spring to inject a managed Bean
   * into the instance variable. In this case, Spring creates a single instance
   * of ParkService and injects it here without the developer needing to use the
   * new operator.
   */
  @Autowired
  private ParkService parkService;

  /**
   * This method is mapped to a POST request sent to /pet_park/contributor. If
   * successful, it returns status 201 (Created). The method is used to create a
   * contributor in the contributor table. The contributor data is supplied as
   * JSON in the request body (payload). The Jackson JSON library automatically
   * converts the JSON to a ContributorData object. For this to happen, the JSON
   * object names must exactly match the Java instance variable names. The JSON
   * payload must look exactly like this (the values may differ):
   * 
   * <pre>
   {
     "contributorName": "Sandy Blotts",
     "contributorEmail": "sandy@blotts.r.us"
   }
   * </pre>
   * 
   * Here are the annotations:
   * 
   * @PostMapping This tells Spring that a POST request is to be mapped to this
   *              method. "/contributor" inside the annotation means to add
   *              "/contributor" onto the end of the base mapping. To put it all
   *              together, a POST request sent to "/pet_park/contributor" is
   *              mapped to this method.
   * 
   * @ResponseStatus This overrides the default status of 200 (OK) with 201
   *                 (Created).
   * 
   * @RequestBody This tells Spring to use Jackson to convert the JSON request
   *              payload into a ContributorData object, which it then supplies
   *              to the method as a parameter.
   * 
   * @param contributorData The contributor data, which is added to the
   *        contributor table if there are no errors. The supplied JSON must not
   *        have a contributor ID.
   * @return Returns a contributorData object with the generated contributor ID.
   */
  @PostMapping("/contributor")
  @ResponseStatus(code = HttpStatus.CREATED)
  public ContributorData insertContributor(
      @RequestBody ContributorData contributorData) {
    log.info("Creating contributor {}", contributorData);
    return parkService.saveContributor(contributorData);
  }

  /**
   * This method is used to modify data for an existing contributor. Id is
   * mapped to a PUT request sent to "/pet_parks/contributor/{contributorId}
   * where {contributorId} is the primary key value (ID) of the contributor. The
   * contributor ID is passed in the request URI and the contributor data is
   * passed in the request body (payload). The JSON payload must look exactly
   * like this (the values may differ):
   * 
   * <pre>
   {
     "contributorName": "Sandy Blotts",
     "contributorEmail": "sandy@blotts.r.us"
   }
   * </pre>
   * 
   * Here are the annotations:
   * 
   * @PutMapping This tells Spring to map an HTTP PUT request to this method.
   *             The actual request mapped to this method is
   *             "/pet_park/contributor/{ID}". So, if the ID of the contributor
   *             to update is 5, you would send the PUT request to
   *             "/pet_park/contributor/5".
   * 
   * @PathVariable This tells Spring that the variable in the URI is to be
   *               supplied to the parameter in the method. Spring will match on
   *               the parameter/path variable name. So, the variable specified
   *               in @PutMapping as {contributorId} will map to the method
   *               parameter with the same name.
   * 
   * @RequestBody This tells Spring to use Jackson to convert the JSON request
   *              payload into a ContributorData object, which it then supplies
   *              to the method as a parameter.
   * 
   * @param contributorId This is the ID (primary key value) of the contributor
   *        to modify.
   * @param contributorData The data used to modify the contributor. This is
   *        supplied as JSON in the request payload.
   * @return The contributor data as it exists in the database schema table.
   */
  @PutMapping("/contributor/{contributorId}")
  public ContributorData updateContributor(@PathVariable Long contributorId,
      @RequestBody ContributorData contributorData) {
    contributorData.setContributorId(contributorId);
    log.info("Updating contributor {}", contributorData);
    return parkService.saveContributor(contributorData);
  }

  /**
   * This method is used to retrieve all contributors. It is mapped to a PUT
   * request sent to "/pet_park/contributor".
   * 
   * Here are explanations of the annotations:
   * 
   * @GetMapping This tells Spring to map a GET request to
   *             "/pet_park/contributor".
   * 
   * @return A list of all contributors.
   */
  @GetMapping("/contributor")
  public List<ContributorData> retrieveAllContributors() {
    log.info("Retrieving all contributors.");
    return parkService.retrieveAllContributors();
  }

  /**
   * This method is used to retrieve a single contributor with the given ID. It
   * is mapped to a GET request sent to "/pet_park/contributor/{contributorId}
   * where {contributorId} is the ID (primary key value) of the contributor to
   * retrieve. So, if the contributor to be retrieve has an ID of 3, you would
   * send a GET request to "/pet_park/contributor/3".
   * 
   * Explanations of the annotations used in this method:
   * 
   * @GetMapping This tells Spring to map a GET request to this method. The
   *             mapping in the @GetMapping annotation adds
   *             "/contributor/{contributorId} onto the end of the class mapping
   *             ("/pet_park").
   * 
   * @PathVariable This tells Spring to map the contributor ID value in the URI
   *               to the contributorId parameter.
   * 
   * @param contributorId The ID (primary key value) of the contributor to
   *        retrieve.
   * @return The contributor data of the contributor to retrieve.
   */
  @GetMapping("/contributor/{contributorId}")
  public ContributorData retrieveContributorById(
      @PathVariable Long contributorId) {
    log.info("Retrieving contributor with ID={}", contributorId);
    return parkService.retrieveContributorById(contributorId);
  }

  /**
   * This method is used to delete a contributor. It is called by sending an
   * HTTP DELETE request to "/pet_park/contributor/{contributorId} where
   * {contributorId} is the ID (primary key) value of the contributor to delete.
   * So, if the ID of the contributor to delete is 8, you would send a DELETE
   * request to "/pet_park/contributor/8".
   * 
   * Explanation of annotations:
   * 
   * @DeleteMapping Instructs Spring to map a DELETE request to
   *                "/pet_park/contributor/{contributorId}".
   * 
   * @PathVariable This tells Spring to map the contributor ID value in the URI
   *               to the contributorId parameter.
   * 
   * @param contributorId The ID of the contributor to delete.
   * @return A success message, along with a 200 (OK) status if successful.
   */
  @DeleteMapping("/contributor/{contributorId}")
  public Map<String, String> deleteContributorById(
      @PathVariable Long contributorId) {
    log.info("Deleting contributor with ID={}", contributorId);

    parkService.deleteContributorById(contributorId);

    return Map.of("message",
        "Contributor with ID=" + contributorId + " deleted successfully.");
  }

  /**
   * This method is reached by sending an HTTP DELETE request to
   * "/pet_park/contributor". According to REST conventions, this means to apply
   * the DELETE operation to all contributors, since a specific resource ID is
   * not specified. This is not allowed.
   * 
   * @throws UnsupportedOperationException
   */
  @DeleteMapping("/contributor")
  public void deleteAllContributors() {
    log.info("Attempting to delete all contributors");
    throw new UnsupportedOperationException(
        "Deleting all contributors is not allowed.");
  }

  /**
   * This method is used to add a park description from a contributor. It is
   * called by sending a POST request to
   * "/pet_park/contributor/{contributorId}/park". So, if you want to add a park
   * description to a contributor with ID=3, you would send a POST request to
   * "/pet_park/contributor/3/park". The park data is in the request body. It
   * must conform to the following:
   * 
   * <pre>
  {
  "parkName": "My Modified Pet Park",
  "directions": "Drive there",
  "stateOrProvince": "ID",
  "country": "USA",
  "geoLocation": {
    "latitude": "44.50045",
    "longitude": "-111.25493"
  },
  "amenities": [
    "dog friendly", "chicken friendly", "hot dog stand", "wifi"
  ]
  }
   * </pre>
   * 
   * @param contributorId The ID (primary key value) of the contributor on which
   *        to add the park description. This is passed in the URI.
   * @param petParkData The pet park data. This is passed in the request body.
   * @return The pet park data with primary key and the associated contributor
   *         information.
   */
  @PostMapping("/contributor/{contributorId}/park")
  @ResponseStatus(code = HttpStatus.CREATED)
  public PetParkData insertPetPark(@PathVariable Long contributorId,
      @RequestBody PetParkData petParkData) {

    log.info("Creating park {} for contributor with ID={}", petParkData,
        contributorId);

    return parkService.savePetPark(contributorId, petParkData);
  }

  /**
   * This method updates park data for the park with the given park ID. It is
   * called by sending an HTTP PUT request to
   * "/pet_park/contributor/{contributorId}/park/{parkId}. So, if the
   * contributor ID is 2 and the park ID is 5, send the PUT request to
   * "/pet_park/contributor/2/park/5".
   * 
   * @param contributorId The ID (primary key value) of the contributor. This is
   *        supplied in the URI.
   * @param parkId The ID (primary key value) of the park. This is supplied in
   *        the URI.
   * @param petParkData The park data. See
   *        {@link #insertPetPark(Long, PetParkData)} for the correct format.
   *        The data is supplied in the request body.
   * @return The modified data object with the contributor data.
   */
  @PutMapping("/contributor/{contributorId}/park/{parkId}")
  public PetParkData updatePetPark(@PathVariable Long contributorId,
      @PathVariable Long parkId, @RequestBody PetParkData petParkData) {

    petParkData.setPetParkId(parkId);

    log.info("Creating park {} for contributor with ID={}", petParkData,
        contributorId);

    return parkService.savePetPark(contributorId, petParkData);
  }

  /**
   * This method retrieves the park and contributor data for a given park ID and
   * contributor ID. The contributor ID must belong to the contributor
   * associated with the park. To call this method, send a GET request to
   * "/pet_park/contributor/{contributorId}/park/{parkId}". So, if the
   * contributor ID is 4 and the park ID is 3, send a GET request to
   * "/pet_park/contributor/4/park/3".
   * 
   * @param contributorId The contributor ID (primary key value). This value is
   *        supplied in the request URI.
   * @param parkId The park ID (primary key value). This value is supplied in
   *        the request URI.
   * @return The park and contributor data.
   * @throws NoSuchElementException Thrown if the contributor or park with the
   *         given IDs are not found.
   * @throws IllegalStateException Thrown if the park is not associated with the
   *         contributor.
   */
  @GetMapping("/contributor/{contributorId}/park/{parkId}")
  public PetParkData retrievePetParkById(@PathVariable Long contributorId,
      @PathVariable Long parkId) {
    log.info("Retrieving pet park with ID={} for contributor with ID={}",
        parkId, contributorId);

    return parkService.retrievePetParkById(contributorId, parkId);
  }
}
