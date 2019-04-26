package dishWashS;

import java.util.concurrent.Semaphore;

public class WetDishRack {
	private int rackSize;
	private static int count = 0;
	private final Semaphore barrier;
	private final Semaphore barrier2;

	WetDishRack(int rackSize) {
		this.rackSize = rackSize;
		this.barrier = new Semaphore(0);
		this.barrier2 = new Semaphore(1);
	}

	public void addDish(int dish_id) throws InterruptedException {
		Semaphore sync = new Semaphore(1);
		count = dish_id;

		synchronized (sync) {
			if (count == rackSize) {
				synchronized (barrier) {
					barrier2.wait();
					barrier.notifyAll();
				}
			}
		}
		synchronized (barrier) {
			barrier.wait();
			barrier.notify();
		}
	}

	public int removeDish() throws InterruptedException {
		Semaphore sync = new Semaphore(1);
		synchronized (sync) {
			count -= 1;
			if (count == rackSize) {
				synchronized (barrier2) {
					barrier.wait();
					barrier2.notifyAll();
				}
			}
		}
		synchronized (barrier2) {
			barrier2.wait();
			barrier2.notify();
		}
		return count; // replace with correct code here
	}

}
