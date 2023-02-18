package com.example.assigneeauto.persistance.mapper;

import com.example.assigneeauto.persistance.domain.Reviewer;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ReviewerMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateReviewer(Reviewer source, @MappingTarget Reviewer target);
}
