package com.unindra.ngrancang.enumeration;

import com.unindra.ngrancang.interfaces.AbstractEnumConverter;
import com.unindra.ngrancang.interfaces.PersistableEnum;

public enum IssueType implements PersistableEnum<String> {
    TASK("task"),
    BUG("bug"),
    STORY("story"),
    EPIC("epic"),
    SUB_TASK("sub_task");

    private final String value;

    @Override
    public String getValue() {
        return value;
    }

    private IssueType(String value) {
        this.value= value;
    }

    public static class Converter extends AbstractEnumConverter<IssueType, String> {
        public Converter() {
            super(IssueType.class);
        }
    }

    // private String code;
    // private IssueType(String code) {
    //     this.code = code;
    // }

    // public String getCode() {
    //     return code;
    // }

    // public static IssueType fromCode(String code) {
    //     for (IssueType type : values()) {
    //         if (type.code.equals(code)) {
    //             return type;
    //         }
    //     }
    //     throw new IllegalArgumentException("Invalid IssueType code: " + code);
    // }
}
