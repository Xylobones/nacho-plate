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
	thread.sleep(KThread.currentThread());
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
    	public ThreadTime (KThread currentThread, long wakeTime){
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
}
