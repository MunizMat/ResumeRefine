import { GetServerSidePropsContext, InferGetServerSidePropsType } from 'next';
import { ResumeAnalysis } from '../../../../src/modules/Home/components/ResumeAnalysis';
import { resume } from '../../../../src/services/resume';

export default function Page({
  resumeAnalysis,
}: InferGetServerSidePropsType<typeof getServerSideProps>) {
  return <ResumeAnalysis analysis={resumeAnalysis} />;
}

export async function getServerSideProps(ctx: GetServerSidePropsContext) {
  try {
    if (!ctx.params) throw new Error('Params missing');

    const { email, analysisId } = ctx.params;

    if (!email || !analysisId) throw new Error('Missing email or analysisId');

    const resumeAnalysis = await resume.getResumeAnalysis({
      analysisId: analysisId as string,
      email: email as string,
    });

    return {
      props: {
        resumeAnalysis,
      },
    };
  } catch (error) {
    console.error(error);

    return {
      redirect: {
        destination: '/404',
        permanent: false,
      },
    };
  }
}
