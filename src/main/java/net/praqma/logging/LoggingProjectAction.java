package net.praqma.logging;

import hudson.model.Job;
import hudson.model.ProminentProjectAction;
import org.apache.commons.io.FileUtils;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LoggingProjectAction implements ProminentProjectAction {

    private Job<?, ?> job;

    public LoggingProjectAction( Job<?, ?> job ) {
        this.job = job;
    }

    @Override
    public String getIconFileName() {
        return "/plugin/logging/images/notebook.png";
    }

    @Override
    public String getDisplayName() {
        return "Poll Logging";
    }

    @Override
    public String getUrlName() {
        return "poll-logging";
    }

    public List<Logging.PollLoggingFile> getLogs() {
        return Logging.getPollLogs( new File( job.getRootDir(), Logging.POLLLOGPATH ) );
    }

    public void doGetPollLog( StaplerRequest req, StaplerResponse rsp ) throws IOException, ServletException {
        String logname = req.getParameter( "log" );

        File logPath = new File( job.getRootDir(), Logging.POLLLOGPATH );
        File logFile = new File( logPath, logname );

        if( logFile.exists() ) {
            Calendar t = Calendar.getInstance();
            t.setTimeInMillis(logFile.lastModified());
            rsp.serveFile( req, FileUtils.openInputStream( logFile ), t.getTimeInMillis(), logFile.getTotalSpace(), logFile.getName() );
        } else {
            rsp.sendError( HttpServletResponse.SC_NO_CONTENT );
        }
    }
}
