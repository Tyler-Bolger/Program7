public class GaussianFilter implements Filter {
    public void filter(PixelImage pi) {
        pi.transformImage(4, 2, 1);
    }
}

