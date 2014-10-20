package com.mobiarch.pulsar;

import java.io.Serializable;
import java.rmi.RemoteException;

public interface Task extends Serializable {
	 public void execute()
			    throws RemoteException, Exception;
}
