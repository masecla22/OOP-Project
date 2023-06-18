package nl.rug.oop.rts.server.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * This is the class used to format messages and errors to the console.
 */
public class CustomFormatter extends Formatter {
    @Override
    public String format(LogRecord message) {
        String msg = message.getMessage();

        StringBuffer sb = new StringBuffer();
        sb.append(getTimeAndDate());
        sb.append(message.getLevel());
        sb.append(": ");
        sb.append(msg).append("\n");

        if (message.getThrown() != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            message.getThrown().printStackTrace(pw);
            sb.append(sw.toString()).append("\n");
        }

        return sb.toString();
    }

    private String getTimeAndDate() {
        DateFormat format = new SimpleDateFormat("HH:mm:ss");
        return "[" + format.format(Instant.now().toEpochMilli()) + "] ";
    }
}
