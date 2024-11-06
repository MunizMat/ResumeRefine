package com.MunizMat.models;

import com.MunizMat.ResumeFeedback;
import com.amazonaws.services.dynamodbv2.datamodeling.*;

@DynamoDBTable(tableName = "resume-refine-main-table-prod")
public class ResumeAnalysis {
    private String partitionKey;
    private String sortKey;
    private String email;
    private String analysisId;
    private String filename;
    private long createdAt;
    private ResumeFeedback resumeAnalysis;
    private String ipAddress;

    public ResumeAnalysis(){

    }

    public ResumeAnalysis(
            String partitionKey,
            String sortKey,
            String email,
            String analysisId,
            String filename,
            long createdAt,
            ResumeFeedback resumeAnalysis,
            String ipAddress
    ) {
        this.analysisId = analysisId;
        this.sortKey = sortKey;
        this.email = email;
        this.resumeAnalysis = resumeAnalysis;
        this.partitionKey = partitionKey;
        this.filename = filename;
        this.createdAt = createdAt;
        this.ipAddress = ipAddress;
    }

    @DynamoDBHashKey
    public String getPartitionKey() {
        return partitionKey;
    }
    public void setPartitionKey(String partitionKey) {
        this.partitionKey = partitionKey;
    }

    @DynamoDBRangeKey
    public String getSortKey() {
        return sortKey;
    }
    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }

    @DynamoDBAttribute
    public String getFilename() {
        return filename;
    }
    public void setFilename(String filename) {
        this.filename = filename;
    }

    @DynamoDBAttribute
    public String getAnalysisId() { return analysisId; }
    public void setAnalysisId(String analysisId) { this.analysisId = analysisId; }

    @DynamoDBAttribute
    public String getEmail() { return email; }
    public void setEmail(String email) {
        this.email = email;
    }

    @DynamoDBAttribute
    public long getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    @DynamoDBAttribute
    public ResumeFeedback getResumeAnalysis() {
        return resumeAnalysis;
    }
    public void setResumeAnalysis(ResumeFeedback resumeAnalysis) {
        this.resumeAnalysis = resumeAnalysis;
    }

    @DynamoDBAttribute
    public String getIpAddress() { return ipAddress; }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}
