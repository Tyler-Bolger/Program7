public class LaplaceFilter implements Filter {
    public void filter(PixelImage pi) {
        pi.transformImage(8, -1, -1);
    }
}

