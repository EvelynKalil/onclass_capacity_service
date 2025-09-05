package com.onclass.capacity.domain.usecase;

import com.onclass.capacity.domain.api.CapacityServicePort;
import com.onclass.capacity.domain.constants.Constants;
import com.onclass.capacity.domain.enums.TechnicalMessage;
import com.onclass.capacity.domain.exceptions.BusinessException;
import com.onclass.capacity.domain.model.Capacity;
import com.onclass.capacity.domain.spi.CapacityPersistencePort;
import com.onclass.capacity.domain.spi.TechnologyGatewayPort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

public class CapacityUseCase implements CapacityServicePort {

    private final CapacityPersistencePort persistence;
    private final TechnologyGatewayPort technologyGatewayPort;

    public CapacityUseCase(CapacityPersistencePort persistence, TechnologyGatewayPort technologyGatewayPort) {
        this.persistence = persistence;
        this.technologyGatewayPort = technologyGatewayPort;
    }

    @Override
    public Mono<Capacity> register(Capacity capacity) {
        return Mono.justOrEmpty(capacity)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.INVALID_REQUEST)))
                .flatMap(c -> {
                    // ===== Reglas existentes (nombre/descr, longitudes y único) =====
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

                    // ===== Reglas HU2 (min 3, max 20, sin repetidas) =====
                    final List<Long> ids = Optional.ofNullable(c.getTechnologyIds()).orElse(List.of());
                    if (ids.size() < 3) {
                        return Mono.error(new BusinessException(TechnicalMessage.CAPACITY_MIN_TECHS));
                    }
                    if (ids.size() > 20) {
                        return Mono.error(new BusinessException(TechnicalMessage.CAPACITY_MAX_TECHS));
                    }
                    if (ids.stream().distinct().count() != ids.size()) {
                        return Mono.error(new BusinessException(TechnicalMessage.CAPACITY_DUP_TECHS));
                    }

                    // ===== Validación de existencia contra microservicio technology (reactivo) =====
                    return technologyGatewayPort.findExistingIds(ids)
                            .collectList()
                            .flatMap(foundIds -> {
                                if (foundIds.size() != ids.size()) {
                                    // Algún ID no existe en technology
                                    return Mono.error(new BusinessException(TechnicalMessage.TECHNOLOGY_NOT_FOUND));
                                }
                                // ===== Validación de nombre único + guardado =====
                                return persistence.existsByName(c.getName())
                                        .flatMap(exists -> exists
                                                ? Mono.error(new BusinessException(TechnicalMessage.CAPACITY_ALREADY_EXISTS))
                                                : persistence.save(c)
                                        );
                            });
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
