package com.onclass.capacity.domain.spi;

import reactor.core.publisher.Flux;
import java.util.List;

public interface TechnologyGatewayPort {
    Flux<Long> findExistingIds(List<Long> ids);
}