package com.example.assigneeauto.persistance.mapper;

import com.example.assigneeauto.persistance.domain.HistoryReview;
import com.example.assigneeauto.persistance.dto.web.HistoryDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HistoryMapper {

    @Mapping(source = "reviewer.username", target = "reviewerUsername")
    HistoryDto from(HistoryReview historyReview);

    List<HistoryDto> from(List<HistoryReview> reviews);
    default Page<HistoryDto> from(Page<HistoryReview> reviews) {
        if (reviews == null) {
            return null;
        }

        return reviews.map(this::from);
    }
}
