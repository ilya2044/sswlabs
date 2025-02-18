public class Main {

    public static void main(String[] args) {
        int numThreads = 5;
        int calculationTime = 10000;

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
                        clearConsole();
                        printProgressBar(threadId, progressBar.toString());
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

    private synchronized static void printProgressBar(int threadId, String progressBar) {
        for (int i = 1; i <= 5; i++) {
            System.out.print("Thread " + i + " " + progressBar + "\t");
        }
        System.out.println();
    }
}
