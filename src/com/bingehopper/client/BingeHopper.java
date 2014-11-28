package com.bingehopper.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.geocode.Geocoder;
import com.google.gwt.maps.client.geocode.LatLngCallback;
import com.google.gwt.maps.client.geom.Bounds;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.LatLngBounds;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.user.client.ui.DockLayoutPanel;

public class BingeHopper implements EntryPoint

{
	// create panels
	private VerticalPanel mainPanel = new VerticalPanel();
	private HorizontalPanel venueUpdatePanel = new HorizontalPanel();
	private HorizontalPanel bookmarksUpdatePanel = new HorizontalPanel();
	private HorizontalPanel searchPanel = new HorizontalPanel();

	private VerticalPanel searchTab = new VerticalPanel();
	private VerticalPanel bookmarksTab = new VerticalPanel();
	private VerticalPanel visitedTab = new VerticalPanel();
	private VerticalPanel mapTab = new VerticalPanel();
	private HorizontalPanel socialTab = new HorizontalPanel();
	private VerticalPanel loginPanel = new VerticalPanel();

	// create tables
	private CellTable<VenueDetails> venuesTable = new CellTable<VenueDetails>();
	private CellTable<VenueDetails> bookmarksTable = new CellTable<VenueDetails>();

	// create columns for tables
	private TextColumn<VenueDetails> nameColumn;
	private TextColumn<VenueDetails> addressColumn;
	private TextColumn<VenueDetails> cityColumn;
	private TextColumn<VenueDetails> postalCodeColumn;
	private TextColumn<VenueDetails> telephoneColumn;
	private TextColumn<VenueDetails> typeColumn;
	private TextColumn<VenueDetails> capacityColumn;

	// create pagination
	private SimplePager.Resources pagerResources = GWT
			.create(SimplePager.Resources.class);
	private SimplePager venuesPager = new SimplePager(TextLocation.CENTER,
			pagerResources, false, 200, true);
	private SimplePager bookmarksPager = new SimplePager(TextLocation.CENTER,
			pagerResources, false, 200, true);

	// create buttons
	private Button updateVenuesButton = new Button("Update");
	private Button searchButton = new Button("Search");
	private Button removeAllButton = new Button("Remove All");

	// create labels
	private Label updatedVenueLabel = new Label();
	private Label updatedBookmarksLabel = new Label();
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

	// Create fields for drop down list box
	private Set<String> listOfTypes = new TreeSet<String>();
	private Set<String> listOfCities = new TreeSet<String>();

	// create custom icons
	private Image searchIcon = new Image();
	private Image visitedIcon = new Image();
	private Image mapIcon = new Image();
	private Image bookmarksIcon = new Image();
	private Image socialIcon = new Image();

	// social network integration elements
	private String facebookCommentURL = "\"http://teamfantastic310.appspot.com/\"";
	private FacebookCommentBox facebookCommentBox = new FacebookCommentBox(
			facebookCommentURL);
	private HorizontalPanel facebookPanel = new HorizontalPanel();

	private String twitterTimelineURL = "\"https://twitter.com/hashtag/teamfantastic310\"";
	private TwitterTimeline twitterTimeline = new TwitterTimeline(
			twitterTimelineURL);
	private HorizontalPanel twitterPanel = new HorizontalPanel();

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

	// create ArrayList of VenueDetails objects
	private ArrayList<VenueDetails> listOfVenues;
	private ArrayList<VenueDetails> listOfBookmarks;

	// create ListDataProvider for CellTable
	private ListDataProvider<VenueDetails> venuesProvider;
	private ListDataProvider<VenueDetails> bookmarksProvider;

	// Create Map Widget
	private Label statusLabel;
	private MapWidget map;
	private double maxLat = 49.2851;
	private double maxLon = -122.9080;
	private double minLat = 49.1842;
	private double minLon = -123.2537;
	private LatLng northEast;
	private LatLng southWest;
	private LatLngBounds bound;
	private static LatLng ne;
	private static LatLng sw;
	private static LatLngBounds b;
	private static LatLng center;
	private ArrayList<LatLng> points = new ArrayList<LatLng>();

	// private ArrayList<Marker> markers = new ArrayList<Marker>();

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

		// fetch bookmarked venues from server
		loadBookmarks();

		// fetch venue data from server
		loadVenues();

		// Create cell table for venues and bookmarks
		setUpCellTable();

		// Assemble Update Venues panel.
		// updatePanel.add(updateVenuesButton);
		venueUpdatePanel.add(updatedVenueLabel);
		venueUpdatePanel.addStyleName("updatePanel");
		bookmarksUpdatePanel.add(updatedBookmarksLabel);
		bookmarksUpdatePanel.add(removeAllButton);
		bookmarksUpdatePanel.setCellHorizontalAlignment(removeAllButton,
				HasHorizontalAlignment.ALIGN_RIGHT);
		bookmarksUpdatePanel.addStyleName("updatePanel");

		// Assemble Facebook Comment Box panel
		facebookPanel.add(facebookCommentBox);
		facebookPanel.addStyleName("fbLikePanel");

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

		// Initialize Map Widget
		LatLng vancouver = LatLng.newInstance(49.2500, -123.1000);
		statusLabel = new Label();
		map = new MapWidget(vancouver, 9);
		map.setSize("100%", "100%");
		// map.addControl(new LargeMapControl());

		// Create Map Panel
		final DockLayoutPanel dock = new DockLayoutPanel(Unit.PX);
		dock.addNorth(map, 500);
		dock.setVisible(true);

		tabs.addSelectionHandler(new SelectionHandler<Integer>() {
			public void onSelection(SelectionEvent<Integer> event) {
				int tabId = event.getSelectedItem();
				Widget tabWidget = tabs.getWidget(tabId);
				if (tabWidget != null && tabId == 3) {
					map.checkResizeAndCenter();
					dock.setVisible(true);
					setZoomingBound();
					ne = northEast.newInstance(maxLat, maxLon);
					sw = southWest.newInstance(minLat, minLon);
					b = bound.newInstance(sw, ne);
					center = b.getCenter();
					map.setZoomLevel(map.getBoundsZoomLevel(b));
					map.setCenter(center);
				} else
					dock.setVisible(false);
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

				updatedVenueLabel.setText("reached clickhandler");

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

		// Listen for mouse events on the Add button.
		removeAllButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				removeAllBookmarks();
			}
		});

		/*
		 * // Add sorting for name column
		 * ColumnSortEvent.ListHandler<VenueDetails> nameSortHandler = new
		 * ColumnSortEvent.ListHandler<VenueDetails>( venuesProvider.getList());
		 * nameSortHandler.setComparator(nameColumn, new
		 * Comparator<VenueDetails>() { public int compare(VenueDetails venue1,
		 * VenueDetails venue2) { if (venue1 == venue2) { return 0; } if (venue1
		 * != null) { return (venue2 != null) ? venue1.getVenueName()
		 * .compareTo(venue2.getVenueName()) : 1; } return -1; } });
		 * 
		 * // Add sorting for address column
		 * ColumnSortEvent.ListHandler<VenueDetails> addressSortHandler = new
		 * ColumnSortEvent.ListHandler<VenueDetails>( venuesProvider.getList());
		 * addressSortHandler.setComparator(addressColumn, new
		 * Comparator<VenueDetails>() { public int compare(VenueDetails venue1,
		 * VenueDetails venue2) { if (venue1 == venue2) { return 0; } if (venue1
		 * != null) { return (venue2 != null) ? venue1.getVenueAdd1()
		 * .compareTo(venue2.getVenueAdd1()) : 1; } return -1; } });
		 * 
		 * // Add sorting for capacity column
		 * ColumnSortEvent.ListHandler<VenueDetails> capacitySortHandler = new
		 * ColumnSortEvent.ListHandler<VenueDetails>( venuesProvider.getList());
		 * capacitySortHandler.setComparator(capacityColumn, new
		 * Comparator<VenueDetails>() { public int compare(VenueDetails venue1,
		 * VenueDetails venue2) { if (venue1 == venue2) { return 0; } if (venue1
		 * != null) { return (venue2 != null) ? venue1.getVenueCapacity()
		 * .compareTo(venue2.getVenueCapacity()) : 1; } return -1; } });
		 */

		// ----------- TABS --------------

		// Organize Search Tab
		searchIcon.setUrl("search.png");
		searchIcon.addStyleName("tabIcon");
		searchTab.add(searchTitle);
		searchTitle.addStyleName("title");
		searchTab.add(venueUpdatePanel);
		searchTab.add(searchPanel);
		searchTab.add(venuesTable);
		searchTab.add(venuesPager);
		searchTab.addStyleName("tabElement");

		// Organize Bookmarks Tab
		bookmarksIcon.setUrl("bookmarks.png");
		bookmarksIcon.addStyleName("tabIcon");
		bookmarksTab.add(bookmarksTitle);
		bookmarksTab.add(bookmarksUpdatePanel);
		bookmarksTitle.addStyleName("title");
		bookmarksTab.add(bookmarksTable);
		bookmarksTab.add(bookmarksPager);
		bookmarksTab.addStyleName("tabElement");

		// Organize Visited Tab
		visitedIcon.setUrl("visited.png");
		visitedIcon.addStyleName("tabIcon");
		visitedTab.add(visitedTitle);
		visitedTitle.addStyleName("title");
		visitedTab.addStyleName("tabElement");

		// Organize Map Tab
		mapIcon.setUrl("maps.png");
		mapIcon.addStyleName("tabIcon");
		mapTab.add(dock);
		// mapTab.add(mapButton);
		mapTab.addStyleName("tab");

		// Organize Social Tab
		socialIcon.setUrl("social.png");
		socialIcon.addStyleName("tabIcon");
		socialTab.add(socialIcon);
		socialTitle.addStyleName("title");
		socialTab.add(facebookPanel);
		socialTab.add(twitterPanel);
		socialTab.addStyleName("tabElement");

		// configure tabs
		tabs.add(searchTab, searchIcon);
		tabs.add(bookmarksTab, bookmarksIcon);
		tabs.add(visitedTab, visitedIcon);
		tabs.add(mapTab, mapIcon);
		tabs.add(socialTab, socialIcon);

		// show the 'map' tab initially
		tabs.selectTab(0);

		// add to mainPanel
		mainPanel.add(tabs);

		// set stylename for tabs
		tabs.setStyleName("tabs");

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

	private void setUpColumnSort(CellTable<VenueDetails> table,
			ListDataProvider<VenueDetails> provider) {
		// Add sorting for address column ListHandler<VenueDetails>
		ColumnSortEvent.ListHandler<VenueDetails> addressSortHandler = new ColumnSortEvent.ListHandler<VenueDetails>(
				provider.getList());
		addressSortHandler.setComparator(addressColumn,
				new Comparator<VenueDetails>() {
					public int compare(VenueDetails venue1, VenueDetails venue2) {
						if (venue1 == venue2) {
							return 0;
						}
						if (venue1 != null) {
							return (venue2 != null) ? venue1.getVenueAdd1()
									.compareTo(venue2.getVenueAdd1()) : 1;
						}
						return -1;
					}
				});
		table.addColumnSortHandler(addressSortHandler);
		table.getColumnSortList().push(addressColumn);

		// Add sorting for city column ListHandler<VenueDetails>
		ColumnSortEvent.ListHandler<VenueDetails> citySortHandler = new ColumnSortEvent.ListHandler<VenueDetails>(
				provider.getList());
		citySortHandler.setComparator(cityColumn,
				new Comparator<VenueDetails>() {
					public int compare(VenueDetails venue1, VenueDetails venue2) {
						if (venue1 == venue2) {
							return 0;
						}
						if (venue1 != null) {
							return (venue2 != null) ? venue1.getVenueCity()
									.compareTo(venue2.getVenueCity()) : 1;
						}
						return -1;
					}
				});
		table.addColumnSortHandler(citySortHandler);
		table.getColumnSortList().push(cityColumn);

		// Add sorting for city column ListHandler<VenueDetails>
		ColumnSortEvent.ListHandler<VenueDetails> postalCodeSortHandler = new ColumnSortEvent.ListHandler<VenueDetails>(
				provider.getList());
		postalCodeSortHandler.setComparator(postalCodeColumn,
				new Comparator<VenueDetails>() {
					public int compare(VenueDetails venue1, VenueDetails venue2) {
						if (venue1 == venue2) {
							return 0;
						}
						if (venue1 != null) {
							return (venue2 != null) ? venue1.getVenuePostal()
									.compareTo(venue2.getVenuePostal()) : 1;
						}
						return -1;
					}
				});
		table.addColumnSortHandler(postalCodeSortHandler);
		table.getColumnSortList().push(postalCodeColumn);

		// Add sorting for city column ListHandler<VenueDetails>
		ColumnSortEvent.ListHandler<VenueDetails> telephoneSortHandler = new ColumnSortEvent.ListHandler<VenueDetails>(
				provider.getList());
		telephoneSortHandler.setComparator(telephoneColumn,
				new Comparator<VenueDetails>() {
					public int compare(VenueDetails venue1, VenueDetails venue2) {
						if (venue1 == venue2) {
							return 0;
						}
						if (venue1 != null) {
							return (venue2 != null) ? venue1.getVenuePhone()
									.compareTo(venue2.getVenuePhone()) : 1;
						}
						return -1;
					}
				});
		table.addColumnSortHandler(telephoneSortHandler);
		table.getColumnSortList().push(telephoneColumn);

		// Add sorting for city column ListHandler<VenueDetails>
		ColumnSortEvent.ListHandler<VenueDetails> typeSortHandler = new ColumnSortEvent.ListHandler<VenueDetails>(
				provider.getList());
		typeSortHandler.setComparator(typeColumn,
				new Comparator<VenueDetails>() {
					public int compare(VenueDetails venue1, VenueDetails venue2) {
						if (venue1 == venue2) {
							return 0;
						}
						if (venue1 != null) {
							return (venue2 != null) ? venue1.getVenueType()
									.compareTo(venue2.getVenueType()) : 1;
						}
						return -1;
					}
				});
		table.addColumnSortHandler(typeSortHandler);
		table.getColumnSortList().push(typeColumn);

		// Add sorting for capacity column ListHandler<VenueDetails>
		ColumnSortEvent.ListHandler<VenueDetails> capacitySortHandler = new ColumnSortEvent.ListHandler<VenueDetails>(
				provider.getList());
		capacitySortHandler.setComparator(capacityColumn,
				new Comparator<VenueDetails>() {
					public int compare(VenueDetails venue1, VenueDetails venue2) {
						if (venue1 == venue2) {
							return 0;
						}
						if (venue1 != null) {
							return (venue2 != null) ? venue1.getVenueCapacity()
									.compareTo(venue2.getVenueCapacity()) : 1;
						}
						return -1;
					}
				});
		table.addColumnSortHandler(capacitySortHandler);
		table.getColumnSortList().push(capacityColumn);

		// Add sorting in name column
		ColumnSortEvent.ListHandler<VenueDetails> nameSortHandler = new ColumnSortEvent.ListHandler<VenueDetails>(
				provider.getList());
		nameSortHandler.setComparator(nameColumn,
				new Comparator<VenueDetails>() {
					public int compare(VenueDetails venue1, VenueDetails venue2) {
						if (venue1 == venue2) {
							return 0;
						}
						if (venue1 != null) {
							return (venue2 != null) ? venue1.getVenueName()
									.compareTo(venue2.getVenueName()) : 1;
						}
						return -1;
					}
				});
		table.addColumnSortHandler(nameSortHandler);
		table.getColumnSortList().push(nameColumn);
	}

	private void setUpCellTable() {
		// Create a CellTable
		venuesTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);

		// Display 100 rows in one page
		venuesTable.setPageSize(100);
		// venuesTable.setVisibleRange(0, 100);

		// Text column to show the name.
		nameColumn = new TextColumn<VenueDetails>() {
			@Override
			public String getValue(VenueDetails venue) {
				return venue.getVenueName();
			}
		};

		// Text column to show the address.
		addressColumn = new TextColumn<VenueDetails>() {
			@Override
			public String getValue(VenueDetails venue) {
				return venue.getVenueAdd1();
			}
		};

		// Text column to show the city.
		cityColumn = new TextColumn<VenueDetails>() {
			@Override
			public String getValue(VenueDetails venue) {
				return venue.getVenueCity();
			}
		};

		// Text column to show the postal code.
		postalCodeColumn = new TextColumn<VenueDetails>() {
			@Override
			public String getValue(VenueDetails venue) {
				return venue.getVenuePostal();
			}
		};

		// Text column to show the telephone number.
		telephoneColumn = new TextColumn<VenueDetails>() {
			@Override
			public String getValue(VenueDetails venue) {
				return venue.getVenuePhone();
			}
		};

		// Text column to show the establishment type.
		typeColumn = new TextColumn<VenueDetails>() {
			@Override
			public String getValue(VenueDetails venue) {
				return venue.getVenueType();
			}
		};

		// Text column to show the capacity.
		capacityColumn = new TextColumn<VenueDetails>() {
			@Override
			public String getValue(VenueDetails venue) {
				return venue.getVenueCapacity();
			}
		};

		// Button column to add venues to bookmarks.
		ButtonCell addBookmarkButton = new ButtonCell();
		Column<VenueDetails, String> addbookmarkColumn = new Column<VenueDetails, String>(
				addBookmarkButton) {
			@Override
			public String getValue(final VenueDetails venue) {
				return "+Bookmark";
			}
		};
		addbookmarkColumn
				.setFieldUpdater(new FieldUpdater<VenueDetails, String>() {
					@Override
					public void update(int index, VenueDetails venue,
							String value) {
						addBookmark(venue);
					}
				});

		// Button column to remove venues from bookmarks.
		ButtonCell removeBookmarkButton = new ButtonCell();
		Column<VenueDetails, String> removebookmarkColumn = new Column<VenueDetails, String>(
				removeBookmarkButton) {
			@Override
			public String getValue(final VenueDetails venue) {
				return "Remove";
			}
		};
		removebookmarkColumn
				.setFieldUpdater(new FieldUpdater<VenueDetails, String>() {
					@Override
					public void update(int index, VenueDetails venue,
							String value) {
						removeBookmark(venue);
					}
				});

		// Checkbox column to mark venues as selected in bookmarks.
		CheckboxCell visitedCheckbox = new CheckboxCell();
		Column<VenueDetails, Boolean> visitedColumn = new Column<VenueDetails, Boolean>(
				visitedCheckbox) {
			@Override
			public Boolean getValue(VenueDetails venue) {
				return venue.getVisited();
			}
		};
		visitedColumn
				.setFieldUpdater(new FieldUpdater<VenueDetails, Boolean>() {
					public void update(int index, VenueDetails venue,
							Boolean visited) {
						setVisited(venue);
					}
				});

		// Add Columns to venues CellTable
		venuesTable.addColumn(nameColumn, "Name");
		venuesTable.addColumn(addressColumn, "Address");
		venuesTable.addColumn(cityColumn, "City");
		venuesTable.addColumn(postalCodeColumn, "Postal Code");
		venuesTable.addColumn(telephoneColumn, "Telephone");
		venuesTable.addColumn(typeColumn, "Type");
		venuesTable.addColumn(capacityColumn, "Capacity");
		venuesTable.addColumn(addbookmarkColumn, "Bookmark");

		// Add Columns to bookmarks CellTable
		bookmarksTable.addColumn(nameColumn, "Name");
		bookmarksTable.addColumn(addressColumn, "Address");
		bookmarksTable.addColumn(cityColumn, "City");
		bookmarksTable.addColumn(postalCodeColumn, "Postal Code");
		bookmarksTable.addColumn(telephoneColumn, "Telephone");
		bookmarksTable.addColumn(typeColumn, "Type");
		bookmarksTable.addColumn(capacityColumn, "Capacity");
		bookmarksTable.addColumn(visitedColumn, "Visited");
		bookmarksTable.addColumn(removebookmarkColumn, "Bookmark");

		// Make the columns sortable
		nameColumn.setSortable(true);
		addressColumn.setSortable(true);
		cityColumn.setSortable(true);
		postalCodeColumn.setSortable(true);
		telephoneColumn.setSortable(true);
		typeColumn.setSortable(true);
		capacityColumn.setSortable(true);

		// Set up column sort handler
		// venuesTable.addColumnSortHandler(addressSortHandler);
		// venuesTable.getColumnSortList().push(addressColumn);

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

	public void loadVenues() {
		if (venueDetailsSvc == null) {
			venueDetailsSvc = GWT.create(VenueDetailsService.class);
		}

		AsyncCallback<List<VenueDetails>> callback = new AsyncCallback<List<VenueDetails>>() {

			public void onFailure(Throwable caught) {
				errorMsgLabel
						.setText("Error while fetching venues from server");
				errorMsgLabel.setVisible(true);
			}

			public void onSuccess(List<VenueDetails> result) {
				listOfVenues = new ArrayList<VenueDetails>(result);
				venuesProvider = new ListDataProvider<VenueDetails>(result);
				venuesProvider.addDataDisplay(venuesTable);
				venuesPager.setDisplay(venuesTable);
				addDropDownList();
				setUpColumnSort(venuesTable, venuesProvider);
			}

		};

		updatedVenueLabel.setText("Last update : "
				+ DateTimeFormat.getMediumDateTimeFormat().format(new Date()));
		// Make the call to the venue price service.
		venueDetailsSvc.getPrices(callback);
	}

	private void searchVenues(final String name, final String address,
			final String city, final String type) {
		ArrayList<VenueDetails> filteredVenueList = new ArrayList<VenueDetails>();
		if (name.isEmpty() && address.isEmpty() && city == "ALL"
				&& type == "ALL") {
			filteredVenueList = listOfVenues;
		} else {

			TreeSet<String> selectedCity = new TreeSet<String>();
			selectedCity.add(city);
			TreeSet<String> selectedType = new TreeSet<String>();
			selectedType.add(type);
			if (city.equals("ALL")) {
				if (type.equals("ALL"))
					filteredVenueList = filter(name, address, listOfCities,
							listOfTypes);
				else
					filteredVenueList = filter(name, address, listOfCities,
							selectedType);

			} else {
				if (type.equals("ALL"))
					filteredVenueList = filter(name, address, selectedCity,
							listOfTypes);
				else
					filteredVenueList = filter(name, address, selectedCity,
							selectedType);
			}
		}
		venuesProvider.getList().clear();
		venuesProvider.getList().addAll(filteredVenueList);
		venuesProvider.refresh();
		venuesPager.firstPage();
		updatedVenueLabel.setText("Last update : "
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

	// retrieves an ArrayList of Venue id's that have been bookmarked by the
	// current user,
	// and uses the list to display the corresponding Venues in the
	// bookmarksFlexTable using the displayBookmarks method
	private void loadBookmarks() {

		updatedBookmarksLabel.setText("Loading Bookmarks...");
		venueService.getVenues(new AsyncCallback<List<VenueDetails>>() {

			public void onFailure(Throwable error) {

				updatedBookmarksLabel
						.setText("failed to retrieve Bookmarked venues");
			}

			public void onSuccess(List<VenueDetails> bookmarkedVenues)

			{
				listOfBookmarks = new ArrayList<VenueDetails>(bookmarkedVenues);
				bookmarksProvider = new ListDataProvider<VenueDetails>(
						bookmarkedVenues);
				bookmarksProvider.addDataDisplay(bookmarksTable);
				bookmarksPager.setDisplay(bookmarksTable);
				setUpColumnSort(bookmarksTable, bookmarksProvider);
				if (!listOfBookmarks.isEmpty())
					plotBookmarks();
				if (bookmarkedVenues.isEmpty())
					updatedBookmarksLabel
							.setText("Oops. You dont have any venues in your bookmarks!!");
				else {
					updatedBookmarksLabel
							.setText("Bookmarked venues successfully loaded");
				}
			}

		});

	}

	// adds the Venue with symbol (venueName+venueAdd1) to the current user's
	// bookmarks list
	private void addBookmark(final VenueDetails venue) {
		ArrayList<String> symbols = new ArrayList<String>();

		for (VenueDetails bookmark : listOfBookmarks) {
			String symbol = bookmark.getSymbol();
			symbols.add(symbol);
		}
		if (symbols.contains(venue.getSymbol()))
			updatedVenueLabel.setText("You already have '"
					+ venue.getVenueName() + "' in your Bookmarks.");
		else {
			venueService.addVenue(venue, new AsyncCallback<Void>() {

				public void onFailure(Throwable error) {

					updatedVenueLabel.setText("Failed to add '"
							+ venue.getVenueName() + "' to Bookmarks.");
				}

				public void onSuccess(Void ignore)

				{
					listOfBookmarks.add(venue);
					Collections.sort(listOfBookmarks);
					bookmarksProvider.getList().clear();
					bookmarksProvider.getList().addAll(listOfBookmarks);
					bookmarksProvider.refresh();
					plotBookmarks();
					updatedVenueLabel.setText("Successfully added '"
							+ venue.getVenueName() + "' to Bookmarks.");
				}

			});
		}
	}

	// removes the Venue with symbol (venueName+venueAdd1) from the current
	// user's bookmarks list
	private void removeBookmark(final VenueDetails venue) {
		venueService.removeVenue(venue, new AsyncCallback<Void>() {
			public void onFailure(Throwable error) {

				updatedBookmarksLabel.setText("Failed to remove '"
						+ venue.getVenueName() + ("'"));

			}

			public void onSuccess(Void ignore) {
				listOfBookmarks.remove(venue);
				bookmarksProvider.getList().clear();
				bookmarksProvider.getList().addAll(listOfBookmarks);
				bookmarksProvider.refresh();
				if (listOfBookmarks.isEmpty())
					updatedBookmarksLabel
							.setText("Your Bookmarks is now empty. Removed '"
									+ venue.getVenueName() + "'.");
				else {
					plotBookmarks();
					updatedBookmarksLabel.setText("Successfully removed '"
							+ venue.getVenueName() + ("'"));
				}

			}
		});

	}

	// removes all venues from the current user's bookmarks list
	private void removeAllBookmarks() {
		if (listOfBookmarks.isEmpty())
			updatedBookmarksLabel
					.setText("Oops. Your Bookmarks is already empty!");
		else {
			venueService.removeAllVenues(new AsyncCallback<Void>() {
				public void onFailure(Throwable error) {
					updatedBookmarksLabel
							.setText("Failed to remove all Bookmarks");
				}

				public void onSuccess(Void ignore) {
					int bookmarkSize = listOfBookmarks.size();
					listOfBookmarks.clear();
					bookmarksProvider.getList().clear();
					bookmarksProvider.refresh();
					map.clearOverlays();
					if (bookmarkSize == 1)
						updatedBookmarksLabel.setText("Successfully removed "
								+ bookmarkSize + (" venue."));
					else
						updatedBookmarksLabel.setText("Successfully removed "
								+ bookmarkSize + (" venues."));

				}
			});
		}
	}

	// sets venues as visited and not visited in bookmarks
	private void setVisited(final VenueDetails venue) {
		venueService.setVisited(venue, new AsyncCallback<Void>() {
			public void onFailure(Throwable error) {
				if (venue.getVisited() == true)
					updatedBookmarksLabel.setText("Failed to mark '"
							+ venue.getVenueName() + "' as not visited.");
				else
					updatedBookmarksLabel.setText("Failed to mark '"
							+ venue.getVenueName() + "' as visited.");
			}

			public void onSuccess(Void ignore) {
				for (VenueDetails bookmark : listOfBookmarks) {
					if (venue.getSymbol().equals(bookmark.getSymbol()))
						if (venue.getVisited() == true) {
							bookmark.setVisited(false);
							updatedBookmarksLabel
									.setText("Succesfully marked '"
											+ venue.getVenueName()
											+ "' as not visited.");
						} else {
							bookmark.setVisited(true);
							updatedBookmarksLabel
									.setText("Succesfully marked '"
											+ venue.getVenueName()
											+ "' as visited.");
						}
				}
				bookmarksProvider.getList().clear();
				bookmarksProvider.getList().addAll(listOfBookmarks);
				bookmarksProvider.refresh();
			}
		});
	}

	private void plotBookmarks() {
		// Callback Method post obtaining LatLng
		LatLngCallback callback = new LatLngCallback() {

			public void onFailure() {
				statusLabel.setText("Address was Not Found");
			}

			public void onSuccess(LatLng point) {
				statusLabel.setText("Address was Found");
				Marker marker = new Marker(point);
				map.addOverlay(marker);
				points.add(point);
			}
		};
		map.clearOverlays();
		for (VenueDetails venue : listOfBookmarks) {
			Geocoder geocoder = new Geocoder();
			geocoder.getLatLng(venue.getMapAddress(), callback);
		}
		// map.checkResizeAndCenter();
	}

	private void setZoomingBound() {
		TreeSet<Double> lats = new TreeSet<Double>();
		TreeSet<Double> lons = new TreeSet<Double>();
		for (LatLng point : points) {
			double pointLat = point.getLatitude();
			double pointLon = point.getLongitude();
			lats.add(pointLat);
			lons.add(pointLon);
		}
		minLat = lats.first();
		minLon = lons.first();
		maxLat = lats.last();
		maxLon = lons.last();
		points.clear();
	}

}
