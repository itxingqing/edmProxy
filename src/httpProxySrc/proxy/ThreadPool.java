package httpProxySrc.proxy;

import java.util.*;

/**
 * ������
 * 
 * @author $����ǿ$
 * @version $Revision$
 */
public class ThreadPool {

	protected Thread[] threads = null;
	Collection assignments = new ArrayList(3); // ��ʼΪ3��Ԫ�أ���̬������
	protected Done done = new Done();
	private static ThreadPool pool = null;

	// ���ӳ�����-Ӧ�������ļ��ж�ȡ
	static int threadSize = 10;

	//
	public static ThreadPool getInstance() {

		if (pool == null) {

			pool = new ThreadPool(threadSize);
		}

		return pool;
	}

	/**
	 * ����һ���µ� ThreadPool ����.
	 * 
	 * @param size
	 *            ������
	 */
	private ThreadPool(int size) {

		threads = new WorkerThread[size];

		for (int i = 0; i < threads.length; i++) {

			threads[i] = new WorkerThread(this);
			threads[i].start();
		}
	}

	/**
	 * ������ע��Ҫִ�е�����
	 * 
	 * @param r
	 *            ������
	 */
	public synchronized void assign(Runnable r) {

		done.workerBegin();
		assignments.add(r);
		notify();
	}

	/**
	 * ������
	 * 
	 * @return ������
	 */
	public synchronized Runnable getAssignment() {

		try {

			while (!assignments.iterator().hasNext())
				wait();

			Runnable r = (Runnable) assignments.iterator().next();
			assignments.remove(r);

			return r;
		} catch (InterruptedException e) {

			done.workerEnd();

			return null;
		}
	}

	/**
	 * ������
	 */
	public void complete() {

		done.waitBegin();
		done.waitDone();
	}

	/**
	 * ������
	 */
	protected void finalize() {

		done.reset();

		for (int i = 0; i < threads.length; i++) {

			threads[i].interrupt();
			done.workerBegin();

			// threads[i].destroy();
		}

		done.waitDone();
	}
}

/**
 * ������ִ��������̶߳���
 * 
 * @author $author$
 * @version $Revision$
 */
class WorkerThread extends Thread {

	public boolean busy;
	public ThreadPool owner;

	/**
	 * ����һ���µ� WorkerThread ���󣬲���������.
	 * 
	 * @param o
	 *            ������
	 */
	WorkerThread(ThreadPool o) {

		owner = o;
	}

	/**
	 * ������ʵ�ֽӿ�
	 */
	public void run() {

		Runnable target = null;

		do {

			target = owner.getAssignment();

			if (target != null) {

				target.run();
				owner.done.workerEnd();
			}
		} while (target != null);
	}
}