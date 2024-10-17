import { api } from '../api';
import { GetPresignedUrlInput, GetPresignedUrlOutput } from './types';

export const getPresignedUrl = async ({
  email,
  filename,
}: GetPresignedUrlInput) => {
  try {
    const { data } = await api.get<GetPresignedUrlOutput>('/presigned-url', {
      params: { filename, email },
    });

    return data;
  } catch (error) {
    console.error(error);
    throw error;
  }
};
