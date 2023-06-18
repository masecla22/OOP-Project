package nl.rug.oop.rts.util.file;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * File filter which only allows JSON files to go through.
 */
public class MapSaveJsonFilter extends FileFilter {

    @Override
    public boolean accept(File file) {
        return file.isDirectory() || file.getName().endsWith(".json");
    }

    @Override
    public String getDescription() {
        return "JSON files (*.json)";
    }
}
