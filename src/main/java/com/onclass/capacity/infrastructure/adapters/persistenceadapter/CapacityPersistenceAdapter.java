package com.onclass.capacity.infrastructure.adapters.persistenceadapter;

import com.onclass.capacity.domain.model.Capacity;
import com.onclass.capacity.domain.spi.CapacityPersistencePort;
import com.onclass.capacity.infrastructure.adapters.persistenceadapter.entity.CapacityEntity;
import com.onclass.capacity.infrastructure.adapters.persistenceadapter.entity.CapacityTechnologyEntity;
import com.onclass.capacity.infrastructure.adapters.persistenceadapter.mapper.CapacityEntityMapper;
import com.onclass.capacity.infrastructure.adapters.persistenceadapter.repository.CapacityRepository;
import com.onclass.capacity.infrastructure.adapters.persistenceadapter.repository.CapacityTechnologyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CapacityPersistenceAdapter implements CapacityPersistencePort {

    private final CapacityRepository repository;
    private final CapacityTechnologyRepository ctRepository; // ← NUEVO
    private final CapacityEntityMapper mapper;

    @Override
    public Mono<Boolean> existsByName(String name) {
        return repository.existsByName(name);
    }

    @Override
    public Mono<Capacity> save(Capacity capacity) {
        CapacityEntity entity = mapper.toEntity(capacity);

        return repository.save(entity) // guarda capacities
                .flatMap(saved -> {
                    Long capacityId = saved.getId();
                    List<Long> techIds = capacity.getTechnologyIds() == null ? List.of() : capacity.getTechnologyIds();

                    Mono<Void> saveLinks = Flux.fromIterable(techIds)
                            .flatMap(tid -> ctRepository.save(
                                    CapacityTechnologyEntity.builder()
                                            .capacityId(capacityId)
                                            .technologyId(tid)
                                            .build()
                            ))
                            .then();

                    return saveLinks.thenReturn(
                            // devolvemos el dominio con technologyIds
                            Capacity.builder()
                                    .id(saved.getId())
                                    .name(saved.getName())
                                    .description(saved.getDescription())
                                    .technologyIds(techIds)
                                    .build()
                    );
                });
    }

    @Override
    public Flux<Capacity> findAll() {
        // (Opcional) Traer también los technologyIds
        return repository.findAll()
                .flatMap(entity ->
                        ctRepository.findAllByCapacityId(entity.getId())
                                .map(CapacityTechnologyEntity::getTechnologyId)
                                .collectList()
                                .map(ids -> Capacity.builder()
                                        .id(entity.getId())
                                        .name(entity.getName())
                                        .description(entity.getDescription())
                                        .technologyIds(ids)
                                        .build()
                                )
                );
    }

    @Override
    public Mono<Capacity> findById(Long id) {
        // (Opcional) Traer también los technologyIds
        return repository.findById(id)
                .flatMap(entity ->
                        ctRepository.findAllByCapacityId(entity.getId())
                                .map(CapacityTechnologyEntity::getTechnologyId)
                                .collectList()
                                .map(ids -> Capacity.builder()
                                        .id(entity.getId())
                                        .name(entity.getName())
                                        .description(entity.getDescription())
                                        .technologyIds(ids)
                                        .build()
                                )
                );
    }
}
