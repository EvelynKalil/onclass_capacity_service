package com.onclass.capacity.infrastructure.adapters.persistenceadapter.repository;

import com.onclass.capacity.infrastructure.adapters.persistenceadapter.entity.CapacityTechnologyEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface    CapacityTechnologyRepository extends ReactiveCrudRepository<CapacityTechnologyEntity, Long> {
    Flux<CapacityTechnologyEntity> findAllByCapacityId(Long capacityId);
}
