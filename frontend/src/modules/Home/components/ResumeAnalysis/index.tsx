import { Flex, Image, List, Tabs, Text } from '@mantine/core';

import styles from './ResumeAnalysis.module.css';
import { ResumeAnalysis as IAnalysis } from '../../../../services/resume/types';
import { FC } from 'react';
import { format } from 'date-fns';

interface Props {
  analysis: IAnalysis;
}

export const ResumeAnalysis: FC<Props> = ({ analysis }) => {
  const { filename, resumeAnalysis, email, analysisId, createdAt } = analysis;
  const { finalAnswer, strengths, suggestions, weaknesses } = resumeAnalysis;

  const resumeImg = `https://resume-refine-main-bucket-prod.s3.us-east-1.amazonaws.com/${encodeURIComponent(
    email
  )}/${analysisId}/resume.jpg`;

  return (
    <Flex className={styles.container}>
      <Flex className={styles.wrapper}>
        <Flex direction="column" gap={28} maw={700}>
          <Flex gap={24} className={styles.resume_wrapper}>
            <Flex direction="column">
              <Text fz={48}>Your Resume:</Text>

              <Text fz={16}>File: {filename}</Text>

              <Text fz={16}>Email: {email}</Text>

              <Text fz={16}>
                Date: {format(new Date(createdAt), 'dd MMMM yyyy, HH:mm')}
              </Text>

              <Text mt={20} fz={18}>
                {finalAnswer}
              </Text>
            </Flex>

            <Flex className={styles.resume_mobile}>
              <Image radius={8} src={resumeImg} />
            </Flex>
          </Flex>

          <Tabs defaultValue="strengths">
            <Tabs.List mb={24}>
              <Tabs.Tab value="strengths">
                <Text fw={600}>Strengths</Text>
              </Tabs.Tab>

              <Tabs.Tab value="weaknesses">
                <Text fw={600}>Weaknesses</Text>
              </Tabs.Tab>

              <Tabs.Tab value="suggestions">
                <Text fw={600}>Suggestions</Text>
              </Tabs.Tab>
            </Tabs.List>

            <Tabs.Panel value="strengths">
              <List spacing={16}>
                {strengths.map(({ output, explanation }) => (
                  <List.Item key={output}>
                    <Text fz={18} fw={600}>
                      {output}
                    </Text>
                    <Text fz={16}>{explanation}</Text>
                  </List.Item>
                ))}
              </List>
            </Tabs.Panel>

            <Tabs.Panel value="weaknesses">
              <List spacing={16}>
                {weaknesses.map(({ output, explanation }) => (
                  <List.Item key={output}>
                    <Text fz={18} fw={600}>
                      {output}
                    </Text>
                    <Text fz={16}>{explanation}</Text>
                  </List.Item>
                ))}
              </List>
            </Tabs.Panel>

            <Tabs.Panel value="suggestions">
              <List spacing={16}>
                {suggestions.map(({ output, explanation }) => (
                  <List.Item key={output}>
                    <Text fz={18} fw={600}>
                      {output}
                    </Text>
                    <Text fz={16}>{explanation}</Text>
                  </List.Item>
                ))}
              </List>
            </Tabs.Panel>
          </Tabs>
        </Flex>

        <Flex className={styles.right_section}>
          <Image
            className={styles.resume_img}
            radius={8}
            height={600}
            width={400}
            src={resumeImg}
          />
        </Flex>
      </Flex>
    </Flex>
  );
};
