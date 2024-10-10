import { Cluster, ContainerImage } from 'aws-cdk-lib/aws-ecs';
import { ApplicationLoadBalancedEc2Service } from 'aws-cdk-lib/aws-ecs-patterns';
import { Construct } from 'constructs';
import path = require('path');

interface Props {
  env: string;
  bucketName: string;
}

export class ResumeRefineApi extends Construct {
  constructor(scope: Construct, id: string, props: Props) {
    super(scope, id);

    const { env, bucketName } = props;
    const { OPEN_AI_API_KEY } = process.env;

    if (!OPEN_AI_API_KEY) throw new Error('OPEN_AI_API_KEY is not defined');

    const cluster = new Cluster(this, `ResumeRefineCluster-${env}`);

    new ApplicationLoadBalancedEc2Service(
      this,
      `ResumeRefineEc2Service-${env}`,
      {
        cluster,
        memoryLimitMiB: 1024,
        taskImageOptions: {
          image: ContainerImage.fromAsset(path.join(__dirname)),
          environment: {
            BUCKET_NAME: bucketName,
            OPEN_AI_API_KEY,
          },
          command: ['command'],
          entryPoint: ['entry', 'point'],
        },
        desiredCount: 2,
      }
    );
  }
}
