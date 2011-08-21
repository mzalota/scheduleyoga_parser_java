package com.scheduleyoga.dao;

import org.hibernate.Session;

public interface IDBAccess {
	public abstract Session openSession();
}
