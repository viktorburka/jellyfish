import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class Main {

    public static void main(String[] args) {

        String uri = System.getenv("JOCTOPUS_DBCONNECTION");
        String database = System.getenv("JOCTOPUS_DATABASE");

        Repository repo = getRepository(uri, database);
        if (repo == null) {
            System.err.println("Unknown db scheme: " + obfuscateUri(uri));
            System.exit(-1);
        }

        do {
            System.out.println("connecting to db...");
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                System.err.println("sleep exception in db connect loop");
                return;
            }
        } while (!repo.connect());

        eventLoop(repo);
    }

    private static Repository getRepository(String uri, String database) {
        if (uri.startsWith("mongodb")) {
            return new MongoDbRepository(uri, database);
        }
        return null;
    }

    private static void eventLoop(Repository repo) {

        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("tick");
            }
        },0,1000);

        while (true) {

            Job job = repo.getNextJob();

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.out.println("Timer stopped. Leaving...");
                break;
            }
        }

        timer.cancel();
    }

    private static String obfuscateUri(String uri) {
        return uri;
    }
}
