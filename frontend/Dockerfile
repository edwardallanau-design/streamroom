# Build stage
FROM node:20-alpine AS builder

WORKDIR /app

# Copy package files
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy source code
COPY . .

# Vite bakes VITE_* vars into the JS bundle at build time.
# Pass them as build args so they're available when npm run build runs.
ARG VITE_TWITCH_CHANNEL=piggyplaysph
ARG VITE_API_BASE_URL=/api
ENV VITE_TWITCH_CHANNEL=$VITE_TWITCH_CHANNEL
ENV VITE_API_BASE_URL=$VITE_API_BASE_URL

# Build the application
RUN npm run build

# Runtime stage
FROM nginx:alpine

# Remove default nginx config
RUN rm /etc/nginx/conf.d/default.conf

# Copy custom nginx configuration to the main config
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Copy built application from builder to nginx html root
COPY --from=builder /app/dist /usr/share/nginx/html

# Make sure permissions are correct
RUN chmod -R 755 /usr/share/nginx/html

# Expose port 80
EXPOSE 80

# Start nginx in foreground
CMD ["nginx", "-g", "daemon off;"]
