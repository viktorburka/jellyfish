import java.net.MalformedURLException;
import java.net.URL;

public class JobRunner implements Runnable {

    private Job job;
    private String error = "";

    JobRunner(Job job) {
        this.job = job;
    }

    public synchronized boolean isError() {
        return error.isEmpty();
    }

    public void run() {
        System.out.println("transferring from " + job.srcUrl + " to " + job.dstUrl);

        URL srcUrl;
        URL dstUrl;

        try {
            srcUrl = new URL(job.srcUrl);
        } catch (MalformedURLException e) {
            String error = String.format("invalid src url: %s", e.getMessage());
            setError(error);
            return;
        }

        try {
            dstUrl = new URL(job.dstUrl);
        } catch (MalformedURLException e) {
            String error = String.format("invalid dst url: %s", e.getMessage());
            setError(error);
            return;
        }

        Downloader probe = Downloader.getProbeForScheme(srcUrl.getProtocol());
    }

    private synchronized void setError(String error) {
        this.error = error;
    }
}
