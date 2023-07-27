package com.unindra.ngrancang.enumeration;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class IssueStatusConverter implements AttributeConverter<IssueStatus, String>{

    @Override
    public String convertToDatabaseColumn(IssueStatus issueStatus) {
        if (issueStatus == null) {
            return null;
        }
        return issueStatus.getCode();
    }

    @Override
    public IssueStatus convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }
        return IssueStatus.fromCode(code);
    }
    
}
