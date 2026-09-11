import { useRouter } from 'next/router';
import { LoadingResumeAnalysis } from '../../../../src/modules/Home/components/LoadingResumeAnalysis';

export default function Page() {
  const { query } = useRouter();

  const analysisId = query.analysisId as string | undefined;

  if (!analysisId) return null;

  return <LoadingResumeAnalysis analysisId={analysisId} />;
}
