package com.sucre.myThread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.sucre.listUtil.MutiList;
import com.sucre.mainUtil.MyUtil;

/**
 * 多线程操作网络数据包! 定义为抽象类,方便回调子类的方法
 * 
 * @author sucre
 *
 */
abstract public class Thread4Net implements Runnable {
	// 抽象方法,要求子类必须覆盖.
	abstract public int doWork(int index);

	// 定义锁
	private final Lock lock = new ReentrantLock();
	// 定义列表的索引.
	private int index = 0;
	// 定义列表索引上限.
	private int uIndex = 0;
	//定义线程是否自动循环
    private boolean isCircle=false;
    //定义线程上否继续工作
    private boolean isWork=true;
	// 构造器传递索引上限
	protected Thread4Net(int u,boolean isCircle) {
		uIndex = u;
		this.isCircle=isCircle;
	}
	// 线程主方法
	public void run() {

		while(isWork){
			int p = doWork(getIndex());
			//延时操作
			//MyUtil.sleeps(9000);
			//System.out.println(p);
		}
	}

	private int getIndex() {
		int i;
		lock.lock();
		i = index;
		index++;
		if (index > uIndex) {
			index = 0;
			//告知线程停止工作
			if(!isCircle) {isWork=false;}
			
		}
		lock.unlock();
		return i;
	}
}
