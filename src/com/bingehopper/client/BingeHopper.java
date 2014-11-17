package com.bingehopper.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class BingeHopper implements EntryPoint 

{
	
	// create ArrayList of VenueDetails objects
	ArrayList<VenueDetails> listOfVenues = new ArrayList<VenueDetails>();
	
	// create panels
	private VerticalPanel mainPanel = new VerticalPanel();  
	private HorizontalPanel updatePanel = new HorizontalPanel();
	private HorizontalPanel searchPanel = new HorizontalPanel();
	private HorizontalPanel likePanel = new HorizontalPanel();
	private HorizontalPanel tweetPanel = new HorizontalPanel();
	private VerticalPanel searchTab = new VerticalPanel();
	private VerticalPanel bookmarksTab = new VerticalPanel();
	private VerticalPanel visitedTab = new VerticalPanel();
	private VerticalPanel mapTab = new VerticalPanel();
	private VerticalPanel socialTab = new VerticalPanel();
	private VerticalPanel loginPanel = new VerticalPanel();
	
	
	// create tables
	private FlexTable venuesFlexTable = new FlexTable();
	private FlexTable bookmarksFlexTable = new FlexTable();
	
	
	// create buttons
	private Button updateVenuesButton = new Button("Update");
	private Button searchButton = new Button("Search");
	private Button removeBookmarkButton = new Button("x");
	private Button addBookmarksButton = new Button("Add");
	private Button removeBoomarksButton = new Button("Remove");
	private Button displayBookmarksButton = new Button("Display");
	
	
	// create labels
	private Label lastUpdatedLabel = new Label();
	private Label searchTitle = new Label("Search");
	private Label nameLabel = new Label("Name");
	private Label addressLabel = new Label("Address");
	private Label typeLabel = new Label("Type");
	private Label cityLabel = new Label("City");
	private Label socialTitle = new Label("Social");
	private Label bookmarksTitle = new Label("Bookmarks");
	private Label visitedTitle = new Label("Visited");
	private Label errorMsgLabel = new Label();
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
	
	
	// create boxes
	private TextBox nameBox = new TextBox();	
	private TextBox addressBox = new TextBox();	
	private ListBox typeListBox = new ListBox();
	private ListBox cityListBox = new ListBox();
	private CheckBox visitedCheckbox = new CheckBox();
	
	
	
	// create custom icons
	private Image searchIcon = new Image();
	private Image visitedIcon = new Image();
	private Image mapIcon = new Image();
	private Image bookmarksIcon = new Image();
	
	
	// social network integration elements	
	private String fbHtml = "<div class='fb-like' data-href='http://teamfantastic310.appspot.com/' data-layout='button_count' data-action='like' data-show-faces='true' data-share='true'></div>";
	private HTML likeHtml = new HTML(fbHtml);
	private String twtrHtml = "<a href='https://twitter.com/share' class='twitter-share-button' data-url='http://teamfantastic310.appspot.com/' data-hashtags='TeamFantastic'>Tweet</a>";
	private HTML twitterHtml = new HTML(twtrHtml);
	private HTML twtfcn = new HTML("<script>!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0],p=/^http:/.test(d.location)?'http':'https';if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src=p+'://platform.twitter.com/widgets.js';fjs.parentNode.insertBefore(js,fjs);}}(document, 'script', 'twitter-wjs');</script>");
	
	
	// create tab panel
	private TabPanel tabs = new TabPanel();
	
	
	// initialize async callback service
	private VenueDetailsServiceAsync venueDetailsSvc = GWT.create(VenueDetailsService.class);	
	private final VenueServiceAsync venueService = GWT.create(VenueService.class);
	
	// setup login service
	private LoginInfo loginInfo = null;
	private Anchor signInLink = new Anchor("Sign In");
	private Anchor signOutLink = new Anchor("Sign Out");
	
	
	
	
	
	
	
	
	// EntryPoint method
	public void onModuleLoad() 
	{
		
		// Check login status using login service
	    LoginServiceAsync loginService = GWT.create(LoginService.class);
	    loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() 
	    {
	    	
		      public void onFailure(Throwable error) 
		      {
		    	  
		    	  errorMsgLabel.setText(error.getMessage());
				  errorMsgLabel.setVisible(true);
		    	  
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
	      
	     }
	    					);
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
	    signOutLink.addStyleName("signOutLink");
	    
		
		// Create table for venue data		
	    setUpFirstRow();  
	    
		
		// Assemble Update Venues panel. 		
		updatePanel.add(updateVenuesButton);
		updatePanel.add(lastUpdatedLabel);
		updatePanel.addStyleName("updatePanel");
		
		
		// Assemble Facebook like panel
		likePanel.add(likeHtml);
		likePanel.addStyleName("fbLikePanel");
		
		
		// Assemble Twitter like panel
		tweetPanel.add(twitterHtml);
		tweetPanel.add(twtfcn);
		tweetPanel.addStyleName("fbLikePanel");
		
		
		// Assemble Search Venues panel		
		searchPanel.add(nameLabel);
		searchPanel.add(nameBox);
		searchPanel.add(addressLabel);
		searchPanel.add (addressBox);		
		searchPanel.add(cityLabel);
		
		cityListBox.addItem("All");
		cityListBox.addItem("Abbotsford");
		cityListBox.addItem("Aldergrove");
		cityListBox.addItem("Bamfield");
		cityListBox.addItem("Barkerville");
		cityListBox.addItem("Bowen Island");
		cityListBox.addItem("Burnaby");
		cityListBox.addItem("Chilliwack");
		cityListBox.addItem("Cloverdale");
		cityListBox.addItem("Coquitlam");
		cityListBox.addItem("Delta");
		cityListBox.addItem("Fort Langley");
		cityListBox.addItem("Hoarrison Hot Springs");
		cityListBox.addItem("Kamloops");
		cityListBox.addItem("Kelowna");
		cityListBox.addItem("Lake Cowichan");
		cityListBox.addItem("Langley");
		cityListBox.addItem("Maple Ridge");
		cityListBox.addItem("Mission");
		cityListBox.addItem("Nanaimo");
		cityListBox.addItem("New Westminster");
		cityListBox.addItem("North Vancouver");
		cityListBox.addItem("Parksville");
		cityListBox.addItem("Pemberton");
		cityListBox.addItem("Pitt Meadows");
		cityListBox.addItem("Port Coquitlam");
		cityListBox.addItem("Port Moody");
		cityListBox.addItem("Prince George");
		cityListBox.addItem("Prince Rupert");
		cityListBox.addItem("Richmond");
		cityListBox.addItem("Squamish");
		cityListBox.addItem("Surrey");
		cityListBox.addItem("Terrace");
		cityListBox.addItem("Tsawwassen");
		cityListBox.addItem("Vancouver");
		cityListBox.addItem("Victoria");
		cityListBox.addItem("West Vancouver");
		cityListBox.addItem("Whistler");
		cityListBox.addItem("White Rock");
		searchPanel.add(cityListBox);
		
		searchPanel.add(typeLabel);
		typeListBox.addItem("All");
		typeListBox.addItem("Food Primary");
		typeListBox.addItem("Independant Agent");
		typeListBox.addItem("Licensee Retail Store");
		typeListBox.addItem("Liquor Primary");
		typeListBox.addItem("Manufacturer Agent");
		typeListBox.addItem("Ubrew/Uvin");
		typeListBox.addItem("Wine Store");
		typeListBox.addItem("Winery");
		searchPanel.add(typeListBox);
	
		searchPanel.add(searchButton);
		
		
		// Create table for Bookmarks
		bookmarksFirstRow();
		
		
		// Assemble Main panel.		
		errorMsgLabel.setStyleName("errorMessage");
	    errorMsgLabel.setVisible(false);
	    
	    mainPanel.add(signOutLink);
	    mainPanel.add(errorMsgLabel);
	    
		
		// Associate the Main panel with the HTML host page.
		RootPanel.get("venueList").add(mainPanel);
		
		
		// Listen for mouse events on the Add button.
	    updateVenuesButton.addClickHandler(new ClickHandler() 
	    {
	    	
		      public void onClick(ClickEvent event) 
		      {
		    	
		    	lastUpdatedLabel.setText("reached clickhandler");
		        refreshVenueList();
		        
		      }
		      
	    }								  );
	    
		// Listen for mouse events on the venue Search button.
	
		searchButton.addClickHandler(new ClickHandler() 
		{
			
			public void onClick(ClickEvent event) 
			{
				
				searchVenues (
						
								nameBox.getText(),
								addressBox.getText(),
								cityListBox.getItemText(cityListBox.getSelectedIndex()),
								typeListBox.getItemText(typeListBox.getSelectedIndex())
								
							 );
				
			}
			
		}							 );
	
		// Listen for keyboard events in the Name input box.
		nameBox.addKeyDownHandler(new KeyDownHandler() 
		{
			
			public void onKeyDown(KeyDownEvent event) 
			{
				
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) 
				{
										
					searchVenues(
							
									nameBox.getText(), 
									addressBox.getText(),
									cityListBox.getItemText(cityListBox.getSelectedIndex()),
									typeListBox.getItemText(typeListBox.getSelectedIndex())
									
								);
					
				}
				
			}
			
		}						  );
	
		// Listen for keyboard events in the Address input box.
		addressBox.addKeyDownHandler(new KeyDownHandler() 
		{
			
			public void onKeyDown(KeyDownEvent event) 
			{
				
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) 
				{
					
					searchVenues(
							
									nameBox.getText(), 
									addressBox.getText(),
									cityListBox.getItemText(cityListBox.getSelectedIndex()),
									typeListBox.getItemText(typeListBox.getSelectedIndex())
							
								);
					
				}
				
			}
			
		}							);
		
		// implemented to test the bookmark functionality. Just added 4 arbitrary VenueDetails objects from the listOfVenues
		addBookmarksButton.addClickHandler(new ClickHandler() 
	    {
	    	
		      public void onClick(ClickEvent event) 
		      {
		    	
		    	addBookmark(listOfVenues.get(5).getId());
		    	addBookmark(listOfVenues.get(7).getId());
		    	addBookmark(listOfVenues.get(10).getId());
		    	addBookmark(listOfVenues.get(18).getId());
		    	
		        
		      }
		      
	    }								  );
		
		
		// implemented to test the removal of bookmarks functionality. Removes the 4 bookmarks added above
		removeBookmarkButton.addClickHandler(new ClickHandler() 
	    {
	    	
		      public void onClick(ClickEvent event) 
		      {
		    	
		    	removeBookmark(listOfVenues.get(5).getId());
		    	removeBookmark(listOfVenues.get(7).getId());
		    	removeBookmark(listOfVenues.get(10).getId());
		    	removeBookmark(listOfVenues.get(18).getId());
		    	
		        
		      }
		      
	    }								  );
	    
		
		// this function can stay as is. Calling retrieveAndDisplayBookmarks() will display all the current bookmarks
		// for the current user in the bookmarksFlexTable
		displayBookmarksButton.addClickHandler(new ClickHandler() 
	    {
	    	
		      public void onClick(ClickEvent event) 
		      {
		    	
		    	  retrieveAndDisplayBookmarks();
		        
		      }
		      
	    }								  );
		
		
	    // ----------- TABS --------------
	    

	    // Organize Search Tab
		searchIcon.setUrl("search.png");
		searchIcon.addStyleName("tabIcon");
		searchTab.add(searchTitle);
		searchTitle.addStyleName("title");
	    searchTab.add(updatePanel);
	    searchTab.add(searchPanel);
	    searchTab.add(venuesFlexTable);
	    
	    // Organize Bookmarks Tab
	    bookmarksIcon.setUrl("bookmarks.png");
	    bookmarksIcon.addStyleName("tabIcon");
	    bookmarksTab.add(bookmarksTitle);
	    bookmarksTab.add(addBookmarksButton);
	    bookmarksTab.add(removeBookmarkButton);
	    bookmarksTab.add(displayBookmarksButton);
	    bookmarksTitle.addStyleName("title");
	    bookmarksTab.add(bookmarksFlexTable);
	    
	    // Organize Visited Tab
	    visitedIcon.setUrl("visited.png");
	    visitedIcon.addStyleName("tabIcon");
	    visitedTab.add(visitedTitle);
	    visitedTitle.addStyleName("title");
	    
	    // Organize Map Tab
	    mapIcon.setUrl("maps.png");
	    mapIcon.addStyleName("tabIcon");
	    Label mapTest = new Label("I solemnly swear I'm up to no good.");
	    mapTab.add(mapTest);
	    
	    // Organize Social Tab
	    socialTab.add(socialTitle);
	    socialTitle.addStyleName("title");
	    socialTab.add(likePanel);
	 
	    // configure tabs
	    tabs.add(searchTab, searchIcon);
		tabs.add(bookmarksTab, bookmarksIcon);
		tabs.add(visitedTab, visitedIcon);
		tabs.add(mapTab, mapIcon);
		tabs.add(socialTab, "Social");
	
		
		// show the 'map' tab initially
		tabs.selectTab(0);
		
		// add to mainPanel
		mainPanel.add(tabs);
		
		// set stylename for tabs
		tabs.setStyleName("tabs");
	
	}
	
	
	
	
	
	private void bookmarksFirstRow() 
	{
		
		bookmarksFlexTable.setText(0, 0, "Name");  
		bookmarksFlexTable.setText(0, 1, "Address"); 
		bookmarksFlexTable.setText(0, 2, "City");
		bookmarksFlexTable.setText(0, 3, "Postal Code");
		bookmarksFlexTable.setText(0, 4, "Telephone");
		bookmarksFlexTable.setText(0, 5, "Type");
		bookmarksFlexTable.setText(0, 6, "Capacity");
		bookmarksFlexTable.setText(0, 7, "Share");
		bookmarksFlexTable.setText(0, 8, "Visited");
		bookmarksFlexTable.setText(0, 9, "Remove");
		
		// Add styles to elements in the bookmarks list table.
	    bookmarksFlexTable.getRowFormatter().addStyleName(0, "venueListHeader");
	    bookmarksFlexTable.addStyleName("venueList");
	    
	}
	
	
	
	
	
	private void setUpFirstRow() 
	{
		
		venuesFlexTable.setText(0, 0, "Name");  
		venuesFlexTable.setText(0, 1, "Address"); 
		venuesFlexTable.setText(0, 2, "City");
		venuesFlexTable.setText(0, 3, "Postal Code");
		venuesFlexTable.setText(0, 4, "Telephone");
		venuesFlexTable.setText(0, 5, "Type");
		venuesFlexTable.setText(0, 6, "Capacity");
		venuesFlexTable.setText(0, 7, "Bookmark");
		
		// Add styles to elements in the venue list table.
	    venuesFlexTable.getRowFormatter().addStyleName(0, "venueListHeader");
	    venuesFlexTable.addStyleName("venueList");
	    
	}
	
	
	
	
	
	private void searchVenues(final String name, final String address,
			final String city, final String type) 
	{
		
		if (name.isEmpty() && address.isEmpty() && city == "All"
				&& type == "All") 
		{
			
			displayVenues(listOfVenues);
			
		}
		
		else 
		{
			
			ArrayList<VenueDetails> filteredVenueList = filter(listOfVenues, name, address, city, type);
			
			//pagination();
			displayVenues(filteredVenueList);
			
		}
		
		
		lastUpdatedLabel.setText("Last update : "
				+ DateTimeFormat.getMediumDateTimeFormat().format(new Date()));
		
	}
	
	
	
	
	
	

	private ArrayList<VenueDetails> filter(ArrayList<VenueDetails> venues, String name, String address, String city, String type) 
	{
		
		if (name.isEmpty() && address.isEmpty() && city.equals("All") && type.equals("All"))
			// empty search returns all venues
			return venues; 
		
		else 
		{
			
			ArrayList<VenueDetails> filteredVenueList = new ArrayList<VenueDetails>();
			
			if (city.equals("All")) 
			{
				
				if (type.equals("All")) 
				{
					
					for (int i = 0; i < venues.size(); i++) 
					{
						
						VenueDetails curr = venues.get(i);
						
						if (curr.getVenueName().trim().toLowerCase().contains(name.trim().toLowerCase()) 
							&& curr.getVenueAdd1().trim().toLowerCase().contains(address.trim().toLowerCase()))
							
							filteredVenueList.add(curr);
						
					}
					
				}
				
				else 
				{
					
					for (int i = 0; i < venues.size(); i++) 
					{
						
						VenueDetails curr = venues.get(i);
						
						if (curr.getVenueName().trim().toLowerCase().contains(name.trim().toLowerCase()) 
							&& curr.getVenueAdd1().trim().toLowerCase().contains(address.trim().toLowerCase())
							&& curr.getVenueType().trim().toLowerCase().contains(type.trim().toLowerCase()))
							
							filteredVenueList.add(curr);
					}
				}
				
			}
			
			else 
			{
				
				if (type.equals("All")) 
				{
					
					for (int i = 0; i < venues.size(); i++) 
					{
						
						VenueDetails curr = venues.get(i);
						
						if (curr.getVenueName().trim().toLowerCase().contains(name.trim().toLowerCase())
								&& curr.getVenueAdd1().trim().toLowerCase().contains(address.trim().toLowerCase())
								&& curr.getVenueCity().trim().toLowerCase().equals(city.trim().toLowerCase()))
							
							filteredVenueList.add(curr);
						
					}
					
				} 
				
				else 
				{
					
					for (int i = 0; i < venues.size(); i++) 
					{
						
						VenueDetails curr = venues.get(i);
						
						if (curr.getVenueName().trim().toLowerCase().contains(name.trim().toLowerCase())
							&& curr.getVenueAdd1().trim().toLowerCase().contains(address.trim().toLowerCase())
							&& curr.getVenueCity().trim().toLowerCase().equals(city.trim().toLowerCase())
							&& curr.getVenueType().trim().toLowerCase().contains(type.trim().toLowerCase()))
							
							filteredVenueList.add(curr);
						
					}
					
				}
				
			}
			
			return filteredVenueList;
			
		}
	}
	
	
	
	// adds the Venue with id "id" to the current user's bookmarks list
	private void addBookmark(final int id)
	{
		
		// calls the getVenues method to retrieve the Venue ids that have already been bookmarked by the current user, then checks if 
		// the current "id" being added exists in that list. If it does not exist, it adds the Venue with "id" to the bookmarks list, if
		// not, it does nothing
		venueService.getVenues(new AsyncCallback<ArrayList<Integer>>()
				{
			
					public void onFailure(Throwable error)
					{
						
						lastUpdatedLabel.setText("reached failure");
					}
					
					public void onSuccess(ArrayList<Integer> ids)
					
					{
						if (!ids.contains(id))
						{
							// this is essentially how to add a Venue to the bookmarks list. If not for the preceding check code,
							// calling only this method to add bookmarks will add duplicates as well
							venueService.addVenue(id, new AsyncCallback<Void>() 
									{
									      public void onFailure(Throwable error) 
									      {
									    	  
									    	  lastUpdatedLabel.setText("failed to add venue");
									    	  
									      }
									      public void onSuccess(Void ignore) 
									      {
									    	  
									        lastUpdatedLabel.setText("successfully added");
									        
									      }
									 });
							
						}						
						
					}
			
				});
		
	}
	
	
	// retrieves an ArrayList of Venue id's that have been bookmarked by the current user, 
	// and uses the list to display the corresponding Venues in the bookmarksFlexTable using the displayBookmarks method
	private void retrieveAndDisplayBookmarks ()
	{
		
		final ArrayList<VenueDetails> bookmarkedVenues = new ArrayList <VenueDetails>();
		venueService.getVenues(new AsyncCallback<ArrayList<Integer>>()
				{
			
					public void onFailure(Throwable error)
					{
						
						lastUpdatedLabel.setText("failed to retrieve bookmarked venues");
					}
					
					public void onSuccess(ArrayList<Integer> ids)
					
					{

						for (int i = 0; i<ids.size();i++)
						{
							
							for (int j = 0; j<listOfVenues.size(); j++)
							{
								
								if (ids.get(i) == listOfVenues.get(j).getId())
								{
									
									bookmarkedVenues.add(listOfVenues.get(j));
									
								}
								
							}
							

							
						}
						
						displayBookmarks (bookmarkedVenues);
						
					}
			
				});
		
		
		
		
	}
	
	
	// removes the Venue with "id" from the current user's bookmarks list
	private void removeBookmark (int id)
	{
		
		venueService.removeVenue(id, new AsyncCallback<Void>() 
				{
				      public void onFailure(Throwable error) 
				      {
				    	  
				    	  lastUpdatedLabel.setText("failed to remove venue");
				    	  
				      }
				      public void onSuccess(Void ignore) 
				      {
				    	  
				        lastUpdatedLabel.setText("successfully removed");
				        
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
				
				VenueDetails[] venues = cleanVenueArray(result);
				
				for (int i = 0; i<venues.length; i++)
				{
					
					listOfVenues.add(venues[i]);
					
				}
				
				lastUpdatedLabel.setText("successfully added");
				
			}
		};
		
		// Make the call to the venue price service.
		
		venueDetailsSvc.getPrices(callback);
		
		
	}
	
	
	
	
	
	

	/**
	 * Takes list of venues minues the first row, removes empty venue names, and
	 * sorts it alphabetically;
	 */
	private VenueDetails[] cleanVenueArray(VenueDetails[] venues) 
	{
		
		VenueDetails[] results = new VenueDetails[(venues.length) - 1];
		
		for (int i = 0; i < results.length; i++) 
		{
			
			// first row is field parameters
			results[i] = venues[i + 1]; 
			
		}
		
		Arrays.sort(results);
		return results;
	}
	
	

	private void findCity(VenueDetails[] result) 
	{
		
		Set<String> cities = new TreeSet<String>();
		
		for (VenueDetails venue : result) 
		{
			
			cities.add(venue.getVenueCity());
			
		}
		
	}
	
	
	private ArrayList<VenueDetails> getListOfVenues()
	{
		
		return this.listOfVenues;
		
	}
	
	
	

	/**
	 * Add venues to FlexTable. Executed when the user clicks the
	 * updateVenuesButton
	 */
	private void displayVenues(ArrayList<VenueDetails> venues)
	{
		
		venuesFlexTable.removeAllRows();
		setUpFirstRow();
		lastUpdatedLabel.setText(Integer.toString(venues.size()));
		
		for (int i = 0; i < venues.size(); i++) 
		{
			
			venuesFlexTable.setText(i + 1, 0, venues.get(i).getVenueName());
			venuesFlexTable.setText(i + 1, 1, venues.get(i).getVenueAdd1());
			venuesFlexTable.setText(i + 1, 2, venues.get(i).getVenueCity());
			venuesFlexTable.setText(i + 1, 3, venues.get(i).getVenuePostal());
			venuesFlexTable.setText(i + 1, 4, venues.get(i).getVenuePhone());
			venuesFlexTable.setText(i + 1, 5, venues.get(i).getVenueType());
			venuesFlexTable.setText(i + 1, 6, venues.get(i).getVenueCapacity());		

		}

	}
	
	private void displayBookmarks(ArrayList<VenueDetails> venues)
	{
		
		lastUpdatedLabel.setText("reached displayBookmark");
		bookmarksFlexTable.removeAllRows();
		bookmarksFirstRow();
		//lastUpdatedLabel.setText(Integer.toString(venues.size()));
		
		for (int i = 0; i < venues.size(); i++) 
		{
			
			bookmarksFlexTable.setText(i + 1, 0, venues.get(i).getVenueName());
			bookmarksFlexTable.setText(i + 1, 1, venues.get(i).getVenueAdd1());
			bookmarksFlexTable.setText(i + 1, 2, venues.get(i).getVenueCity());
			bookmarksFlexTable.setText(i + 1, 3, venues.get(i).getVenuePostal());
			bookmarksFlexTable.setText(i + 1, 4, venues.get(i).getVenuePhone());
			bookmarksFlexTable.setText(i + 1, 5, venues.get(i).getVenueType());
			bookmarksFlexTable.setText(i + 1, 6, venues.get(i).getVenueCapacity());		

		}

	}

}
