import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

class Application {

    void eventLoop(Repository repo) {

        final int MaxJobs = 1;

        List<JobRunner> runningJobs = new ArrayList<>();

        while (true) {

            // this method examines for errors
            runningJobs.forEach(w -> {
                if (w.isError()) {
                    System.err.println(w.getError());
                }
            });

            // this method removes completed jobs from the list
            runningJobs.removeIf(w -> w.getState() == Thread.State.TERMINATED);

            // queue only if not exceeding the limit of concurrent jobs
            if (runningJobs.size() < MaxJobs) {
                Job job = repo.getNextJob(); //query from repo
                if (job != null) {
                    CountDownLatch startSignal = new CountDownLatch(1);
                    Job copy = new Job(job); // pass copy to avoid concurrent access
                    JobRunner jobRunner = new JobRunner(copy);
                    jobRunner.setSignalOnStart(startSignal);
                    runningJobs.add(jobRunner);
                    jobRunner.start();
                    // wait until thread started
                    try {
                        startSignal.await();
                    } catch (InterruptedException e) {
                        System.out.println("CountDownLatch stopped. Leaving...");
                        break;
                    }
                }
            }
            // pause
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.out.println("Stopped. Leaving...");
                break;
            }
        }
    }
}
