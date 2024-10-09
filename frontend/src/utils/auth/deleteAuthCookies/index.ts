import { deleteCookie } from 'cookies-next';
import { AuthTokens } from '../../../types';

export const deleteAuthCookies = () => {
  const keys: Array<keyof AuthTokens> = [
    'accessToken',
    'idToken',
    'refreshToken',
  ];

  keys.forEach((key) => {
    deleteCookie(`trade-hub-${key}`);
  });
};
