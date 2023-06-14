package nl.rug.oop.rts.server.logging;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * This is an output stream used for logging which will take input, chunk it,
 * and output to the logger every line.
 */
@RequiredArgsConstructor
public class LoggingOutputStream extends OutputStream {
	@NonNull
	private Logger logger;
	@NonNull
	private Level level;
	private StringBuilder stringBuilder = new StringBuilder();

	@Override
	public final void write(int i) throws IOException {
		char c = (char) i;
		if (c == '\r' || c == '\n') {
			if (stringBuilder.length() > 0) {
				logger.log(level, stringBuilder.toString());
				stringBuilder = new StringBuilder();
			}
		} else
			stringBuilder.append(c);
	}
}
