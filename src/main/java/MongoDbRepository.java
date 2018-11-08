import com.mongodb.client.*;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import org.bson.Document;

public class MongoDbRepository implements Repository {

    private String uri;
    private String dbName;

    private MongoClient client;
    private MongoDatabase db;

    public MongoDbRepository(String uri, String database) {
        this.uri = uri;
        this.dbName = database;
    }

    public boolean connect() {

        try {
            client = MongoClients.create(uri);
            db = client.getDatabase(dbName);
        } catch (Exception e) {
            System.err.println("db connection error");
            System.err.println(e);
            return false;
        }

        System.out.println("successfully connected");
        return true;
    }

    public Job getNextJob() {
        MongoCollection<Document> jobs = db.getCollection("jobs");
        Document query = new Document("status", "Created");
        Document update = new Document("$set", new Document("status", "Running"));
        FindOneAndUpdateOptions opt = new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER);
        Document newJobDocument = jobs.findOneAndUpdate(query, update, opt);
        if (newJobDocument != null) {
            return new Job(newJobDocument);
        }
        return null;

//        return new Job("1", "Running", "s3://s3.amazonaws.com/vps-video/maven.tar.gz", "s3://s3.amazonaws.com/vps-video-test/test/maven.tar.gz", "maven");
    }
}
