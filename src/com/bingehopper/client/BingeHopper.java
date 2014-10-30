package com.bingehopper.client;

import java.util.Date;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class BingeHopper implements EntryPoint {

private VerticalPanel mainPanel = new VerticalPanel();  
private FlexTable venuesFlexTable = new FlexTable();  
private HorizontalPanel updatePanel = new HorizontalPanel();  
private Button updateVenuesButton = new Button("Update");  
private Label lastUpdatedLabel = new Label();

private VenueDetailsServiceAsync venueDetailsSvc = GWT.create(VenueDetailsService.class);

private Label errorMsgLabel = new Label();

private LoginInfo loginInfo = null;
private VerticalPanel loginPanel = new VerticalPanel();
private Label loginLabel = new Label(
    "Please sign in to your Google Account to access the BingeHopper application.");
private Anchor signInLink = new Anchor("Sign In");
private Anchor signOutLink = new Anchor("Sign Out");

/**  * Entry point method.  */  public void onModuleLoad() 
{
	// Check login status using login service.
    LoginServiceAsync loginService = GWT.create(LoginService.class);
    loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() 
    {
      public void onFailure(Throwable error) 
      {
    	  
    	  
      }

      public void onSuccess(LoginInfo result) 
      {
        loginInfo = result;
        if(loginInfo.isLoggedIn()) 
        {	
        	loadBingeHopper();
        }
        
        else 
        {
            loadLogin();
        }
      }
     });
}

private void loadLogin() 
{
    // Assemble login panel.
    signInLink.setHref(loginInfo.getLoginUrl());
    loginPanel.add(loginLabel);
    loginPanel.add(signInLink);
    RootPanel.get("venueList").add(loginPanel);
}

private void loadBingeHopper() 
{
	// Set up sign out hyperlink.
    signOutLink.setHref(loginInfo.getLogoutUrl());
	
	// Create table for venue data
	
	venuesFlexTable.setText(0, 0, "Name");  
	venuesFlexTable.setText(0, 1, "Address Line 1");  
	venuesFlexTable.setText(0, 2, "Address Line 2");  
	venuesFlexTable.setText(0, 3, "City");
	venuesFlexTable.setText(0, 4, "Postal Code");
	venuesFlexTable.setText(0, 5, "Telephone");
	venuesFlexTable.setText(0, 6, "Type");
	venuesFlexTable.setText(0, 7, "Capacity"); 
	
	// Add styles to elements in the venue list table.
    venuesFlexTable.getRowFormatter().addStyleName(0, "venueListHeader");
    venuesFlexTable.addStyleName("venueList");
    
	
	// Assemble Update Venues panel. 
	
	updatePanel.add(updateVenuesButton);
	updatePanel.add(lastUpdatedLabel);
	updatePanel.addStyleName("updatePanel");
	
	
	// Assemble Main panel.
	
	errorMsgLabel.setStyleName("errorMessage");
    errorMsgLabel.setVisible(false);
    
    mainPanel.add(signOutLink);
    mainPanel.add(errorMsgLabel);
    mainPanel.add(updatePanel);
	mainPanel.add(venuesFlexTable);
	
	// Associate the Main panel with the HTML host page.
	RootPanel.get("venueList").add(mainPanel);
	
	// Listen for mouse events on the Add button.
    updateVenuesButton.addClickHandler(new ClickHandler() 
    {
      public void onClick(ClickEvent event) 
      {
        refreshVenueList();
      }
    });
}

private void refreshVenueList()
{
	
	if (venueDetailsSvc == null) 
	{
	      venueDetailsSvc = GWT.create(VenueDetailsService.class);
	}

	    // Set up the callback object.
	    AsyncCallback<VenueDetails[]> callback = new AsyncCallback<VenueDetails[]>() 
	    {
	      public void onFailure(Throwable caught) 
	      {
	    	  errorMsgLabel.setText("Error while making call to server");
	          errorMsgLabel.setVisible(true);	      
	      }

	      public void onSuccess(VenueDetails[] result) 
	      {
	    	  addVenues(result);
	      }
	      
	    };

	    // Make the call to the venue price service.
	    venueDetailsSvc.getPrices(callback);
	
}

/**
 * Add venues to FlexTable. Executed when the user clicks the updateVenuesButton
 */
private void addVenues(VenueDetails[] venues) 
{
	  for (int i = 0; i < venues.length; i++) 
	  {
		  
	    venuesFlexTable.setText(i+1, 0, venues[i].getVenueName());
	    venuesFlexTable.setText(i+1, 1, venues[i].getVenueAdd1());
	    venuesFlexTable.setText(i+1, 2, venues[i].getVenueAdd2());
	    venuesFlexTable.setText(i+1, 3, venues[i].getVenueCity());
	    venuesFlexTable.setText(i+1, 4, venues[i].getVenuePostal());
	    venuesFlexTable.setText(i+1, 5, venues[i].getVenuePhone());
	    venuesFlexTable.setText(i+1, 6, venues[i].getVenueType());
	    venuesFlexTable.setText(i+1, 7, venues[i].getVenueCapacity());
	    
	  }
	  
	  // Display timestamp showing last refresh.  
	  lastUpdatedLabel.setText("Last update : "  + DateTimeFormat.getMediumDateTimeFormat().format(new Date()));
}
	  




}