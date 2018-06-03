public class EdgyFilter implements Filter {
    public void filter(PixelImage pi) {
        pi.transformImage(9, -1, -1);
    }
}

