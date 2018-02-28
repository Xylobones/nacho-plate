package nachos.threads;

import nachos.machine.*;
import java.util.LinkedList;

/**
 *   An implementation of ReactWater using a lock and condition variables. 
 *   This allows for synchronization of making water when hydrogen and 
 *   oxygen threads arrive in unpredictable order.
 */
public class ReactWater{
	
	private Condition2 Hydrogen;
	private Condition2 Oxygen;
	private int oCount;
	private int hCount;
	private Lock lock;
	
    /** 
     *   Constructor of ReactWater
     *   Allocate a lock and conditions to allow synchronization of making 
     *   water. Initialize hydrogena nd oxygen counts to 0.
     **/
    public ReactWater() {
		lock=new Lock();
    	Hydrogen = new Condition2(lock);
		Oxygen = new Condition2(lock);
    	oCount=0;
    	hCount=0;
    } // end of ReactWater()

    /** 
     *   When H element comes, add to count and acquire lock. Then, 
     *   if there already exist another H element and an O element, 
     *   wake them, then call the method of Makewater(). 
     *   Or let H element wait in line. 
     **/ 
    public void hReady() {
    	hCount++;
		lock.acquire();
		if(hCount >= 2 && oCount >= 1){
			Hydrogen.wake();
			Oxygen.wake();
		}	
		else
			Hydrogen.sleep();
		MakeWater();
    	
    } // end of hReady()
 
    /** 
     *   When O element comes, add to count and acquire lock. Then, 
     *   if there already exist another two H elements, wake them, 
     *   then call the method of Makewater(). 
     *   Or let O element wait in line. 
     **/ 
    public void oReady() {
    	oCount++;
		lock.acquire();
		if(hCount >= 2 && oCount >= 1){
			Hydrogen.wake();
			Hydrogen.wake();
		}	
		else
			Oxygen.sleep();
		MakeWater();
    } // end of oReady()
    
    /** 
     *   Verify the number of atoms while still holding lock. 
     *   If sufficient, remove them from counts.
     *   Print out the message of "water was made!".
     *   Release the lock.
     **/
    public void MakeWater() {
    	while(hCount >= 2 && oCount >= 1){
			System.out.print("Enough atoms for water to be made. - ");//debug message
			hCount -= 2;
			oCount--;
			System.out.println("Water was made.");
		}
		lock.release();
    } // end of Makewater()
    
    /**
     * This test case will demonstrate the effect of having atoms that are 
     * insufficient to make a water. It can be shown with any amount that 
     * does not contain both 2 hydogens and an oxygen in the waiting queue.
     */
    public static void selfTest1(){
    	System.out.println("Start of test case 1:");
    	final ReactWater reactWater = new ReactWater();
    	Runnable H = new Runnable(){
    		public void run(){
    			System.out.println("Hydrogen is about to be prepared!");
    			reactWater.hReady();
    		}
    	};
    	Runnable O = new Runnable(){
    		public void run(){
    			System.out.println("Oxygen is about to be prepared!");
    			reactWater.oReady();
    		}
    	};
    	KThread thread1 = new KThread(H);
    	KThread thread2 = new KThread(O);
    	thread1.fork();
    	thread2.fork();
    	thread1.join();
    	thread2.join();
    	System.out.println("End of test case 1.");
    } // end of first test
    
    /**
     * This test case will demonstrate the effect of having sufficient 
     * atoms to create exactly one water. The effect will be one call 
     * of makeWater, causing all threads to be returned.
     */
    public static void selfTest2(){
    	System.out.println("Start of test case 2:");
    	final ReactWater reactWater = new ReactWater();
    	Runnable H = new Runnable(){
    		public void run(){
    			System.out.println("Hydrogen is about to be prepared!");
    			reactWater.hReady();
    		}
    	};
    	Runnable O = new Runnable(){
    		public void run(){
    			System.out.println("Oxygen is about to be prepared!");
    			reactWater.oReady();
    		}
    	};
    	KThread thread1 = new KThread(H);
    	KThread thread2 = new KThread(O);
    	KThread thread3 = new KThread(H);
    	thread1.fork();
    	thread2.fork();
    	thread3.fork();
    	thread1.join();
    	thread2.join();
    	thread3.join();
    	System.out.println("End of test case 2.");
    } // end of second test
    
    /**
     * This test case will demonstrate the effect of having abundant 
     * atoms. The effect will be: threads are added to the ready queue 
     * until sufficient atoms for makeWater exist, makeWater will be 
     * called returning 2 hydrogen and 1 oxygen leaving any extra atoms 
     * as sleeping threads. This effect will continue until all water 
     * that can be made is made. Either all threads will be returned or 
     * remaining upaired atoms will wait indefinitely. In this case, 
     * there will be an unpaired oxygen left over.
     */
    public static void selfTest3(){
    	System.out.println("Start of test case 3:");
    	final ReactWater reactWater = new ReactWater();
    	Runnable H = new Runnable(){
    		public void run(){
    			System.out.println("Hydrogen is about to be prepared!");
    			reactWater.hReady();
    		}
    	};
    	Runnable O = new Runnable(){
    		public void run(){
    			System.out.println("Oxygen is about to be prepared!");
    			reactWater.oReady();
    		}
    	};
    	KThread thread1 = new KThread(H);
    	KThread thread2 = new KThread(O);
    	KThread thread3 = new KThread(H);
    	KThread thread4 = new KThread(O);
    	KThread thread5 = new KThread(O);
    	KThread thread6 = new KThread(H);
    	KThread thread7 = new KThread(H);
    	KThread thread8 = new KThread(O);
    	KThread thread9 = new KThread(H);
    	KThread thread10 = new KThread(H);
    	KThread thread11 = new KThread(O);
    	KThread thread12 = new KThread(H);
    	KThread thread13 = new KThread(H);
    	KThread thread14 = new KThread(O);
    	KThread thread15 = new KThread(H);
    	KThread thread16 = new KThread(H);
    	thread1.fork();
    	thread2.fork();
    	thread3.fork();
    	thread4.fork();
    	thread5.fork();
    	thread6.fork();
    	thread7.fork();
    	thread8.fork();
    	thread9.fork();
    	thread10.fork();
    	thread11.fork();
    	thread12.fork();
    	thread13.fork();
    	thread14.fork();
    	thread15.fork();
    	thread16.fork();
    	thread1.join();
    	thread2.join();
    	thread3.join();
    	thread4.join();
    	thread5.join();
    	thread6.join();
    	thread7.join();
    	thread8.join();
    	thread9.join();
    	thread10.join();
    	thread11.join();
    	thread12.join();
    	thread13.join();
    	thread14.join();
    	thread15.join();
    	thread16.join();
    	System.out.println("End of test case 3.");
    } // end of third test
    
    /**
     * This test case will demonstrate the effect of having abundant 
     * atoms. The effect will be: threads are added to the ready queue 
     * until sufficient atoms for makeWater exist, makeWater will be 
     * called returning 2 hydrogen and 1 oxygen leaving any extra atoms 
     * as sleeping threads. This effect will continue until all water 
     * that can be made is made. Either all threads will be returned or 
     * remaining upaired atoms will wait indefinitely. In this case, 
     * all atoms will be paired and the machine can exit.
     */
    public static void selfTest4(){
    	System.out.println("Start of test case 4: ");
    	final ReactWater reactWater = new ReactWater();
    	Runnable H = new Runnable(){
    		public void run(){
    			System.out.println("Hydrogen is about to be prepared!");
    			reactWater.hReady();
    		}
    	};
    	Runnable O = new Runnable(){
    		public void run(){
    			System.out.println("Oxygen is about to be prepared!");
    			reactWater.oReady();
    		}
    	};
    	KThread thread1 = new KThread(H);
    	KThread thread2 = new KThread(H);
    	KThread thread3 = new KThread(H);
    	KThread thread4 = new KThread(O);
    	KThread thread5 = new KThread(O);
    	KThread thread6 = new KThread(H);
    	KThread thread7 = new KThread(H);
    	KThread thread8 = new KThread(O);
    	KThread thread9 = new KThread(H);
    	KThread thread10 = new KThread(H);
    	KThread thread11 = new KThread(O);
    	KThread thread12 = new KThread(H);
    	KThread thread13 = new KThread(H);
    	KThread thread14 = new KThread(O);
    	KThread thread15 = new KThread(H);
    	thread1.fork();
    	thread2.fork();
    	thread3.fork();
    	thread4.fork();
    	thread5.fork();
    	thread6.fork();
    	thread7.fork();
    	thread8.fork();
    	thread9.fork();
    	thread10.fork();
    	thread11.fork();
    	thread12.fork();
    	thread13.fork();
    	thread14.fork();
    	thread15.fork();
    	thread1.join();
    	thread2.join();
    	thread3.join();
    	thread4.join();
    	thread5.join();
    	thread6.join();
    	thread7.join();
    	thread8.join();
    	thread9.join();
    	thread10.join();
    	thread11.join();
    	thread12.join();
    	thread13.join();
    	thread14.join();
    	thread15.join();
    	System.out.println("End of test case 4.");
    } // end of fourth test
    
} // end of class ReactWater
