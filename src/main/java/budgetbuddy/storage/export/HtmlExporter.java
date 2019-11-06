package budgetbuddy.storage.export;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Simulates a class which handles the exporting of Strings to HTML.
 */
public class HtmlExporter extends Exporter {

    /**
     * Exports a String to a HTML file.
     * @param formattedString The formatted content.
     * @return True if the file does not exist, false if the file already exists.
     * @throws IOException The exception to be thrown.
     */
    public static boolean export(String formattedString) throws IOException {
        File dir = new File(EXPORT_DIRECTORY_PATH);
        dir.mkdir();

        String filePath = EXPORT_DIRECTORY_PATH + "account.html";

        File file = new File(filePath);
        if (file.exists()) {
            return false;
        }


        FileOutputStream fos = new FileOutputStream(file);
        fos.write(formattedString.getBytes());
        fos.flush();
        fos.close();

        return true;
    }
}
