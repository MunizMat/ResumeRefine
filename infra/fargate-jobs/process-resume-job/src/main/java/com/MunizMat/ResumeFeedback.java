package com.MunizMat;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

import java.util.List;

@DynamoDBDocument
public class ResumeFeedback {

    private List<FeedbackItem> strengths;
    private List<FeedbackItem> weaknesses;
    private List<FeedbackItem> suggestions;
    private String finalAnswer;

    @DynamoDBAttribute
    public List<FeedbackItem> getStrengths() {
        return strengths;
    }

    public void setStrengths(List<FeedbackItem> strengths) {
        this.strengths = strengths;
    }

    @DynamoDBAttribute
    public List<FeedbackItem> getWeaknesses() {
        return weaknesses;
    }

    public void setWeaknesses(List<FeedbackItem> weaknesses) {
        this.weaknesses = weaknesses;
    }

    @DynamoDBAttribute
    public List<FeedbackItem> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<FeedbackItem> suggestions) {
        this.suggestions = suggestions;
    }

    @DynamoDBAttribute
    public String getFinalAnswer() {
        return finalAnswer;
    }

    public void setFinalAnswer(String finalAnswer) {
        this.finalAnswer = finalAnswer;
    }

    @DynamoDBDocument
    public static class FeedbackItem {
        private String output;
        private String explanation;

        @DynamoDBAttribute
        public String getOutput() {
            return output;
        }

        public void setOutput(String output) {
            this.output = output;
        }

        @DynamoDBAttribute
        public String getExplanation() {
            return explanation;
        }

        public void setExplanation(String explanation) {
            this.explanation = explanation;
        }
    }

    @Override
    public String toString() {
        return "ResumeFeedback{" +
                "strengths=" + strengths +
                ", weaknesses=" + weaknesses +
                ", suggestions=" + suggestions +
                ", finalAnswer='" + finalAnswer + '\'' +
                '}';
    }
}

