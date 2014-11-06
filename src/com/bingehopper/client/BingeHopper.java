package com.bingehopper.client;


import java.util.ArrayList;
import java.util.Arrays;
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
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;


public class BingeHopper implements EntryPoint {

private static final VenueDetails[][] VenueDetails = null;
private VerticalPanel mainPanel = new VerticalPanel();  
private FlexTable venuesFlexTable = new FlexTable();  
private HorizontalPanel updatePanel = new HorizontalPanel();
private HorizontalPanel searchPanel = new HorizontalPanel();
private HorizontalPanel likePanel = new HorizontalPanel();
private Button updateVenuesButton = new Button("Update");
private Label lastUpdatedLabel = new Label();
private Button searchVenuesButton = new Button("Search by name");
private TextBox searchBox = new TextBox();
private Button searchAddressButton = new Button("Search by address");
private TextBox addressBox = new TextBox();
private Label typeLabel = new Label("Type");
private ListBox typeListBox = new ListBox();
private String fbHtml = "<div class='fb-like' data-href='http://teamfantastic310.appspot.com/' data-layout='button_count' data-action='like' data-show-faces='true' data-share='true'></div>";
private HTML likeHtml = new HTML(fbHtml);


private VenueDetailsServiceAsync venueDetailsSvc = GWT.create(VenueDetailsService.class);

private Label errorMsgLabel = new Label();

private LoginInfo loginInfo = null;
private VerticalPanel loginPanel = new VerticalPanel();
private Label loginLabel = new Label("Please sign in to your Google Account to access the BingeHopper application.");
private Label welcomeText = new Label("Welcome to BingeHopper!");
private HTML appDescription = new HTML("<p>This app aims to provide people with the capacity to search for"
		+ " and navigate to multiple local Venues that have liquor licenses in British Columbia. "
		+ "This is done through the extension of Google, meaning that users of this app will be asked "
		+ "to login via their Google Accounts before they are provided this service.</p>"
		+ "<p>Once logged in, users will be able to see a list of venues with liquor licenses. "
		+ "Provided a set of search parameters, users can further fine tune their search for their "
		+ "desired destinations. Users can then add venues to their bookmarks list and view those "
		+ "locations on a map. Users can then select each location for a brief description of the Venue.</p>"
		+ "<p>Enjoying the night with friends is encouraged. Users are able to share locations with their "
		+ "friends via Google+ integration.</p>");
private Anchor signInLink = new Anchor("Sign In");
private Anchor signOutLink = new Anchor("Sign Out");

/**  * Entry point method.  */  
public void onModuleLoad() 
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
    loginPanel.add(welcomeText);
    loginPanel.add(appDescription);
    loginPanel.add(loginLabel);
    loginPanel.add(signInLink);
    RootPanel.get("venueList").add(loginPanel);
    
    // Add styles to elements in the login page.
    welcomeText.addStyleName("welcomeText");
    appDescription.addStyleName("appDescription");
    signInLink.addStyleName("signInLink");
}

private void loadBingeHopper() 
{
	// Set up sign out hyperlink.
    signOutLink.setHref(loginInfo.getLogoutUrl());
	
	// Create table for venue data
	
	setUpFirstRow();    
	
	// Assemble Update Venues panel. 
	
	updatePanel.add(updateVenuesButton);
	updatePanel.add(lastUpdatedLabel);
	updatePanel.addStyleName("updatePanel");
	
	// Assemble Facebook like panel
	likePanel.add(likeHtml);
	likePanel.addStyleName("fbLikePanel");
	
	// Assemble Search Venues panel
	
	searchPanel.add (searchVenuesButton);
	searchPanel.add (searchBox);
	searchPanel.add (searchAddressButton);
	searchPanel.add (addressBox);
	searchPanel.add (typeLabel);
	typeListBox.addItem("All");
	typeListBox.addItem("Ubrew/Uvin");
	typeListBox.addItem("Food Primary");
	typeListBox.addItem("Liquor Primary");
	typeListBox.addItem("Manufacturer Agent");
	typeListBox.addItem("Winery");
	typeListBox.addItem("Licensee Retail Store");
	typeListBox.addItem("Independant Agent");
	searchPanel.add(typeLabel);
	searchPanel.add(typeListBox);
	
	// Assemble Main panel.
	
	errorMsgLabel.setStyleName("errorMessage");
    errorMsgLabel.setVisible(false);
    
    mainPanel.add(signOutLink);
    mainPanel.add(errorMsgLabel);
    mainPanel.add(updatePanel);
    mainPanel.add(likePanel);
    mainPanel.add(searchPanel);
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
    
// Listen for mouse events on the Venue Name Search button.
   
    searchVenuesButton.addClickHandler(new ClickHandler()   
    {
     public void onClick(ClickEvent event) 
     {
    	 
    	 searchByName(searchBox.getText());
    	 
   	  
     }
    });

}

private void setUpFirstRow() {
	venuesFlexTable.setText(0, 0, "Name");  
	venuesFlexTable.setText(0, 1, "Address Line 1");  
	venuesFlexTable.setText(0, 2, "Address Line 2");  
	venuesFlexTable.setText(0, 3, "City");
	venuesFlexTable.setText(0, 4, "Postal Code");
	venuesFlexTable.setText(0, 5, "Telephone");
	venuesFlexTable.setText(0, 6, "Type");
	venuesFlexTable.setText(0, 7, "Capacity");
	venuesFlexTable.setText(0, 8, "Share");
	
	// Add styles to elements in the venue list table.
    venuesFlexTable.getRowFormatter().addStyleName(0, "venueListHeader");
    venuesFlexTable.addStyleName("venueList");
}


private void searchByName (String searchPar)
{
	
	if(!searchPar.isEmpty())
	{
	refreshVenueList(searchPar);
	}
	else
	{
		
		errorMsgLabel.setText("Please input text");
        errorMsgLabel.setVisible(true);
		
	}
	
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
	    	  
	    	  VenueDetails[] resultsAppended = new VenueDetails[(result.length)-1];
	    	  for (int i=0;i<resultsAppended.length;i++)
	    	  {
	    		  
	    		  resultsAppended[i] = result[i+1];
	    		  
	    	  }
	    	  Arrays.sort(resultsAppended);
	    	  addVenues(resultsAppended);
	    	  
	      }
	      
	    };
	    
	    lastUpdatedLabel.setText("Last update : "  + DateTimeFormat.getMediumDateTimeFormat().format(new Date()));
	    
	    // Make the call to the venue price service.
	    venueDetailsSvc.getPrices(callback);
	
}

/**
 * Takes list of venues minues the first row and sorts it alphabetically;
 */
private VenueDetails[] cleanVenueList(VenueDetails[] venues) {
	VenueDetails[] results = new VenueDetails[(venues.length)-1];  //don't want first row of field paramters
	for (int i=0; i<results.length; i++) {
		results[i] = venues[i+1];  //first row is field paramters
	}
	Arrays.sort(results);
	return results;
}
private void refreshVenueList(final String searchName)
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
	    	  
	    	  searchAndAppend (result,searchName);
	    	  

	      }
	      
	      
	      
	    };
	    

	    // Make the call to the venue price service.
	    venueDetailsSvc.getPrices(callback);
	
}

private void searchAndAppend (VenueDetails[] result,String searchName)
{
	
	ArrayList<VenueDetails> appended = new ArrayList<VenueDetails>();
	
	
	appended = filterByName(result, searchName);
	
	VenueDetails[] appendedAsArray = new VenueDetails[appended.size()];
	appendedAsArray = appended.toArray(new VenueDetails[appended.size()]);
	System.out.println("reached");
	Arrays.sort(appendedAsArray);
	addVenues(appendedAsArray);
	
}

private ArrayList<VenueDetails> filterByName(VenueDetails[] result,
		String searchName) 
{	
	ArrayList<VenueDetails> filteredByName = new ArrayList<VenueDetails>();
	for (int i=0;i<result.length;i++)
	{
		VenueDetails curr = result[i];
		if (curr.getVenueName().toLowerCase().contains(searchName.toLowerCase()))
		{
			
			filteredByName.add(curr);
			
		}
		
	}
	
	return filteredByName;
}

/**
 * Add venues to FlexTable. Executed when the user clicks the updateVenuesButton
 */
private void addVenues(VenueDetails[] venues) 
{
	  
	  venuesFlexTable.removeAllRows();
	  setUpFirstRow();
	  lastUpdatedLabel.setText(Integer.toString(venues.length));
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
	  
}
	  




}