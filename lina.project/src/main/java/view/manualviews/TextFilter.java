package view.manualviews;

import java.io.File;
import javax.swing.filechooser.FileFilter;
import util.FileUtil;

/**
 *
 * @author Wesllen Sousa Lima
 */
public class TextFilter extends FileFilter {

    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        String extension = FileUtil.getFileExtension(f);
        if (extension != null) {
            return extension.equals("arff")
                    || extension.equals("txt")
                    || extension.equals("csv");
        }

        return false;
    }

    @Override
    public String getDescription() {
        return "Only Files (.arff .txt .csv)";
    }

}
