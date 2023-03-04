package com.example.assigneeauto.assignee.include;

import com.example.assigneeauto.assignee.choose.assignee.NumberChangesMergeRequestFiles;
import com.example.assigneeauto.presets.domain.ReviewerPreset;
import com.example.assigneeauto.presets.test.GitEntryPreset;
import com.example.assigneeauto.presets.test.MergeRequestPreset;
import com.example.assigneeauto.repository.cache.NumberChangesMergeRequestCacheRepository;
import com.example.assigneeauto.service.GitServiceApi;
import com.example.assigneeauto.service.ReviewerServiceApi;
import com.example.assigneeauto.service.impl.ReviewerService;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.RawText;
import org.eclipse.jgit.lib.PersonIdent;
import org.gitlab4j.api.models.MergeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations="classpath:test.properties")
public class NumberChangesMergeRequestFilesTests {

    @Autowired
    private NumberChangesMergeRequestFiles numberChangesMergeRequestFiles;

    @MockBean
    private GitServiceApi gitServiceApi;

    @MockBean
    private ReviewerServiceApi reviewerServiceApi;

    @MockBean
    private NumberChangesMergeRequestCacheRepository numberChangesMergeRequestCacheRepository;

    @BeforeEach
    public void setUp() {
//        Mockito.when(numberChangesMergeRequestCacheRepository.findById(Mockito.anyString())).then(x -> null);
//        Mockito.when(numberChangesMergeRequestCacheRepository.save(Mockito.any()));
    }

    @Test
    public void numberChangesOneReviewerAndOneFileWithTwoAuthorsWithReviewer() {
        var reviewer = ReviewerPreset.first();
        var mergeRequest = MergeRequestPreset.first();
        var blameResult = Mockito.mock(BlameResult.class);
        var diffEntry = Mockito.mock(DiffEntry.class);
        var branch = mergeRequest.getSourceBranch();

        Mockito.when(gitServiceApi.getDiffBranches(branch)).thenReturn(List.of(diffEntry));
        var newPath = "test/new/path";
        Mockito.when(diffEntry.getNewPath()).thenReturn(newPath);
        Mockito.when(gitServiceApi.getBlameFile(newPath)).thenReturn(blameResult);

        var rawText = Mockito.mock(RawText.class);
        Mockito.when(blameResult.getResultContents()).thenReturn(rawText);
        Mockito.when(rawText.size()).thenReturn(3);

        var firstPerson = Mockito.mock(PersonIdent.class);
        var secondPerson = Mockito.mock(PersonIdent.class);
        Mockito.when(firstPerson.getName()).thenReturn(reviewer.getReviewerNames().get(0));
        Mockito.when(secondPerson.getName()).thenReturn(newPath);

        Mockito.when(blameResult.getSourceAuthor(0)).thenReturn(firstPerson);
        Mockito.when(blameResult.getSourceAuthor(1)).thenReturn(secondPerson);
        Mockito.when(blameResult.getSourceAuthor(2)).thenReturn(secondPerson);

        Mockito.when(reviewerServiceApi.getAllActive()).thenReturn(List.of(reviewer));
        var result = numberChangesMergeRequestFiles.getWeight(reviewer, mergeRequest);
        assertThat(result).isEqualTo(0);
    }

    @Test
    public void numberChangesOneReviewerAndOneFileWithOneAuthorWithoutReviewer() {
        var reviewer = ReviewerPreset.first();
        var mergeRequest = MergeRequestPreset.first();
        var blameResult = Mockito.mock(BlameResult.class);
        var diffEntry = Mockito.mock(DiffEntry.class);
        var branch = mergeRequest.getSourceBranch();

        Mockito.when(gitServiceApi.getDiffBranches(branch)).thenReturn(List.of(diffEntry));
        var newPath = "test/new/path";
        Mockito.when(diffEntry.getNewPath()).thenReturn(newPath);
        Mockito.when(gitServiceApi.getBlameFile(newPath)).thenReturn(blameResult);

        var rawText = Mockito.mock(RawText.class);
        Mockito.when(blameResult.getResultContents()).thenReturn(rawText);
        Mockito.when(rawText.size()).thenReturn(3);

        var person = Mockito.mock(PersonIdent.class);
        Mockito.when(person.getName()).thenReturn(newPath);

        Mockito.when(blameResult.getSourceAuthor(0)).thenReturn(person);
        Mockito.when(blameResult.getSourceAuthor(1)).thenReturn(person);
        Mockito.when(blameResult.getSourceAuthor(2)).thenReturn(person);

        Mockito.when(reviewerServiceApi.getAllActive()).thenReturn(List.of(reviewer));
        var result = numberChangesMergeRequestFiles.getWeight(reviewer, mergeRequest);
        assertThat(result).isEqualTo(0);
    }

    @Test
    public void numberChangesOneReviewerWithoutDiffChanges() {
        var reviewer = ReviewerPreset.first();
        var mergeRequest = MergeRequestPreset.first();
        var branch = mergeRequest.getSourceBranch();

        Mockito.when(gitServiceApi.getDiffBranches(branch)).thenReturn(List.of());

        Mockito.when(reviewerServiceApi.getAllActive()).thenReturn(List.of(reviewer));
        var result = numberChangesMergeRequestFiles.getWeight(reviewer, mergeRequest);
        assertThat(result).isEqualTo(0);
    }

    @Test
    public void numberChangesOneReviewerAndEmptyFile() {
        var reviewer = ReviewerPreset.first();
        var mergeRequest = MergeRequestPreset.first();
        var blameResult = Mockito.mock(BlameResult.class);
        var diffEntry = Mockito.mock(DiffEntry.class);
        var branch = mergeRequest.getSourceBranch();

        Mockito.when(gitServiceApi.getDiffBranches(branch)).thenReturn(List.of(diffEntry));
        var newPath = "test/new/path";
        Mockito.when(diffEntry.getNewPath()).thenReturn(newPath);
        Mockito.when(gitServiceApi.getBlameFile(newPath)).thenReturn(blameResult);

        var rawText = Mockito.mock(RawText.class);
        Mockito.when(blameResult.getResultContents()).thenReturn(rawText);
        Mockito.when(rawText.size()).thenReturn(0);

        Mockito.when(reviewerServiceApi.getAllActive()).thenReturn(List.of(reviewer));
        var result = numberChangesMergeRequestFiles.getWeight(reviewer, mergeRequest);
        assertThat(result).isEqualTo(0);
    }

    @Test
    public void numberChangesTwoReviewersAndOneFileWithTwoAuthorsWithReviewers() {
        var firstReviewer = ReviewerPreset.first();
        var secondReviewer = ReviewerPreset.second();
        var mergeRequest = MergeRequestPreset.first();
        var blameResult = Mockito.mock(BlameResult.class);
        var diffEntry = Mockito.mock(DiffEntry.class);
        var branch = mergeRequest.getSourceBranch();

        Mockito.when(gitServiceApi.getDiffBranches(branch)).thenReturn(List.of(diffEntry));
        var newPath = "test/new/path";
        Mockito.when(diffEntry.getNewPath()).thenReturn(newPath);
        Mockito.when(gitServiceApi.getBlameFile(newPath)).thenReturn(blameResult);

        var rawText = Mockito.mock(RawText.class);
        Mockito.when(blameResult.getResultContents()).thenReturn(rawText);
        Mockito.when(rawText.size()).thenReturn(4);

        var firstPerson = Mockito.mock(PersonIdent.class);
        var secondPerson = Mockito.mock(PersonIdent.class);
        Mockito.when(firstPerson.getName()).thenReturn(firstReviewer.getReviewerNames().get(0));
        Mockito.when(secondPerson.getName()).thenReturn(secondReviewer.getReviewerNames().get(0));

        Mockito.when(blameResult.getSourceAuthor(0)).thenReturn(firstPerson);
        Mockito.when(blameResult.getSourceAuthor(1)).thenReturn(secondPerson);
        Mockito.when(blameResult.getSourceAuthor(2)).thenReturn(secondPerson);
        Mockito.when(blameResult.getSourceAuthor(3)).thenReturn(secondPerson);

        Mockito.when(reviewerServiceApi.getAllActive()).thenReturn(List.of(firstReviewer, secondReviewer));
        var firstReviewerResult = numberChangesMergeRequestFiles.getWeight(firstReviewer, mergeRequest);
        assertThat(firstReviewerResult).isEqualTo(0);
        var secondReviewerResult = numberChangesMergeRequestFiles.getWeight(secondReviewer, mergeRequest);
        assertThat(secondReviewerResult).isEqualTo(100);
    }

    @Test
    public void numberChangesTwoReviewersAndTwoFileWithThreeAuthorsWithReviewers() {
        var firstReviewer = ReviewerPreset.first();
        var secondReviewer = ReviewerPreset.second();
        var mergeRequest = MergeRequestPreset.first();
        var firstBlameResult = Mockito.mock(BlameResult.class);
        var secondBlameResult = Mockito.mock(BlameResult.class);

        var firstDiffEntry = Mockito.mock(DiffEntry.class);
        var secondDiffEntry = Mockito.mock(DiffEntry.class);
        var branch = mergeRequest.getSourceBranch();

        Mockito.when(gitServiceApi.getDiffBranches(branch)).thenReturn(List.of(firstDiffEntry, secondDiffEntry));
        var newPathFirst = "test/new/path/1";
        var newPathSecond = "test/new/path/1";
        Mockito.when(firstDiffEntry.getNewPath()).thenReturn(newPathFirst);
        Mockito.when(gitServiceApi.getBlameFile(newPathFirst)).thenReturn(firstBlameResult);

        Mockito.when(secondDiffEntry.getNewPath()).thenReturn(newPathSecond);
        Mockito.when(gitServiceApi.getBlameFile(newPathSecond)).thenReturn(secondBlameResult);

        var rawText = Mockito.mock(RawText.class);
        Mockito.when(firstBlameResult.getResultContents()).thenReturn(rawText);
        Mockito.when(rawText.size()).thenReturn(2);
        Mockito.when(secondBlameResult.getResultContents()).thenReturn(rawText);

        var firstPerson = Mockito.mock(PersonIdent.class);
        var secondPerson = Mockito.mock(PersonIdent.class);
        var otherPerson = Mockito.mock(PersonIdent.class);
        Mockito.when(firstPerson.getName()).thenReturn(firstReviewer.getReviewerNames().get(0));
        Mockito.when(secondPerson.getName()).thenReturn(secondReviewer.getReviewerNames().get(0));
        Mockito.when(otherPerson.getName()).thenReturn("other");

        Mockito.when(firstBlameResult.getSourceAuthor(0)).thenReturn(firstPerson);
        Mockito.when(firstBlameResult.getSourceAuthor(1)).thenReturn(otherPerson);

        Mockito.when(secondBlameResult.getSourceAuthor(0)).thenReturn(secondPerson);
        Mockito.when(secondBlameResult.getSourceAuthor(1)).thenReturn(secondPerson);

        Mockito.when(reviewerServiceApi.getAllActive()).thenReturn(List.of(firstReviewer, secondReviewer));
        var firstReviewerResult = numberChangesMergeRequestFiles.getWeight(firstReviewer, mergeRequest);
        assertThat(firstReviewerResult).isEqualTo(0);
        var secondReviewerResult = numberChangesMergeRequestFiles.getWeight(secondReviewer, mergeRequest);
        assertThat(secondReviewerResult).isEqualTo(100);
    }

    @Test
    public void numberChangesTwoReviewersAndOneFileWithTwoAuthorsWithReviewersWithTwoNames() {
        var firstReviewer = ReviewerPreset.first();
        var secondReviewer = ReviewerPreset.second();
        var mergeRequest = MergeRequestPreset.first();
        var blameResult = Mockito.mock(BlameResult.class);
        var diffEntry = Mockito.mock(DiffEntry.class);
        var branch = mergeRequest.getSourceBranch();

        Mockito.when(gitServiceApi.getDiffBranches(branch)).thenReturn(List.of(diffEntry));
        var newPath = "test/new/path";
        Mockito.when(diffEntry.getNewPath()).thenReturn(newPath);
        Mockito.when(gitServiceApi.getBlameFile(newPath)).thenReturn(blameResult);

        var rawText = Mockito.mock(RawText.class);
        Mockito.when(blameResult.getResultContents()).thenReturn(rawText);
        Mockito.when(rawText.size()).thenReturn(6);

        var firstPerson = Mockito.mock(PersonIdent.class);
        var secondPerson = Mockito.mock(PersonIdent.class);
        var thirdPerson = Mockito.mock(PersonIdent.class);
        Mockito.when(firstPerson.getName()).thenReturn(firstReviewer.getReviewerNames().get(0));
        Mockito.when(secondPerson.getName()).thenReturn(secondReviewer.getReviewerNames().get(0));
        Mockito.when(thirdPerson.getName()).thenReturn(secondReviewer.getReviewerNames().get(1));

        Mockito.when(blameResult.getSourceAuthor(0)).thenReturn(firstPerson);
        Mockito.when(blameResult.getSourceAuthor(1)).thenReturn(firstPerson);
        Mockito.when(blameResult.getSourceAuthor(2)).thenReturn(secondPerson);
        Mockito.when(blameResult.getSourceAuthor(3)).thenReturn(secondPerson);
        Mockito.when(blameResult.getSourceAuthor(4)).thenReturn(thirdPerson);
        Mockito.when(blameResult.getSourceAuthor(5)).thenReturn(thirdPerson);

        Mockito.when(reviewerServiceApi.getAllActive()).thenReturn(List.of(firstReviewer, secondReviewer));
        var firstReviewerResult = numberChangesMergeRequestFiles.getWeight(firstReviewer, mergeRequest);
        assertThat(firstReviewerResult).isEqualTo(0);
        var secondReviewerResult = numberChangesMergeRequestFiles.getWeight(secondReviewer, mergeRequest);
        assertThat(secondReviewerResult).isEqualTo(100);
    }
}
