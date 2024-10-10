#!/usr/bin/env node
import 'source-map-support/register';
import * as cdk from 'aws-cdk-lib';
// import { ResumeRefineBackendStack } from '../lib/ResumeRefineBackendStack';
import { ResumeRefineFrontendStack } from '../lib/ResumeRefineFrontendStack';

const app = new cdk.App();
const env = 'PROD';

// new ResumeRefineBackendStack(app, `ResumeRefineBackendStack-${env}`, {
//   environment: env,
// });

new ResumeRefineFrontendStack(app, `ResumeRefineFrontendStack-${env}`, {
  environment: env,
});
