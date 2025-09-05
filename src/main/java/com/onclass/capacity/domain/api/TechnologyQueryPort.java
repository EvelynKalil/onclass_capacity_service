package com.onclass.capacity.domain.api;

import reactor.core.publisher.Flux;
import java.util.List;

public interface TechnologyQueryPort {
    Flux<Long> findExistingIdsByIds(List<Long> ids);
}
