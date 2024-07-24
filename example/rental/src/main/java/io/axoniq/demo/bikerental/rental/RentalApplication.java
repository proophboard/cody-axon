package io.axoniq.demo.bikerental.rental;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.axoniq.demo.bikerental.coreapi.rental.BikeStatus;
import org.axonframework.eventhandling.tokenstore.jpa.TokenEntry;
import org.axonframework.modelling.saga.repository.jpa.SagaEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackageClasses = {BikeStatus.class, SagaEntry.class, TokenEntry.class})
public class RentalApplication {

	public static void main(String[] args) {
		SpringApplication.run(RentalApplication.class, args);
	}

	@Autowired
	public void configureSerializers(ObjectMapper objectMapper) {
		objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(),
										   ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT);
	}
}
