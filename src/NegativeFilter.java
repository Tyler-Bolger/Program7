
public class NegativeFilter implements Filter {
	
	public void filter(PixelImage pi) {
	
		Pixel[][] data = pi.getData();
		
		for(Pixel[] p : data) {
			for(Pixel rgb: p) {
				rgb.red = 255 - rgb.red;
				rgb.blue = 255 - rgb.blue;
				rgb.green = 255 - rgb.green;
			}
		}

		pi.setData(data);
	
	}
	
}
