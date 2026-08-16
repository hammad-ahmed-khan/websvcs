package oracle.retail.sim.client.swing.dialog;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.locale.Translator;

/********************************************************************************************************
 * Factory to help create file choosers and file filters.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class FileChooserFactory {

    /****************************************************************************************************
     * Private constructor.
     ***************************************************************************************************/
    private FileChooserFactory() {
    }

    /****************************************************************************************************
     * Creates a file chooser with a dialog title. The title is translated.
     * <p>
     * @param title The title.
     * @param startLocation The location to start choosing files from.
     *            <p>
     * @return A JFileChooser with the correct settings.
     ***************************************************************************************************/
    public static JFileChooser createFileChooser(String title, String startLocation) {
        JFileChooser chooser = new JFileChooser(startLocation);

        chooser.setLocale(LocaleManager.getLanguageLocale());
        chooser.setDialogTitle(Translator.getText(title));

        return chooser;
    }

    /****************************************************************************************************
     * Creates a java file chooser with a dialog title. The title is translated.
     * <p>
     * @param startLocation The location to start choosing files from.
     *            <p>
     * @return A JFileChooser with the correct settings.
     ***************************************************************************************************/
    public static JFileChooser createJavaFileChooser(String startLocation) {
        JFileChooser chooser = new JFileChooser(startLocation);

        chooser.setLocale(LocaleManager.getLanguageLocale());
        chooser.setDialogTitle(Translator.getText("Select Java File"));
        chooser.setFileFilter(createJavaFileFilter());

        return chooser;
    }

    /****************************************************************************************************
     * Creates a directory file chooser with a dialog title. The title is translated.
     * <p>
     * @param dialogTitle A title to display at the top of the dialog.
     *            <p>
     * @return A JFileChooser with the correct settings.
     ***************************************************************************************************/
    public static JFileChooser createDirectoryFileChooser() {
        JFileChooser chooser = new JFileChooser();

        chooser.setLocale(LocaleManager.getLanguageLocale());
        chooser.setDialogTitle(Translator.getText("Select Directory"));
        chooser.setFileFilter(createDirectoryFileFilter());
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        return chooser;
    }

    /****************************************************************************************************
     * Creates a new directory file filter.
     * <p>
     * @return An directory file filter.
     ***************************************************************************************************/
    public static FileFilter createDirectoryFileFilter() {
        FileChooserFactory factory = new FileChooserFactory();
        return factory.getDirectoryFileFilter();
    }

    /****************************************************************************************************
     * Creates a new java file filter.
     * <p>
     * @return An java file filter.
     ***************************************************************************************************/
    public static FileFilter createJavaFileFilter() {
        FileChooserFactory factory = new FileChooserFactory();
        return factory.getJavaFileFilter();
    }

    /****************************************************************************************************
     * Private instance method that returns a new directory file filter.
     ***************************************************************************************************/
    private FileFilter getDirectoryFileFilter() {
        return new DirectoryFileFilter();
    }

    /****************************************************************************************************
     * Private instance method that returns a new directory file filter.
     ***************************************************************************************************/
    private FileFilter getJavaFileFilter() {
        return new JavaFileFilter();
    }

    /****************************************************************************************************
     * Creates an xml file chooser with a dialog title. The title is translated.
     * <p>
     * @param dialogTitle A title to display at the top of the dialog.
     *            <p>
     * @return A JFileChooser with the correct settings.
     ***************************************************************************************************/
    public static JFileChooser createXMLFileChooser(String dialogTitle, String startLocation) {
        JFileChooser chooser = new JFileChooser(startLocation);

        chooser.setLocale(LocaleManager.getLanguageLocale());
        chooser.setDialogTitle(Translator.getText(dialogTitle));
        chooser.setFileFilter(createXmlFileFilter());
        chooser.setMultiSelectionEnabled(false);

        return chooser;
    }

    /****************************************************************************************************
     * Creates a new XML file filter.
     * <p>
     * @return An XML file filter.
     ***************************************************************************************************/
    public static FileFilter createXmlFileFilter() {
        FileChooserFactory factory = new FileChooserFactory();
        return factory.getXmlFileFilter();
    }

    /****************************************************************************************************
     * Private instance method that returns a new XML file filter.
     ***************************************************************************************************/
    private FileFilter getXmlFileFilter() {
        return new XmlFileFilter();
    }

    /****************************************************************************************************
     * INNER CLASS - An XML file filter that will filter all files that do not end with .xml.
     ***************************************************************************************************/
    private class XmlFileFilter extends FileFilter {

        public boolean accept(File file) {
            return file.isDirectory() || file.getAbsolutePath().endsWith(".xml");
        }

        public String getDescription() {
            return Translator.getText("XML files only");
        }
    }

    /****************************************************************************************************
     * INNER CLASS - Directory only file filter.
     ***************************************************************************************************/
    private class DirectoryFileFilter extends FileFilter {

        public boolean accept(File file) {
            return file.isDirectory();
        }

        public String getDescription() {
            return Translator.getText("Directories Only");
        }
    }

    /****************************************************************************************************
     * INNER CLASS - Java only file filter.
     ***************************************************************************************************/
    private class JavaFileFilter extends FileFilter {

        public boolean accept(File file) {
            if (file.isDirectory()) {
                return true;
            }
            if (file.isFile()) {
                return file.getName().endsWith(".java");
            }
            return false;
        }

        public String getDescription() {
            return ".java";
        }
    }
}
