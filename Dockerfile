FROM eclipse-temurin:21

# Setting up working directory
WORKDIR /app

# Default ENV
ENV VIDEO_DIR=/videos
ENV OUTPUT_DIR=/results
ENV JOBS_DIR=app/jobs
ENV JAR_PATH=/app/target/videoprocessor.jar

# Installing Curl and Node.js
RUN apt-get update && \
    apt-get install -y curl gnupg && \
    curl -fsSL https://deb.nodesource.com/setup_20.x | bash - && \
    apt-get install -y nodejs && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Copy the Node.js package files and installing dependencies
COPY server/package*.json ./server/
RUN cd server && npm install

# Copying remaining code from the server and JAR
COPY server ./server
COPY target/videoprocessor.jar ./target/videoprocessor.jar

# Expose backend port
EXPOSE 8080

WORKDIR /app/server

# Start the server
CMD ["npm", "run", "dev"]
