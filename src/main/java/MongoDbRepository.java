import com.mongodb.client.*;
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
        MongoCursor<Document> cursor = jobs.find().iterator();
        if (cursor.hasNext()) {
            cursor.close();
            return new Job();
        }
        return null;
    }
}
