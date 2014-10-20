package com.mobiarch.pulsar.test;

import java.rmi.RemoteException;
import java.util.logging.Logger;

import com.mobiarch.pulsar.Task;

public class TestTask implements Task {
	private static final long serialVersionUID = 8122298388820623514L;
	Logger logger = Logger.getLogger(getClass().getName());
	
	public void execute() throws RemoteException, Exception {
		logger.info("Scheduled task running.");
	}

}
