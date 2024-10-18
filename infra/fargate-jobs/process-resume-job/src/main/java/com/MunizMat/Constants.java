package com.MunizMat;

import java.util.List;
import java.util.Map;

public final class Constants {
    public static Map<String, Object> getSchemaAsMap() {
        return Map.of(
                "type", "object",
                "properties", Map.of(
                        "strengths", Map.of(
                                "type", "array",
                                "items", Map.of(
                                        "type", "object",
                                        "properties", Map.of(
                                                "explanation", Map.of("type", "string"),
                                                "output", Map.of("type", "string")
                                        ),
                                        "required", List.of("explanation", "output"),
                                        "additionalProperties", false
                                )
                        ),
                        "weaknesses", Map.of(
                                "type", "array",
                                "items", Map.of(
                                        "type", "object",
                                        "properties", Map.of(
                                                "explanation", Map.of("type", "string"),
                                                "output", Map.of("type", "string")
                                        ),
                                        "required", List.of("explanation", "output"),
                                        "additionalProperties", false
                                )
                        ),
                        "final_answer", Map.of("type", "string"),
                        "suggestions", Map.of(
                                "type", "array",
                                "items", Map.of(
                                        "type", "object",
                                        "properties", Map.of(
                                                "explanation", Map.of("type", "string"),
                                                "output", Map.of("type", "string")
                                        ),
                                        "required", List.of("explanation", "output"),
                                        "additionalProperties", false
                                )
                        )
                ),
                "required", List.of("strengths", "weaknesses", "final_answer", "suggestions"),
                "additionalProperties", false
        );
    }
    public static final String RESUME_ANALYSIS_PROMPT = """
            Given the text below extracted from a resume, analyze the content
            by identifying at least three positive points (strengths) that highlight the candidate's
            skills in building the resume. Also, identify at least three areas of improvement (weaknesses)
            where the candidate can enhance their resume to increase their chances of being hired.
            After providing your analysis, include actionable suggestions for improvement. In your answers
            make sure to refer to the candidate in second person.
            """;
}

