package com.onclass.capacity.infrastructure.adapters.persistenceadapter;

import com.onclass.capacity.domain.model.Capacity;
import com.onclass.capacity.domain.spi.CapacityPersistencePort;
import com.onclass.capacity.infrastructure.adapters.persistenceadapter.entity.CapacityEntity;
import com.onclass.capacity.infrastructure.adapters.persistenceadapter.mapper.CapacityEntityMapper;
import com.onclass.capacity.infrastructure.adapters.persistenceadapter.repository.CapacityRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CapacityPersistenceAdapter implements CapacityPersistencePort {

    private final CapacityRepository repository;
    private final CapacityEntityMapper mapper;

    @Override
    public Mono<Boolean> existsByName(String name) {
        return repository.existsByName(name);
    }

    @Override
    public Mono<Capacity> save(Capacity capacity) {
        CapacityEntity entity = mapper.toEntity(capacity);
        return repository.save(entity).map(mapper::toModel);
    }

    @Override
    public Flux<Capacity> findAll() {
        return repository.findAll().map(mapper::toModel);
    }

    @Override
    public Mono<Capacity> findById(Long id) {
        return repository.findById(id).map(mapper::toModel);
    }
}
