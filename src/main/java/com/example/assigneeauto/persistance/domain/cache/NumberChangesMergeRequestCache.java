package com.example.assigneeauto.persistance.domain.cache;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import java.util.Map;

@Getter
@Setter
@RequiredArgsConstructor
@RedisHash(value = "NumberChangesMergeRequestCache", timeToLive = 300)
public class NumberChangesMergeRequestCache extends BaseCacheEntity {

    private Map<String, Long> countRowsByReviewerName;
}
