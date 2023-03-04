package com.example.assigneeauto.presets.test;

import com.example.assigneeauto.presets.test.dto.TestDiffEntry;
import lombok.SneakyThrows;
import org.eclipse.jgit.blame.BlameGenerator;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.internal.storage.dfs.InMemoryRepository;
import org.eclipse.jgit.lib.Repository;

public class GitEntryPreset {

    public static DiffEntry firstDiffEntry() {
        return TestDiffEntry.create("/test/1");
    }

    public static DiffEntry secondDiffEntry() {
        return TestDiffEntry.create("/test/2");
    }

    @SneakyThrows
    public static BlameResult firstBlameResult() {
        var options = new InMemoryRepository.Builder().build();
        var generator = new BlameGenerator(options, "/test/1");

        return BlameResult.create(generator);
    }
}
