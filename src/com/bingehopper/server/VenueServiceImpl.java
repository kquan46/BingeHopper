package com.bingehopper.server;

import com.bingehopper.client.NotLoggedInException;
import com.bingehopper.client.VenueService;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;


public class VenueServiceImpl extends RemoteServiceServlet implements VenueService
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(VenueServiceImpl.class.getName());
	private static final PersistenceManagerFactory PMF = JDOHelper.getPersistenceManagerFactory("transactions-optional");
	

	public void addVenue(int venueId) throws NotLoggedInException 
	{
		
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		
		try 
		{
			
			pm.makePersistent(new Venue(getUser(), venueId));
			
		}
		
		finally
		{
			
			pm.close();
			
		}
		
	}


	public void removeVenue(int venueId) throws NotLoggedInException 
	{
		
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		
		try
		{
			
			long deleteCount = 0;
			Query q = pm.newQuery(Venue.class, "user == u");
			q.declareParameters("com.google.appengine.api.users.User u");
			@SuppressWarnings("unchecked")
			List<Venue> venues = (List<Venue>) q.execute(getUser());
			
			for (Venue venue : venues)
			{
				
				if (venueId == venue.getVenueId())
				{
					
					deleteCount++;
					pm.deletePersistent(venue);
					
				}
				
			}
			
			if (deleteCount != 1)
			{
				
				LOG.log(Level.WARNING, "removeVenue deleted "+deleteCount+" Venues");
				
			}
			
			
		}
		
		finally
		{
			
			pm.close();
			
		}
		
	}


	public ArrayList<Integer> getVenues() throws NotLoggedInException 
	{
	
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		ArrayList<Integer> listOfIds = new ArrayList<Integer>();
		
		try
		{
			
			Query q = pm.newQuery(Venue.class, "user == u");
			q.declareParameters("com.google.appengine.api.users.User u");
			q.setOrdering("createDate");
			@SuppressWarnings("unchecked")
			List<Venue> venues = (List<Venue>) q.execute(getUser());
			
			
			for (Venue venue: venues)
			{
				
				listOfIds.add(venue.getVenueId());
				
			}
			
		}
		
		finally
		{
			
			pm.close();
			
		}
		
		return listOfIds;
		
	}
	
	
	private void checkLoggedIn() throws NotLoggedInException 
	{
		
	    if (getUser() == null) 
	    {
	    	
	      throw new NotLoggedInException("Not logged in.");
	      
	    }
	}
	
	
	private User getUser() 
	{
		
	    UserService userService = UserServiceFactory.getUserService();
	    return userService.getCurrentUser();
	    
	}
	
	
	private PersistenceManager getPersistenceManager() 
	{
		
	    return PMF.getPersistenceManager();
	    
	}

}
