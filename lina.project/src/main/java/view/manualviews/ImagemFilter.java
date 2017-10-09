package view.manualviews;

import java.io.File;
import javax.swing.filechooser.FileFilter;
import util.FileUtil;

/**
 *
 * @author Wesllen Sousa Lima
 */
public class ImagemFilter extends FileFilter {

    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        String extension = FileUtil.getFileExtension(f);
        if (extension != null) {
            return extension.equals("tiff")
                    || extension.equals("tif")
                    || extension.equals("gif")
                    || extension.equals("jpeg")
                    || extension.equals("jpg")
                    || extension.equals("bmp")
                    || extension.equals("png");
        }

        return false;
    }

    @Override
    public String getDescription() {
        return "Only Images";
    }

}
