package com.comsysto.android.appone.service;

public class OnlineBackupServiceFactory {

	public static OnlineBackupService service;
	
	public static OnlineBackupService getOnlineBackupService(){
		if (service == null){
			service = new OnlineBackupServiceMock();
		}
		return service;
		
	}
}
