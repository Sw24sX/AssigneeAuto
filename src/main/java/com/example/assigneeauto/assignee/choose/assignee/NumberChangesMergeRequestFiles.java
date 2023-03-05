package com.example.assigneeauto.assignee.choose.assignee;

import com.example.assigneeauto.assignee.PartChooseAssignee;
import com.example.assigneeauto.persistance.domain.Reviewer;
import com.example.assigneeauto.persistance.domain.cache.NumberChangesMergeRequestCache;
import com.example.assigneeauto.persistance.dto.PercentWeightByMinMaxSettings;
import com.example.assigneeauto.persistance.properties.choose.assignee.properties.NumberChangesMergeRequestFilesProperties;
import com.example.assigneeauto.repository.cache.NumberChangesMergeRequestCacheRepository;
import com.example.assigneeauto.service.GitServiceApi;
import com.example.assigneeauto.service.PercentWeightByMinMaxValuesApi;
import com.example.assigneeauto.service.ProjectInfoServiceApi;
import com.example.assigneeauto.service.WeightByNotValuesApi;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.PersonIdent;
import org.gitlab4j.api.models.MergeRequest;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


/**
 * Анализирует git репозиторий.
 * Получает список изменяемых в mr список файлов.
 * По этому списку, на основании команды git blame, рассчитывавет суммарное количество строк,
 * которые изменял обрабатываемый ревьювер. Далее рассчитывает вес от 0 до 100, где 100 - наибольшее
 * количество измененных строк среди всех ревьюверов, а 0 - наименьшее
 */
@Component
@Slf4j
public class NumberChangesMergeRequestFiles extends PartChooseAssignee {

    private final GitServiceApi gitServiceApi;
    private final PercentWeightByMinMaxValuesApi percentWeightByMinMaxValuesApi;
    private final NumberChangesMergeRequestCacheRepository numberChangesMergeRequestCacheRepository;
    private final ProjectInfoServiceApi projectInfoServiceApi;

    protected NumberChangesMergeRequestFiles(NumberChangesMergeRequestFilesProperties properties,
                                             GitServiceApi gitServiceApi,
                                             PercentWeightByMinMaxValuesApi percentWeightByMinMaxValuesApi,
                                             NumberChangesMergeRequestCacheRepository numberChangesMergeRequestCacheRepository,
                                             ProjectInfoServiceApi projectInfoServiceApi) {
        super(properties);
        this.gitServiceApi = gitServiceApi;
        this.percentWeightByMinMaxValuesApi = percentWeightByMinMaxValuesApi;
        this.numberChangesMergeRequestCacheRepository = numberChangesMergeRequestCacheRepository;
        this.projectInfoServiceApi = projectInfoServiceApi;
    }

    @Override
    protected Integer getWeightPart(Reviewer reviewer, MergeRequest mergeRequest) {
        log.info("Run NumberChangesMergeRequestFiles for reviewer {}", reviewer.getUsername());
        PercentWeightByMinMaxSettings settings = PercentWeightByMinMaxSettings
                .builder()
                .reviewer(reviewer)
                .mergeRequest(mergeRequest)
                .weightByNotValuesApi(new CurrentWeight())
                .build();
        return percentWeightByMinMaxValuesApi.getCorrectWeight(settings);
    }

    private class CurrentWeight implements WeightByNotValuesApi {

        @Override
        public Long getPersonalWeight(Reviewer reviewer, MergeRequest mergeRequest) {
            var rowCount = calculateRowNumbersByReviewers(mergeRequest);
            return rowCount.entrySet().stream()
                    .filter(e -> reviewer.getReviewerNames().contains(e.getKey()))
                    .mapToLong(Map.Entry::getValue)
                    .sum();
        }

        private Map<String, Long> calculateRowNumbersByReviewers(MergeRequest mergeRequest) {
            var key = buildCacheKey(mergeRequest);
            var result = numberChangesMergeRequestCacheRepository.findById(key);
            if (result.isPresent()) {

                var countRowsByReviewerName = result.get().getCountRowsByReviewerName();
                if (countRowsByReviewerName == null) {
                    log.warn("In NumberChangesMergeRequestFiles, 'countRowsByReviewerName' is empty");
                    return new HashMap<>();
                }
                return result.get().getCountRowsByReviewerName();
            }

            var project = projectInfoServiceApi.getByProjectId(mergeRequest.getProjectId().toString());
            gitServiceApi.updateRepository(mergeRequest.getSourceBranch(), mergeRequest.getTargetBranch(), project);
            var rowCount = new HashMap<String, Long>();

            for (DiffEntry diff : gitServiceApi.getDiffBranches(mergeRequest.getSourceBranch(), mergeRequest.getTargetBranch(), project)) {
                BlameResult blameResult = gitServiceApi.getBlameFile(diff.getNewPath(), project);
                if (blameResult == null) {
                    log.warn("In NumberChangesMergeRequestFiles, 'blame result' for file {} is null", diff.getNewPath());
                    continue;
                }

                for(int i = 0; i < blameResult.getResultContents().size(); i++) {
                    PersonIdent ident = blameResult.getSourceAuthor(i);
                    rowCount.put(ident.getName(), rowCount.getOrDefault(ident.getName(), 0L) + 1);
                }
            }
            var cache = new NumberChangesMergeRequestCache();
            cache.setKey(key);
            cache.setCountRowsByReviewerName(rowCount);
            numberChangesMergeRequestCacheRepository.save(cache);
            return rowCount;
        }

        private String buildCacheKey(MergeRequest mergeRequest) {
            return String.format("NumberChangesMergeRequestFiles_%s", mergeRequest.getIid());
        }
    }
}
