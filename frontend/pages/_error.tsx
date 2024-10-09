/* ------------- External ------------ */
import { Flex, Title } from '@mantine/core';
import { IconExclamationCircle } from '@tabler/icons-react';

export default function ErrorPage() {
  return (
    <Flex
      gap={50}
      direction="column"
      justify="center"
      align="center"
      w="100%"
      py={100}
    >
      <IconExclamationCircle size={100} stroke={1} />
      <Title>An unexpected error occurred. We apologize</Title>
    </Flex>
  );
}
