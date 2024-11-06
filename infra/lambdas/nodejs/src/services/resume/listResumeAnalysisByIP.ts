import { QueryCommand, QueryCommandInput } from "@aws-sdk/client-dynamodb";
import { dynamoDbClient } from "../../clients/dynamoDb";
import { marshall, unmarshall } from "@aws-sdk/util-dynamodb";
import { ResumeAnalysis } from "../../interfaces/resume";

interface ListResumeAnalysisByIPInput {
  ip: string;
}

interface ListResumeAnalysisByIPOuput {
  success: boolean;
  items: ResumeAnalysis[];
}

export const listResumeAnalysisByIP = async ({ ip }: ListResumeAnalysisByIPInput): Promise<ListResumeAnalysisByIPOuput> => {
  try {
    const { TABLE_NAME } = process.env;
    if (!TABLE_NAME) throw new Error('TABLE_NAME is not defined');

    const input: QueryCommandInput = {
      TableName: TABLE_NAME,
      KeyConditionExpression: '#partitionKey = :partitionKey and begins_with(#sortKey, :sortKey)',
      ExpressionAttributeNames: {
        '#partitionKey': 'partitionKey',
        '#sortKey': 'sortKey',
      },
      ExpressionAttributeValues: marshall({
        ':partitionKey': `USER_IP#${ip}`,
        ':sortKey': 'ANALYSIS_ID#'
      })
    }

    const { Items = [] } = await dynamoDbClient.send(new QueryCommand(input))

    return { success: true, items: Items.map(item => unmarshall(item) as ResumeAnalysis) }
  } catch (error) {
    console.error(error);

    return { success: false, items: [] }
  }
}