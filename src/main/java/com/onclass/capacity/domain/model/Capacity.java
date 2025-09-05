package com.onclass.capacity.domain.model;

import jdk.jshell.Snippet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class Capacity {
    private Long id;
    private String name;
    private String description;
    private List<Long> technologyIds;
}
