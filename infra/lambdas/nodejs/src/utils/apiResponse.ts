import { APIGatewayProxyResult } from "aws-lambda";

export const apiResponse = (status: number, data: Record<string, unknown>): APIGatewayProxyResult => ({
  statusCode: status,
  body: JSON.stringify(data),
  headers: {
    'Access-Control-Allow-Headers': 'Content-Type',
    'Access-Control-Allow-Origin': '*',
    'Access-Control-Allow-Methods': '*',
  },
})