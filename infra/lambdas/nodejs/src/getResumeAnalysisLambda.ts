import { APIGatewayProxyHandler, APIGatewayProxyResult } from "aws-lambda";
import { ApiError } from "./utils/ApiError";
import { dynamoDbClient } from "./clients/dynamoDb";
import { GetItemCommand, GetItemCommandInput } from "@aws-sdk/client-dynamodb";
import { marshall, unmarshall } from "@aws-sdk/util-dynamodb";
import { apiResponse } from "./utils/apiResponse";

export const handler: APIGatewayProxyHandler = async (event): Promise<APIGatewayProxyResult> => {
  try {
    const { TABLE_NAME } = process.env;
    if (!TABLE_NAME) throw new Error('TABLE_NAME is not defined');

    if (!event.queryStringParameters) throw new ApiError(400, 'Missing required query parameters')
    if (!event.pathParameters) throw new ApiError(400, 'Missing required path parameters')

    const { email } = event.queryStringParameters;
    const { analysisId } = event.pathParameters;

    if (!analysisId || !email) throw new ApiError(400, 'Missing required parameters')

    const input: GetItemCommandInput = {
      TableName: TABLE_NAME,
      Key: marshall({
        partitionKey: `USER#${email}`,
        sortKey: `ANALYSIS_ID#${analysisId}`
      })
    }

    const { Item } = await dynamoDbClient.send(new GetItemCommand(input));

    if (!Item) throw new ApiError(404, 'Analysis not found');

    return apiResponse(200, { resumeAnalysis: unmarshall(Item) })
  } catch (error) {
    console.error(error);

    if (error instanceof ApiError) return apiResponse(error.status, { message: error.message });

    return apiResponse(500, { message: (error as Error).message })
  }
}