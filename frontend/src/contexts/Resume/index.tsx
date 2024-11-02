import {
  createContext,
  Dispatch,
  FC,
  ReactNode,
  SetStateAction,
  useContext,
  useMemo,
  useState,
} from 'react';
import { Resume } from '../../types/resume';

import { ResumeAnalysis } from '../../services/resume/types';

interface ResumeProviderProps {
  children: ReactNode;
}

interface ResumeContextData {
  resume: Resume | null;
  setResume: Dispatch<SetStateAction<Resume | null>>;

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

  const value = useMemo(
    () => ({
      resume,
      setResume,

      resumeAnalysis,
      setResumeAnalysis,
    }),
    [resume, setResume, resumeAnalysis, setResumeAnalysis]
  );
  return (
    <ResumeContext.Provider value={value}>{children}</ResumeContext.Provider>
  );
};

export const useResume = () => {
  const ctx = useContext(ResumeContext);

  return ctx;
};
