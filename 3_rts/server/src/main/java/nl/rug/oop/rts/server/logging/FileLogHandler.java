package nl.rug.oop.rts.server.logging;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * This class handles logging to a file.
 */
public class FileLogHandler extends Handler {

    private File logsDirectory = new File("logs");
    private boolean enabled = true;

    /**
     * Create a new FileLogHandler.
     * If the logs directory does not exist, it will be created.
     */
    public FileLogHandler() {
        try {
            if (!logsDirectory.exists()) {
                if (!logsDirectory.mkdirs()) {
                    throw new IOException("Could not create logs directory!");
                }
            }
        } catch (IOException e) {
            enabled = false;
            System.err.println("Something went wrong while creating log files! Logging will be disabled " +
                    e.getMessage());
        }
    }

    private File getFile() throws IOException {
        DateFormat format = new SimpleDateFormat("dd_MMM_yyyy");
        String fileName = format.format(Instant.now().toEpochMilli()) + ".log";
        File f = new File(logsDirectory, fileName);
        if (!f.exists()) {
            f.createNewFile();
        }
        return f;
    }

    @Override
    public void publish(LogRecord record) {
        if (!enabled) {
            return;
        }
        try {
            File f = getFile();
            FileOutputStream stream = new FileOutputStream(f, true);
            stream.write(getFormatter().format(record).getBytes());
            stream.close();
        } catch (IOException e) {
            System.out.println("Something went wrong while logging! " + e.getMessage());
        }
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() throws SecurityException {
        this.enabled = false;
    }
}
