package com.onclass.capacity.domain.usecase;

import com.onclass.capacity.domain.api.CapacityServicePort;
import com.onclass.capacity.domain.constants.Constants;
import com.onclass.capacity.domain.enums.TechnicalMessage;
import com.onclass.capacity.domain.exceptions.BusinessException;
import com.onclass.capacity.domain.model.Capacity;
import com.onclass.capacity.domain.spi.CapacityPersistencePort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class CapacityUseCase implements CapacityServicePort {

    private final CapacityPersistencePort persistence;

    public CapacityUseCase(CapacityPersistencePort persistence) {
        this.persistence = persistence;
    }

    @Override
    public Mono<Capacity> register(Capacity capacity) {
        // MISMAS validaciones que tenías en TechnologyUseCase, pero con mensajes de Capacity
        return Mono.justOrEmpty(capacity)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.INVALID_REQUEST)))
                .flatMap(c -> {
                    if (c.getName() == null || c.getName().isBlank()) {
                        return Mono.error(new BusinessException(TechnicalMessage.CAPACITY_NAME_REQUIRED));
                    }
                    if (c.getName().length() > Constants.CAPACITY_NAME_MAX_LENGTH) {
                        return Mono.error(new BusinessException(TechnicalMessage.CAPACITY_NAME_TOO_LONG));
                    }
                    if (c.getDescription() == null || c.getDescription().isBlank()) {
                        return Mono.error(new BusinessException(TechnicalMessage.CAPACITY_DESCRIPTION_REQUIRED));
                    }
                    if (c.getDescription().length() > Constants.CAPACITY_DESCRIPTION_MAX_LENGTH) {
                        return Mono.error(new BusinessException(TechnicalMessage.CAPACITY_DESCRIPTION_TOO_LONG));
                    }
                    return persistence.existsByName(c.getName())
                            .flatMap(exists -> exists
                                    ? Mono.error(new BusinessException(TechnicalMessage.CAPACITY_ALREADY_EXISTS))
                                    : persistence.save(c)
                            );
                });
    }

    @Override
    public Flux<Capacity> list() {
        return persistence.findAll();
    }

    @Override
    public Mono<Capacity> findById(Long id) {
        return persistence.findById(id);
    }
}
