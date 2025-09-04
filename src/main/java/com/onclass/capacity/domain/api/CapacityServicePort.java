package com.onclass.capacity.domain.api;

import com.onclass.capacity.domain.model.Capacity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CapacityServicePort {
    Mono<Capacity> register(Capacity capacity);
    Flux<Capacity> list();
    Mono<Capacity> findById(Long id);
}
