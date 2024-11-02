import { Flex, Text } from '@mantine/core';
import { IconCircleCheckFilled } from '@tabler/icons-react';

export const ResumeSuccess = () => {
  return (
    <Flex w="100vw" h="100vh" align="center" justify="center">
      <Flex direction="column" align="center" gap={10}>
        <Text fz={26} maw={800}>
          Thank you for submitting your resume!
        </Text>

        <Text fz={26} maw={800}>
          Check your email shortly for your analysis results
        </Text>

        <IconCircleCheckFilled size={70} style={{ color: '#33c922' }} />
      </Flex>
    </Flex>
  );
};
