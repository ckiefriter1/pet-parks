package pet.park.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
public class Amenity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long amenityId;
	
	private String amenityName;

	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@ManyToMany(mappedBy = "amenities")
	@JoinTable(name = "pet_park_amenity", 
		joinColumns = @JoinColumn(name = "pet_park_id"),
		inverseJoinColumns = @JoinColumn(name = "amenity_id"))
	private Set<PetPark> petParks = new HashSet<>();
}