package com.pan;

import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * 测试队列,生产者模式
 * 
 * @author Pan
 *
 */
public class TestQueue {
	static int queueSize = 10000;//队列大小
	
	static int testType = 1;//1=同步队列,2=普通队列(这种方式不可取,只是一个演示)
	
	//实现1:同步队列
	private ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<>(queueSize);
	
	//实现2:普通队列
	private LinkedList<String> list = new LinkedList<>();

	public static void main(String[] args) {
		TestQueue test = new TestQueue();

		for (int i = 0; i < 2; i++) {//启动生产者
			test.new Producer(i).start();
		}
		
		for (int i = 0; i < 4; i++) {//启动消费者
			test.new Consumer(i).start();
		}

	}

	class Consumer extends Thread {
		int id = 0;
		public Consumer(int id) {
			this.id = id;
		}
		
		@Override
		public void run() {
			consume();
		}

		private void consume() {
			while (true) {
				try {
					if(testType == 1) {//实现1:同步队列
						String obj = queue.take();//用来从队首取元素，如果队列为空，则等待
						System.out.println(id + "---从同步队列取走一个元素:" + obj);
					}else {//实现2:普通队列
						synchronized (list) {
							if(!list.isEmpty()) {
								String obj = list.pollFirst();
								if(obj != null) {
									System.out.println(id + "---从普通队列取走一个元素:" + obj);
								}
							}else{//else就空跑!
								continue;
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	class Producer extends Thread {
		int id = 0;
		int index = 0;
		public Producer(int id) {
			this.id = id;
		}
		
		@Override
		public void run() {
			produce();
		}

		private void produce() {
			while (true) {
				try {
					Thread.sleep(1000);
					String obj = id + "--插入的:" + (index++);
					
					if(testType == 1) {//实现1:同步队列
						queue.put(obj);//方法用来向队尾存入元素，如果队列满，则等待
					}else {//实现2:普通队列
						list.add(obj);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
