import { Anchor, Flex, Text, Title } from '@mantine/core';
import { IconError404 } from '@tabler/icons-react';

export default function Page404() {
  return (
    <Flex
      gap={20}
      py={100}
      direction="column"
      w="100%"
      justify="center"
      align="center"
    >
      <IconError404 size={200} stroke={1} />

      <Title>Oops, it seems that this page does not exist...</Title>

      <Text size="xl">
        <Anchor href="/">Click here</Anchor> to go back to the home page
      </Text>
    </Flex>
  );
}
