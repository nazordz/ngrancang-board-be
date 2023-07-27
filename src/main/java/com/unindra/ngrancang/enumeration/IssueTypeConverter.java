package com.unindra.ngrancang.enumeration;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class IssueTypeConverter implements AttributeConverter<IssueType, String> {

    @Override
    public String convertToDatabaseColumn(IssueType issueType) {
        if (issueType == null) {
            return null;
        }
        return issueType.getValue();
    }

    @Override
    public IssueType convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }
        // return IssueType.values(code);
        return null;
    }


}