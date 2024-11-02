import { ResumeAnalysisResponse } from '../../types/resume';

export interface SaveResumeInput {
  resume: File;
}

export interface SaveResumeOutput {
  url: string;
  filename: string;
  resumeId: string;
}

export interface GetResumeAnalysisInput {
  analysisId: string;
  email: string;
}

export interface ResumeAnalysis {
  partitionKey: string;
  sortKey: string;
  email: string;
  analysisId: string;
  resumeAnalysis: {
    final_answer: string;
    weaknesses: ResumeAnalysisResponse[];
    strengths: ResumeAnalysisResponse[];
    suggestions: ResumeAnalysisResponse[];
  };
}
