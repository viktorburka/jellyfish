import org.bson.Document;

class Job {
    String id;
    String status;
    String srcUrl;
    String dstUrl;
    String description;

    Job(String id,
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

    Job(Job job) {
        this(job.id, job.status, job.srcUrl, job.dstUrl, job.description);
    }

    Job(Document doc) {
        this.id = doc.getString("_id");
        this.status = doc.getString("status");
        this.srcUrl = doc.getString("srcUrl");
        this.dstUrl = doc.getString("dstUrl");
        this.description = doc.getString("description");
    }
}
