#! /bin/bash

cd infra/lambdas/nodejs
yarn install

# Navigate back to the infra folder
cd ../..

# Build the maven project
mvn clean package

# Navigate to the frontend directory
cd ../frontend

# Create .env file
echo "NEXT_PUBLIC_API_URL=" > .env

# Install frontend dependencies
yarn install

#Navigate to the infrastructure folder
cd ../infra/infrastructure

# Deploy the CDK project
cdk deploy