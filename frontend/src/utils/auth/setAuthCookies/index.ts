import { setCookie } from 'cookies-next';
import { AuthTokens } from '../../../types';

export const setAuthCookies = (tokens: Partial<AuthTokens>) => {
  Object.entries(tokens).forEach(([tokenType, tokenValue]) => {
    if (tokenValue) setCookie(`trade-hub-${tokenType}`, tokenValue);
  });
};
