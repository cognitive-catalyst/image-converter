package com.sparkcognition.imageconverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;

public class ImageConverter {

    public static final String ERROR_STRING = "ImageConverter has encountered an error: ";
    public static final String FORMAT_NAME_JPG = "JPG";

    private static void convertToJPG(File inFile, File outFile) {
        try {
            BufferedImage image = ImageIO.read(inFile);
            ImageIO.write(image, FORMAT_NAME_JPG, outFile);
        } catch (IOException e) {
            printError(e.getMessage());
        }
    }

    /**
     * Recursively walk a directory and convert all files with a particular extension to JPG.
     *
     * @param pathToRoot The root of the file tree where we want to start looking for files
     * @param extension The extension name of the images we wish to convert (i.e. ".cgm", ".png")
     * @throws IOException
     */
    public static void recursiveConvertCGMtoJPG(final String pathToRoot, final String extension) throws IOException {
        System.out.println("Starting...");
        Files.walkFileTree(Paths.get(pathToRoot), EnumSet.of(FileVisitOption.FOLLOW_LINKS), Integer.MAX_VALUE,
                new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        if (file.endsWith(extension)) {
                            String outFileName = file.toString().replace(extension, ".jpg");

                            File inFile = file.toFile();
                            File outFile = new File(outFileName);

                            if (outFile.exists()) {
                                boolean deleted = outFile.delete();

                                if (!deleted) {
                                    printError("File " + outFile.getCanonicalPath() + " could not be deleted. Conversion will still be attempted.");
                                }
                            }

                            convertToJPG(inFile, outFile);
                        }
                        return FileVisitResult.CONTINUE;
                    }
                });

    }

    private static void printError(String message) {
        System.err.println(ERROR_STRING + message);
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Insufficient number of arguments");
            System.exit(-1);
        }
        String base = System.getProperty("user.dir");
        File inFile = new File(args[0]);
        int extensionIndex = args[0].lastIndexOf(".");
        String outfilename = args[0].substring(0, extensionIndex) + ".jpg";
        System.out.println("Converting to file: " + outfilename);
        File outFile = new File(outfilename);
        convertToJPG(inFile, outFile);
    }
}
