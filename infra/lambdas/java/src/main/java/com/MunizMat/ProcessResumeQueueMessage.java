package com.MunizMat;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class ProcessResumeQueueMessage {
    private List<Record> records;

    @JsonProperty("Records")
    public List<Record> getRecords() {
        return records;
    }

    public static class Record {
        private String eventVersion;
        private String eventSource;
        private String awsRegion;
        private String eventTime;
        private String eventName;
        private UserIdentity userIdentity;
        private RequestParameters requestParameters;
        private ResponseElements responseElements;
        private S3 s3;

        public S3 getS3() {
            return s3;
        }

        public ResponseElements getResponseElements() {
            return responseElements;
        }

        public String getAwsRegion() {
            return awsRegion;
        }

        public String getEventName() {
            return eventName;
        }

        public String getEventSource() {
            return eventSource;
        }

        public RequestParameters getRequestParameters() {
            return requestParameters;
        }

        public String getEventTime() {
            return eventTime;
        }

        public String getEventVersion() {
            return eventVersion;
        }

        public UserIdentity getUserIdentity() {
            return userIdentity;
        }

        public static class UserIdentity {
            private String principalId;

            @JsonProperty("principalId")
            public String getPrincipalId() {
                return principalId;
            }

            public void setPrincipalId(String principalId) {
                this.principalId = principalId;
            }
        }

        public static class RequestParameters {
            private String sourceIPAddress;

            @JsonProperty("sourceIPAddress")
            public String getSourceIPAddress() {
                return sourceIPAddress;
            }

            public void setSourceIPAddress(String sourceIPAddress) {
                this.sourceIPAddress = sourceIPAddress;
            }
        }

        public static class ResponseElements {
            private String xAmzRequestId;
            private String xAmzId2;

            @JsonProperty("x-amz-request-id")
            public String getXAmzRequestId() {
                return xAmzRequestId;
            }

            public void setXAmzRequestId(String xAmzRequestId) {
                this.xAmzRequestId = xAmzRequestId;
            }

            @JsonProperty("x-amz-id-2")
            public String getXAmzId2() {
                return xAmzId2;
            }

            public void setXAmzId2(String xAmzId2) {
                this.xAmzId2 = xAmzId2;
            }
        }

        public static class S3 {
            private String s3SchemaVersion;
            private String configurationId;
            private Bucket bucket;
            private Object object;

            public Bucket getBucket() {
                return bucket;
            }

            public Object getObject() {
                return object;
            }

            public String getConfigurationId() {
                return configurationId;
            }

            public String getS3SchemaVersion() {
                return s3SchemaVersion;
            }

            public static class Bucket {
                private String name;
                private OwnerIdentity ownerIdentity;
                private String arn;

                public OwnerIdentity getOwnerIdentity() {
                    return ownerIdentity;
                }

                public String getArn() {
                    return arn;
                }

                public String getName() {
                    return name;
                }

                public static class OwnerIdentity {
                    private String principalId;

                    @JsonProperty("principalId")
                    public String getPrincipalId() {
                        return principalId;
                    }

                    public void setPrincipalId(String principalId) {
                        this.principalId = principalId;
                    }
                }
            }

            public static class Object {
                private String key;
                private long size;
                private String eTag;
                private String sequencer;

                public long getSize() {
                    return size;
                }

                public String geteTag() {
                    return eTag;
                }

                public String getKey() {
                    return key;
                }

                public String getSequencer() {
                    return sequencer;
                }
            }
        }
    }
}

