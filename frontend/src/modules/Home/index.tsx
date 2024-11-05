/* --------------- External ------------- */
import { Flex, Text, Title } from '@mantine/core';

/* --------------- Styles ------------- */
import styles from './Home.module.css';
import { ResumeUploadBox } from './components/ResumeUploadBox';
import { InterviewIcon } from '../../components/Icons/Interview';
import { HiringIcon } from '../../components/Icons/HiringIcon';

export const HomeModule = () => {
  return (
    <Flex className={styles.container}>
      <InterviewIcon className={styles.interview_icon} />
      <HiringIcon className={styles.hiring_icon} />

      <Title className={styles.title}>ResumeRefine</Title>

      <Text className={styles.slogan}>
        Polish Your{' '}
        <Text span inherit c="#58CC5F">
          Resume
        </Text>
        , Shine in Your{' '}
        <Text span inherit c="#58CC5F">
          Career
        </Text>
      </Text>

      <ResumeUploadBox />
    </Flex>
  );
};
