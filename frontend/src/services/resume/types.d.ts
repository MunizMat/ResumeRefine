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
  resumeId: string;
}

export interface ResumeAnalysis {
  final_answer: string;
  weaknesses: ResumeAnalysisResponse[];
  strengths: ResumeAnalysisResponse[];
  suggestions: ResumeAnalysisResponse[];
}
