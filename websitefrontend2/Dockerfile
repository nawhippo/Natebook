
FROM node:16-alpine as build-node


WORKDIR /app


COPY websitefrontend/package.json websitefrontend/package-lock.json ./


RUN npm install


COPY websitefrontend/ ./


RUN npm run build && ls -la build


RUN ls -la build


FROM nginx:alpine
WORKDIR /usr/share/nginx/html


COPY --from=build-node /app/build .


EXPOSE 80


CMD ["nginx", "-g", "daemon off;"]