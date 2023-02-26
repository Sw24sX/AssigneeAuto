package com.example.assigneeauto.presets.test;

public enum GitBranch {
    MAIN("main"),
    TEST_1("test_1"),
    TEST_2("test_2");

    private final String name;

    GitBranch(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
