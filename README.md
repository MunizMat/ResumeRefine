- # ResumeRefine

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

> An AI-powered tool that automatically evaluates uploaded resumes and delivers insightful feedback.
> 
**Live Demo:** [resume-refine.com](https://resume-refine.com)

<img src="https://github.com/user-attachments/assets/e5ad51b4-8f2a-488f-93d2-4cad24462e86" width="500" />

## Table of Contents 

*   [About The Project](#about-the-project)
    *   [Built With](#built-with)
*   [Getting Started](#getting-started)
    *   [Prerequisites](#prerequisites)
    *   [Installation](#installation)

---

## About The Project
This project is an AI-powered resume analysis tool designed to automatically evaluate resumes and provide users with actionable feedback. It aims to streamline the initial screening process often faced by applicants and recruiters by offering quick, automated insights.

The system features a robust and scalable backend architecture built on AWS:

*   **Event-Driven Processing:** Leverages AWS services for an asynchronous workflow. Resume uploads to an S3 bucket trigger an event, sending a message to an SQS queue. An AWS Lambda function (written in Java) then processes the resume from the queue, ensuring the system can handle uploads efficiently without blocking user interaction.
*   **Resource Management & Cost Control:** Implements IP-based rate limiting using DynamoDB. By storing request metadata (like IP address and timestamp), the system can effectively limit requests from individual sources via API Gateway, preventing potential abuse, managing load on backend resources, and minimizing operational costs.
*   **Infrastructure as Code:** The AWS infrastructure (Lambda, SQS, S3, DynamoDB, API Gateway) is defined and deployed using the AWS Cloud Development Kit (CDK) with Java, promoting consistency and repeatability.

This project demonstrates the practical application of serverless computing, event-driven architectures, and cloud-native services (specifically within the AWS ecosystem) to build an efficient, scalable, and cost-effective analysis tool.

### Built With
*   [Java](https://www.java.com/en/)
*   [Node.js](https://nodejs.org/)
*   [AWS](https://aws.amazon.com/)
*   [CDK](https://aws.amazon.com/cdk/)
*   [Next.js](https://nextjs.org/)

---

## Getting Started
### Prerequisites
*   CDK CLI
*   Node.js
*   Yarn
*   Java
*   Maven

### Installation

1.  Clone the repo
    ```sh
    git clone https://github.com/MunizMat/ResumeRefine.git
    ```
2. Navigate to the NodeJS lambdas directory, inside the infra folder
    ```sh
    cd ResumeRefine/infra/lambdas/nodejs
    ```
3. Install the dependencies
    ```sh
    yarn install
    ```
4. Navigate back to the infra folder
    ```sh
    cd ../..
    ```
5. Build the maven project
    ```sh
    mvn clean package
    ```
6. Navigate to the infrastructure folder
    ```sh
    cd infrastructure
    ```
7. Deploy the CDK project
    ```sh
    cdk deploy
    ``