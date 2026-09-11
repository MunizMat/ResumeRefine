import { Button, Flex, Text } from '@mantine/core';
import { IconCircleCheckFilled } from '@tabler/icons-react';
import { useRouter } from 'next/router';
import { FC, useEffect, useRef, useState } from 'react';
import { notifications } from '@mantine/notifications';

import styles from './LoadingResumeAnalysis.module.css';
import { resume } from '../../../../services/resume';

interface Props {
  analysisId: string;
}

/* How often we ask the API whether the analysis is ready. */
const POLL_INTERVAL_MS = 3000;

/* Roughly how long we let each step "work" before moving to the next one.
 * The last step keeps spinning until the analysis is actually ready. */
const STEP_DURATION_MS = 7000;

/* After this long we stop promising an imminent redirect and reassure the
 * user that the result is also on its way to their inbox. */
const SLOW_THRESHOLD_MS = 4 * 60 * 1000;

const STEPS = [
  'Uploading your resume',
  'Reading your PDF',
  'Extracting your experience',
  'Analysing structure & keywords',
  'Writing your personalised feedback',
];

const ENCOURAGEMENTS = [
  'Great resumes take a moment to appreciate.',
  'Our AI is reading every line, just like a recruiter would.',
  'Looking for your strengths and a few things to polish.',
  'Almost there. Good things are worth the wait.',
];

export const LoadingResumeAnalysis: FC<Props> = ({ analysisId }) => {
  const router = useRouter();

  const [activeStep, setActiveStep] = useState<number>(1);
  const [ready, setReady] = useState<boolean>(false);
  const [isSlow, setIsSlow] = useState<boolean>(false);
  const [encouragement, setEncouragement] = useState<string>(ENCOURAGEMENTS[0]);

  const failuresRef = useRef<number>(0);

  /* --------------- Poll the API for readiness --------------- */
  useEffect(() => {
    if (!analysisId) return undefined;

    let cancelled = false;

    const check = async () => {
      try {
        const status = await resume.getResumeAnalysisStatus({ analysisId });

        failuresRef.current = 0;

        if (!cancelled && status === 'READY') setReady(true);
      } catch (error) {
        failuresRef.current += 1;
        console.error(error);

        if (failuresRef.current === 3) {
          notifications.show({
            title: 'Trouble reaching the server',
            message: 'We keep trying — your analysis is still being prepared.',
            color: 'yellow',
          });
        }
      }
    };

    check();
    const interval = setInterval(check, POLL_INTERVAL_MS);

    return () => {
      cancelled = true;
      clearInterval(interval);
    };
  }, [analysisId]);

  /* --------------- Advance the visible steps over time --------------- */
  useEffect(() => {
    if (ready) return undefined;

    const interval = setInterval(() => {
      setActiveStep((prev) => Math.min(prev + 1, STEPS.length - 1));
    }, STEP_DURATION_MS);

    return () => clearInterval(interval);
  }, [ready]);

  /* --------------- Rotate the encouraging copy --------------- */
  useEffect(() => {
    const interval = setInterval(() => {
      setEncouragement((prev) => {
        const next = ENCOURAGEMENTS.indexOf(prev) + 1;
        return ENCOURAGEMENTS[next % ENCOURAGEMENTS.length];
      });
    }, 5000);

    return () => clearInterval(interval);
  }, []);

  /* --------------- Flag a slow analysis --------------- */
  useEffect(() => {
    const timeout = setTimeout(() => setIsSlow(true), SLOW_THRESHOLD_MS);

    return () => clearTimeout(timeout);
  }, []);

  /* --------------- Redirect once ready --------------- */
  useEffect(() => {
    if (!ready) return undefined;

    setActiveStep(STEPS.length);

    const timeout = setTimeout(() => {
      router.replace(`/analysis/${analysisId}`);
    }, 900);

    return () => clearTimeout(timeout);
  }, [ready, analysisId, router]);

  return (
    <Flex className={styles.page}>
      <Flex className={styles.card}>
        <div className={styles.orb} data-ready={ready || undefined}>
          <span className={styles.orb_ring} />
          <span className={styles.orb_ring} />
          <span className={styles.orb_core} />
        </div>

        <Text className={styles.title}>
          {ready ? 'Your analysis is ready!' : 'Analysing your resume'}
        </Text>

        <Text className={styles.subtitle}>
          {ready ? 'Taking you there now…' : encouragement}
        </Text>

        <ul className={styles.steps}>
          {STEPS.map((label, index) => {
            const state =
              index < activeStep
                ? 'done'
                : index === activeStep
                ? 'active'
                : 'pending';

            return (
              <li key={label} className={styles.step} data-state={state}>
                <span className={styles.step_icon}>
                  {state === 'done' ? (
                    <IconCircleCheckFilled size={22} />
                  ) : (
                    <span className={styles.step_dot} />
                  )}
                </span>

                <Text className={styles.step_label}>{label}</Text>
              </li>
            );
          })}
        </ul>

        {isSlow && !ready && (
          <Flex className={styles.slow}>
            <Text className={styles.slow_text}>
              This is taking a little longer than usual. You can keep this page
              open — we&apos;ll also email the link to your analysis as soon as
              it&apos;s ready.
            </Text>

            <Button variant="subtle" size="sm" onClick={() => router.push('/')}>
              Back to home
            </Button>
          </Flex>
        )}
      </Flex>
    </Flex>
  );
};
