package miniRPG.ui.asset;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;

public final class AssetLoader {

    private final Class<?> anchor;

    public AssetLoader(Class<?> anchor) {
        this.anchor = anchor;
    }

    public Image loadRaw(String path) {
        try (InputStream is = anchor.getResourceAsStream(path)) {
            if (is == null) return null;
            return ImageIO.read(is);
        } catch (IOException e) {
            return null;
        }
    }

    public Image loadScaled(String path, int w, int h, int scaleHint) {
        Image raw = loadRaw(path);
        if (raw == null) return null;
        return raw.getScaledInstance(w, h, scaleHint);
    }
}
