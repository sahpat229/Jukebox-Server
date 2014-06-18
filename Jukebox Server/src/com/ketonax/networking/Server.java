package com.ketonax.networking;

import edu.rutgers.winlab.jmfapi.GUID;

public class Server {
	
	static GUID SERVER_GUID;

	/**
	 * Server checks the beginning of messages for command strings Separate
	 * multiple strings with ',' Creating a new station:
	 * "/new_station,station name"
	 * 
	 * Adding a user to a station: "/add_user,stationGUID,userGUID"
	 * 
	 * Adding a song to a station: "/add_song,songGUID,userGUID"
	 * */
	/* Server Commands */
	String CREATE_STATION_CMD = "/new_station";
	String ADD_USER_CMD = "/add_user";
	String ADD_SONG_CMD = "/add_song";

	/**
	 * Multiple strings are separated with ','
	 * */
	/* Response Headers */
	String USER_GUID_RESPONSE = "/user_GUID_value"; // For returning GUID to
													// requesting device.
	String SONG_GUID_RESPONSE = "/song_GUID_value";

	public static void main(String[] args) {

		SERVER_GUID = NamingService.assignGUID();
	}
}
