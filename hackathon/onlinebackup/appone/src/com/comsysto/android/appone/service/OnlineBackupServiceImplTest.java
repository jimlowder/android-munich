package com.comsysto.android.appone.service;

import android.test.InstrumentationTestCase;

public class OnlineBackupServiceImplTest extends InstrumentationTestCase {


	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
	}
	
	
	public void testConnection (){
		OnlineBackupServiceImpl service = new OnlineBackupServiceImpl();
		service.getContacts();
	}
}
