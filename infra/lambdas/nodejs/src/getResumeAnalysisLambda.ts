import { APIGatewayProxyHandler, APIGatewayProxyResult } from "aws-lambda";
import { ApiError } from "./utils/ApiError";
import { dynamoDbClient } from "./clients/dynamoDb";
import { QueryCommand, QueryCommandInput } from "@aws-sdk/client-dynamodb";
import { marshall, unmarshall } from "@aws-sdk/util-dynamodb";
import { apiResponse } from "./utils/apiResponse";

export const handler: APIGatewayProxyHandler = async (event): Promise<APIGatewayProxyResult> => {
  try {
    const { TABLE_NAME } = process.env;
    if (!TABLE_NAME) throw new Error('TABLE_NAME is not defined');

    if (!event.pathParameters) throw new ApiError(400, 'Missing required path parameters')

    const { analysisId } = event.pathParameters;

    if (!analysisId) throw new ApiError(400, 'Missing required parameters')

    const input: QueryCommandInput = {
      TableName: TABLE_NAME,
      IndexName: 'sortKey-partitionKey-gsi',
      KeyConditionExpression: '#sortKey = :sortKey and begins_with(#partitionKey, :partitionKey)',
      ExpressionAttributeNames: {
        '#partitionKey': 'partitionKey',
        '#sortKey': 'sortKey'
      },
      ExpressionAttributeValues: marshall({
        ':sortKey': `ANALYSIS_ID#${analysisId}`,
        ':partitionKey': 'USER_IP#'
      })
    }

    const { Items = [] } = await dynamoDbClient.send(new QueryCommand(input));

    const item = Items[0]

    if (!item) throw new ApiError(404, 'Analysis not found');

    return apiResponse(200, { resumeAnalysis: unmarshall(item) })
  } catch (error) {
    console.error(error);

    if (error instanceof ApiError) return apiResponse(error.status, { message: error.message });

    return apiResponse(500, { message: (error as Error).message })
  }
}