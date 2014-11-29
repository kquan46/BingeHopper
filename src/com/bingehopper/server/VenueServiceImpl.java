package com.bingehopper.server;

import com.bingehopper.client.NotLoggedInException;
import com.bingehopper.client.VenueDetails;
import com.bingehopper.client.VenueService;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

public class VenueServiceImpl extends RemoteServiceServlet implements
		VenueService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final PersistenceManagerFactory PMF = JDOHelper
			.getPersistenceManagerFactory("transactions-optional");

	public void addVenue(VenueDetails venue) throws NotLoggedInException {

		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();

		try {

			pm.makePersistent(new Venue(getUser(), venue));

		}

		finally {

			pm.close();

		}

	}

	public void removeVenue(VenueDetails v) throws NotLoggedInException {

		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();

		try {
			Query q = pm.newQuery(Venue.class, "user == u");
			q.declareParameters("com.google.appengine.api.users.User u");
			@SuppressWarnings("unchecked")
			List<Venue> venues = (List<Venue>) q.execute(getUser());

			for (Venue venue : venues) {
				if (v.getSymbol().equals(venue.getSymbol()))
					pm.deletePersistent(venue);
			}

		}

		finally {

			pm.close();

		}

	}

	public void removeAllVenues() throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();

		try {

			Query q = pm.newQuery(Venue.class, "user == u");
			q.declareParameters("com.google.appengine.api.users.User u");
			@SuppressWarnings("unchecked")
			List<Venue> venues = (List<Venue>) q.execute(getUser());
			pm.deletePersistentAll(venues);

		}

		finally {

			pm.close();

		}
	}

	public void setVisited(VenueDetails v) throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		try {
			Query q = pm.newQuery(Venue.class, "user == u");
			q.declareParameters("com.google.appengine.api.users.User u");
			@SuppressWarnings("unchecked")
			List<Venue> venues = (List<Venue>) q.execute(getUser());
			for (Venue venue : venues) {
				if (v.getSymbol().equals(venue.getSymbol())) {
					VenueDetails location = venue.getVenue();
					pm.deletePersistent(venue);
					if (location.getVisited() == true)
						location.setVisited(false);
					else
						location.setVisited(true);
					pm.makePersistent(new Venue(getUser(), location));

				}
			}
		}

		finally {

			pm.close();

		}
	}

	public List<VenueDetails> getVenues() throws NotLoggedInException {

		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		List<VenueDetails> listOfBookmarks = new ArrayList<VenueDetails>();

		try {

			Query q = pm.newQuery(Venue.class, "user == u");
			q.declareParameters("com.google.appengine.api.users.User u");
			q.setOrdering("createDate");
			@SuppressWarnings("unchecked")
			List<Venue> venues = (List<Venue>) q.execute(getUser());
			for (Venue venue : venues) {
				listOfBookmarks.add(venue.getVenue());
			}
			Collections.sort(listOfBookmarks);
		}

		finally {

			pm.close();

		}
		return listOfBookmarks;
	}

	private void checkLoggedIn() throws NotLoggedInException {

		if (getUser() == null) {

			throw new NotLoggedInException("Not logged in.");

		}
	}

	private User getUser() {

		UserService userService = UserServiceFactory.getUserService();
		return userService.getCurrentUser();

	}

	private PersistenceManager getPersistenceManager() {

		return PMF.getPersistenceManager();

	}

}
