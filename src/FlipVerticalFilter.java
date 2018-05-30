
public class FlipVerticalFilter implements Filter {

	public void filter(PixelImage pi) {
	    
		Pixel[][] data = pi.getData();
		
        Pixel[] temp = new Pixel[data.length]; // This temporarily holds the row that needs to be flipped out
        for (int row = 0; row<data.length/2; row++){ // Working one row at a time and only do half the image!!!
            temp = data[(data.length) - row - 1]; // Collect the temp row from the 'other side' of the array
            data[data.length - row - 1] = data[row]; // Put the current row in the row on the 'other side' of the array
            data[row] = temp; // Now put the row from the other side in the current row
        }

	    pi.setData(data);
		
	}

}
