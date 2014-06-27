package com.ketonax.station;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.ketonax.networking.NamingService;

import edu.rutgers.winlab.jmfapi.GUID;

public class Station implements Runnable {

	private String stationName;
	private GUID stationGUID;

	private List<GUID> userList = null;
	private List<GUID> songList = null;
	private Map<GUID, Integer> songLengthMap = null;
	private Map<GUID, GUID> songSourceMap = null;

	private boolean stopRunning = false;

	/* Commands To Devices */
	private static final String PLAY_SONG_GUID_CMD = "/play_song";

	/* Commands from devices */
	private static final String JOIN_STATION_CMD = "/join_station";
	private static final String LEAVE_STATION_CMD = "/leave_station";
	private static final String ADD_SONG_CMD = "/add_song";

	public Station(String stationName) {

		/* Initialize variables */
		this.stationName = stationName;
		userList = Collections.synchronizedList(new ArrayList<GUID>());
		songList = Collections.synchronizedList(new ArrayList<GUID>());
		songLengthMap = Collections
				.synchronizedMap(new HashMap<GUID, Integer>());
		songSourceMap = Collections.synchronizedMap(new HashMap<GUID, GUID>());
		stationGUID = NamingService.assignGUID();

		/* Start station */
		Sender Sender = new Sender();
		Sender.start();
	}

	public String getName() {
		return stationName;
	}

	public GUID getGUID() {
		return stationGUID;
	}

	public void addUser(GUID userGUID) {
		synchronized (userList) {
			userList.add(userGUID);
		}
	}

	public void removeUser(GUID userGUID) throws StationException {

		synchronized (userList) {
			if (!userList.contains(userGUID))
				throw new StationException("User (GUID: " + userGUID.getGUID()
						+ ") is not on station (GUID: " + getGUID().getGUID()
						+ ") list.");
			userList.remove(userGUID);
		}
	}

	public List<GUID> getUsers() throws StationException {
		/* Contains list of users on this station */

		if (userList.isEmpty())
			throw new StationException("User list is empty.");

		return userList;
	}

	public void addSong(GUID userGUID, GUID songGUID, int songLength)
			throws StationException {

		synchronized (songList) {
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
		}

		synchronized (songSourceMap) {
			songSourceMap.put(songGUID, userGUID);
		}

		synchronized (songLengthMap) {
			songLengthMap.put(songGUID, songLength);
		}
	}

	public void removeSong(GUID songGUID) throws StationException {

		synchronized (songList) {
			// TODO
		}
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

	public void halt() {
		stopRunning = true;
	}

	public boolean hasStopped() {
		return stopRunning;
	}

	public boolean isEmpty() {
		return userList.isEmpty();
	}

	@Override
	public String toString() {
		return stationName;
	}

	/** Station controls the flow of the playlist for its users. */
	public void run() {
		// TODO Coordinate playlist play back for all devices

		stopRunning = false;
		while (stopRunning == false) {

			synchronized (songList) {
				Iterator<GUID> it = songList.iterator();
				while (it.hasNext()) {
					
					/*
					 * if(reciveSong)
					 * {
					 * 		send devices songlist
					 * }
					 * 
					 */
					GUID song = it.next();
					playSong(songSourceMap.get(it), song,
							songLengthMap.get(song));
					it.remove(); // Remove played song from the playlist
				}
			}

			synchronized (userList) {

				if (userList.isEmpty())
					halt();
			}
		}

		System.out.println(stationName + " has stopped running.");
	}

	private void playSong(GUID userGUID, GUID songGUID, int songLength) {
		/* Establish a socket connection and play song with GUID songGUID */

		/* Demo */
		System.out.println(stationName + " now streaming song (GUID: "
				+ songGUID.getGUID() + ") from user (GUID: "
				+ songSourceMap.get(songGUID).getGUID() + ")."
				+ " Total songs played = " + songSourceMap.size());
		/* Demo End */
		// TODO Send command to device to play song
		try {
			Thread.sleep(songLength);
		} catch (InterruptedException e) {
			System.err.println(e.getMessage());
		}
	}

	private class Sender extends Thread {

		int size = 10;
		GUID[] user = new GUID[size];

		public void run() {

			/* Demo Begin */
			// Assign GUIDs to users and songs
			for (int i = 0; i < size; i++) {
				user[i] = NamingService.assignGUID();
				// song[i] = NamingService.assignGUID();
				addUser(user[i]);
			}

			// Add songs to station
			GUID songID;
			try {

				Random generator = new Random();
				for (int i = 0; i < size; i++) {

					int x = generator.nextInt(10) + 1;
					sleep(x * 1000);
					songID = NamingService.assignGUID();
					int songLength = generator.nextInt(30000) + 1000;
					addSong(user[x % 3], songID, songLength);
				}
			} catch (StationException e) {
				// TODO Auto-generated catch block
				System.err.println(e.getMessage());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			synchronized (userList) {
				if (!userList.isEmpty()) {

					try {
						sleep(1000); // 600000 for demo
						Iterator<GUID> it = userList.iterator();
						while (it.hasNext()) {
							it.next();
							it.remove();
						}
					} catch (InterruptedException e) {
						System.err.println(e.getMessage());
					}
				}
			}
			/* Demo End */
		}
	}
}
