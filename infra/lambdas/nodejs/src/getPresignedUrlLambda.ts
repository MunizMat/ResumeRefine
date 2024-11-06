import { APIGatewayProxyHandler, APIGatewayProxyResult } from 'aws-lambda'
import { apiResponse } from './utils/apiResponse';
import { ApiError } from './utils/ApiError';
import { v4 as uuidV4 } from 'uuid';
import { getSignedUrl } from '@aws-sdk/s3-request-presigner'
import { s3Client } from './clients/s3';
import { PutObjectCommand } from '@aws-sdk/client-s3';
import { listResumeAnalysisByIP } from './services/resume/listResumeAnalysisByIP';
import { isToday } from 'date-fns';

const MAX_REQUESTS_PER_DAY = 5;

export const handler: APIGatewayProxyHandler = async (event): Promise<APIGatewayProxyResult> => {
  try {
    console.log('Event: ', event);

    const { BUCKET_NAME } = process.env;
    if (!BUCKET_NAME) throw new Error('BUCKET_NAME is not defined');

    if (!event.queryStringParameters) throw new ApiError(400, 'Missing required query parameters');

    const { filename, email } = event.queryStringParameters;

    if (!filename) throw new ApiError(400, 'filename is required');
    if (!email) throw new ApiError(400, 'email is required');

    const userIp = event.requestContext.identity.sourceIp;

    const { success, items } = await listResumeAnalysisByIP({ ip: userIp });

    if (!success) throw new ApiError(500, 'failed to fetch resume analysis');

    const itemsCreatedToday = items.filter(({ createdAt }) => isToday(new Date(createdAt)))

    if (itemsCreatedToday.length >= MAX_REQUESTS_PER_DAY)
      throw new ApiError(429, "You've reached your daily limit for resume analysis");

    const objectId = uuidV4();

    console.log('Client Data: ', {
      objectId,
      email,
      filename
    });

    const command = new PutObjectCommand({
      Bucket: BUCKET_NAME,
      Key: `${email}/${objectId}/resume.pdf`,
      Metadata: {
        email,
        filename
      },
    })

    const url = await getSignedUrl(s3Client, command, { expiresIn: 60 * 10 });

    return apiResponse(200, { url })
  } catch (error) {
    console.error(error);

    if (error instanceof ApiError) return apiResponse(error.status, { message: error.message })

    return apiResponse(500, { message: (error as Error).message })
  }
}