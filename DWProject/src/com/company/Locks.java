package com.company;

import java.util.HashMap;

public class Locks {

	private HashMap<String, Boolean> map = new HashMap<>();  //this map contains the information about which table has been locked.
	private static Locks lock = null;

	private Locks() {
	}

	static synchronized public Locks getInstance() {  //singleton pattern so that only one instance can access the map
		if (lock == null) {
			lock = new Locks();
		}
		return lock;
	}

	public synchronized boolean addLockToTable(String table) {   //add a lock to the table by setting the table name and
		String threadName = Thread.currentThread().getName();
		// to display which thread is requesting for the lock
		if (map.containsKey(table) == false || map.get(table) == false) {
			System.out.println(threadName+" has acquired lock");
			map.put(table, true);
			return true;
		} else
			return false;
	}

	public synchronized boolean removeLockforTable(String table) {
		System.out.println("Lock acquired by "+Thread.currentThread().getName()+" has is being released now");
		if (map.get(table) == true) {
			map.put(table, false);
			return true;
		} else
			return false;
	}

}
