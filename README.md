[English](#english) | [Português](#portugues)

<a name="english"></a>

# ResumeRefine

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

> An AI-powered tool that automatically evaluates uploaded resumes and delivers insightful feedback.
>
> **Live Demo:** [resume-refine.com](https://resume-refine.com)

<img src="https://github.com/user-attachments/assets/e5ad51b4-8f2a-488f-93d2-4cad24462e86" width="500" />

## Table of Contents

*   [About The Project](#about-the-project)
    *   [Built With](#built-with)
*   [Getting Started](#getting-started)
    *   [Prerequisites](#prerequisites)
    *   [Installation](#installation)
    *   [Usage](#usage)

---

## About The Project

This project is an AI-powered resume analysis tool designed to automatically evaluate resumes and provide users with actionable feedback on areas such as keyword relevance, formatting consistency, and clarity of skill presentation. It aims to streamline the initial screening process often faced by applicants and recruiters by offering quick, automated insights.

The system features a robust and scalable backend architecture built on AWS:

*   **Event-Driven Processing:** Leverages AWS services for an asynchronous workflow. Resume uploads to an S3 bucket trigger an event, sending a message to an SQS queue. An AWS Lambda function (written in Java) then processes the resume from the queue, ensuring the system can handle uploads efficiently without blocking user interaction.
*   **Resource Management & Cost Control:** Implements IP-based rate limiting using DynamoDB. By storing request metadata (like IP address and timestamp), the system can effectively limit requests from individual sources via API Gateway, preventing potential abuse, managing load on backend resources, and minimizing operational costs.
*   **Infrastructure as Code:** The AWS infrastructure (Lambda, SQS, S3, DynamoDB, API Gateway) is defined and deployed using the AWS Cloud Development Kit (CDK) with Java, promoting consistency and repeatability.

This project demonstrates the practical application of serverless computing, event-driven architectures, and cloud-native services (specifically within the AWS ecosystem) to build an efficient, scalable, and cost-effective analysis tool.

### Built With

*   [Java](https://www.java.com/en/)
*   [Node.js](https://nodejs.org/) 
*   [AWS](https://aws.amazon.com/) (Lambda, SQS, S3, DynamoDB, API Gateway)
*   [CDK](https://aws.amazon.com/cdk/)
*   [Next.js](https://nextjs.org/)
*   [Maven](https://maven.apache.org/)
*   [Yarn](https://yarnpkg.com/)

---

## Getting Started

Follow these steps to get a local copy up and running.

### Prerequisites

*   CDK CLI (v2.x+ recommended)
*   Node.js (v18+ recommended)
*   Yarn (v1.x+)
*   Java (JDK 17+ recommended)
*   Maven (v3.6+ recommended)
*   AWS Account *with configured credentials* (e.g., via `aws configure` or environment variables).

### Installation

1.  Clone the repo
    ```sh
    git clone https://github.com/MunizMat/ResumeRefine.git
    ```
2.  Navigate to the project directory
    ```sh
    cd ResumeRefine
    ```
3.  Run the installation script. This script handles installing frontend (Node.js) and backend (Java) dependencies and deploying the necessary AWS infrastructure via CDK.
    ```sh
    ./install.sh
    ```

### Usage

ResumeRefine is publicly available at [resume-refine.com](https://resume-refine.com), but if you prefer running it locally, you can use the following steps after installation:

1.  Find your deployed API Gateway endpoint URL. Check the **Outputs tab of the deployed CloudFormation stack** in the AWS Console, or **navigate to the API Gateway service** to find the invoke URL for your deployed stage.
2.  In the `frontend` directory, create or update your `.env` file with your API URL:
    ```sh
    NEXT_PUBLIC_API_KEY=https://<api_id>.execute-api.<aws_region>.amazonaws.com/<api_stage>
    ```
3.  Run the frontend application from the `frontend` directory:
    ```sh
    yarn dev
    ```
    Your local instance should now be running, typically at `http://localhost:3000`.

---
---

<a name="portugues"></a>

# ResumeRefine (Português)

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

> Uma ferramenta com IA que avalia automaticamente currículos enviados e fornece feedback perspicaz.
>
> **Demonstração ao Vivo:** [resume-refine.com](https://resume-refine.com)

<img src="https://github.com/user-attachments/assets/e5ad51b4-8f2a-488f-93d2-4cad24462e86" width="500" />

## Índice

*   [Sobre o Projeto](#sobre-o-projeto-pt)
    *   [Construído Com](#construido-com-pt)
*   [Começando](#comecando-pt)
    *   [Pré-requisitos](#pre-requisitos-pt)
    *   [Instalação](#instalacao-pt)
    *   [Uso](#uso-pt)

---

## Sobre o Projeto <a name="sobre-o-projeto-pt"></a>

Este projeto é uma ferramenta de análise de currículos baseada em IA, projetada para avaliar automaticamente currículos e fornecer aos usuários feedback acionável sobre áreas como relevância de palavras-chave, consistência da formatação e clareza na apresentação das habilidades. O objetivo é otimizar o processo de triagem inicial frequentemente enfrentado por candidatos e recrutadores, oferecendo insights rápidos e automatizados.

O sistema possui uma arquitetura de backend robusta e escalável construída na AWS:

*   **Processamento Orientado a Eventos:** Utiliza serviços da AWS para um fluxo de trabalho assíncrono. O upload de currículos para um bucket S3 dispara um evento, enviando uma mensagem para uma fila SQS. Uma função AWS Lambda (escrita em Java) processa então o currículo da fila, garantindo que o sistema possa lidar com uploads eficientemente sem bloquear a interação do usuário.
*   **Gerenciamento de Recursos e Controle de Custos:** Implementa limitação de taxa (rate limiting) baseada em IP usando DynamoDB. Ao armazenar metadados de requisição (como endereço IP e timestamp), o sistema pode limitar efetivamente as requisições de fontes individuais via API Gateway, prevenindo abuso potencial, gerenciando a carga nos recursos de backend e minimizando custos operacionais.
*   **Infraestrutura como Código:** A infraestrutura AWS (Lambda, SQS, S3, DynamoDB, API Gateway) é definida e implantada usando o AWS Cloud Development Kit (CDK) com Java, promovendo consistência e repetibilidade.

Este projeto demonstra a aplicação prática de computação sem servidor (serverless), arquiteturas orientadas a eventos e serviços nativos da nuvem (especificamente dentro do ecossistema AWS) para construir uma ferramenta de análise eficiente, escalável e com custo otimizado.

### Construído Com <a name="construido-com-pt"></a>

*   [Java](https://www.java.com/en/)
*   [Node.js](https://nodejs.org/) 
*   [AWS](https://aws.amazon.com/) 
*   [CDK](https://aws.amazon.com/cdk/) 
*   [Next.js](https://nextjs.org/)
*   [Maven](https://maven.apache.org/) 
*   [Yarn](https://yarnpkg.com/) 

---

## Começando <a name="comecando-pt"></a>

Siga estes passos para obter uma cópia local em execução.

### Pré-requisitos <a name="pre-requisitos-pt"></a>

*   CDK CLI (v2.x+ recomendado)
*   Node.js (v18+ recomendado)
*   Yarn (v1.x+)
*   Java (JDK 17+ recomendado)
*   Maven (v3.6+ recomendado)
*   Conta AWS *com credenciais configuradas* (ex: via `aws configure` ou variáveis de ambiente).

### Instalação <a name="instalacao-pt"></a>

1.  Clone o repositório
    ```sh
    git clone https://github.com/MunizMat/ResumeRefine.git
    ```
2.  Navegue até o diretório do projeto
    ```sh
    cd ResumeRefine
    ```
3.  Execute o script de instalação. Este script cuida da instalação das dependências do frontend (Node.js) e backend (Java) e implanta a infraestrutura AWS necessária via CDK.
    ```sh
    ./install.sh
    ```

### Uso <a name="uso-pt"></a>

O ResumeRefine está publicamente disponível em [resume-refine.com](https://resume-refine.com), mas se preferir executá-lo localmente, você pode usar os seguintes passos após a instalação:

1.  Encontre a URL do endpoint do seu API Gateway implantado. Verifique a **aba Outputs da stack do CloudFormation implantada** no Console da AWS, ou **navegue até o serviço API Gateway** para encontrar a URL de invocação (invoke URL) do seu stage implantado.
2.  No diretório `frontend`, crie ou atualize seu arquivo `.env` com a sua URL da API:
    ```sh
    NEXT_PUBLIC_API_KEY=https://<api_id>.execute-api.<aws_region>.amazonaws.com/<api_stage>
    ```
3.  Execute a aplicação frontend a partir do diretório `frontend`:
    ```sh
    yarn dev
    ```
    Sua instância local deve agora estar rodando, tipicamente em `http://localhost:3000`.