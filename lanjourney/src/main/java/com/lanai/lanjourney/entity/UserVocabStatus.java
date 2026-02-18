package com.lanai.lanjourney.entity;

public enum UserVocabStatus {
    new_, learning, reviewing, mastered;

    // Because your DB default is 'new' (reserved word-ish in Java),
    // we map Java enum new_ -> "new" string in DB manually if needed.
    public String dbValue() {
        return this == new_ ? "new" : this.name();
    }

    public static UserVocabStatus fromDb(String v) {
        if ("new".equalsIgnoreCase(v)) {
            return new_;
        }
        return UserVocabStatus.valueOf(v.toLowerCase());
    }
}
