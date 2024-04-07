package fr.uga.l3miage.spring.tp3.exceptions;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@Builder
public class StepNotFoundResponse {
    private Long stepId;
    private String errorMessage;
    private String uri;
}