package com.mobiarch.pulsar.test;

import java.rmi.RemoteException;
import java.util.logging.Logger;

import javax.ejb.Stateless;

import com.mobiarch.pulsar.Task;

@Stateless
public class SimpleEJB implements Task {
	private static final long serialVersionUID = -6110222237944363619L;
	Logger logger = Logger.getLogger(getClass().getName());

	public void execute() throws RemoteException, Exception {
		logger.info("***Scheduled EJB task running.");
	}
}
