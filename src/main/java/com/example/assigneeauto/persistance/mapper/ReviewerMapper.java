package com.example.assigneeauto.persistance.mapper;

import com.example.assigneeauto.persistance.domain.Reviewer;
import com.example.assigneeauto.persistance.domain.ReviewerInfo;
import com.example.assigneeauto.persistance.dto.web.ReviewerDto;
import org.gitlab4j.api.models.Member;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ReviewerMapper {

    @Mapping(target = "reviewer", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateInfo(ReviewerInfo source, @MappingTarget ReviewerInfo target);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateReviewer(Reviewer source, @MappingTarget Reviewer target);

    @Mapping(target = "maxCountReview", source = "info.maxCountReview")
    ReviewerDto from(Reviewer reviewer);

    @Mapping(target = "info.maxCountReview", source = "maxCountReview")
    Reviewer to(ReviewerDto dto);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "id", target = "memberId")
    @Mapping(source = "accessLevel", target = "accessLevelGitLab")
    void updateReviewer(Member source, @MappingTarget Reviewer target);
}
