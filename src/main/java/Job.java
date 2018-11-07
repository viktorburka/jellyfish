public class Job {
    String id;
    String status;
    String srcUrl;
    String dstUrl;
    String description;

    public Job(String id,
               String status,
               String srcUrl,
               String dstUrl,
               String description)
    {
        this.id = id;
        this.status = status;
        this.srcUrl = srcUrl;
        this.dstUrl = dstUrl;
        this.description = description;
    }

    public Job(Job job){
        this(job.id, job.status, job.srcUrl, job.dstUrl, job.description);
    }
}
