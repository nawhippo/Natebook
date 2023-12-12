# Stage 1: Build the React application
FROM node:16-alpine as build

# Set the working directory in the Docker container
WORKDIR /app

# Copy the package.json and package-lock.json (or yarn.lock) from your project into the Docker container
COPY package.json package-lock.json ./

# Install the dependencies in the container
RUN npm install

# Copy the rest of your frontend code into the Docker container
COPY . ./

# Build the React application
RUN npm run build

# Stage 2: Serve the React application from Nginx
FROM nginx:alpine

# Copy the build output from the first stage into the Nginx container
COPY --from=build /app/build /usr/share/nginx/html

# Expose port 80 to the Docker host, so we can access the container
EXPOSE 80

# Start Nginx and serve the React application
CMD ["nginx", "-g", "daemon off;"]