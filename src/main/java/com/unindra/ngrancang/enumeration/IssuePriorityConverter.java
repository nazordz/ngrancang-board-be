package com.unindra.ngrancang.enumeration;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class IssuePriorityConverter implements AttributeConverter<IssuePriority, String> {
    @Override
    public String convertToDatabaseColumn(IssuePriority issuePriority) {
        if (issuePriority == null) {
            return null;
        }
        return issuePriority.getCode();
    }

    @Override
    public IssuePriority convertToEntityAttribute(String code) {

        if (code == null) {
            return null;
        }

        return IssuePriority.fromCode(code);
    }
}
