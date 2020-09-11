package main;

import java.io.IOException;
import java.util.*;

import network.NetworkSystem;
import utility.Logger;

public class Main {
	
	public static void main(String[] args) {
		boolean running = true;
		// Bind to network
		Optional<NetworkSystem> networkSystem = Optional.empty();
		try {
			networkSystem = Optional.of(new NetworkSystem(Integer.parseInt(args[0])));
		} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
			Logger.ERROR.log("Missing or wrong program arguments: <port:int>");
		} catch (IOException e) {
			Logger.ERROR.log("Error while binding server socket: " + e.getLocalizedMessage());
		}

		final Scanner scanner = new Scanner(System.in);
		while(running) {
			if(scanner.next().equalsIgnoreCase("stop")) {
				running = false;
			}
		}

		networkSystem.ifPresent(NetworkSystem::stop);
	}
}
