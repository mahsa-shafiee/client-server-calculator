package log4j.customAppender;

import org.apache.log4j.FileAppender;
import org.apache.log4j.MDC;
import org.apache.log4j.spi.LoggingEvent;

import java.io.IOException;

/**
 * This customized Log4j customAppender will separate the log messages based on their
 * LEVELS and will write them into separate files.
 */

public class LogLevelFilterFileAppender extends FileAppender {

    private static final String ORIG_LOG_FILE_NAME = "OriginalLogFileName";

    @Override
    public void activateOptions() {
        MDC.put(ORIG_LOG_FILE_NAME, fileName);
    }

    @Override
    public void append(LoggingEvent event) {
        try {
            setFile(appendLevelToFileName((String) MDC.get(ORIG_LOG_FILE_NAME),
                    event.getLevel().toString()), fileAppend, bufferedIO, bufferSize);
        } catch (IOException ie) {
            ie.printStackTrace();
        }
        super.append(event);
    }

    private String appendLevelToFileName(String oldLogFileName, String level) {
        return oldLogFileName.substring(0, oldLogFileName.indexOf("."))
                + "-" + level + oldLogFileName.substring(oldLogFileName.indexOf("."));
    }
}