package nachos.threads;

import nachos.machine.*;
import java.util.LinkedList;

public class ReactWater{
	
	private LinkedList<KThread> HydrogenList;
	private LinkedList<KThread> OxygenList;
	private int oCount;
	private int hCount;
	private Lock lock;
	
    /** 
     *   Constructor of ReactWater
     **/
    public ReactWater() {
    	HydrogenList=new LinkedList<KThread>();
		OxygenList=new LinkedList<KThread>();
    	oCount=0;
    	hCount=0;
    	lock=new Lock();

    } // end of ReactWater()

    /** 
     *   When H element comes, if there already exist another H element 
     *   and an O element, then call the method of Makewater(). Or let 
     *   H element wait in line. 
     **/ 
    public void hReady() {
    	hCount++;
		HydrogenList.add(KThread.currentThread());
		MakeWater();
    	
    } // end of hReady()
 
    /** 
     *   When O element comes, if there already exist another two H
     *   elements, then call the method of Makewater(). Or let O element
     *   wait in line. 
     **/ 
    public void oReady() {
    	oCount++;
		OxygenList.add(KThread.currentThread());
		MakeWater();
    } // end of oReady()
    
    /** 
     *   Print out the message of "water was made!".
     **/
    public void MakeWater() {
    	while(hCount >= 2 && oCount >= 1){
			System.out.println("Enough atoms for water to be made.");//debug message
			hCount -= 2;
			oCount--;
			HydrogenList.removeFirst();
			HydrogenList.removeFirst();
			OxygenList.removeFirst();
			System.out.println("Water was made.");
		}	
    } // end of Makewater()
    
    /**
     * This test case will demonstrate the effect of having atoms that are 
     * insufficient to make a water. It can be shown with any amount that 
     * does not contain both 2 hydogens and an oxygen in the waiting queue.
     */
    public static void selfTest1(){
    	final ReactWater reactWater = new ReactWater();
    	Runnable A = new Runnable(){
    		public void run(){
    			System.out.println("Hydrogen is about to be prepared!");
    			reactWater.hReady();
    		}
    	};
    	Runnable B = new Runnable(){
    		public void run(){
    			System.out.println("Oxygen is about to be prepared!");
    			reactWater.oReady();
    		}
    	};
    	KThread thread1 = new KThread(A);
    	KThread thread2 = new KThread(B);
    	thread1.fork();
    	thread2.fork();
    	//thread1.join();
    	//thread2.join();
    } // end of first test
    
    /**
     * This test case will demonstrate the effect of having sufficient 
     * atoms to create exactly one water. The effect will be one call 
     * of makeWater, causing all threads to be returned.
     */
    public static void selfTest2(){
    	final ReactWater reactWater = new ReactWater();
    	Runnable A = new Runnable(){
    		public void run(){
    			System.out.println("Hydrogen is about to be prepared!");
    			reactWater.hReady();
    		}
    	};
    	Runnable B = new Runnable(){
    		public void run(){
    			System.out.println("Oxygen is about to be prepared!");
    			reactWater.oReady();
    		}
    	};
    	KThread thread1 = new KThread(A);
    	KThread thread2 = new KThread(B);
    	KThread thread3 = new KThread(A);
    	thread1.fork();
    	thread2.fork();
    	thread3.fork();
    	thread1.join();
    	thread2.join();
    	thread3.join();
    } // end of second test
    
    /**
     * This test case will demonstrate the effect of having abundant 
     * atoms. The effect will be: threads are added to the ready queue 
     * until sufficient atoms for makeWater exist, makeWater will be 
     * called returning 2 hydrogen and 1 oxygen leaving any extra atoms 
     * as sleeping threads. This effect will continue until all water 
     * that can be made is made. Either all threads will be returned or 
     * remaining upaired atoms will wait indefinitely.
     */
    public static void selfTest3(){
    	final ReactWater reactWater = new ReactWater();
    	Runnable A = new Runnable(){
    		public void run(){
    			System.out.println("Hydrogen is about to be prepared!");
    			reactWater.hReady();
    		}
    	};
    	Runnable B = new Runnable(){
    		public void run(){
    			System.out.println("Oxygen is about to be prepared!");
    			reactWater.oReady();
    		}
    	};
    	KThread thread1 = new KThread(A);
    	KThread thread2 = new KThread(B);
    	KThread thread3 = new KThread(A);
    	KThread thread4 = new KThread(B);
    	KThread thread5 = new KThread(B);
    	KThread thread6 = new KThread(A);
    	KThread thread7 = new KThread(A);
    	KThread thread8 = new KThread(B);
    	KThread thread9 = new KThread(A);
    	KThread thread10 = new KThread(A);
    	KThread thread11 = new KThread(B);
    	KThread thread12 = new KThread(A);
    	KThread thread13 = new KThread(A);
    	KThread thread14 = new KThread(B);
    	KThread thread15 = new KThread(A);
    	KThread thread16 = new KThread(A);
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
    } // end of third test
    
} // end of class ReactWater

