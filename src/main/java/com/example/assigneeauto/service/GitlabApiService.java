package com.example.assigneeauto.service;

import org.gitlab4j.api.Constants;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Member;
import org.gitlab4j.api.models.MergeRequest;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для управления api Gitlab
 */
public interface GitlabApiService {

    /**
     * Список участников проекта
     * @return Список участников проекта
     */
    List<Member> getListMembers() throws GitLabApiException;

    /**
     * Список открытых merge request проекта
     * @return Список merge request проекта
     */
    List<MergeRequest> getListMergeRequest() throws GitLabApiException;

    /**
     * Список merge request проекта по статусу
     * @param status статус merge request
     * @return список подходящих merge request
     */
    List<MergeRequest> getListMergeRequestByStatus(Constants.MergeRequestState status) throws GitLabApiException;

    /**
     * Список merge request проекта по id ревьювера
     * @param assigneeId id ревьювера
     * @param status статус возвращаемых merge request
     * @return список подходящих merge request
     */
    List<MergeRequest> getListMergeRequestByAssigneeId(Long assigneeId, Constants.MergeRequestState status) throws GitLabApiException;

    /**
     * Merge request проекта по iid
     * @param iid уникальный номер merge request
     * @return merge request с заданным iid
     */
    Optional<MergeRequest> getMergeRequest(Long iid);

    /**
     * Назначить ревьювера на merge request
     * @param mergeRequestIid Идентификатор merge request
     * @param memberId Идентификатор назначаемого участника проекта
     * @return Обновленный merge request
     */
    MergeRequest setAssigneeToMergeRequest(Long mergeRequestIid, Long memberId) throws GitLabApiException;
}
