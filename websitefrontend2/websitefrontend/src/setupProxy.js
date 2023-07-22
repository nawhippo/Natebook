const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = function (app) {
  app.use(
    '/api', // Specify the base path you want to proxy
    createProxyMiddleware({
      target: 'http://localhost:8081', // Specify the target URL of your Spring Boot backend
      changeOrigin: true,
    })
  );
};