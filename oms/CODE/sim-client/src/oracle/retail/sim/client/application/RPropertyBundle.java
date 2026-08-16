package oracle.retail.sim.client.application;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackInputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.common.configutil.ResourceManager;
import oracle.retail.sim.common.core.locale.NumberParser;
import oracle.retail.sim.common.logging.LogService;

/********************************************************************************************************
 * This class loads a property file consisting of a KEY = VALUE pairs, usually on each line of the file.
 * RPropertyBundle places these keys and values in a HashMap for storage as Strings. The KEY may contain
 * spaces, colons and semi-colons, but may NOT contain an "=" sign. It is the only prohibited part of the
 * key.
 * <p>
 * The character "\" at the end of a line signifies line continuance allowing multiple lines to make up a
 * single key-value pair. The "!" or "#" at the beginning of a line indicate a comment line and will be
 * ignored, as will a blank line.
 * <p>
 * This class contains a set of methods to retrieve the information in the property file in different
 * formats. It currently supports Boolean, Integer, Double, String and String[].
 * <p>
 * An absolute filename will automatically be used. If only a filename is supplied, this bundle looks
 * first in the execution directory and then in the classpath to find the file.
 * <p>
 * Also, the properties bundle assumes that numeric values are stored as English US as far as local
 * specifics are concerned. It also assumes that Boolean values are stored as the English US strings
 * (true|false);
 * <p>
 * This class is not fully internationalized. Even though the entire Unicode set can be contained in the
 * property file, the values '=', '!', '#', '\\', and New Line are hardcoded here.
 *
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RPropertyBundle {
    private Map<String, String> propertyMap = new HashMap<String, String>();

    private static final String EXTENSION = ".properties";
    private static final String UNDERLINE = "_";
    private static final String COMMA = ",";

    private static final String ENCODE_ERROR = "Invalid byte encoding for file {0} at line {1}";
    private static final String FILE_ERROR = "File {0} could not found or could not be accessed.";
    private static final String FORMAT_ERROR = "Invalid property format found in file {0} at line {1}";
    private static final String GENERAL_ERROR = "Unknown Exception: [{0}]  File: {1}  Line: {2}";
    private static final String IO_ERROR = "IO error occurred attempting to read file [{0}].";

    /****************************************************************************************************
     * Creates and returns new empty RPropertyBundle object.
     ***************************************************************************************************/
    public RPropertyBundle() {
    }

    /****************************************************************************************************
     * Creates and returns a new RPropertyBundle object. It will create the bundle with a properties file
     * with the matching filename.
     * <p>
     * @param filename The file name of the property file to load.
     *            <p>
     * @throws Exception Thrown if the method fails to load the property bundle file.
     ***************************************************************************************************/
    public RPropertyBundle(String filename) throws Exception {
        loadPropertyBundle(filename);
    }

    /****************************************************************************************************
     * Creates and returns a new RPropertyBundle object. It will create the bundle with a properties file
     * with the matching filename.
     * <p>
     * @param filename The file name of the property file to load.
     * @param encoding The character encoding to use when reading the property file.
     *
     * @throws Exception Thrown if the method fails to load the property bundle file.
     ***************************************************************************************************/
    public RPropertyBundle(String filename, String encoding) throws Exception {
        loadPropertyBundle(filename, encoding);
    }

    /****************************************************************************************************
     * Removes all key/value pairs from the property bundle.
     ***************************************************************************************************/
    public void clear() {
        propertyMap.clear();
    }

    /****************************************************************************************************
     * Retrieves whether or not the property bundle is empty.
     * <p>
     * @return True if the property bundle is empty, false if it is not.
     ***************************************************************************************************/
    public boolean isEmpty() {
        return propertyMap.isEmpty();
    }

    /****************************************************************************************************
     * Retrieves a string array of the entire key set of the bundle.
     * <p>
     * @return An string array of the entire key set of the bundle.
     ***************************************************************************************************/
    public String[] getKeys() {
        Set<String> keys = propertyMap.keySet();
        return keys.toArray(new String[keys.size()]);
    }

    /****************************************************************************************************
     * Retrieves the first character of a property value for the given key. If the property does not
     * exist, or is empty, a empty character is returned.
     * <p>
     * @param key The key to retrieve a property value for.
     * @return The property value converted into a character.
     ***************************************************************************************************/
    public char getChar(String key) {
        try {
            return getString(key).charAt(0);
        } catch (Exception e) {
            return '\0';
        }
    }

    /****************************************************************************************************
     * Retrieves a property value for the given key as an Boolean object. This method will return a null
     * value if the requested property does not exist or is not a valid boolean value.
     * <p>
     * @param key The key to retrieve a property value for.
     * @return The property value converted into a Boolean object.
     ***************************************************************************************************/
    public Boolean getBoolean(String key) {
        try {
            return StringUtility.booleanValue(getString(key));
        } catch (Exception e) {
            return null;
        }
    }

    /****************************************************************************************************
     * Retrieves a property value for the given key as an Integer object. This method will return a null
     * object if the requested property does not exist or is not a valid integer.
     * <p>
     * @param key The key to retrieve a property value for.
     * @return The property value converted into an Integer object.
     ***************************************************************************************************/
    public Integer getInteger(String key) {
        try {
            return NumberParser.getInstance(Locale.US).getInteger(getString(key));
        } catch (Exception e) {
            return null;
        }
    }

    /****************************************************************************************************
     * Retrieves a property value for the given key as an Double object. This method will return a null
     * object if the requested property does not exist or is not a valid double.
     * <p>
     * @param key The key to retrieve a property value for.
     * @return The property value converted into an Double object.
     ***************************************************************************************************/
    public Double getDouble(String key) {
        try {
            return NumberParser.getInstance(Locale.US).getDouble(getString(key));
        } catch (Exception e) {
            return null;
        }
    }

    /****************************************************************************************************
     * Retrieves a property value for the given key as a String object. This method returns the original
     * key if a value was not found. Since the key is a String, this is the only get() method that
     * returns the key in such a manner.
     * <p>
     * @param key The key to retrieve a property value for.
     * @return The property value converted into a String object.
     ***************************************************************************************************/
    public String getString(String key) {
        return propertyMap.get(key);
    }

    /****************************************************************************************************
     * Retrieves a property value for the given key as a String array. This method returns a null array
     * if the value was not found. If a value is found, it will be parsed on commas (',') to determine
     * the breaks in the string.
     * <p>
     * @param key The key to retrieve a property value for.
     * @return The property value converted into a String array.
     ***************************************************************************************************/
    public String[] getStringArray(String key) {
        String string = propertyMap.get(key);
        if (string != null) {
            return StringUtility.getStringArray(string, COMMA);
        }
        return null;
    }

    /****************************************************************************************************
     * Loads a file into the property resource bundle, parsing the KEY-VALUE information stored within
     * the file and stores it internally.
     * <p>
     * The filename will be loaded, followed immediately by attempting to load the same filename with an
     * _1, _2, _3, etc until a file cannot be found.
     * <p>
     * Note that this method does not clear the bundle before loading the file. This allows for loading
     * multiple files into the same property bundle.
     * <p>
     * @param filename The file name of the property file to load.
     * @throws Exception Thrown if the fileName is not a valid file, a file IO error occurs, or the data
     *             in the file is not properly formatted.
     ***************************************************************************************************/
    public void loadPropertyBundle(String filename) throws Exception {
        loadPropertyBundle(filename, null);
    }

    /****************************************************************************************************
     * Loads a file into the property resource bundle, parsing the KEY-VALUE information stored within
     * the file and storing it internally.
     * <p>
     * The filename will be loaded, followed immediately by attempting to load the same filename with an
     * _1, _2, _3, etc until a file cannot be found.
     * <p>
     * Note that this method does not clear the bundle before loading the file. This allows for loading
     * multiple files into the same property bundle.
     * <p>
     * Notes that this method checks for at least 3 bytes of information exists in the file so that it
     * doesn't attempt to read empty Unicode files where the first two bytes indicate only the encoding.
     * <p>
     * @param filename The file name of the property file to load.
     * @param encoding The character encoding to use when reading the file.
     * @throws Exception Thrown if the fileName is not a valid file, a file IO error occurs, or the data
     *             in the file is not properly formatted.
     ***************************************************************************************************/
    public void loadPropertyBundle(String filename, String encoding) throws Exception {
        if (filename.charAt(0) == '\\') {
            filename = filename.substring(1);
        }
        String fullFilename = filename + EXTENSION;
        InputStream inputStream = ResourceManager.getInputStream("conf/" + fullFilename);
        if (inputStream == null) {
            LogService.info(this, "Requested properties file " + fullFilename + " not found.");
            return;
        }
        LogService.info(this, "Loading properties file " + fullFilename);
        loadPropertyBundle(inputStream, filename, encoding);
        int counter = 1;
        while (true) {
            String nextFilename = filename + UNDERLINE + String.valueOf(counter++) + EXTENSION;
            inputStream = ResourceManager.getInputStream("conf/" + nextFilename);
            if (inputStream == null) {
                break;
            }
            LogService.info(this, "Loading properties file " + nextFilename);
            loadPropertyBundle(inputStream, filename, encoding);
        }
    }

    /****************************************************************************************************
     * Loads a file into the property resource bundle, parsing the KEY-VALUE information stored within
     * the file and storing it internally.
     * <p>
     * Note that this method does not clear the bundle before loading the file. This allows for loading
     * multiple files into the same property bundle.
     * <p>
     * Notes that this method checks for at least 3 bytes of information exists in the file so that it
     * doesn't attempt to read empty Unicode files where the first two bytes indicate only the encoding.
     * <p>
     * @param inputStream The input stream to process.
     * @param filename The file name of the property file to be used for messages.
     * @param encoding The character encoding to use when reading the file.
     * @throws Exception Thrown if the fileName is not a valid file, a file IO error occurs, or the data
     *             in the file is not properly formatted.
     ***************************************************************************************************/
    private void loadPropertyBundle(InputStream inputStream, String filename, String encoding) throws Exception {
        BufferedReader bufferedReader = null;
        PushbackInputStream pushbackStream = null;
        int lineNumber = 0;
        try {
            byte[] byteArray = new byte[3];
            pushbackStream = new PushbackInputStream(inputStream, byteArray.length);
            int bytesRead = pushbackStream.read(byteArray, 0, byteArray.length);
            if (bytesRead < byteArray.length) {
                LogService.info(this, filename + EXTENSION + " is empty.");
                return;
            }
            pushbackStream.unread(byteArray);
            if (encoding != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(pushbackStream, encoding));
            } else {
                bufferedReader = new BufferedReader(new InputStreamReader(pushbackStream));
            }
            StringBuilder lineBuffer = new StringBuilder();
            while (true) {
                String singleLine = bufferedReader.readLine();
                lineNumber++;
                if (singleLine == null) {
                    break;
                }
                singleLine = singleLine.trim();
                if (singleLine.length() < 1) {
                    continue;
                }
                char firstChar = singleLine.charAt(0);
                if (firstChar == '!' || firstChar == '#') {
                    continue;
                }
                if (singleLine.endsWith("\\")) {
                    lineBuffer.append(singleLine.substring(0, singleLine.length() - 1));
                    continue;
                }
                if (lineBuffer.length() > 0) {
                    lineBuffer.append(singleLine);
                    singleLine = lineBuffer.toString();
                    lineBuffer = new StringBuilder();
                }
                int index = StringUtility.indexOf(singleLine, "=");
                String key = StringUtility.substring(singleLine, 0, index).trim();
                String value = StringUtility.substring(singleLine, index + 1).trim();
                propertyMap.put(key, value);
            }
        } catch (IndexOutOfBoundsException ioobException) {
            throw new Exception(Translator.getMessage(FORMAT_ERROR, filename, String.valueOf(lineNumber)));
        } catch (UnsupportedEncodingException ueException) {
            throw new Exception(Translator.getMessage(ENCODE_ERROR, filename, String.valueOf(lineNumber)));
        } catch (FileNotFoundException fnfException) {
            throw new Exception(Translator.getMessage(FILE_ERROR, filename));
        } catch (IOException ioException) {
            throw new Exception(Translator.getMessage(IO_ERROR, filename));
        } catch (Exception exception) {
            LogService.error(this, "Unexpected exception: " + exception.getClass().getName(), exception);
            throw new Exception(Translator.getMessage(GENERAL_ERROR, exception.toString(), filename));
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ioe) {
                    LogService.error(this, "Failed to close bufferedReader", ioe);
                }
            } else if (pushbackStream != null) {
                try {
                    pushbackStream.close();
                } catch (IOException ioe) {
                    LogService.error(this, "Failed to close pushbackStream", ioe);
                }
            }
        }
    }
}
