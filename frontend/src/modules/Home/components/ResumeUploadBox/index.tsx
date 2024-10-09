import { Button, Flex, Paper, Text } from '@mantine/core';

import styles from './ResumeUploadBox.module.css';

import { ResumeDropzone } from '../ResumeDropzone';
import { useResume } from '../../../../contexts/Resume';

export const ResumeUploadBox = () => {
  const { handleAnalyseResume, resume } = useResume();

  return (
    <Paper className={styles.container} shadow="xl" p={16}>
      <Flex direction="column" gap={16}>
        <Text fz={18} fw={400}>
          Upload your resume and obtain a detailed analysis with meaningful
          insights that will help attract recruiters!
        </Text>

        <ResumeDropzone />

        <Button
          onClick={handleAnalyseResume}
          disabled={!resume}
          className={styles.button}
          size="lg"
          data-disabled={!resume}
        >
          Analyse My Resume
        </Button>
      </Flex>
    </Paper>
  );
};
