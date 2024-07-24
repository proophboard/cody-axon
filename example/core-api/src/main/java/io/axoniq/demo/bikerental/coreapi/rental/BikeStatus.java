package io.axoniq.demo.bikerental.coreapi.rental;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class BikeStatus {

	@Id private String bikeId;
	private String bikeType;
	private String location;
}
