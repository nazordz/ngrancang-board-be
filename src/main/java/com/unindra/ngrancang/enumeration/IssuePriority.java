package com.unindra.ngrancang.enumeration;

public enum IssuePriority {
    HIGHEST("highest"),
    HIGH("high"),
    MEDIUM("medium"),
    LOW("low"),
    LOWEST("lowest");

    private String code;
    
    private IssuePriority(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static IssuePriority fromCode(String code) {
        for (IssuePriority priority : values()) {
            if (priority.code.equals(code)) {
                return IssuePriority.valueOf(priority.toString());
                // return priority;
            }
        }
        throw new IllegalArgumentException("Invalid IssuePriority code: " + code);


    }
}
