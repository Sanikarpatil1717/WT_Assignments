/*
import java.util.*;

class Biker extends Thread implements Comparable<Biker> 
{
    private String bikerName;
    private double startTime;
    private double endTime;
    private double timeTaken;
    private int speed;
    private static final int RACE_DISTANCE = 500;
    private final Object startSignal; // Lock object for synchronization

    public Biker(String bikerName, int speed, Object startSignal) 
    {
        this.bikerName = bikerName;
        this.speed = speed;
        this.startSignal = startSignal;
    }

    public void setStartTime(double startTime) 
    {
        this.startTime = startTime;
    }

    public void setEndTime(double endTime) 
    {
        this.endTime = endTime;
        this.timeTaken = endTime - startTime;
    }

    public String getBikerName() 
    {
        return bikerName;
    }

    public double getStartTime() 
    {
        return startTime;
    }

    public double getEndTime() 
    {
        return endTime;
    }

    public double getTimeTaken() 
    {
        return timeTaken;
    }

    public int getSpeed() 
    {
        return speed;
    }

    @Override
    public void run() 
    {
        try 
        {
            synchronized (startSignal) 
            {
                startSignal.wait(); // Wait for the signal to start
            }

            double start = (System.currentTimeMillis() / 1000.0);
            setStartTime(start);

            // Calculate time taken based on speed
            double timeTaken = (RACE_DISTANCE / 1000.0) / (speed / 3600.0);
            Thread.sleep((long) (timeTaken * 1000)); // Simulate race

            double end = (System.currentTimeMillis() / 1000.0);
            setEndTime(end);
        } 
        catch (Exception e) 
        {
            System.out.println(bikerName + " was interrupted!");
        }
    }

    @Override
    public int compareTo(Biker other) 
    {
        return Double.compare(this.timeTaken, other.timeTaken);
    }
}

public class BikeRacingGame 
{
    public static void main(String[] args) throws Exception 
    {
        List<Biker> bikers = new ArrayList<>();
        Random random = new Random();
        Object startSignal = new Object(); // Lock object for synchronized start

        // Initialize bikers
        for (int i = 1; i <= 10; i++) 
        {
            int speed = random.nextInt(20) + 30; // Random speed between 30 and 50 km/h
            Biker biker = new Biker("Biker-" + i, speed, startSignal);
            bikers.add(biker);
        }

        System.out.println("Starting Countdown...");
        for (int i = 10; i > 0; i--) 
        {
            System.out.println(i);
            Thread.sleep(1000);
        }
        System.out.println("GO!\n");

        // Start all bikers and make them wait for the signal
        for (Biker biker : bikers) 
        {
            biker.start();
        }

        // Release all bikers simultaneously
        synchronized (startSignal) 
        {
            startSignal.notifyAll(); // Signal all bikers to start
        }

        // Wait for all bikers to finish
        for (Biker biker : bikers) 
        {
            biker.join();
        }

        // Sort bikers by time taken
        Collections.sort(bikers);

        // Display results
        System.out.println("Rank       Name            Speed(km/h)    Start Time      End Time        Time Taken");
        int rank = 1;
        for (Biker biker : bikers) 
        {
            System.out.printf("%-10d %-15s %-15d %-15.2f %-15.2f %.2f\n", rank++, biker.getBikerName(),
                    biker.getSpeed(), biker.getStartTime(), biker.getEndTime(), biker.getTimeTaken());
        }
    }
}
*/


import java.util.*;

class Biker extends Thread {
    String name;
    long startTime;
    long endTime;
    long timeTaken;
    static final Object lock = new Object(); // Shared lock object

    Biker(String name) {
        this.name = name;
        start(); // Start thread immediately
    }

    @Override
    public void run() {
        System.out.println(name + " is ready to race...");
        synchronized (lock) {
            try {
                lock.wait(); // Wait for the race signal
            } catch (InterruptedException e) {
                System.out.println(name + " was interrupted!");
            }
        }

        try {
            startTime = System.currentTimeMillis(); 
            for (int i = 1; i <= RacingDetails.distance; i++) {
                Thread.sleep((int) (Math.random() * 100)); 
                if (i % 100 == 0) {
                    System.out.print("."); // Progress marker
                }
            }
            System.out.println("\n" + name + " has finished the race!");
            endTime = System.currentTimeMillis(); 
            timeTaken = endTime - startTime; 
        } catch (InterruptedException e) {
            System.out.println(name + " was interrupted mid-race!");
        }
    }

    @Override
    public String toString() {
        return String.format("%s | Start: %d | End: %d | Time Taken: %d ms", name, startTime, endTime, timeTaken);
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

        Biker[] bikers = new Biker[noOfBikers];
        for (int i = 0; i < noOfBikers; i++) {
            System.out.print("Enter biker name: ");
            String name = scanner.nextLine();
            bikers[i] = new Biker(name);
        }

        // Countdown
        System.out.println("\nCountdown Started...");
        for (int i = 10; i > 0; i--) {
            System.out.println(i);
            Thread.sleep(1000);
        }
        System.out.println("GO!");

        // Notify all bikers to start racing
        synchronized (Biker.lock) {
            Biker.lock.notifyAll();
        }

        // Wait for all bikers to finish
        for (Biker biker : bikers) {
            biker.join();
        }

        // Sort and display results
        Arrays.sort(bikers, Comparator.comparingLong(b -> b.timeTaken));
        System.out.println("\nResults:");
        for (int i = 0; i < bikers.length; i++) {
            System.out.printf("Rank %d: %s\n", i + 1, bikers[i]);
        }
    }
}

