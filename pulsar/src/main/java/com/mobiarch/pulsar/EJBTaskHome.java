package com.mobiarch.pulsar;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface EJBTaskHome extends EJBHome {
	public EJBTask create()
		    throws CreateException, RemoteException;
}
