package core;

public class JpegChecker {

    public boolean isJpeg(byte[] source) {
        return source[0] == -1 && source[1] == -40;
    }

}