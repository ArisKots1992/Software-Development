package main;

import java.awt.FontFormatException;
import java.io.IOException;
import java.net.UnknownHostException;

import database.DBfunctions;
import gui.GUI;
import server.WSConnector;

public class Main {
	final static DBfunctions dbfunctions = new DBfunctions();
	
	public static void main(String[] args) {
		WSConnector.getInstance();			// publish serer's WS
		try {
			GUI gui = new GUI();			// start GUI
		} catch (FontFormatException e) {
			//e.printStackTrace();
		} catch (IOException e) {
			//e.printStackTrace();
		}
		
	}
	
}
