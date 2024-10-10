import { Stack, StackProps } from 'aws-cdk-lib';
import { Construct } from 'constructs';
import { Nextjs } from 'cdk-nextjs-standalone';
import path = require('path');
import { config } from 'dotenv';
config();

interface Props extends StackProps {
  environment: string;
}

export class ResumeRefineFrontendStack extends Stack {
  constructor(scope: Construct, id: string, props: Props) {
    const { NEXT_PUBLIC_API_URL } = process.env;

    if (!NEXT_PUBLIC_API_URL)
      throw new Error('NEXT_PUBLIC_API_URL is not defined');

    super(scope, id, props);

    const { environment } = props;

    new Nextjs(this, `ResumeRefineNextjsApp-${environment}`, {
      nextjsPath: path.join(__dirname, '..', '..', 'frontend'),
      environment: {
        NEXT_PUBLIC_API_URL,
      },
    });
  }
}
