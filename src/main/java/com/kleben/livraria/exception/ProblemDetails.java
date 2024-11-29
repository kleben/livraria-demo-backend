package com.kleben.livraria.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProblemDetails {

    private String type;
    private String title;
    private int status;
    private String detail;
    private String instance;

    public static ProblemDetails create(String title, int status, String detail, String instance) {
        return ProblemDetails.builder()
                .title(title)
                .status(status)
                .detail(detail)
                .instance(instance)
                .build();
    }

    public static ProblemDetails createWithURI(String type, String title, int status, String detail, String instance) {
        return ProblemDetails.builder()
                .type(type)
                .title(title)
                .status(status)
                .detail(detail)
                .instance(instance)
                .build();
    }
}
