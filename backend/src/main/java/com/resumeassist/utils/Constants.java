package com.resumeassist.utils;

public final class Constants {
    public static final String OPEN_AI_JSON_SCHEMA = """
            {
                "type": "object",
                "properties": {
                    "strengths": {
                        "type": "array",
                        "items": {
                            "type": "object",
                            "properties": {
                                "explanation": { "type": "string" },
                                "output": { "type": "string" }
                            },
                            "required": ["explanation", "output"],
                            "additionalProperties": false
                        }
                    },
                    "weaknesses": {
                        "type": "array",
                        "items": {
                            "type": "object",
                            "properties": {
                                "explanation": { "type": "string" },
                                "output": { "type": "string" }
                            },
                            "required": ["explanation", "output"],
                            "additionalProperties": false
                        }
                    },
                    "final_answer": { "type": "string" },
                    "suggestions": {
                        "type": "array",
                        "items": {
                            "type": "object",
                            "properties": {
                                "explanation": { "type": "string" },
                                "output": { "type": "string" }
                            },
                            "required": ["explanation", "output"],
                            "additionalProperties": false
                        }
                    }
                },
                "required": ["strengths", "weaknesses", "final_answer", "suggestions"],
                "additionalProperties": false
            }
        """;

    public static final String RESUME_ANALYSIS_PROMPT = """
            Given the text below extracted from a resume, analyze the content
            by identifying at least three positive points (strengths) that highlight the candidate's
            skills in building the resume. Also, identify at least three areas of improvement (weaknesses)
            where the candidate can enhance their resume to increase their chances of being hired.
            After providing your analysis, include actionable suggestions for improvement. In your answers
            make sure to refer to the candidate in second person.
            """;
}
