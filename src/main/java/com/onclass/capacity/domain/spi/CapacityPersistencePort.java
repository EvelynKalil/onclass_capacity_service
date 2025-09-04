package com.onclass.capacity.domain.spi;

import com.onclass.capacity.domain.model.Capacity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CapacityPersistencePort {
    Mono<Boolean> existsByName(String name);
    Mono<Capacity> save(Capacity capacity);
    Flux<Capacity> findAll();
    Mono<Capacity> findById(Long id);
}
