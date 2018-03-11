package al.artofsoul.batbatgame.handlers;

import java.util.logging.Logger;

public class LoggingHelper {

	private LoggingHelper() {
		throw new IllegalStateException("Utility Class");
	}

	public static final Logger LOGGER = Logger.getLogger(LoggingHelper.class.getName());
}
