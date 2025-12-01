# Classes Architecture

##Optional future class
- Class that checks if the centroid is staying in the same position within 'X' amount of time and the graph doesn't need to update. When it does move, we can erase visited standpoint in case it goes back to that area again.

 
 # Completed  
- ~~Class that handles checking each frame to find the largest Centroid - Part of Original Centroid Project:~~
	- ~~Will also return if there is no centroid found~~
- ~~Class that processes coordinates of the largest Centroid - Part of Original Centroid Project~~

- ~~Class that handles processing how many frames there are in a video~~
   - ~~Class that finds the frames per second of the video - These can be the same class~~

- ~~Class that chooses what the centroid color is~~ 

# Jar Command (Created by Tav for personal Jar)
- mvn clean compile assembly:single <!-- Create a single jar file -->
- mv target/centroid-finder-1.0-SNAPSHOT-jar-with-dependencies.jar videoprocessor.jar <!-- Renaming the jar file -->
- java -jar target/videoprocessor.jar videos/ensantina.mp4  output/output.csv 255,0,0  95 <!-- Executing -->

# Final Touches
- VideoProcessor class - Handles the CLI prompt and is the merging point for the ColorParser and PathHandler logic

- VideoFrameRunner class - Merging point for all other classes, takes information passed into VideoProcessor and enacts the logic from required classes to follow the salamander walking around

- PathHandler class - Validates that the input video path is correct and creates the output directory for the csv file, or if one is already there, creates the csv file to write the coordinates to using CsvLogger

- ColorParser class - Checks in the color inputted by the user into the CLI is valid and converts it into a Color object

- CsvLogger class - Writes the timestamp, x coord, y coord of the largest centroid in a frame to a csv

- CentroidPlotter class: Takes the csv file in output and converts it to a displayable chart using XChart

- FPSFinder - Finds the fps of the provided video, the amount of frames, and the accurate length of the video.

- CentroidFinderPerFrame - Finds all centroids of the provided color in a frame

- Centroid CoordsPerFram - Takes the provided centroids from CentroidFinder, finds the largest one, and returns its coordinates

# Known Issue

- Around the one minute mark, the camera position was adjusted and a part of the background was revealed. Due to it being roughly the same color as the salamander and larger, the program started tracking that instead, the camera was adjusted back and the program started tracking the salamander again. It happened again later on. This can be fixed by not moving the camera or ensuring that there is something surround the container that makes it a different color than the salamander.
