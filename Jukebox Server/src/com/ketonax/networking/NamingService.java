package com.ketonax.networking;

import edu.rutgers.winlab.jmfapi.GUID;

public class NamingService {

	private static int guid = 1;

	public static GUID assignGUID() {

		GUID id = new GUID(guid);
		guid++;

		return id;
	}
}
