package com.onclass.capacity.infrastructure.adapters.persistenceadapter.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("capacity_technologies")
public class CapacityTechnologyEntity {
    @Id
    private Long id;

    @Column("capacity_id")
    private Long capacityId;

    @Column("technology_id")
    private Long technologyId;
}
