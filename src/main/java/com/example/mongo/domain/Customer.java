package com.example.mongo.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document
public class Customer {
    @Id
    private String id;
    @NotBlank
    private String fullName;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
