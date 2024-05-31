A utility that combines source code and an image to create a type of image that might be used on
the terminal or used as part of a larger murial project.

There are two modes that are of most interest in this project. The first is to print characters
to the terminal. Modern terminals have support for colours that we can use to colour the image.
The second mode is to generate an image file that could then be scaled and printed for display
on a wall.



# Processing Steps

[TODO]
Concept - the source image defines the size of the end result?
For a huge image (3m x 3m) we might need 5GB of memory to load this image into a BufferedImage (ChatGPT)
Build small modular parts until we are more sure about how they will integrate.
We might need to consider a tiling approach where we output parts of the image. Or make a smaller
image and then scale it up after. Especially a linear scaling algorithm. This would 
not reduce the image quality.


## Reader
Reads in the source code files in a deterministic way. For example, depth-first file walk.
If filtering of the license headers is required, this would be the point to complete this.
Lastly, the file is then streamed as a character array.

Input - Folder to read files from
Output - Stream of characters

# Matrix
The matrix stage takes the stream of characters and performs a simple layout. The layout will
be based on the desired width of the image in a real-world measurement and a font size.

Input - Image width
Input - Image height
Input - Font size
Output - Stream of characters with new line symbols

# Image Generation
At this stage, we have a stream of characters in the desired layout format.
We then need to take the source image and scale it to the correct size for the image width.
We then create a buffered image using the image width and the font size.
Then we read characters from the stream, work out the corresponding colour from the source image, 
and then print those characters to the buffered image in the correct position.
For the moment, we might want to assume a square image as that is simpler.

Input - Source image
Input - Image width
Input - Font size
Output - Generated image file