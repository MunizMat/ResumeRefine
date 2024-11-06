export interface ResumeAnalysis {
  partitionKey: string;
  sortKey: string;
  ipAddress: string;
  email: string;
  analysisId: string;
  filename: string;
  createdAt: number;
}