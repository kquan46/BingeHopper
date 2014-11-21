package com.bingehopper.server;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.users.User;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Venue {
	
	  @PrimaryKey
	  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	  private Long id;
	  
	  @Persistent
	  private User user;
	  
	  @Persistent
	  private int venueId;
	  
	  @Persistent
	  private Date createDate;
	  
	  public Venue()
	  {
		  
		  this.createDate = new Date();
		  
	  }
	  
	  public Venue(User user, int venueId)
	  {
		  
		  this();
		  this.user = user;
		  this.venueId = venueId;
		  
	  }
	  
	  public Long getId()
	  {
		  
		  return this.id;
		  
	  }
	  
	  public User getUser()
	  {
		  
		  return this.user;
		  
	  }
	  
	  public int getVenueId()
	  {
		  
		  return this.venueId;
		  
	  }
	  
	  public Date getCreateDate()
	  {
		  
		  return this.createDate;
		  
	  }
	  
	  public void setUser (User user)
	  {
		  
		  this.user = user;
		  
	  }	  


}
