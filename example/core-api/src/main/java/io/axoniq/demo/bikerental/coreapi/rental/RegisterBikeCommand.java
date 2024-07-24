package io.axoniq.demo.bikerental.coreapi.rental;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@AllArgsConstructor
public class RegisterBikeCommand {
	@TargetAggregateIdentifier private String bikeId;
	private String bikeType;
	private String location;
}
