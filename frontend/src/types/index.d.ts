export interface ApiResponse<T> {
  error: string | null;
  message: string | null;
  data: T;
}

export interface AuthTokens {
  idToken: string;
  accessToken: string;
  refreshToken: string;
}
