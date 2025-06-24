/* ----------- External ----------- */
import Head from 'next/head';
import { MantineProvider } from '@mantine/core';
import { GoogleAnalytics } from '@next/third-parties/google';

/* ----------- Theme ----------- */
import { theme } from '../theme';

/* ----------- Assets ----------- */
import '../index.css';
import '@mantine/core/styles.css';
import '@mantine/charts/styles.css';
import '@mantine/dropzone/styles.css';
import '@mantine/notifications/styles.css';
import { Notifications } from '@mantine/notifications';

export default function App({ Component, pageProps }: any) {
  return (
    <MantineProvider theme={theme}>
      <Head>
        <title>ResumeRefine</title>
        <meta
          name="viewport"
          content="minimum-scale=1, initial-scale=1, width=device-width, user-scalable=no"
        />
        <link rel="shortcut icon" href="/favicon.ico" />
      </Head>

      <Notifications />

      <GoogleAnalytics gaId={process.env.NEXT_PUBLIC_GA_ID ?? ''} />

      <Component {...pageProps} />
    </MantineProvider>
  );
}
