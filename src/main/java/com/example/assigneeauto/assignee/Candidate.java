package com.example.assigneeauto.assignee;

import com.example.assigneeauto.persistance.domain.Reviewer;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Candidate implements Comparable {
    private Reviewer reviewer;
    private Long weight;

    @Override
    public boolean equals(Object obj) {
        return ((Candidate) obj).weight.equals(weight);
    }

    @Override
    public int compareTo(Object o) {
        Candidate candidate = (Candidate) o;
        return weight.compareTo(candidate.weight);
    }
}
