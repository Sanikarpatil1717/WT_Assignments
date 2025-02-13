import java.util.*;
import java.util.concurrent.CountDownLatch;

class Biker extends Thread {
    private String name;
    private long startTime;
    private long endTime;
    private long timeTaken;
    private CountDownLatch startLatch;
    private CountDownLatch finishLatch;
    
    public Biker(String name, CountDownLatch startLatch, CountDownLatch finishLatch) {
        this.name = name;
        this.startLatch = startLatch;
        this.finishLatch = finishLatch;
    }

    @Override
    public void run() {
        System.out.println(name + " is ready to race...");
        try {
            startLatch.await(); // Wait for countdown to finish
            startTime = System.currentTimeMillis();
            
            for (int i = 1; i <= RacingDetails.distance; i++) {
                Thread.sleep((int) (Math.random() * 100)); 
                if (i % 100 == 0) {
                    System.out.print(".");
                }
            }
            
            endTime = System.currentTimeMillis();
            timeTaken = endTime - startTime;
            System.out.println("\n" + name + " has finished the race!");
            
        } catch (InterruptedException e) {
            System.out.println(name + " was interrupted mid-race!");
        } finally {
            finishLatch.countDown(); // Indicate biker has finished
        }
    }

    public long getTimeTaken() {
        return timeTaken;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public String getBName() {
        return name;
    }
}

class RacingDetails {
    static int distance = 1000;
}

public class BikeRacingGame {
    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of bikers: ");
        int noOfBikers = scanner.nextInt();
        scanner.nextLine(); 

        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch finishLatch = new CountDownLatch(noOfBikers);
        
        Biker[] bikers = new Biker[noOfBikers];
        for (int i = 0; i < noOfBikers; i++) {
            System.out.print("Enter biker name: ");
            String name = scanner.nextLine();
            bikers[i] = new Biker(name, startLatch, finishLatch);
        }

        // Start all bikers after taking input
        for (Biker biker : bikers) {
            biker.start();
        }

        Thread.sleep(1000);

        // Countdown
        System.out.println("\nCountdown Started...");
        for (int i = 10; i > 0; i--) {
            System.out.println(i);
            Thread.sleep(1000);
        }
        System.out.println("GO!");

        // Start the race
        startLatch.countDown();

        // Wait for all bikers to finish
        finishLatch.await();

        // Sort and display results
        Arrays.sort(bikers, Comparator.comparingLong(Biker::getTimeTaken));
        System.out.println("\nResults:");
        for (int i = 0; i < bikers.length; i++) {
            System.out.printf("Rank %d: %s | Start Time: %d | End Time: %d | Time Taken: %d ms\n",
                i + 1, bikers[i].getBName(), bikers[i].getStartTime(), bikers[i].getEndTime(), bikers[i].getTimeTaken());
        }
        
        scanner.close();
    }
}
