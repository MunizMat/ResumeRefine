import { APIGatewayProxyHandler, APIGatewayProxyResult } from 'aws-lambda'
import { apiResponse } from './utils/apiResponse';
import { ApiError } from './utils/ApiError';
import { v4 as uuidV4 } from 'uuid';
import { getSignedUrl } from '@aws-sdk/s3-request-presigner'
import { s3Client } from './clients/s3';
import { ObjectCannedACL, PutObjectCommand } from '@aws-sdk/client-s3';

export const handler: APIGatewayProxyHandler = async (event): Promise<APIGatewayProxyResult> => {
  try {
    const { BUCKET_NAME } = process.env;
    if (!BUCKET_NAME) throw new Error('BUCKET_NAME is not defined');

    if (!event.queryStringParameters) throw new ApiError(400, 'Missing required query parameters');

    const { filename, email } = event.queryStringParameters;

    if (!filename) throw new ApiError(400, 'filename is required');
    if (!email) throw new ApiError(400, 'email is required');

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
      ACL: ObjectCannedACL.public_read_write
    })

    const url = await getSignedUrl(s3Client, command, { expiresIn: 60 * 10 });

    return apiResponse(200, { url })
  } catch (error) {
    console.error(error);

    if (error instanceof ApiError) return apiResponse(error.status, { message: error.message })

    return apiResponse(500, { message: (error as Error).message })
  }
}