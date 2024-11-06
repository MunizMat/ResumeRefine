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
}

export interface ResumeAnalysis {
  partitionKey: string;
  sortKey: string;
  email: string;
  analysisId: string;
  filename: string;
  createdAt: number;
  resumeAnalysis: {
    finalAnswer: string;
    weaknesses: ResumeAnalysisResponse[];
    strengths: ResumeAnalysisResponse[];
    suggestions: ResumeAnalysisResponse[];
  };
}

export interface GetResumeAnalysisOutput {
  resumeAnalysis: ResumeAnalysis;
}
