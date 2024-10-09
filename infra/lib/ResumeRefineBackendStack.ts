import { Stack, StackProps } from 'aws-cdk-lib';
import { Construct } from 'constructs';
import { ResumeRefineMainBucket } from './constructs/ResumeRefineMainBucket';

interface Props extends StackProps {
  environment: string;
}

export class ResumeRefineBackendStack extends Stack {
  readonly mainBucket: ResumeRefineMainBucket;

  constructor(scope: Construct, id: string, props: Props) {
    super(scope, id, props);

    const { environment } = props;

    this.mainBucket = new ResumeRefineMainBucket(
      this,
      `ResumeRefineMainBucket-${environment}`,
      {
        bucketName: `resume-refine-main-bucket-${environment.toLowerCase()}`,
        env: environment,
      }
    );
  }
}
