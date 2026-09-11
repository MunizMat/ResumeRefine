/* ------------- External ----------- */
import { isAxiosError } from 'axios';

/* ------------- Api Instance ----------- */
import { api } from '../api';

/* ------------- Types ----------- */
import {
  GetResumeAnalysisInput,
  GetResumeAnalysisOutput,
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

const getResumeAnalysis = async ({ analysisId }: GetResumeAnalysisInput) => {
  try {
    const { data } = await api.get<GetResumeAnalysisOutput>(
      `/resume/${analysisId}`
    );

    return data;
  } catch (error) {
    console.error(error);

    throw error;
  }
};

/**
 * Lightweight readiness check used by the loading page while it polls.
 * The analysis endpoint returns 404 until the resume has been processed,
 * so a 404 simply means "not ready yet" rather than a hard error.
 */
const getResumeAnalysisStatus = async ({
  analysisId,
}: GetResumeAnalysisInput): Promise<'READY' | 'PENDING'> => {
  try {
    await api.get<GetResumeAnalysisOutput>(`/resume/${analysisId}`);

    return 'READY';
  } catch (error) {
    if (isAxiosError(error) && error.response?.status === 404) return 'PENDING';

    throw error;
  }
};

export const resume = {
  saveResume,
  getResumeAnalysis,
  getResumeAnalysisStatus,
};
