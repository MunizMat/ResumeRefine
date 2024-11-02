/* --------------- External ------------- */
import { Flex, Text, Title } from '@mantine/core';

/* --------------- Styles ------------- */
import styles from './Home.module.css';
import { ResumeUploadBox } from './components/ResumeUploadBox';
import { DotLottiePlayer } from '@dotlottie/react-player';

export const HomeModule = () => {
  return (
    <Flex className={styles.container}>
      <Flex direction="column">
        <Title className={styles.title}>ResumeRefine</Title>

        <Text className={styles.slogan}>
          Polish Your Resume, Shine in Your Career
        </Text>

        <ResumeUploadBox />
      </Flex>

      <Flex h="100%" justify="center" align="center">
        <DotLottiePlayer
          autoplay
          loop
          className={styles.lottie_player}
          src="https://lottie.host/32c2fc26-a039-47c0-8715-577174ed7bae/mvebCFeLJY.json"
        />
      </Flex>
    </Flex>
  );
};
