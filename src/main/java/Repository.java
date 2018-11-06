public interface Repository {
    boolean connect();
    Job getNextJob();
}
