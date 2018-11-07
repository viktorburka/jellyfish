import java.util.ArrayList;
import java.util.List;

class Application {

    void eventLoop(Repository repo) {

        final int MaxJobs = 1;

        List<Thread> runningJobs = new ArrayList<>();

        while (true) {
            // this method removes completed jobs from the list
            runningJobs.removeIf(w-> w.getState() == Thread.State.TERMINATED);

            // queue only if not exceeding the limit of concurrent jobs
            if (runningJobs.size() < MaxJobs) {
                Job job = repo.getNextJob(); //query from repo
                if (job != null) {
                    Job copy = new Job(job); // pass copy to avoid concurrent access
                    JobRunner jobRunner = new JobRunner(copy);
                    Thread runnerThread = new Thread(jobRunner);
                    runningJobs.add(runnerThread);
                    runnerThread.start();
                }
            }
            // pause
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.out.println("Timer stopped. Leaving...");
                break;
            }
        }
    }
}
