package com.bingehopper.client;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.HeadingElement;
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
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class BingeHopper implements EntryPoint {

private static final VenueDetails[][] VenueDetails = null;
private VerticalPanel mainPanel = new VerticalPanel();  
private FlexTable venuesFlexTable = new FlexTable();  
private HorizontalPanel updatePanel = new HorizontalPanel();
private HorizontalPanel searchPanel = new HorizontalPanel();
private HorizontalPanel likePanel = new HorizontalPanel();
private HorizontalPanel tweetPanel = new HorizontalPanel();
private Button updateVenuesButton = new Button("Update");
private Label lastUpdatedLabel = new Label();

private Image searchIcon = new Image();
private Label searchTitle = new Label("Search");
private Button searchButton = new Button("Search");
private Label nameLabel = new Label("Name");
private TextBox nameBox = new TextBox();
private Label addressLabel = new Label("Address");

private TextBox addressBox = new TextBox();
private Label typeLabel = new Label("Type");
private Label cityLabel = new Label("City");
private ListBox typeListBox = new ListBox();
private ListBox cityListBox = new ListBox();

private Label socialTitle = new Label("Social");
private String fbHtml = "<div class='fb-like' data-href='http://teamfantastic310.appspot.com/' data-layout='button_count' data-action='like' data-show-faces='true' data-share='true'></div>";
private HTML likeHtml = new HTML(fbHtml);
private String twtrHtml = "<a href='https://twitter.com/share' class='twitter-share-button' data-url='http://teamfantastic310.appspot.com/' data-hashtags='TeamFantastic'>Tweet</a>";
private HTML twitterHtml = new HTML(twtrHtml);
private HTML twtfcn = new HTML("<script>!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0],p=/^http:/.test(d.location)?'http':'https';if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src=p+'://platform.twitter.com/widgets.js';fjs.parentNode.insertBefore(js,fjs);}}(document, 'script', 'twitter-wjs');</script>");

private Image bookmarksIcon = new Image();
private Label bookmarksTitle = new Label("Bookmarks");
private FlexTable bookmarksFlexTable = new FlexTable();
private Button removeBookmarkButton = new Button("x");
private CheckBox visitedCheckbox = new CheckBox();

private Image visitedIcon = new Image();
private Label visitedTitle = new Label("Visited");

private Image mapIcon = new Image();

private TabPanel tabs = new TabPanel();
private VerticalPanel searchTab = new VerticalPanel();
private VerticalPanel bookmarksTab = new VerticalPanel();
private VerticalPanel visitedTab = new VerticalPanel();
private VerticalPanel mapTab = new VerticalPanel();
private VerticalPanel socialTab = new VerticalPanel();

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
    	  
        refreshVenueList();
        
      }
    });
    
	// Listen for mouse events on the venue Search button.

	searchButton.addClickHandler(new ClickHandler() {
		public void onClick(ClickEvent event) {
			searchVenues(
					nameBox.getText(),
					addressBox.getText(),
					cityListBox.getItemText(cityListBox.getSelectedIndex()),
					typeListBox.getItemText(typeListBox.getSelectedIndex()));
		}
	});

	// Listen for keyboard events in the Name input box.
	nameBox.addKeyDownHandler(new KeyDownHandler() {
		public void onKeyDown(KeyDownEvent event) {
			if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
				searchVenues(nameBox.getText(), addressBox.getText(),
						cityListBox.getItemText(cityListBox
								.getSelectedIndex()),
						typeListBox.getItemText(typeListBox
								.getSelectedIndex()));
			}
		}
	});

	// Listen for keyboard events in the Address input box.
	addressBox.addKeyDownHandler(new KeyDownHandler() {
		public void onKeyDown(KeyDownEvent event) {
			if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
				searchVenues(nameBox.getText(), addressBox.getText(),
						cityListBox.getItemText(cityListBox
								.getSelectedIndex()),
						typeListBox.getItemText(typeListBox
								.getSelectedIndex()));
			}
		}
	});


    
    // ----------- TABS --------------
    //
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

private void bookmarksFirstRow() {
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

private void setUpFirstRow() {
	venuesFlexTable.setText(0, 0, "Name");  
	venuesFlexTable.setText(0, 1, "Address"); 
	venuesFlexTable.setText(0, 2, "City");
	venuesFlexTable.setText(0, 3, "Postal Code");
	venuesFlexTable.setText(0, 4, "Telephone");
	venuesFlexTable.setText(0, 5, "Type");
	venuesFlexTable.setText(0, 6, "Capacity");
	venuesFlexTable.setText(0, 7, "Share");
	
	// Add styles to elements in the venue list table.
    venuesFlexTable.getRowFormatter().addStyleName(0, "venueListHeader");
    venuesFlexTable.addStyleName("venueList");
}


	private void searchVenues(final String name, final String address,
			final String city, final String type) {
		if (venueDetailsSvc == null) {
			venueDetailsSvc = GWT.create(VenueDetailsService.class);
		}
		// Set up the callback object.
		AsyncCallback<VenueDetails[]> callback = new AsyncCallback<VenueDetails[]>() {
			public void onFailure(Throwable caught) {
				errorMsgLabel.setText("Error while making call to server");
				errorMsgLabel.setVisible(true);
			}

			public void onSuccess(VenueDetails[] result) {
				VenueDetails[] venues = cleanVenueArray(result);
				Arrays.sort(venues);
				findCity(result); // used to get all cities
				if (name.isEmpty() && address.isEmpty() && city == "All"
						&& type == "All") {
					displayVenues(venues);
				} else {
					VenueDetails[] filteredVenueArray = filter(venues, name,
							address, city, type);
					
					//pagination();
					displayVenues(filteredVenueArray);
				}
			}
		};
		lastUpdatedLabel.setText("Last update : "
				+ DateTimeFormat.getMediumDateTimeFormat().format(new Date()));
		// Make the call to the venue price service.
		venueDetailsSvc.getPrices(callback);
	}

	private VenueDetails[] filter(VenueDetails[] venues, String name,
			String address, String city, String type) {
		if (name.isEmpty() && address.isEmpty() && city.equals("All")
				&& type.equals("All"))
			return venues; // empty search returns all venues
		else {
			ArrayList<VenueDetails> filteredVenueList = new ArrayList<VenueDetails>();
			if (city.equals("All")) {
				if (type.equals("All")) {
					for (int i = 0; i < venues.length; i++) {
						VenueDetails curr = venues[i];
						if (curr.getVenueName().trim().toLowerCase()
								.contains(name.trim().toLowerCase())
								&& curr.getVenueAdd1().trim().toLowerCase()
										.contains(address.trim().toLowerCase()))
							filteredVenueList.add(curr);
					}
				} else {
					for (int i = 0; i < venues.length; i++) {
						VenueDetails curr = venues[i];
						if (curr.getVenueName().trim().toLowerCase()
								.contains(name.trim().toLowerCase())
								&& curr.getVenueAdd1().trim().toLowerCase()
										.contains(address.trim().toLowerCase())
								&& curr.getVenueType().trim().toLowerCase()
										.contains(type.trim().toLowerCase()))
							filteredVenueList.add(curr);
					}
				}
			} else {
				if (type.equals("All")) {
					for (int i = 0; i < venues.length; i++) {
						VenueDetails curr = venues[i];
						if (curr.getVenueName().trim().toLowerCase()
								.contains(name.trim().toLowerCase())
								&& curr.getVenueAdd1().trim().toLowerCase()
										.contains(address.trim().toLowerCase())
								&& curr.getVenueCity().trim().toLowerCase()
										.equals(city.trim().toLowerCase()))
							filteredVenueList.add(curr);
					}
				} else {
					for (int i = 0; i < venues.length; i++) {
						VenueDetails curr = venues[i];
						if (curr.getVenueName().trim().toLowerCase()
								.contains(name.trim().toLowerCase())
								&& curr.getVenueAdd1().trim().toLowerCase()
										.contains(address.trim().toLowerCase())
								&& curr.getVenueCity().trim().toLowerCase()
										.equals(city.trim().toLowerCase())
								&& curr.getVenueType().trim().toLowerCase()
										.contains(type.trim().toLowerCase()))
							filteredVenueList.add(curr);
					}
				}
			}
			VenueDetails[] filteredVenueArray = new VenueDetails[filteredVenueList
					.size()];
			filteredVenueArray = filteredVenueList
					.toArray(new VenueDetails[filteredVenueList.size()]);
			return filteredVenueArray;
		}
	}

	private void refreshVenueList() {
		if (venueDetailsSvc == null) {
			venueDetailsSvc = GWT.create(VenueDetailsService.class);
		}
		// Set up the callback object.
		AsyncCallback<VenueDetails[]> callback = new AsyncCallback<VenueDetails[]>() {
			public void onFailure(Throwable caught) {
				errorMsgLabel.setText("Error while making call to server");
				errorMsgLabel.setVisible(true);
			}

			public void onSuccess(VenueDetails[] result) {
				VenueDetails[] venues = cleanVenueArray(result);
				displayVenues(venues);
			}
		};
		lastUpdatedLabel.setText("Last update : "
				+ DateTimeFormat.getMediumDateTimeFormat().format(new Date()));
		// Make the call to the venue price service.
		venueDetailsSvc.getPrices(callback);
	}

	/**
	 * Takes list of venues minues the first row, removes empty venue names, and
	 * sorts it alphabetically;
	 */
	private VenueDetails[] cleanVenueArray(VenueDetails[] venues) {
		VenueDetails[] results = new VenueDetails[(venues.length) - 1];
		for (int i = 0; i < results.length; i++) {
			results[i] = venues[i + 1]; // first row is field parameters
		}
		Arrays.sort(results);
		return results;
	}

	private void findCity(VenueDetails[] result) {
		Set<String> cities = new TreeSet<String>();
		for (VenueDetails venue : result) {
			cities.add(venue.getVenueCity());
		}
	}

	/**
	 * Add venues to FlexTable. Executed when the user clicks the
	 * updateVenuesButton
	 */
	private void displayVenues(VenueDetails[] venues) {
		venuesFlexTable.removeAllRows();
		setUpFirstRow();
		lastUpdatedLabel.setText(Integer.toString(venues.length));
		for (int i = 0; i < venues.length; i++) {
			venuesFlexTable.setText(i + 1, 0, venues[i].getVenueName());
			venuesFlexTable.setText(i + 1, 1, venues[i].getVenueAdd1());
			venuesFlexTable.setText(i + 1, 2, venues[i].getVenueCity());
			venuesFlexTable.setText(i + 1, 3, venues[i].getVenuePostal());
			venuesFlexTable.setText(i + 1, 4, venues[i].getVenuePhone());
			venuesFlexTable.setText(i + 1, 5, venues[i].getVenueType());
			venuesFlexTable.setText(i + 1, 6, venues[i].getVenueCapacity());

		}

	}

}
