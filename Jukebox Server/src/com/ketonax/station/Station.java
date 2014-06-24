package com.ketonax.station;

import java.util.ArrayList;
import java.util.HashMap;

import com.ketonax.networking.NamingService;

import edu.rutgers.winlab.jmfapi.GUID;

public class Station {

	private String stationName;
	private GUID stationGUID;
	private ArrayList<GUID> userList = null;
	private ArrayList<GUID> songList = null;
	private HashMap<GUID, Integer> songLengthMap = null;
	private HashMap<GUID, GUID> songSourceMap = null;

	public Station(String stationName) {

		/* Initialize variables */
		this.stationName = stationName;
		userList = new ArrayList<GUID>();
		songList = new ArrayList<GUID>();
		songLengthMap = new HashMap<GUID, Integer>();
		songSourceMap = new HashMap<GUID, GUID>();
		stationGUID = NamingService.assignGUID();
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

	public void removeUser(GUID userGUID) throws StationException {

		if (!userList.contains(userGUID))
			throw new StationException("User (GUID: " + userGUID.getGUID()
					+ ") is not on station (GUID: " + getGUID().getGUID()
					+ ") list.");
		userList.remove(userGUID);
	}

	public ArrayList<GUID> getUsers() throws StationException {
		/* Contains list of users on this station */

		if (userList.isEmpty())
			throw new StationException("User list is empty.");

		return userList;
	}

	public void addSong(GUID userGUID, GUID songGUID, int songLength)
			throws StationException {

		if (!userList.contains(userGUID))
			throw new StationException("Cannot add music. User (GUID: "
					+ userGUID.getGUID()
					+ ") is not part of this station (GUID: "
					+ getGUID().getGUID() + ").");

		if (songLengthMap.containsKey(songGUID))
			throw new StationException("Song (GUID: " + songGUID.getGUID()
					+ ") is already on this station (GUID: "
					+ getGUID().getGUID() + ") playlist.");

		songList.add(songGUID);
		songSourceMap.put(songGUID, userGUID);
		songLengthMap.put(songGUID, songLength);
	}

	public GUID getSongSource(GUID songGUID) throws StationException {
		/* Returns the GUID of the user device holding the given song */

		if (!songSourceMap.containsKey(songGUID))
			throw new StationException("Song (GUID: " + songGUID
					+ " is not on list.");

		return songSourceMap.get(songGUID);
	}

	public int getSongLength(GUID songGUID) throws StationException {
		/* Returns the song length in milliseconds */

		if (!songLengthMap.containsKey(songGUID))
			throw new StationException("Song (GUID: " + songGUID
					+ " is not on list.");

		return songLengthMap.get(songGUID);
	}

	public ArrayList<GUID> getPlayList() {
		return songList;
	}

	@Override
	public String toString() {
		return stationName;
	}
}
