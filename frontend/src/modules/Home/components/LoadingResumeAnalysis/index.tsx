import { DotLottiePlayer } from '@dotlottie/react-player';
import { Flex, Progress, Text } from '@mantine/core';
import { useEffect, useState } from 'react';

// const animationIds = [
//   '32c2fc26-a039-47c0-8715-577174ed7bae/mvebCFeLJY',
//   '8fa7e17e-be36-43c1-9cbc-a158f96154f0/FurgQOnu6j',
//   'cc2a2fba-5c68-4076-995c-62bb81e80f21/VqL8HFLoj2',
// ];

export const LoadingResumeAnalysis = () => {
  const [value, setValue] = useState<number>(0);

  useEffect(() => {
    const interval = setInterval(() => {
      setValue((prev) => (prev > 70 ? prev : prev + 10));
    }, 2000);

    return () => {
      clearInterval(interval);
    };
  }, []);

  return (
    <Flex p={16} justify="center" direction="column" align="center">
      <Text c="white" fz={30}>
        Your resume is being analysed...
      </Text>

      <DotLottiePlayer
        autoplay
        loop
        src={`https://lottie.host/8fa7e17e-be36-43c1-9cbc-a158f96154f0/FurgQOnu6j.json`}
        className=""
      />

      <Progress
        animated
        value={value}
        color="violet.9"
        mt={60}
        w="80%"
        size="lg"
      />
    </Flex>
  );
};
