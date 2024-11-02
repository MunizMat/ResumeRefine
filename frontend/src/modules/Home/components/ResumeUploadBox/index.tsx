import { Button, FileInput, Paper, Text, TextInput } from '@mantine/core';
import z from 'zod';

import styles from './ResumeUploadBox.module.css';

import { useForm, zodResolver } from '@mantine/form';

import { analyseResumeSchema } from '../../../../schemas/analyseResumeSchema';
import { useState } from 'react';
import { getPresignedUrl } from '../../../../services/presigned-url';
import axios from 'axios';
import { notifications } from '@mantine/notifications';
import { useRouter } from 'next/router';

type AnalyseResumeForm = z.infer<typeof analyseResumeSchema>;

export const ResumeUploadBox = () => {
  const { push } = useRouter();
  const [loading, setLoading] = useState<boolean>(false);

  const { getInputProps, values, onSubmit } = useForm<AnalyseResumeForm>({
    initialValues: {
      email: '',
      resume: null,
    },
    validate: zodResolver(analyseResumeSchema),
  });

  const handleSubmit = async ({ email, resume }: AnalyseResumeForm) => {
    setLoading(true);

    try {
      const { url: presignedUrl } = await getPresignedUrl({
        email,
        filename: resume?.name || '',
      });

      await axios.put(presignedUrl, resume);

      push('/analysis/success');
    } catch (error) {
      notifications.show({
        title: 'Failed to upload resume',
        message: 'We failed to upload your resume. Please try again',
        color: 'red',
      });
    }

    setLoading(false);
  };

  return (
    <Paper className={styles.container} shadow="xl" p={16}>
      <form className={styles.form} onSubmit={onSubmit(handleSubmit)}>
        <Text fz={18} fw={400}>
          Upload your resume and obtain a detailed analysis with meaningful
          insights that will help attract recruiters!
        </Text>

        <TextInput
          variant="unstyled"
          label="Email"
          placeholder="Email"
          {...getInputProps('email')}
        />

        <FileInput
          label="Resume"
          placeholder="Upload resume"
          variant="unstyled"
          accept=".pdf"
          {...getInputProps('resume')}
        />

        <Button
          type="submit"
          loading={loading}
          disabled={!values.resume || !values.email}
          className={styles.button}
          size="lg"
          data-disabled={!values.resume || !values.email}
        >
          Analyse My Resume
        </Button>
      </form>
    </Paper>
  );
};
