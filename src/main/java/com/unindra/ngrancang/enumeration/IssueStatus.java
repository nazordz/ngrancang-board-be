package com.unindra.ngrancang.enumeration;

public enum IssueStatus {
    TODO("todo"),
    IN_PROGRESS("in_progress"),
    REVIEW("review"),
    DONE("done");

    private String code;
    
    private IssueStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static IssueStatus fromCode(String code) {
        for (IssueStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid IssueStatus code: " + code);
    }

}
