public interface Repository {

    static Repository getRepository(String uri, String database) {
        if (uri.startsWith("mongodb")) {
            return new MongoDbRepository(uri, database);
        }
        return null;
    }

    boolean connect();

    Job getNextJob();
}
