package io.axoniq.demo.bikerental.rental.query;

import io.axoniq.demo.bikerental.coreapi.rental.BikeRegisteredEvent;
import io.axoniq.demo.bikerental.coreapi.rental.BikeStatus;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.stereotype.Component;

@Component
public class BikeStatusProjection {

	private final BikeStatusRepository bikeStatusRepository;
	private final QueryUpdateEmitter updateEmitter;

	public BikeStatusProjection(BikeStatusRepository bikeStatusRepository, QueryUpdateEmitter updateEmitter) {
		this.bikeStatusRepository = bikeStatusRepository;
		this.updateEmitter = updateEmitter;
	}

	@EventHandler
	public void on(BikeRegisteredEvent event) {
		var bikeStatus = new BikeStatus(event.getBikeId(), event.getBikeType(), event.getLocation());
		bikeStatusRepository.save(bikeStatus);
		updateEmitter.emit(q -> "findAll".equals(q.getQueryName()), bikeStatus);
	}

	@QueryHandler(queryName = "findAll")
	public Iterable<BikeStatus> findAll() {
		return bikeStatusRepository.findAll();
	}

	@QueryHandler(queryName = "findOne")
	public BikeStatus findOne(String bikeId) {
		return bikeStatusRepository.findById(bikeId).orElse(null);
	}
}
