package nachos.threads;

import nachos.machine.*;

/**
 * A <i>communicator</i> allows threads to synchronously exchange 32-bit
 * messages. Multiple threads can be waiting to <i>speak</i>,
 * and multiple threads can be waiting to <i>listen</i>. But there should never
 * be a time when both a speaker and a listener are waiting, because the two
 * threads can be paired off at this point.
 */
public class Communicator {
	
	private Lock lock;				//lock variable
	private static Condition2 listenerReceiving;
	private static Condition2 speakerSending;
	private Integer wordTransfer;
	private int listener; //counters to keep the number of listeners waiting.
    /**
     * Allocate a new communicator.
     */
    public Communicator() {
    	
    	lock = new Lock();
    	listenerReceiving = new Condition2(lock);
    	speakerSending = new Condition2(lock);
    	wordTransfer = null;
    	listener = 0;
    }

    /**
     * Wait for a thread to listen through this communicator, and then transfer
     * <i>word</i> to the listener.
     *
     * <p>
     * Does not return until this thread is paired up with a listening thread.
     * Exactly one listener should receive <i>word</i>.
     *
     * @param	word	the integer to transfer.
     */
    public void speak(int word) {
    	
    	lock.acquire();
    	//if there are no listeners, or word has been spoken, send the
    	//speaker to sleep atomically.
    		while (listener == 0 || wordTransfer != null){
    			Machine.interrupt().disable();
    			speakerSending.sleep();
    			Machine.interrupt().enable();
    		}
    	lock.acquire();
    	wordTransfer = new Integer(word); //unboxing the Integer.
    	listenerReceiving.wake();
    	lock.release();
    }

    /**
     * Wait for a thread to speak through this communicator, and then return
     * the <i>word</i> that thread passed to <tt>speak()</tt>.
     *
     * @return	the integer transferred.
     */    
    public int listen() {
    	lock.acquire();
    	listener++;
    		//if there are no messages to listen for, wake up speaker,
    		//wakew speaker then send listener to sleep atomically.
    		while (wordTransfer == null){
    			Machine.interrupt().disable();
    			speakerSending.wake();
    			listenerReceiving.sleep();
    			Machine.interrupt().enable();
    		}
    	int message = wordTransfer.intValue(); //unboxing the Integer.
    	listener--; 	//reduce the amount of listeners in the queue
    	wordTransfer = null;
    	lock.release(); 	//finally, release the lock and
    						//return the message.
	return message;
    }
    
    
    /**
     * This test case will demonstrate the effects of the Communicator
     * class while having both a speaker and a communicator in the ready
     * queue.
     */
    public static void selfTest1(){ 
    	
		System.out.println("Running condition test case 1 of 5");
		final Lock testLock = new Lock();
    	final Communicator communicator = new Communicator();	
		
    	Runnable A = new Runnable(){
    		
    		public void run(){
    			testLock.acquire();
    			System.out.println("Speaker Thread speaks to listener. Awaiting Verification!");
    			communicator.speak(5);
    			speakerSending.sleep();
    			System.out.println("Yup! Speaker Thread has finished speaking.");
    			testLock.release();
    		}
    	};
    	
    	Runnable B = new Runnable(){
    		public void run(){
    			testLock.acquire();
    			System.out.println("Listener receiving word spoken by speaker. Please wait for verification.");
    			int x = communicator.listen();
    			listenerReceiving.sleep();
    			System.out.println("Listener received!!!! Is this your word?: " + x);
    			testLock.release();
    		}
    	};
    	
    	KThread thread1 = new KThread(A);
    	KThread thread2 = new KThread(B);
		thread1.fork();
		thread2.fork();
		thread1.join();
		thread2.join();
    	
    }	// End of TestCase 1.
    
    /**
     * This test case will demonstrate the effects of having one speaker and no
     * listener in the ready queue. This will be demonstrated in both ways for
     * one speaker with no listener, as well as one listener with no speaker in
     * the waiting queue. 
     */
    public void selfTest2(){
    	System.out.println("Running communicator test case 2 of 5");	
    		final Lock testLock = new Lock();
    		final Communicator communicator = new Communicator();	
		
    		Runnable A = new Runnable(){
    			public void run(){
    				testLock.acquire();
    				System.out.println("Speaker Thread speaking to listener.");
    				communicator.speak(5);
    				speakerSending.sleep();
    				System.out.println("Speaker Thread finished speaking, waiting on responce.");
    				testLock.release();				
    			}
    		};
    		
        	Runnable B = new Runnable(){
        		public void run(){
        			testLock.acquire();
        			System.out.println("Listener receiving word spoken by speaker.");
        			int x = communicator.listen();
        			listenerReceiving.sleep();
        			//here would print "Done" if there was another thread in the queue.
        			testLock.release();
        		}
        	};
    		
        	
        	KThread thread1 = new KThread(A); //speaker
        	thread1.fork();
        	thread1.join();
        	  
    }
    
    /**
     * This will test the effects of having multiple speaker or listener threads,
     * and none of the other. Assuming 3 threads, they will respectively act upon
     * their duties, and will be added to a waiting list until there is a thread of
     * the respective origin.
     */
    public void selfTest3(){
    	System.out.println("Running communicator test case 3 of 5");
		final Lock testLock = new Lock();
		final Communicator communicator = new Communicator();	
	
		Runnable A = new Runnable(){
			public void run(){
				testLock.acquire();
				System.out.println("Speaker Thread A speaking to listener.");
				communicator.speak(5);
				speakerSending.sleep();
				System.out.println("Speaker Thread A finished speaking, waiting on responce.");
				testLock.release();				
			}
		};
		
		Runnable B = new Runnable(){
			public void run(){
				testLock.acquire();
				System.out.println("Speaker Thread B speaking to listener.");
				communicator.speak(6);
				speakerSending.sleep();
				System.out.println("Speaker Thread B finished speaking, waiting on responce.");
				testLock.release();				
			}
		};
		
		Runnable C = new Runnable(){
			public void run(){
				testLock.acquire();
				System.out.println("Speaker Thread C speaking to listener.");
				communicator.speak(7);
				speakerSending.sleep();
				System.out.println("Speaker Thread C finished speaking, waiting on responce.");
				testLock.release();				
			}
		};
    	
		KThread thread1 = new KThread(A); //speaker
		KThread thread2 = new KThread(B); //speaker
		KThread thread3 = new KThread(C); //speaker
    	thread1.fork();
    	thread2.fork();
    	thread3.fork();
    	thread1.join();
    	thread2.join();
    	thread3.join();
    	
    
    	
    }
    
    /**
     * This will test the abilities of having multiple speaker threads and one listener thread.
     * The effects will be that the first instance of a communication will be printed out
     * and the rest will be waiting for a response.  
     */
    public void selfTest4(){
    	System.out.println("Running communicator test case 4 of 5");
    
		final Lock testLock = new Lock();
		final Communicator communicator = new Communicator();	
	
		Runnable A = new Runnable(){
			public void run(){
				testLock.acquire();
				System.out.println("Speaker Thread A speaking to listener.");
				communicator.speak(5);
				speakerSending.sleep();
				System.out.println("Speaker Thread A finished speaking, waiting on responce.");
				testLock.release();				
			}
		};

		Runnable B = new Runnable(){
			public void run(){
				testLock.acquire();
				System.out.println("Speaker Thread B speaking to listener.");
				communicator.speak(5);
				speakerSending.sleep();
				System.out.println("Speaker Thread B finished speaking, waiting on responce.");
				testLock.release();				
			}
		};
    	
    	Runnable C = new Runnable(){ //listener
    		public void run(){
    			testLock.acquire();
    			System.out.println("Listener receiving word spoken by speaker.");
    			int x = communicator.listen();
    			listenerReceiving.sleep();
    			System.out.println("Listener received: " + x);
    			testLock.release();
    		}
    	};
    	
		KThread thread1 = new KThread(A); //speaker
		KThread thread2 = new KThread(B); //speaker
		KThread thread3 = new KThread(C); //listener
    	thread1.fork();
    	thread2.fork();
    	thread3.fork();
    	thread1.join();
    	thread2.fork();
    	thread3.fork();
    }
    
    /**
     * This case will test a real life situation. 
     */
    public void selfTest5(){
    	System.out.println("Running communicator test case 5 of 5");
		final Lock testLock = new Lock();
		final Communicator communicator = new Communicator();
		
		Runnable A = new Runnable(){
			public void run(){
				testLock.acquire();
				System.out.println("Speaker Thread A speaking to listener.");
				communicator.speak(1);
				speakerSending.sleep();
				System.out.println("Speaker Thread A finished speaking, waiting on responce.");
				testLock.release();				
			}
		};

		Runnable B = new Runnable(){
			public void run(){
				testLock.acquire();
				System.out.println("Speaker Thread B speaking to listener.");
				communicator.speak(2);
				speakerSending.sleep();
				System.out.println("Speaker Thread B finished speaking, waiting on responce.");
				testLock.release();				
			}
		};

    	Runnable C = new Runnable(){ //listener
    		public void run(){
    			testLock.acquire();
    			System.out.println("Listener receiving word spoken by speaker.");
    			int x = communicator.listen();
    			listenerReceiving.sleep();
    			System.out.println("Listener received: " + x);
    			testLock.release();
    		}
    	};
		
    	Runnable D = new Runnable(){ //listener
    		public void run(){
    			testLock.acquire();
    			System.out.println("Listener receiving word spoken by speaker.");
    			int x = communicator.listen();
    			listenerReceiving.sleep();
    			System.out.println("Listener received: " + x);
    			testLock.release();
    		}
    	};
    	
    	Runnable E = new Runnable(){ //listener
    		public void run(){
    			testLock.acquire();
    			System.out.println("Listener receiving word spoken by speaker.");
    			int x = communicator.listen();
    			listenerReceiving.sleep();
    			System.out.println("Listener received: " + x);
    			testLock.release();
    		}
    	};
    	
		Runnable F = new Runnable(){
			public void run(){
				testLock.acquire();
				System.out.println("Speaker Thread F speaking to listener.");
				communicator.speak(3);
				speakerSending.sleep();
				System.out.println("Speaker Thread F finished speaking, waiting on responce.");
				testLock.release();				
			}
		};
    	
    	Runnable G = new Runnable(){ //listener
    		public void run(){
    			testLock.acquire();
    			System.out.println("Listener receiving word spoken by speaker.");
    			int x = communicator.listen();
    			listenerReceiving.sleep();
    			System.out.println("Listener received: " + x);
    			testLock.release();
    		}
    	};
    	
    	Runnable H = new Runnable(){ //listener
    		public void run(){
    			testLock.acquire();
    			System.out.println("Listener receiving word spoken by speaker.");
    			int x = communicator.listen();
    			listenerReceiving.sleep();
    			System.out.println("Listener received: " + x);
    			testLock.release();
    		
    	}
    };
    
    
	Runnable I = new Runnable(){
		public void run(){
			testLock.acquire();
			System.out.println("Speaker Thread F speaking to listener.");
			communicator.speak(3);
			speakerSending.sleep();
			System.out.println("Speaker Thread F finished speaking, waiting on responce.");
			testLock.release();				
		}
	};
    
	KThread thread1 = new KThread(A); //speaker
	KThread thread2 = new KThread(B); //speaker
	KThread thread3 = new KThread(C); //listener
	KThread thread4 = new KThread(D); //listener
	KThread thread5 = new KThread(E); //listener
	KThread thread6 = new KThread(F); //speaker
	KThread thread7 = new KThread(G); //listener
	KThread thread8 = new KThread(H); //listener
	KThread thread9 = new KThread(I); //speaker
	thread1.fork();
	thread2.fork();
	thread3.fork();
	thread4.fork();
	thread5.fork();
	thread6.fork();
	thread7.fork();
	thread8.fork();
	thread9.fork();
	thread1.join();
	thread2.fork();
	thread3.fork();
	thread4.fork();
	thread5.fork();
	thread6.fork();
	thread7.fork();
	thread8.fork();
	thread9.fork();
	
}
}