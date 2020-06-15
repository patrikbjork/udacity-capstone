const protocol = window.location.protocol === 'https' ? 'wss' : 'ws';
const host = window.location.host;

export const environment = {
  production: true,
  webSocketBaseUrl: protocol + '://' + host
};
