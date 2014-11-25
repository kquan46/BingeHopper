package com.bingehopper.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
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
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.geocode.Geocoder;
import com.google.gwt.maps.client.geocode.LatLngCallback;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.user.client.ui.DockLayoutPanel;

public class BingeHopper implements EntryPoint

{
	private ListDataProvider<VenueDetails> provider;
	// create ArrayList of VenueDetails objects
	ArrayList<VenueDetails> listOfVenues = new ArrayList<VenueDetails>();

	Set<String> listOfTypes = new TreeSet<String>();
	Set<String> listOfCities = new TreeSet<String>();
	
	// create pagination
	SimplePager.Resources pagerResources = GWT
			.create(SimplePager.Resources.class);
	SimplePager pager = new SimplePager(TextLocation.CENTER, pagerResources,
			false, 200, true);

	// create panels
	private VerticalPanel mainPanel = new VerticalPanel();
	private HorizontalPanel updatePanel = new HorizontalPanel();
	private HorizontalPanel searchPanel = new HorizontalPanel();
	
//CB1
	private HorizontalPanel twitterTimelinePanel = new HorizontalPanel();
	private VerticalPanel searchTab = new VerticalPanel();
	private VerticalPanel bookmarksTab = new VerticalPanel();
	private VerticalPanel visitedTab = new VerticalPanel();
	private VerticalPanel mapTab = new VerticalPanel();
	private VerticalPanel socialTab = new VerticalPanel();
	private VerticalPanel loginPanel = new VerticalPanel();

	// create tables
	private CellTable<VenueDetails> venuesTable = new CellTable<VenueDetails>();
	// private FlexTable venuesFlexTable = new FlexTable();
	private FlexTable bookmarksFlexTable = new FlexTable();

	// create buttons
	private Button updateVenuesButton = new Button("Update");
	private Button searchButton = new Button("Search");
	private Button removeBookmarksButton = new Button(
			"Remove (currently removes the 4 arbitrary Venues added by the add button)");
	private Button addBookmarksButton = new Button(
			"Add (currently adds 4 arbitrary Venues to the bookmarks list: check buttonlistener for details)");
	private Button displayBookmarksButton = new Button(
			"Display (displays the 4 bookmarks if add was pressed last, nothing if remove was pressed)");

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
	private Label loginLabel = new Label(
			"Please sign in to your Google Account to access the BingeHopper application.");
	private Label welcomeText = new Label("Welcome to BingeHopper!");
	private HTML appDescription = new HTML(
			"<p>This app aims to provide people with the capacity to search for"
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
	private String facebookCommentURL = "\"http://teamfantastic310.appspot.com/\"";
	private FacebookCommentBox facebookCommentBox = new FacebookCommentBox(facebookCommentURL);
	private HorizontalPanel facebookPanel = new HorizontalPanel();
	
	private String twitterTimelineURL = "\"https://twitter.com/hashtag/teamfantastic310\"";
	private TwitterTimeline twitterTimeline = new TwitterTimeline(twitterTimelineURL);
	private HorizontalPanel twitterPanel = new HorizontalPanel();
	
	
//CB2
	
	// create tab panel
	private TabPanel tabs = new TabPanel();

	// initialize async callback service
	private VenueDetailsServiceAsync venueDetailsSvc = GWT
			.create(VenueDetailsService.class);
	// async callback service for bookmarks functionality
	private final VenueServiceAsync venueService = GWT
			.create(VenueService.class);

	// setup login service
	private LoginInfo loginInfo = null;
	private Anchor signInLink = new Anchor("Sign In");
	private Anchor signOutLink = new Anchor("Sign Out");

	// EntryPoint method
	public void onModuleLoad() {
		// Check login status using login service
		LoginServiceAsync loginService = GWT.create(LoginService.class);
		loginService.login(GWT.getHostPageBaseURL(),
				new AsyncCallback<LoginInfo>() {

					public void onFailure(Throwable error) {

						errorMsgLabel.setText(error.getMessage());
						errorMsgLabel.setVisible(true);

					}

					public void onSuccess(LoginInfo result) {

						loginInfo = result;

						if (loginInfo.isLoggedIn()) {

							Maps.loadMapsApi(
									"AIzaSyCTk1NtYlfZeSWetnBjL6bOrLnl99A6now",
									"2", false, new Runnable() {
										public void run() {
											loadBingeHopper();
										}
									});

						}

						else {

							loadLogin();

						}

					}

				});

		/*
		 * Asynchronously loads the Maps API.
		 */

	}

	private void loadLogin() {

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

	private void loadBingeHopper() {
		
		// Set up sign out hyperlink.
		signOutLink.setHref(loginInfo.getLogoutUrl());
		signOutLink.addStyleName("signOutLink");

		// fetch venue data from server
		fetchData();

		// Create table for venue data
		setUpCellTable();

		// Assemble Update Venues panel.
		updatePanel.add(updateVenuesButton);
		updatePanel.add(lastUpdatedLabel);
		updatePanel.addStyleName("updatePanel");

		// Assemble Facebook Comment Box panel
		facebookPanel.add(facebookCommentBox);
		facebookPanel.addStyleName("fbLikePanel");

		
// CB3
		// Assmeble Twitter Timeline panel
		twitterPanel.add(twitterTimeline);
		twitterPanel.addStyleName("fbLikePanel");

		// Assemble Search Venues panel
		searchPanel.add(nameLabel);
		searchPanel.add(nameBox);
		searchPanel.add(addressLabel);
		searchPanel.add(addressBox);
		searchPanel.add(cityLabel);
		searchPanel.add(cityListBox);
		searchPanel.add(typeLabel);
		searchPanel.add(typeListBox);
		searchPanel.add(searchButton);

		// Create table for Bookmarks
		bookmarksFirstRow();
		
		
		// Create Map Widget
		LatLng vancouver = LatLng.newInstance(49.2500, -123.1000);
		final Label statusLabel = new Label();
		final MapWidget map = new MapWidget(vancouver, 10);
		map.setSize("100%", "100%");
		map.addControl(new LargeMapControl());
		
		// Callback Method post obtaining LatLng
		LatLngCallback callback = new LatLngCallback() {

			public void onFailure() {
			statusLabel.setText("Address was Not Found");
			}

			public void onSuccess(LatLng point) {
			statusLabel.setText("Address was Found");
			Marker marker = new Marker(point);
			map.addOverlay(marker);
			map.setCenter(point);
			}
		};
			
			// An Arraylist for Testing Purposes
			ArrayList<String> listOfAddresses = new ArrayList<String>(); 
			listOfAddresses.add("812 5300 NO 3 RD");
			listOfAddresses.add("5500 No. 4 Rd");
			
			// Plot the Points from the ArrayList
			// replace listOfAddresses with bookmark ArrayList
			for (int i = 0; i<2; i++) {
			Geocoder geocoder = new Geocoder();
			geocoder.getLatLng(listOfAddresses.get(i), callback);
			}
			
		// Create Map Panel
		final DockLayoutPanel dock = new DockLayoutPanel(Unit.PX);
		dock.addNorth(map, 500);
		dock.setVisible(false);
		
		// Create Button to show map
		Button mapButton = new Button("Click to View Map");
		mapButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
    			dock.setVisible(true);
    			map.checkResizeAndCenter();
			}

		});	
	
			
		// Assemble Main panel.
		errorMsgLabel.setStyleName("errorMessage");
		errorMsgLabel.setVisible(false);

		mainPanel.add(signOutLink);
		mainPanel.add(errorMsgLabel);

		// Associate the Main panel with the HTML host page.
		RootPanel.get("venueList").add(mainPanel);

		// Listen for mouse events on the Add button.
		updateVenuesButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

				lastUpdatedLabel.setText("reached clickhandler");
				// refreshVenueList();

			}

		});

		// Listen for mouse events on the venue Search button.
		searchButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

				searchVenues(

						nameBox.getText(),
						addressBox.getText(),
						cityListBox.getItemText(cityListBox.getSelectedIndex()),
						typeListBox.getItemText(typeListBox.getSelectedIndex())

				);
			}

		});

		// Listen for keyboard events in the Name input box.
		nameBox.addKeyDownHandler(new KeyDownHandler() {

			public void onKeyDown(KeyDownEvent event) {

				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {

					searchVenues(

					nameBox.getText(), addressBox.getText(), cityListBox
							.getItemText(cityListBox.getSelectedIndex()),
							typeListBox.getItemText(typeListBox
									.getSelectedIndex())

					);
				}

			}

		});

		// Listen for keyboard events in the Address input box.
		addressBox.addKeyDownHandler(new KeyDownHandler() {

			public void onKeyDown(KeyDownEvent event) {

				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {

					searchVenues(

					nameBox.getText(), addressBox.getText(), cityListBox
							.getItemText(cityListBox.getSelectedIndex()),
							typeListBox.getItemText(typeListBox
									.getSelectedIndex())

					);
				}

			}

		});

		// implemented to test the bookmark functionality. Just added 4
		// arbitrary VenueDetails objects from the listOfVenues
		addBookmarksButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

				addBookmark(listOfVenues.get(5).getId());
				addBookmark(listOfVenues.get(7).getId());
				addBookmark(listOfVenues.get(10).getId());
				addBookmark(listOfVenues.get(18).getId());

			}

		});

		// implemented to test the removal of bookmarks functionality. Removes
		// the 4 bookmarks added above
		removeBookmarksButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

				removeBookmark(listOfVenues.get(5).getId());
				removeBookmark(listOfVenues.get(7).getId());
				removeBookmark(listOfVenues.get(10).getId());
				removeBookmark(listOfVenues.get(18).getId());

			}

		});

		// this function can stay as is. Calling retrieveAndDisplayBookmarks()
		// will display all the current bookmarks
		// for the current user in the bookmarksFlexTable
		displayBookmarksButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

				retrieveAndDisplayBookmarks();

			}

		});

		// ----------- TABS --------------

		// Organize Search Tab
		searchIcon.setUrl("search.png");
		searchIcon.addStyleName("tabIcon");
		searchTab.add(searchTitle);
		searchTitle.addStyleName("title");
		searchTab.add(updatePanel);
		searchTab.add(searchPanel);
		searchTab.add(venuesTable);
		searchTab.add(pager);
		// searchTab.add(venuesTable);

		// Organize Bookmarks Tab
		bookmarksIcon.setUrl("bookmarks.png");
		bookmarksIcon.addStyleName("tabIcon");
		bookmarksTab.add(bookmarksTitle);
		bookmarksTab.add(addBookmarksButton);
		bookmarksTab.add(removeBookmarksButton);
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
		mapTab.add(dock);
		mapTab.add(mapButton);
		
		// Organize Social Tab
		socialTab.add(socialTitle);
		socialTitle.addStyleName("title");
		socialTab.add(facebookPanel);
		socialTab.add(twitterPanel);
		
//CB4

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

	private void addDropDownList() {
		for (VenueDetails venue : listOfVenues) {
			listOfCities.add(venue.getVenueCity());
			listOfTypes.add(venue.getVenueType());
		}
		cityListBox.addItem("ALL");
		for (String venueCity : listOfCities) {
			if (!venueCity.equals("n/a"))
				cityListBox.addItem(venueCity);
		}
		typeListBox.addItem("ALL");
		for (String venueType : listOfTypes) {
			if (!venueType.equals("n/a")
					&& !venueType.equals("Independent Agent"))
				typeListBox.addItem(venueType);
		}
	}

	private void setUpCellTable() {
		// Create a CellTable
		venuesTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);

		// Display 100 rows in one page
		venuesTable.setPageSize(100);
		// venuesTable.setVisibleRange(0, 100);

		// Add a text column to show the name.
		TextColumn<VenueDetails> nameColumn = new TextColumn<VenueDetails>() {
			@Override
			public String getValue(VenueDetails venue) {
				return venue.getVenueName();
			}
		};

		// Add a text column to show the address.
		TextColumn<VenueDetails> addressColumn = new TextColumn<VenueDetails>() {
			@Override
			public String getValue(VenueDetails venue) {
				return venue.getVenueAdd1();
			}
		};

		// Add a text column to show the city.
		TextColumn<VenueDetails> cityColumn = new TextColumn<VenueDetails>() {
			@Override
			public String getValue(VenueDetails venue) {
				return venue.getVenueCity();
			}
		};

		// Add a text column to show the postal code.
		TextColumn<VenueDetails> postalCodeColumn = new TextColumn<VenueDetails>() {
			@Override
			public String getValue(VenueDetails venue) {
				return venue.getVenuePostal();
			}
		};

		// Add a text column to show the telephone number.
		TextColumn<VenueDetails> telephoneColumn = new TextColumn<VenueDetails>() {
			@Override
			public String getValue(VenueDetails venue) {
				return venue.getVenuePhone();
			}
		};

		// Add a text column to show the establishment type.
		TextColumn<VenueDetails> typeColumn = new TextColumn<VenueDetails>() {
			@Override
			public String getValue(VenueDetails venue) {
				return venue.getVenueType();
			}
		};

		// Add a text column to show the capacity.
		TextColumn<VenueDetails> capacityColumn = new TextColumn<VenueDetails>() {
			@Override
			public String getValue(VenueDetails venue) {
				return venue.getVenueCapacity();
			}
		};

		// Add Columns to CellTable
		venuesTable.addColumn(nameColumn, "Name");
		venuesTable.addColumn(addressColumn, "Address");
		venuesTable.addColumn(cityColumn, "City");
		venuesTable.addColumn(postalCodeColumn, "Postal Code");
		venuesTable.addColumn(telephoneColumn, "Telephone");
		venuesTable.addColumn(typeColumn, "Type");
		venuesTable.addColumn(capacityColumn, "Capacity");

		// Make the columns sortable
		nameColumn.setSortable(true);
		addressColumn.setSortable(true);
		cityColumn.setSortable(true);
		postalCodeColumn.setSortable(true);
		telephoneColumn.setSortable(true);
		typeColumn.setSortable(true);
		capacityColumn.setSortable(true);

		/*
		 * // Add a selection model to handle user selection final
		 * SingleSelectionModel<VenueDetails> selectionModel = new
		 * SingleSelectionModel<VenueDetails>();
		 * venuesTable.setSelectionModel(selectionModel); selectionModel
		 * .addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
		 * public void onSelectionChange(SelectionChangeEvent event) {
		 * VenueDetails selected = selectionModel .getSelectedObject(); if
		 * (selected != null) { Window.alert("You selected: " +
		 * selected.getVenueName()); } } });
		 */
	}

	public void fetchData() {
		if (venueDetailsSvc == null) {
			venueDetailsSvc = GWT.create(VenueDetailsService.class);
		}

		AsyncCallback<List<VenueDetails>> callback = new AsyncCallback<List<VenueDetails>>() {

			public void onFailure(Throwable caught) {
				errorMsgLabel.setText("Error while making call to server");
				errorMsgLabel.setVisible(true);
			}

			public void onSuccess(List<VenueDetails> result) {
				listOfVenues = new ArrayList<VenueDetails>(result);
				provider = new ListDataProvider<VenueDetails>(result);
				provider.addDataDisplay(venuesTable);
				addDropDownList();
			}

		};

		lastUpdatedLabel.setText("Last update : "
				+ DateTimeFormat.getMediumDateTimeFormat().format(new Date()));
		// Make the call to the venue price service.
		venueDetailsSvc.getPrices(callback);
		pager.setDisplay(venuesTable);
	}

	private void searchVenues(final String name, final String address,
			final String city, final String type) {
		ArrayList<VenueDetails> filteredVenueList = new ArrayList<VenueDetails>();
		if (name.isEmpty() && address.isEmpty() && city == "ALL"
				&& type == "ALL") {
			filteredVenueList = listOfVenues;
		} 
		else {
			
			TreeSet<String> selectedCity = new TreeSet<String>();
			selectedCity.add(city);
			TreeSet<String> selectedType = new TreeSet<String>();
			selectedType.add(type);
			if (city.equals("ALL")) {
				if (type.equals("ALL"))
					filteredVenueList = filter(name, address, listOfCities, listOfTypes);
				else
					filteredVenueList = filter(name, address, listOfCities, selectedType);

			} else {
				if (type.equals("ALL"))
					filteredVenueList = filter(name, address, selectedCity, listOfTypes);
				else
					filteredVenueList = filter(name, address, selectedCity, selectedType);
			}
		}
		provider.setList(filteredVenueList);
		provider.refresh();
		pager.setPage(0);
		lastUpdatedLabel.setText("Last update : "
				+ DateTimeFormat.getMediumDateTimeFormat().format(new Date()));
	}

	private ArrayList<VenueDetails> filter(String name, String address,
			Set<String> cities, Set<String> types) {
		ArrayList<VenueDetails> filteredList = new ArrayList<VenueDetails>();
		for (VenueDetails venue : listOfVenues) {
			if (venue.getVenueName().trim().toLowerCase()
					.contains(name.trim().toLowerCase())
					&& venue.getVenueAdd1().trim().toLowerCase()
							.contains(address.trim().toLowerCase())
					&& cities.contains(venue.getVenueCity())
					&& types.contains(venue.getVenueType()))
				filteredList.add(venue);
		}
		return filteredList;
	}

	// adds the Venue with id "id" to the current user's bookmarks list
	private void addBookmark(final int id) {

		// calls the getVenues method to retrieve the Venue ids that have
		// already been bookmarked by the current user, then checks if
		// the current "id" being added exists in that list. If it does not
		// exist, it adds the Venue with "id" to the bookmarks list, if
		// not, it does nothing
		venueService.getVenues(new AsyncCallback<ArrayList<Integer>>() {

			public void onFailure(Throwable error) {

				lastUpdatedLabel.setText("reached failure");
			}

			public void onSuccess(ArrayList<Integer> ids)

			{
				if (!ids.contains(id)) {
					// this is essentially how to add a Venue to the bookmarks
					// list. If not for the preceding check code,
					// calling only this method to add bookmarks will add
					// duplicates as well
					venueService.addVenue(id, new AsyncCallback<Void>() {
						public void onFailure(Throwable error) {

							lastUpdatedLabel.setText("failed to add venue");

						}

						public void onSuccess(Void ignore) {

							lastUpdatedLabel.setText("successfully added");

						}
					});

				}

			}

		});

	}

	// retrieves an ArrayList of Venue id's that have been bookmarked by the
	// current user,
	// and uses the list to display the corresponding Venues in the
	// bookmarksFlexTable using the displayBookmarks method
	private void retrieveAndDisplayBookmarks() {

		final ArrayList<VenueDetails> bookmarkedVenues = new ArrayList<VenueDetails>();
		venueService.getVenues(new AsyncCallback<ArrayList<Integer>>() {

			public void onFailure(Throwable error) {

				lastUpdatedLabel
						.setText("failed to retrieve bookmarked venues");
			}

			public void onSuccess(ArrayList<Integer> ids)

			{

				for (int i = 0; i < ids.size(); i++) {

					for (int j = 0; j < listOfVenues.size(); j++) {

						if (ids.get(i) == listOfVenues.get(j).getId()) {

							bookmarkedVenues.add(listOfVenues.get(j));

						}

					}

				}

				displayBookmarks(bookmarkedVenues);

			}

		});

	}

	// removes the Venue with "id" from the current user's bookmarks list
	private void removeBookmark(int id) {

		venueService.removeVenue(id, new AsyncCallback<Void>() {
			public void onFailure(Throwable error) {

				lastUpdatedLabel.setText("failed to remove venue");

			}

			public void onSuccess(Void ignore) {

				lastUpdatedLabel.setText("successfully removed");

			}
		});

	}

	/**
	 * Takes list of venues minues the first row, removes empty venue names, and
	 * sorts it alphabetically;
	 */
	// private List<VenueDetails> cleanVenueArray(List<VenueDetails> venues) {
	// VenueDetails[] results = new VenueDetails[(venues.length) - 1];
	// for (int i = 0; i < results.length; i++) {
	// results[i] = venues[i + 1]; // first row is field parameters
	// }
	// Arrays.sort(results);
	// return results;
	// }

	private VenueDetails[] cleanVenueArray(VenueDetails[] venues) {

		VenueDetails[] results = new VenueDetails[(venues.length) - 1];

		for (int i = 0; i < results.length; i++) {

			// first row is field parameters
			results[i] = venues[i + 1];

		}

		Arrays.sort(results);
		return results;
	}

	private ArrayList<VenueDetails> getListOfVenues() {

		return this.listOfVenues;

	}

	private void displayBookmarks(ArrayList<VenueDetails> venues) {

		lastUpdatedLabel.setText("reached displayBookmark");
		bookmarksFlexTable.removeAllRows();
		bookmarksFirstRow();
		// lastUpdatedLabel.setText(Integer.toString(venues.size()));

		for (int i = 0; i < venues.size(); i++) {

			bookmarksFlexTable.setText(i + 1, 0, venues.get(i).getVenueName());
			bookmarksFlexTable.setText(i + 1, 1, venues.get(i).getVenueAdd1());
			bookmarksFlexTable.setText(i + 1, 2, venues.get(i).getVenueCity());
			bookmarksFlexTable
					.setText(i + 1, 3, venues.get(i).getVenuePostal());
			bookmarksFlexTable.setText(i + 1, 4, venues.get(i).getVenuePhone());
			bookmarksFlexTable.setText(i + 1, 5, venues.get(i).getVenueType());
			bookmarksFlexTable.setText(i + 1, 6, venues.get(i)
					.getVenueCapacity());

		}

	}

}
