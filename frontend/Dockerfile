# Base image
FROM node:14-alpine

# Set working directory
WORKDIR /app

# Copy package.json and package-lock.json
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy project files
COPY . .

# Build the React app
RUN npm run build

# Set environment variables
ENV PORT=3500
ENV HOST=0.0.0.0

# Expose the port
EXPOSE 3500

# Start the React app
CMD ["npm", "run", "start"]
