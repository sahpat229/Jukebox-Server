package com.ketonax.networking;

import java.util.ArrayList;
import java.util.HashMap;

import com.ketonax.station.Station;
import com.ketonax.station.StationException;

import edu.rutgers.winlab.jmfapi.GUID;

public class Server {

	static GUID SERVER_GUID;
	static ArrayList<Station> jbStationList = null;
	static HashMap<GUID, Station> stationMap = null;
	static ArrayList<GUID> allUsers = null;

	/*
	 * Server checks the beginning of messages for command strings Separate
	 * multiple strings with ',' Creating a new station:
	 * "/new_station,station name,userGUID"
	 * 
	 * Adding a user to a station: "/join_station,stationName,userGUID"
	 * 
	 * Adding a song to a station: "/add_song,songGUID,userGUID"
	 */
	/* Commands To Server */
	static String CREATE_STATION_CMD = "/new_station";
	static String JOIN_STATION_CMD = "/join_station";
	static String LEAVE_STATION_CMD = "/leave_station";
	static String ADD_SONG_CMD = "/add_song";

	/**
	 * Multiple strings are separated with ','
	 * */
	/* Response Headers */
	static String USER_GUID_RESPONSE = "/user_GUID_value"; // Return GUID.

	/* Commands To Devices */
	static String PLAY_SONG_GUID_CMD = "/play_song"; // Return song GUID.

	public static void main(String[] args) {

		/* Initialize variables */
		SERVER_GUID = NamingService.assignGUID();
		jbStationList = new ArrayList<Station>();
		stationMap = new HashMap<GUID, Station>();
		allUsers = new ArrayList<GUID>();

		/* Test Functions */
		GUID[] users = new GUID[3];
		GUID[] songs = new GUID[3];

		for (int i = 0; i < 3; i++) {
			users[i] = NamingService.assignGUID();
			songs[i] = NamingService.assignGUID();
		}

		try {
			createStation("Station 1", users[0]);
			createStation("Station 2", users[2]);
		} catch (ServerException e) {
			System.err.println(e.getMessage());
		}

		try {
			joinStation(users[0], jbStationList.get(0).getGUID());
			joinStation(users[1], jbStationList.get(0).getGUID());
			joinStation(users[2], jbStationList.get(0).getGUID());

			jbStationList.get(0).addSong(users[0], songs[0], 1000);
			jbStationList.get(0).addSong(users[1], songs[0], 1000);
			jbStationList.get(0).addSong(users[2], songs[0], 1000);

		} catch (StationException e) {
			System.err.println(e.getMessage());
		} catch (ServerException e) {
			System.err.println(e.getMessage());
		}
		
		/*Test End*/
	}

	public static void createStation(String stationName, GUID userGUID)
			throws ServerException {

		Station station = new Station(stationName);
		station.addUser(userGUID);

		// Add user to allUsers list
		if (!allUsers.contains(userGUID))
			allUsers.add(userGUID);

		if (!jbStationList.contains(station)) {
			jbStationList.add(station);
			stationMap.put(station.getGUID(), station);
		} else
			throw new ServerException("Station not on jbStationList.");
	}

	public static void joinStation(GUID userGUID, GUID stationGUID)
			throws ServerException {
		/* Add user to a station */

		// Add user to allUsers list
		if (!allUsers.contains(userGUID))
			allUsers.add(userGUID);

		if (stationMap.containsKey(stationGUID))
			stationMap.get(stationGUID).addUser(userGUID);
		else
			throw new ServerException("stationGUID (" + stationGUID.getGUID()
					+ ") not found on stationMap.");

	}

	public static void leaveStation(GUID userGUID, GUID stationGUID)
			throws ServerException {
		/* Remove user from a station */

		if (stationMap.containsKey(stationGUID)) {
			try {
				stationMap.get(stationGUID).removeUser(userGUID);
			} catch (StationException e) {
				System.err.println(e.getMessage());
			}
		} else
			throw new ServerException("stationGUID not found on stationMap.");
	}

	public static ArrayList<Station> sendStationList() {
		/* Sends the a list of the station names to all devices */
		return (jbStationList);
	}
}
