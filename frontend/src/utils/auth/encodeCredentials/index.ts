interface Credentials {
  username: string;
  password: string;
}

export const encodeCredentials = ({ username, password }: Credentials) =>
  Buffer.from(`${username}:${password}`).toString('base64');
