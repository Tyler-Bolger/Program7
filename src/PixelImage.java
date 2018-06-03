import java.awt.image.*;

/**
 * Provides an interface to a picture as an array of Pixels
 */
public class PixelImage
{
  private BufferedImage myImage;
  private int width;
  private int height;

  /**
   * Map this PixelImage to a real image
   * @param bi The image
   */
  public PixelImage(BufferedImage bi)
  {
    // initialise instance variables
    this.myImage = bi;
    this.width = bi.getWidth();
    this.height = bi.getHeight();
  }

  /**
   * Return the width of the image
   */
  public int getWidth()
  {
    return this.width;
  }

  /**
   * Return the height of the image
   */
  public int getHeight()
  {
    return this.height;
  }

  /**
   * Return the BufferedImage of this PixelImage
   */
  public BufferedImage getImage()
  {
    return this.myImage;
  }

  /**
   * Return the image's pixel data as an array of Pixels.  The
   * first coordinate is the x-coordinate, so the size of the
   * array is [width][height], where width and height are the
   * dimensions of the array
   * @return The array of pixels
   */
  public Pixel[][] getData()
  {
    Raster r = this.myImage.getRaster();
    Pixel[][] data = new Pixel[r.getHeight()][r.getWidth()];
    int[] samples = new int[3];

    for (int row = 0; row < r.getHeight(); row++)
    {
      for (int col = 0; col < r.getWidth(); col++)
      {
        samples = r.getPixel(col, row, samples);
        Pixel newPixel = new Pixel(samples[0], samples[1], samples[2]);
        data[row][col] = newPixel;
      }
    }

    return data;
  }

  /**
   * Set the image's pixel data from an array.  This array matches
   * that returned by getData().  It is an error to pass in an
   * array that does not match the image's dimensions or that
   * has pixels with invalid values (not 0-255)
   * @param data The array to pull from
   */
  public void setData(Pixel[][] data)
  {
    int[] pixelValues = new int[3];     // a temporary array to hold r,g,b values
    WritableRaster wr = this.myImage.getRaster();

    if (data.length != wr.getHeight())
    {
      throw new IllegalArgumentException("Array size does not match");
    }
    else if (data[0].length != wr.getWidth())
    {
      throw new IllegalArgumentException("Array size does not match");
    }

    for (int row = 0; row < wr.getHeight(); row++)
    {
      for (int col = 0; col < wr.getWidth(); col++)
      {
        pixelValues[0] = data[row][col].red;
        pixelValues[1] = data[row][col].green;
        pixelValues[2] = data[row][col].blue;
        wr.setPixel(col, row, pixelValues);
      }
    }
  }

  // add a method to compute a new image given weighted averages

  /** Generic method for 3x3 transformations
   * Working for Gaussian, Unsharp. not working for Laplace or Edgy. (???)
   * @param weight0 weight for center pixel RGB values
   * @param weight1 weight for edge RGB values
   * @param weight2 weight for corner RGB values
   */
  public void transformImage(int weight0, int weight1, int weight2) {
    Pixel[][] data = this.getData();
    int rows = data.length;
    int cols = data[0].length;

    for (int row = 1; row < rows - 1; row++) {
      for (int col = 1; col < cols - 1; col++) {
        // get center values
        int centerRed = data[row][col].red;
        int centerGreen = data[row][col].green;
        int centerBlue = data[row][col].blue;

        // get corner values
        int cornersRed = data[row-1][col-1].red +
                data[row-1][col+1].red +
                data[row+1][col-1].red +
                data[row+1][col+1].red;
        int cornersGreen = data[row-1][col-1].green +
                data[row-1][col+1].green +
                data[row+1][col-1].green +
                data[row+1][col+1].green;
        int cornersBlue = data[row-1][col-1].blue +
                data[row-1][col+1].blue +
                data[row+1][col-1].blue +
                data[row+1][col+1].blue;

        // get edge values
        int edgesRed = data[row-1][col].red +
                data[row][col-1].red +
                data[row+1][col].red +
                data[row][col+1].red;
        int edgesGreen = data[row-1][col].green +
                data[row][col-1].green +
                data[row+1][col].green +
                data[row][col+1].green;
        int edgesBlue = data[row-1][col].blue +
                data[row][col-1].blue +
                data[row+1][col].blue +
                data[row][col+1].blue;

        // get divisor for weighted average calculations
        int totalWeight = weight0 + weight1 * 4 + weight2 * 4;
        if (totalWeight == 0) {
          totalWeight = 1;
        }

        // get weighted averages
        int newRed = ((centerRed * weight0) + (edgesRed * weight1) + (cornersRed * weight2)) / totalWeight;
        int newGreen = ((centerGreen * weight0) + (edgesGreen * weight1) + (cornersGreen * weight2)) / totalWeight;
        int newBlue = ((centerBlue * weight0) + (edgesBlue * weight1) + (cornersBlue * weight2)) / totalWeight;

        // check color values for validity
        if (newRed < 0) {
          newRed = 0;
        } else if (newRed > 255) {
          newRed = 255;
        }
        if (newGreen < 0) {
          newGreen = 0;
        } else if (newGreen > 255) {
          newGreen = 255;
        }
        if (newBlue < 0) {
          newBlue = 0;
        } else if (newBlue > 255) {
          newBlue = 255;
        }

        data[row][col].red = newRed;
        data[row][col].green = newGreen;
        data[row][col].blue = newBlue;
      }
    }

    this.setData(data);
  }

}
