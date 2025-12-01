package io.github.TiaMarieG.centroidFinder;

import java.awt.Color;

public class ColorParser {

   /**
    * Parses a string into a Color object.
    * Accepts either "#RRGGBB" format or "R,G,B" format.
    *
    * @param input the string representing the color
    * @return a Color object or null if invalid
    */
   public static Color parse(String input) {
      try {
         if (input == null || input.isEmpty()) {
            System.err.println("Error: Color input is empty.");
            return null;
         }

         if (input.startsWith("#")) {
            return Color.decode(input); // parses hex like "#FF0000"
         } else {
            String[] parts = input.split(",");
            if (parts.length != 3) {
               System.err.println("Error: RGB color format must be 'R,G,B'.");
               return null;
            }

            int r = Integer.parseInt(parts[0].trim());
            int g = Integer.parseInt(parts[1].trim());
            int b = Integer.parseInt(parts[2].trim());

            if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255) {
               System.err.println("Error: RGB values must be between 0 and 255.");
               return null;
            }

            return new Color(r, g, b);
         }
      } catch (NumberFormatException e) {
         System.err.println("Error: Could not parse RGB components as integers.");
      } catch (Exception e) {
         System.err.println("Error: Invalid color input.");
      }

      return null;
   }
}
