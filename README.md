# image-converter
Spark Cognition's Image Converter

This tool converts an Image from PNG, GIF and CMP formats to JPEG.

Building:
Ensure that you have gradle installed
Run 'build gradle' from the root directory
This will build a jar file under build/libs

Running:
java -jar build/libs/image-converter-0.5.jar <path to image file>
After conversion, file is stored in the same location as the original with the '.jpg' extension
