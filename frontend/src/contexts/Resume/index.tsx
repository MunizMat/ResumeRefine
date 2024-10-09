import {
  createContext,
  Dispatch,
  FC,
  ReactNode,
  SetStateAction,
  useCallback,
  useContext,
  useMemo,
  useState,
} from 'react';
import { Resume } from '../../types/resume';

import { resume as resumeServices } from '../../services/resume';
import { ResumeAnalysis } from '../../services/resume/types';

interface ResumeProviderProps {
  children: ReactNode;
}

interface ResumeContextData {
  resume: Resume | null;
  setResume: Dispatch<SetStateAction<Resume | null>>;
  loadingResumeAnalysis: boolean;
  handleAnalyseResume: () => Promise<void>;

  resumeAnalysis: ResumeAnalysis;
  setResumeAnalysis: Dispatch<SetStateAction<ResumeAnalysis>>;
}

const ResumeContext = createContext({} as ResumeContextData);

export const ResumeProvider: FC<ResumeProviderProps> = ({ children }) => {
  const [resume, setResume] = useState<Resume | null>(null);
  const [resumeAnalysis, setResumeAnalysis] = useState<ResumeAnalysis>({
    final_answer: '',
    strengths: [],
    suggestions: [],
    weaknesses: [],
  });
  const [loadingResumeAnalysis, setLoadingResumeAnalysis] =
    useState<boolean>(false);

  const handleAnalyseResume = useCallback(async () => {
    if (!resume) return;

    const { resumeId } = resume;

    setLoadingResumeAnalysis(true);

    try {
      const { final_answer, strengths, weaknesses, suggestions } =
        await resumeServices.getResumeAnalysis({ resumeId });

      setResumeAnalysis({ final_answer, strengths, weaknesses, suggestions });
    } catch (error) {
      console.error(error);
    }

    setLoadingResumeAnalysis(false);
  }, [resume]);

  const value = useMemo(
    () => ({
      resume,
      setResume,
      loadingResumeAnalysis,
      handleAnalyseResume,
      resumeAnalysis,
      setResumeAnalysis,
    }),
    [
      resume,
      setResume,
      loadingResumeAnalysis,
      resumeAnalysis,
      setResumeAnalysis,
    ]
  );
  return (
    <ResumeContext.Provider value={value}>{children}</ResumeContext.Provider>
  );
};

export const useResume = () => {
  const ctx = useContext(ResumeContext);

  return ctx;
};
