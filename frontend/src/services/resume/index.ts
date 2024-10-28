/* ------------- Api Instance ----------- */
import { api } from '../api';

/* ------------- Types ----------- */
import {
  GetResumeAnalysisInput,
  ResumeAnalysis,
  SaveResumeInput,
  SaveResumeOutput,
} from './types';

const saveResume = async ({ resume }: SaveResumeInput) => {
  try {
    const { data } = await api.post<SaveResumeOutput>(
      '/resume',
      {
        resume,
      },
      {
        headers: {
          'Content-Type': 'multipart/form-data; boundary=3o3on839nf1i03',
        },
      }
    );

    return data;
  } catch (error) {
    console.error(error);
    throw error;
  }
};

const getResumeAnalysis = async ({
  analysisId,
  email,
}: GetResumeAnalysisInput) => {
  try {
    const { data } = await api.get<ResumeAnalysis>(`/resume/${analysisId}`, {
      params: { email },
    });

    return data;
  } catch (error) {
    console.error(error);

    throw error;
  }
};

export const resume = {
  saveResume,
  getResumeAnalysis,
};
