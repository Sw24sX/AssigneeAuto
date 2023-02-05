package com.example.assigneeauto.assignee.choose.assignee;

import com.example.assigneeauto.assignee.PartChooseAssignee;
import com.example.assigneeauto.persistance.domain.Reviewer;
import com.example.assigneeauto.persistance.dto.PercentWeightByMinMaxSettings;
import com.example.assigneeauto.persistance.properties.choose.assignee.properties.NumberChangesMergeRequestFilesProperties;
import com.example.assigneeauto.service.GitService;
import com.example.assigneeauto.service.PercentWeightByMinMaxValues;
import com.example.assigneeauto.service.ReviewerService;
import com.example.assigneeauto.service.WeightByNotValues;
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

    private final GitService gitService;
    private final ReviewerService reviewerService;
    private final PercentWeightByMinMaxValues percentWeightByMinMaxValues;

    protected NumberChangesMergeRequestFiles(NumberChangesMergeRequestFilesProperties properties,
                                             GitService gitService, ReviewerService reviewerService,
                                             PercentWeightByMinMaxValues percentWeightByMinMaxValues) {
        super(properties);
        this.gitService = gitService;
        this.reviewerService = reviewerService;
        this.percentWeightByMinMaxValues = percentWeightByMinMaxValues;
    }

    @Override
    protected Integer getWeightPart(Reviewer reviewer, MergeRequest mergeRequest) {
        log.info("Run NumberChangesMergeRequestFiles for reviewer {}", reviewer.getUsername());
        String newBranchName = mergeRequest.getSourceBranch();
        gitService.updateRepository();
        PercentWeightByMinMaxSettings settings = PercentWeightByMinMaxSettings
                .builder()
                .reviewer(reviewer)
                .mergeRequest(mergeRequest)
                .weightByNotValues(new CurrentWeight(newBranchName))
                .build();
        return percentWeightByMinMaxValues.getCorrectWeight(settings);
    }

    private class CurrentWeight implements WeightByNotValues {

        private static final String CACHE_KEY = "NumberChangesMergeRequestFiles";
        private final String newBranchName;

        private CurrentWeight(String newBranchName) {
            this.newBranchName = newBranchName;
        }

        @Override
        public Integer getPersonalWeight(Reviewer reviewer, MergeRequest mergeRequest) {
            int result = 0;

            for (DiffEntry diff : gitService.getDiffBranches(newBranchName)) {
                // TODO: 05.02.2023 blame result по какой то причине может быть null
                BlameResult blameResult = gitService.getBlameFile(diff.getNewPath());
                for(int i = 0; i < blameResult.getResultContents().size(); i++) {
                    PersonIdent ident = blameResult.getSourceAuthor(i);
                    if (reviewerService.isReviewerGitName(reviewer, ident.getName())) {
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
