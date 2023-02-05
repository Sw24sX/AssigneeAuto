package com.example.assigneeauto.assignee.choose.assignee;

import com.example.assigneeauto.assignee.PartChooseAssignee;
import com.example.assigneeauto.persistance.domain.Reviewer;
import com.example.assigneeauto.persistance.dto.PercentWeightByMinMaxSettings;
import com.example.assigneeauto.persistance.properties.choose.assignee.properties.NumberChangesMergeRequestFilesProperties;
import com.example.assigneeauto.service.GitServiceApi;
import com.example.assigneeauto.service.PercentWeightByMinMaxValuesApi;
import com.example.assigneeauto.service.ReviewerServiceApi;
import com.example.assigneeauto.service.WeightByNotValuesApi;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.PersonIdent;
import org.gitlab4j.api.models.MergeRequest;
import org.springframework.stereotype.Component;


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
    private final ReviewerServiceApi reviewerServiceApi;
    private final PercentWeightByMinMaxValuesApi percentWeightByMinMaxValuesApi;

    protected NumberChangesMergeRequestFiles(NumberChangesMergeRequestFilesProperties properties,
                                             GitServiceApi gitServiceApi, ReviewerServiceApi reviewerServiceApi,
                                             PercentWeightByMinMaxValuesApi percentWeightByMinMaxValuesApi) {
        super(properties);
        this.gitServiceApi = gitServiceApi;
        this.reviewerServiceApi = reviewerServiceApi;
        this.percentWeightByMinMaxValuesApi = percentWeightByMinMaxValuesApi;
    }

    @Override
    protected Integer getWeightPart(Reviewer reviewer, MergeRequest mergeRequest) {
        log.info("Run NumberChangesMergeRequestFiles for reviewer {}", reviewer.getUsername());
        String newBranchName = mergeRequest.getSourceBranch();
        gitServiceApi.updateRepository();
        PercentWeightByMinMaxSettings settings = PercentWeightByMinMaxSettings
                .builder()
                .reviewer(reviewer)
                .mergeRequest(mergeRequest)
                .weightByNotValuesApi(new CurrentWeight(newBranchName))
                .build();
        return percentWeightByMinMaxValuesApi.getCorrectWeight(settings);
    }

    private class CurrentWeight implements WeightByNotValuesApi {

        private static final String CACHE_KEY = "NumberChangesMergeRequestFiles";
        private final String newBranchName;

        private CurrentWeight(String newBranchName) {
            this.newBranchName = newBranchName;
        }

        @Override
        public Integer getPersonalWeight(Reviewer reviewer, MergeRequest mergeRequest) {
            int result = 0;

            for (DiffEntry diff : gitServiceApi.getDiffBranches(newBranchName)) {
                // TODO: 05.02.2023 blame result по какой то причине может быть null
                BlameResult blameResult = gitServiceApi.getBlameFile(diff.getNewPath());
                if (blameResult == null) {
                    log.warn("In NumberChangesMergeRequestFiles, 'blame result' for reviewer {} is null", reviewer.getUsername());
                    continue;
                }

                for(int i = 0; i < blameResult.getResultContents().size(); i++) {
                    PersonIdent ident = blameResult.getSourceAuthor(i);
                    if (reviewerServiceApi.isReviewerGitName(reviewer, ident.getName())) {
                        result++;
                    }
                }
            }

            return result;
        }

        @Override
        public String getCacheKey() {
            return CACHE_KEY;
        }
    }
}
