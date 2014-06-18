package com.ketonax.station;

import java.util.ArrayList;
import java.util.HashMap;

import com.ketonax.networking.NamingService;

import edu.rutgers.winlab.jmfapi.GUID;

public class Station {

	private String stationName;
	private GUID stationGUID;
	private ArrayList<GUID> userList = null;
	private HashMap<GUID, Integer> playlist = null;
	private HashMap<GUID, GUID> songSourceList = null;

	public Station() {

		/* Initialize variables */
		userList = new ArrayList<GUID>();
		playlist = new HashMap<GUID, Integer>();
		stationGUID = NamingService.assignGUID();
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getName() {
		return stationName;
	}

	public GUID getGUID() {
		return stationGUID;
	}

	public void addUser(GUID userGUID) {
		userList.add(userGUID);
	}

	public ArrayList<GUID> getUsers() throws StationException {

		if (userList.isEmpty())
			throw new StationException("User list is empty.");

		return userList;
	}

	public void addSong(GUID userGUID, GUID songGUID, int songLength)
			throws StationException {

		if (playlist.containsKey(songGUID))
			throw new StationException("Song is already on the list.");

		songSourceList.put(songGUID, userGUID);
		playlist.put(songGUID, songLength);
	}
}
