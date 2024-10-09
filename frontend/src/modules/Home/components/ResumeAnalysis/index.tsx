import { Flex, Image, List, Tabs, Text } from '@mantine/core';
import { useResume } from '../../../../contexts/Resume';

export const ResumeAnalysis = () => {
  const { resume, resumeAnalysis } = useResume();

  const { final_answer, strengths, suggestions, weaknesses } = resumeAnalysis;

  return (
    <Flex p={40} justify="space-between" align="center">
      <Flex direction="column" gap={28} maw={700}>
        <Text fz={48}>Your Resume Analysis:</Text>
        <Text fz={20}>{resume?.filename}</Text>

        <Text>{final_answer}</Text>

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

      <Flex>
        <Image radius={8} height={600} width={400} src={resume?.url} />
      </Flex>
    </Flex>
  );
};
