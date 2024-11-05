import { createTheme, Input, InputWrapper } from '@mantine/core';
import styles from './src/styles/input.module.css';

export const theme = createTheme({
  primaryColor: 'green',
  colors: {
    green: [
      '#58CC5F',
      '#58CC5F',
      '#58CC5F',
      '#58CC5F',
      '#58CC5F',
      '#58CC5F',
      '#58CC5F',
      '#58CC5F',
      '#58CC5F',
      '#58CC5F',
    ],
  },
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
