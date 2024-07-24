package io.axoniq.demo.bikerental.coreapi.rental;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BikeRegisteredEvent {
	private String bikeId;
	private String bikeType;
	private String location;
}
