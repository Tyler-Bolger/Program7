public class UnsharpFilter implements Filter {
    public void filter(PixelImage pi) {
        pi.transformImage(28, -2, -1);
    }
}

