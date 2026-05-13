package com.threelabs.enumration.code;

public enum YnCode {
    Y(true),
    N(false);

    private final boolean value;

    YnCode(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }
}
