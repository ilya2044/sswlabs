public class Main {
    public static void main(String[] args) {
        int numThreads = 5;
        int calculationTime = 10000;

        String[] progressBars = new String[numThreads];
        for (int i = 0; i < numThreads; i++) {
            progressBars[i] = "Thread " + (i + 1) + " [                    ]";
        }

        for (int i = 0; i < numThreads; i++) {
            final int threadId = i + 1;
            final long startTime = System.nanoTime();
            final StringBuilder progressBar = new StringBuilder("[                    ]");
            Thread thread = new Thread(() -> {
                for (int j = 0; j <= 20; j++) {
                    try {
                        Thread.sleep(calculationTime / 20);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }

                    progressBar.replace(1, 21, "=".repeat(j) + " ".repeat(20 - j));
                    synchronized (System.out) {
                        progressBars[threadId - 1] = "Thread " + threadId + " " + progressBar.toString();
                        clearConsole();
                        printProgressBars(progressBars);
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                long endTime = System.nanoTime();
                long duration = (endTime - startTime) / 1_000_000;
                synchronized (System.out) {
                    System.out.println("Thread " + threadId + " finished in " + duration + " ms");
                }
            });
            thread.start();
        }
    }

    private synchronized static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private synchronized static void printProgressBars(String[] progressBars) {
        for (String progressBar : progressBars) {
            System.out.println(progressBar);
        }
    }
}
