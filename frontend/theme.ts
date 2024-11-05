import { createTheme, Input, InputWrapper } from '@mantine/core';
import styles from './src/styles/input.module.css';

export const theme = createTheme({
  components: {
    InputWrapper: InputWrapper.extend({
      classNames: {
        label: styles.input_label,
      },
    }),
    Input: Input.extend({
      classNames: {
        input: styles.input_wrapper,
      },
    }),
  },
});
