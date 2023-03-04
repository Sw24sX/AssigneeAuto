package com.example.assigneeauto.presets.test.dto;

import org.eclipse.jgit.diff.DiffEntry;

public class TestDiffEntry extends DiffEntry {
    private TestDiffEntry() {
    }

    public static DiffEntry create(String newPath) {
        var result =  new TestDiffEntry();
        result.newPath = newPath;
        return result;
    }
}
