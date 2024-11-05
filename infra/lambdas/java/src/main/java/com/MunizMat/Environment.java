package com.MunizMat;

public class Environment {
    public static String BUCKET_NAME;
    public static String OPENAI_API_KEY;
    public static String PROCESS_RESUME_QUEUE_URL;
    public static String TABLE_NAME;

    public static void read(){
        Environment.BUCKET_NAME = System.getenv("BUCKET_NAME");
        Environment.OPENAI_API_KEY = System.getenv("OPENAI_API_KEY");
        Environment.PROCESS_RESUME_QUEUE_URL = System.getenv("PROCESS_RESUME_QUEUE_URL");
        Environment.TABLE_NAME = System.getenv("TABLE_NAME");
    }
}
