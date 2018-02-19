package nachos.threads;

import nachos.machine.*;
import java.util.LinkedList;

/**
 * An implementation of condition variables that disables interrupt()s for
 * synchronization.
 *
 * <p>
 * You must implement this.
 *
 * @see	nachos.threads.Condition
 */
public class Condition2 {
    /**
     * Allocate a new condition variable.
     *
     * @param	conditionLock	the lock associated with this condition
     *				variable. The current thread must hold this
     *				lock whenever it uses <tt>sleep()</tt>,
     *				<tt>wake()</tt>, or <tt>wakeAll()</tt>.
     */
    public Condition2(Lock conditionLock) {
	this.conditionLock = conditionLock;
    }

    /**
     * Atomically release the associated lock and go to sleep on this condition
     * variable until another thread wakes it using <tt>wake()</tt>. The
     * current thread must hold the associated lock. The thread will
     * automatically reacquire the lock before <tt>sleep()</tt> returns.
     */
    public void sleep() {
		if(conditionLock.isHeldByCurrentThread()){
			Machine.interrupt().disable();
			waitList.add(KThread.currentThread());
			conditionLock.release();
			KThread.currentThread().sleep();
			Machine.interrupt().enable();
			conditionLock.acquire();	
		}
    }

    /**
     * Wake up at most one thread sleeping on this condition variable. The
     * current thread must hold the associated lock.
     */
    public void wake() {
		if(conditionLock.isHeldByCurrentThread() && !waitList.isEmpty()){
			Machine.interrupt().disable();
			waitList.getFirst().ready();
			waitList.remove();
			Machine.interrupt().enable();
		}
	}

    /**
     * Wake up all threads sleeping on this condition variable. The current
     * thread must hold the associated lock.
     */
    public void wakeAll() {
		if(conditionLock.isHeldByCurrentThread() && !waitList.isEmpty()){
			Machine.interrupt().disable();
			while(!waitList.isEmpty()){
				wake();
			}
			Machine.interrupt().enable();
		}
    }
	
	public static void selfTest1(){
		System.out.println("Running condition test case 1");
		final Lock testLock = new Lock();
		final Condition2 testCondition = new Condition2(testLock);
		
		
		Runnable A = new Runnable() {
			public void run() {
				testLock.acquire();
				System.out.println("ThreadA has the lock! Going to sleep.");
				testCondition.sleep();
				System.out.println("ThreadA is awake!");
			}
		};
		
		Runnable B = new Runnable() {
			public void run() {
				testLock.acquire();
				testCondition.wake();
				System.out.println("ThreadB has the lock!");
				testLock.release();
			}
		};
		
		KThread ThreadA = new KThread(A);
		KThread ThreadB = new KThread(B);
		ThreadA.fork();
		ThreadB.fork();
		ThreadA.join();
		ThreadB.join();
	}
	
	public static void selfTest2(){
		System.out.println("Running condition test case 2");
		final Lock testLock = new Lock();
		final Condition2 testCondition = new Condition2(testLock);
		
		Runnable A = new Runnable() {
			public void run() {
				testLock.acquire();
				System.out.println("ThreadA has the lock!");
				testCondition.sleep();
				System.out.println("ThreadA has the lock!");
				testCondition.wakeAll();
				testLock.release();
			}
		};
		
		Runnable B = new Runnable() {
			public void run() {
				testLock.acquire();
				System.out.println("ThreadB has the lock!");
				testCondition.sleep();
				System.out.println("ThreadB has the lock!");
				testLock.release();
			}
		};
		
		Runnable C = new Runnable() {
			public void run() {
				testLock.acquire();
				System.out.println("ThreadC has the lock!");
				testCondition.sleep();
				System.out.println("ThreadC has the lock!");
				testLock.release();
			}
		};
		
		Runnable D = new Runnable() {
			public void run() {
				testLock.acquire();
				System.out.println("ThreadD has the lock!");
				testCondition.wake();
				testCondition.sleep();
				System.out.println("ThreadD has the lock!");
			}
		};
		
		KThread ThreadA = new KThread(A);
		KThread ThreadB = new KThread(B);
		KThread ThreadC = new KThread(C);
		KThread ThreadD = new KThread(D);
		ThreadA.fork();
		ThreadB.fork();
		ThreadC.fork();
		ThreadD.fork();
		ThreadA.join();
		ThreadB.join();
		ThreadC.join();
		ThreadD.join();
	}
	
	public static void selfTest3(){
		System.out.println("Running condition test case 3");
		final Lock testLock = new Lock();
		final Condition2 testCondition = new Condition2(testLock);
		
		Runnable A = new Runnable() {
			public void run() {
				testLock.acquire();
				System.out.println("ThreadA has the lock! Trying to wake and wakeAll.");
				testCondition.wake();
				testCondition.wakeAll();
				testLock.release();
			}
		};
		
		Runnable B = new Runnable() {
			public void run() {	
				testLock.acquire();
				System.out.println("ThreadB has the lock! There was nothing to wake.");
				testLock.release();
			}
		};
		
		KThread ThreadA = new KThread(A);
		KThread ThreadB = new KThread(B);
		ThreadA.fork();
		ThreadB.fork();
		ThreadA.join();
		ThreadB.join();
	}
	
	public static void selfTest4(){
		System.out.println("Running condition test case 4");
		final Lock testLock = new Lock();
		final Condition2 testCondition = new Condition2(testLock);
		
		Runnable A = new Runnable() {
			public void run() {
				testLock.acquire();
				System.out.println("ThreadA has the lock!");
				testCondition.sleep();
				System.out.println("ThreadA is awake!"); //Should display after B's messages
			}
		};
		
		Runnable B = new Runnable() {
			public void run() {	
				System.out.println("ThreadB is the current thread!");
				testCondition.sleep();
				System.out.println("ThreadB failed to sleep!");
				testCondition.wake();
				testCondition.wakeAll();
				System.out.println("ThreadB failed to wake!");
				testLock.acquire();
				System.out.println("ThreadB now has the lock!");
				testCondition.wake();
				testLock.release();
			}
		};
		
		KThread ThreadA = new KThread(A);
		KThread ThreadB = new KThread(B);
		ThreadA.fork();
		ThreadB.fork();
		ThreadA.join();
		ThreadB.join();
	}
    private LinkedList<KThread> waitList = new LinkedList<KThread>();
    private Lock conditionLock;
}
