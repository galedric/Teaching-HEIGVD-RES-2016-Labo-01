package ch.heigvd.res.lab01.impl.filters;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Logger;

/**
 * This class transforms the streams of character sent to the decorated writer.
 * When filter encounters a line separator, it sends it to the decorated writer.
 * It then sends the line number and a tab character, before resuming the write
 * process.
 * <p>
 * Hello\n\World -> 1\Hello\n2\tWorld
 *
 * @author Olivier Liechti
 */
public class FileNumberingFilterWriter extends FilterWriter {

	private static final Logger LOG = Logger.getLogger(FileNumberingFilterWriter.class.getName());

	private int line = 1;
	private boolean eol = true;
	private boolean firstWrite = true;

	public FileNumberingFilterWriter(Writer out) {
		super(out);
	}

	@Override
	public void write(String str, int off, int len) throws IOException {
		write(str.toCharArray(), off, len);
	}

	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
		for (int i = off; i < off + len; ++i) {
			write(cbuf[i]);
		}
	}

	/**
	 * Writes the line number to the output
	 * @throws IOException
	 */
	private void writeLineNumber() throws IOException {
		out.write(String.format("%d\t", line++));
		eol = false;
	}

	@Override
	public void write(int c) throws IOException {
		if (firstWrite || (eol && c != '\n')) {
			writeLineNumber();
			firstWrite = false;
		}

		out.write(c);

		if (c == '\r') {
			eol = true;
		} else if (c == '\n') {
			writeLineNumber();
		}
	}

}
