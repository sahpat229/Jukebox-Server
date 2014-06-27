package com.ketonax.networking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.ketonax.station.Station;

import edu.rutgers.winlab.jmfapi.GUID;

public class Server {

	static GUID SERVER_GUID;
	static List<Station> stationList = null;
	static ArrayList<GUID> allUsers = null;


	/* Commands To Devices */

	/* Commands To Server */
	private static final String REQUEST_DEVICE_GUID_CMD = "/request_device_guid";
	private static final String REQUEST_SONG_GUID_CMD = "/request_song_guid";
	private static final String CREATE_STATION_CMD = "/new_station";
	private static final String STATION_LIST_REQUEST_CMD = "/request_station_list";

	/* Notification to devices */
	private static final String STATION_KILLED_NOTIFIER = "/station_terminated";
	private static final String RECEIVE_STATION_LIST_NOTIFIER = "/station_list_updated";

	/* Response to devices */
	private static final String USER_GUID_RESPONSE = "/user_guid_value";
	private static final String SONG_GUID_RESPONSE = "/song_guid_value";
	
	public static void main(String[] args) {

		/* Initialize variables */
		SERVER_GUID = NamingService.assignGUID();
		stationList = Collections.synchronizedList(new ArrayList<Station>());
		allUsers = new ArrayList<GUID>();

		/* Demo Begin */
		GUID user = NamingService.assignGUID();
		try {
			String stationHeader = "Station ";

			for (int i = 0; i < 10; i++) {

				String stationName = null;
				stationName = stationHeader + (i + 1);
				createStation(stationName, user);
			}
		} catch (ServerException e) {
			System.err.println(e.getMessage());
		}
		/* Demo End */

		while (true) {
			/* Listen for incoming commands */
			// TODO MobilityFirst network magic

			/* Check to see if stations are running */
			synchronized (stationList) {

				Iterator<Station> it = stationList.iterator();
				while (it.hasNext()) {
					Station s = it.next();
					if (s.hasStopped()) {
						it.remove();
						// TODO send updated station list to all users
					}
				}
			}
		}
	}

	public static void createStation(String stationName, GUID userGUID)
			throws ServerException {

		Station station = new Station(stationName);
		station.addUser(userGUID);

		// Add user to allUsers list
		if (!allUsers.contains(userGUID))
			allUsers.add(userGUID);

		synchronized (stationList) {
			if (!stationList.contains(station)) {
				stationList.add(station);
				new Thread(station).start();// Start new station thread
			} else
				throw new ServerException("Station not on stationList.");
		}
	}


	public static List<Station> sendStationList(GUID userGUID) {
		/* Sends the a list of the station names to all devices */
		return stationList;
	}
}
