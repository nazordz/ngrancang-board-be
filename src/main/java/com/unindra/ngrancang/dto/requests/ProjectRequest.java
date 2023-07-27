package com.unindra.ngrancang.dto.requests;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.unindra.ngrancang.utility.ValidImageFile;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProjectRequest {
    @NotNull(message = "key must not be null")
    @Size(max = 10, message = "Max string is 10")
    private String key;
    
    @NotNull(message = "name must not be null")
    @Size(max = 255, message = "Max characters is 255")
    private String name;
    
    @Size(max = 10 * 1024 * 1024, message = "File size must not exceed 10 MB")
    @ValidImageFile
    private MultipartFile avatar;

    @NotNull(message = "description must not be null")
    private String description;
}
