package com.example.assigneeauto.persistance.mapper;

import com.example.assigneeauto.persistance.domain.Reviewer;
import com.example.assigneeauto.persistance.domain.ReviewerName;
import com.example.assigneeauto.persistance.dto.web.ReviewerDto;
import com.google.common.base.Splitter;
import org.mapstruct.*;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Mapper(componentModel = "spring")
public interface ReviewerMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    @Mapping(source = "reviewerNames", target = "reviewerNames", qualifiedByName = "stringToGitNames")
    void updateReviewer(Reviewer source, @MappingTarget Reviewer target);

    @Mapping(source = "reviewerNames", target = "reviewerNames", qualifiedByName = "gitNamesToString")
    ReviewerDto from(Reviewer reviewer);

    @Named("gitNamesToString")
    default String gitNamesToString(List<ReviewerName> names) {
        return names.stream()
                .map(ReviewerName::getGitName)
                .collect(Collectors.joining(", "));
    }

    @Named("stringToGitNames")
    default List<ReviewerName> stringToGitNames(String names) {
        return StreamSupport.stream(Splitter.on(Pattern.compile(", |,| , ")).split(names).spliterator(), false)
                .map(ReviewerName::new)
                .collect(Collectors.toList());
    }
}
