# ----------------------------------------------------
# STAGE 1: BUILDER - Compiles the Java project with Maven
# ----------------------------------------------------
FROM maven:3.9.5-eclipse-temurin-21 AS builder

# Set the working directory for the build environment
WORKDIR /build

# Copy the entire project code into the builder stage
COPY . /build/

# Package the Maven project. 
# We explicitly skip tests since they rely on external video files.
RUN cd processor && mvn clean package -DskipTests

# ----------------------------------------------------
# STAGE 2: FINAL - Sets up the runtime environment (Node.js/Server)
# ----------------------------------------------------
FROM eclipse-temurin:21

# Setting up working directory
WORKDIR /app

# Default ENV (These paths will be relative to /app)
ENV VIDEO_DIR=/videos
ENV OUTPUT_DIR=/results
ENV JOBS_DIR=/app/jobs
ENV JAR_PATH=/app/videoprocessor.jar

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

# Copying remaining code from the server
COPY server ./server

# CRITICAL STEP: Copy the built JAR from the 'builder' stage 
# The JAR is located at /build/processor/target/videoprocessor.jar
COPY --from=builder /build/processor/target/videoprocessor.jar videoprocessor.jar

# Expose backend port
EXPOSE 8080

WORKDIR /app/server

# Start the server (which will use the JAR_PATH env variable)
CMD ["npm", "run", "dev"]