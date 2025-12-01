# Docker Plan: Centroid Finder Backend

## Goal

The goal of this project is to create and publish a Docker image for the centroid-finder backend. This image should support both Node.js and Java, and work out-of-the-box with minimal configuration. The image will be hosted on GitHub Container Registry and must support video and result volume mounting.

The image should work with this command:

```bash
docker run \
  -p 3000:3000 \
  -v "$VIDEO_DIRECTORY:/videos" \
  -v "$RESULTS_DIRECTORY:/results" \
  ghcr.io/$GITHUB_USER/salamander:latest
```

# Docker Plan: Centroid Finder Backend


---

## Base Image Strategy

We will use a **multi-stage Docker build**:

1. **Builder Stage**:

   * Base: `node:20-bullseye` (includes Debian + Node)
   * Install: OpenJDK 17 for Java
   * Build Java JAR and install Node dependencies

2. **Runtime Stage**:

   * Base: `node:20-slim`
   * Install: OpenJDK
   * Copy built JAR and compiled Node files from builder
   * Keep image size minimal

This avoids shipping dev tools in production and helps with layer caching.

---

## Making Node and Java Available

* We'll copy the JAR from the backend and the Node project files.

* Using `npm i` or `npm install` to get our node dependencies
* Using Java to execute the batch JAR file from the API.

---

## Testing the Docker Image

We'll test the image using:

```bash
docker build -t salamander-dev .
docker run -p 3000:3000 \
  -v "$(pwd)/videos:/videos" \
  -v "$(pwd)/results:/results" \
  -e VIDEOS_DIR=/app/videos \
  -e RESULTS_DIR=/app/results \
  salamander-dev
```

We'll validate:

* `/api/videos` endpoint returns files
* `/thumbnail/:filename` successfully runs
* `/api/binarize-thumbnail` works with RGB + threshold inputs

---

## Exposing Endpoints to Host

To allow access to the Express API from the host machine:

* Use `EXPOSE 3000` in Dockerfile
* We'll expose port 3000 in the Dockerfile and bind it to 3000 or another port (like 3001) on the host.

---

## Accessing Videos and Results

We'll mount the host's video and result directories as Docker volumes:

* `/videos` → `$VIDEO_DIRECTORY`
* `/results` → `$RESULTS_DIRECTORY`

These are accessed in the code via `process.env.VIDEO_DIR` and `process.env.OUTPUT_DIR`, which are configured in `.env` or via Docker `ENV`.

---

## Keeping the Image Small and Fast

* Use a multi-stage build to separate compile and runtime layers
* Use a `.dockerignore` file to exclude `node_modules`, `videos`, `results`, and other unnecessary files
* Copy only needed files into the final image
* Cache `npm install` and `mvn package` layers by copying package files separately before the rest of the code

---

## Diagram

```
[ Local Code ]
     │
     ▼
[ Builder Image: node + openjdk ]
 - npm install
 - mvn package
 - compile jar

     ▼

[ Runtime Image: node + openjdk (slim) ]
 - COPY dist/, app.jar
 - EXPOSE 3000
 - CMD ["node", "index.js"]
```

---

## Summary

We’ll build a Docker image using a multi-stage setup. The final image will be small, production-ready, and able to run Node.js and Java-based workloads while exposing endpoints and processing mounted video files.
