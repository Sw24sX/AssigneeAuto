package com.example.assigneeauto.persistance.mapper;

import com.example.assigneeauto.persistance.domain.Reviewer;
import com.example.assigneeauto.persistance.dto.web.ReviewerDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ReviewerMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "username", ignore = true)
    void updateReviewer(Reviewer source, @MappingTarget Reviewer target);

    ReviewerDto from(Reviewer reviewer);
    Reviewer to(ReviewerDto dto);
}
