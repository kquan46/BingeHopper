package com.bingehopper.server;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.bingehopper.client.VenueDetails;
import com.google.appengine.api.users.User;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Venue {
	
	  @PrimaryKey
	  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	  private Long id;
	  
	  @Persistent
	  private User user;
	  
	  @Persistent(serialized = "true")
	  private VenueDetails venue;
	  
	  @Persistent
	  private Date createDate;
	  
	  public Venue()
	  {
		  
		  this.createDate = new Date();
		  
	  }
	  
	  public Venue(User user, VenueDetails venue)
	  {
		  
		  this();
		  this.user = user;
		  this.venue = venue;
		  
	  }
	  
	  public Long getId()
	  {
		  
		  return this.id;
		  
	  }
	  
	  public User getUser()
	  {
		  
		  return this.user;
		  
	  }
	  
	  public String getSymbol()
	  {
		  
		  return this.venue.getSymbol();
		  
	  }
	  
	  public Date getCreateDate()
	  {
		  
		  return this.createDate;
		  
	  }
	  
	  public void setUser (User user)
	  {
		  
		  this.user = user;
		  
	  }	
	  
	  public VenueDetails getVenue ()
	  {
		  
		  return this.venue;
		  
	  }	 
	  
	  public void setVenue (VenueDetails venue)
	  {
		  
		  this.venue = venue;
		  
	  }	 


}
