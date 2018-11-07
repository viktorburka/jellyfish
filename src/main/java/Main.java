import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;

public class Main {

    public static void main(String[] args) {

        disableMongoDbLogging();

        String uri = System.getenv("JOCTOPUS_DBCONNECTION");
        String database = System.getenv("JOCTOPUS_DATABASE");

        Repository repo = Repository.getRepository(uri, database);
        if (repo == null) {
            System.err.println("Unknown db scheme: " + Tools.obfuscateUri(uri));
            System.exit(-1);
        }

        do {
            System.out.println("connecting to db...");
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
                System.err.println("sleep exception in db connect loop");
                return;
            }
        } while (!repo.connect());

        Application app = new Application();
        app.eventLoop(repo);
    }

    private static void disableMongoDbLogging() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger rootLogger = loggerContext.getLogger("org.mongodb.driver");
        rootLogger.setLevel(Level.OFF);
    }
}
