package com.example.assigneeauto.repository.cache;

import com.example.assigneeauto.persistance.domain.cache.NumberChangesMergeRequestCache;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NumberChangesMergeRequestCacheRepository extends CrudRepository<NumberChangesMergeRequestCache, String> {
}
