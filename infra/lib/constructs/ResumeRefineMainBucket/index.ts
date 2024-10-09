import { Duration, RemovalPolicy } from 'aws-cdk-lib';
import {
  Bucket,
  BucketAccessControl,
  HttpMethods,
  ObjectOwnership,
} from 'aws-cdk-lib/aws-s3';
import { Construct } from 'constructs';

interface Props {
  env: string;
  bucketName: string;
}

export class ResumeRefineMainBucket extends Construct {
  readonly bucket: Bucket;

  constructor(scope: Construct, id: string, props: Props) {
    super(scope, id);

    const { env, bucketName } = props;

    this.bucket = new Bucket(this, `ResumeRefineMainBucket-${env}`, {
      bucketName,
      cors: [
        {
          allowedMethods: [HttpMethods.GET, HttpMethods.POST, HttpMethods.PUT],
          allowedHeaders: ['*'],
          allowedOrigins: ['http://localhost:3000'],
        },
      ],
      autoDeleteObjects: true,
      removalPolicy: RemovalPolicy.DESTROY,
      objectOwnership: ObjectOwnership.BUCKET_OWNER_PREFERRED,
      accessControl: BucketAccessControl.PUBLIC_READ,
      publicReadAccess: true,
      blockPublicAccess: {
        blockPublicAcls: false,
        blockPublicPolicy: false,
        ignorePublicAcls: false,
        restrictPublicBuckets: false,
      },
      lifecycleRules: [
        {
          expiration: Duration.days(2),
        },
      ],
    });
  }
}
