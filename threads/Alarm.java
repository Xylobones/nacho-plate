package nachos.threads;
import nachos.machine.*;
import java.util.PriorityQueue;

/**
 * Uses the hardware timer to provide preemption, and to allow threads to sleep
 * until a certain time.
 */
public class Alarm {
    /**
    * A private priority queue meant for the storage of objects of the threadStorage class in ascending order based upon
    * the waketime of the given stored threads in the object.
    */
    private PriorityQueue<threadStorage> waitingQueue = new PriorityQueue<threadStorage>();
	
    /**
     * Allocate a new Alarm. Set the machine's timer interrupt handler to this
     * alarm's callback.
     *
     * <p><b>Note</b>: Nachos will not function correctly with more than one
     * alarm.
     */
    public Alarm() {
	Machine.timer().setInterruptHandler(new Runnable() {
		public void run() { timerInterrupt(); }
	    });
    }

    /**
     * The timer interrupt handler. This is called by the machine's timer
     * periodically (approximately every 500 clock ticks). Causes the current
     * thread to yield, forcing a context switch if there is another thread
     * that should be run.
     */
    public void timerInterrupt() {
	Machine.interrupt().disable();
	// Wake the threads, whereas the threads waketime >= to current time
	while (!waitingQueue.isEmpty() && (waitingQueue.peek().wakeTime <= Machine.timer().getTime())){
		threadStorage threadStorage = waitingQueue.poll();
		if (threadStorage.currentThread != null){
			threadStorage.currentThread.ready();
		}
	}
	// Yield the current thread
	KThread.yield();
	Machine.interrupt().enable();
    }

    /**
     * Put the current thread to sleep for at least <i>x</i> ticks,
     * waking it up in the timer interrupt handler. The thread must be
     * woken up (placed in the scheduler ready set) during the first timer
     * interrupt where
     *
     * <p><blockquote>
     * (current time) >= (WaitUntil called time)+(x)
     * </blockquote>
     *
     * @param	x	the minimum number of clock ticks to wait.
     *
     * @see	nachos.machine.Timer#getTime()
     */
    public void waitUntil(long x) {
	// Create an object of type threadStorage to store the thread and its waketime in a priortiy queue.
	threadStorage threadStorage = new threadStorage(KThread.currentThread(), (Machine.timer().getTime() + x));
	Machine.interrupt().disable();
	// Store the thread & its wake time in the priority queue and put the thread to sleep.
	waitingQueue.add(threadStorage);
	KThread.currentThread().sleep();
	Machine.interrupt().enable();
    }
     /**
     * An inner private class meant for the storage of a thread and its wake time.
     * Implements comparable alongside the compareTo method to allow for the use and 
     * proper storage of objects of the given type in a priorty queue.
     */
     private class threadStorage  implements Comparable<threadStorage>{
	private KThread currentThread;
    	private long wakeTime;
	
	/**
	* A constructor for the inner class threadStorage, initializes variables wakeTime and  currentThread.
	*
        * @param currentThread		the stored thread.
        * 	 wakeTime		the stored threads wake time.	
	*/  
    	public threadStorage (KThread currentThread, long wakeTime){
    		this.currentThread = currentThread;
    		this.wakeTime = wakeTime;
    	}
    	
	/**
	* The compareTo method is used to compare objects of this type with one another.
	* Returns 1 if greater, -1 if lesser or 0 if equal to the compared threadStorage.
	*
	* @param Storage2	the other object of type threadStorage being compared.
	*/
    	public int compareTo(threadStorage Storage2){
    		if (this.wakeTime > Storage2.wakeTime) { return 1; }	
    		else if (this.wakeTime < Storage2.wakeTime) { return -1; }	
    		else { return 0; }
    	}
    }
	
	/**
	* The first test case, selfTest1() test the alarm class against a singular thread to check wether it sleeps and/or wakes 
	* within expected parameters. It does this by checking if the time taken for the thread to wake is within excpected 
	* parameters given that the machine calls timerInterrupt around every ~500 ticks.
	*/
	public static void selfTest1() {
		Alarm alarm = new Alarm();
		System.out.println("Running alarm test case 1: ");
		
		Runnable A = new Runnable() {
			public void run() {
				long current = Machine.timer().getTime();
				alarm.waitUntil(750);
				// If the therad waits ~750 ticks and wakes the next call to timerInterrupt (~1000 ticks).
				if ((Machine.timer().getTime() >= (current + 750)) 
				    && (Machine.timer().getTime() < (current + 1500))){
					System.out.println("Case #1 Success!");
					}
				else 
					System.out.println("Case #1 Failure!");
				}
			};
		
		KThread ThreadA = new KThread(A);
		ThreadA.fork();
		ThreadA.join();
	}

	/**
	* The second test case tests the alarm class when given odd arguments such as a negatives or a zero. 
	* In this case the thread should wake immediatly or as soon as possible (next timerInturrupt call). 
	* We check if this holds true by checking if the time the thread spent waiting is within excpected parameters 
	* given that the machine calls timerInterrupt around every ~500 ticks.
	*/
	public static void selfTest2() {
		Alarm alarm = new Alarm();
		System.out.println("Running alarm test case 2: ");
		
		Runnable A = new Runnable() {
			public void run() {
				long current = Machine.timer().getTime();
				alarm.waitUntil(0);
				// The following If statements checks if the therad woke immediatly/next timerInterrupt call.
				if (Machine.timer().getTime() <= (current + 500)){
					alarm.waitUntil(-42);
					if (Machine.timer().getTime() <= (current + 1000)){
						System.out.println("Case #2 Success!");
						}
					else 
						System.out.println("Case #2 Failure!");
					}
				else 
					System.out.println("Case #2 Failure!");
				}
			};
		
		KThread ThreadA = new KThread(A);
		ThreadA.fork();
		ThreadA.join();
	}
	
		/**
		* The third test case tests the alarm class against multiple forks of the same thread to check wether they 
		* sleep and/or wake within expected parameters and are awoken in a correct order. 
		* It does this by outputting output ressembling an echo, in other words all messages should be followed by 
		* their counterpart in the fork of the thread, resembling the effect of an echo.
		*/
		public static void selfTest3() {
		Alarm alarm = new Alarm();
		System.out.println("Running alarm test case 3: ");
		
		//The thread should display hello, hello, stop copying me!, stop copying me!, Im Telling On You!!...
		Runnable A = new Runnable() {
			public void run() {
				alarm.waitUntil(750);
				System.out.println("Hello?");
				alarm.waitUntil(2500);
				System.out.println("Stop Copying Me!");
				alarm.waitUntil(7500);
				System.out.println("Im Telling On You!!");
			};
		};
		KThread ThreadA = new KThread(A);
		KThread ThreadB = new KThread(A);
		ThreadA.fork();
		ThreadB.fork();
		ThreadA.join();
	}
	
	/**
	* The fourth test case, selfTest4() tests the alarm class against multiple threads to check wether they sleep and/or 
	* wake within expected parameters and are awoken in a correct order. It does this by checking if the time taken for the 
	* threads to wake is within excpected parameter given that the machine calls timerInterrupt around every ~500 ticks
	* and by ensuring all threads are awoken in correct order based on their wake time.
	*/
	public static void selfTest4() {
		Alarm alarm = new Alarm();
		System.out.println("Running alarm test case 4: ");
		
		Runnable A = new Runnable() {
			public void run() {
				long current = Machine.timer().getTime();
				alarm.waitUntil(750);
				if ((Machine.timer().getTime() >= (current + 750)) 
				    && (Machine.timer().getTime() < (current + 1500))){
					System.out.println("Case #4: ThreadA Success!");
					}
				else {
					System.out.println("Case #4: ThreadA Failure!");
					}
				System.out.println("ThreadA: This Should Display First");
				}
			};
		
		Runnable B = new Runnable() {
			public void run() {
				long current = Machine.timer().getTime();
				alarm.waitUntil(2500);
				if ((Machine.timer().getTime() >= (current + 2500)) 
				    && (Machine.timer().getTime() < (current + 3000))){
					System.out.println("Case #4: ThreadB Success!");
					}
				else {
					System.out.println("Case #4: ThreadB Failure!");
					}
				System.out.println("ThreadB: This Should Display Last");
				}
			};
		
		Runnable C = new Runnable() {
			public void run() {
				long current = Machine.timer().getTime();
				alarm.waitUntil(1500);
				if ((Machine.timer().getTime() >= (current + 1500)) 
				    && (Machine.timer().getTime() < (current + 2000))){
					System.out.println("Case #4: ThreadC Success!");
					}
				else {
					System.out.println("Case #4: ThreadC Failure!");
					}
				System.out.println("ThreadC: This Should Display Second");
				}
			};
		
		KThread ThreadA = new KThread(A);
		KThread ThreadB = new KThread(B);
		KThread ThreadC = new KThread(C);
		ThreadA.fork();
		ThreadB.fork();
		ThreadC.fork();
		ThreadA.join();
		ThreadB.join();
		ThreadC.join();
	}
}

