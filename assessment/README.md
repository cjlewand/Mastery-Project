## Don't Wreck My House Project Plan

### Requirements
####View Reservations for Host
* Display all reservations for a host.
  
      * The user may enter a value that uniquely identifies a host or they can search for a host and pick one out of a list.
      * If the host is not found, display a message.
      * If the host has no reservations, display a message.
      * Show all reservations for that host.
      * Show useful information for each reservation: the guest, dates, totals, etc.
      * Sort reservations in a meaningful way.

####Make a Reservation
* Books accommodations for a guest at a host

        * The user may enter a value that uniquely identifies a guest or they can search for a guest and pick one out of a list.
        * The user may enter a value that uniquely identifies a host or they can search for a host and pick one out of a list.
        * Show all future reservations for that host so the administrator can choose available dates.
        * Enter a start and end date for the reservation.
        * Calculate the total, display a summary, and ask the user to confirm. The reservation total is based on the host's standard rate and weekend rate. For each day in the reservation, determine if it is a weekday or a weekend. If it's a weekday, the standard rate applies. If it's a weekend, the weekend rate applies.
        * On confirmation, save the reservation.

* Validation

        * Guest, host, and start and end dates are required.
        * The guest and host must already exist in the "database". Guests and hosts cannot be created.
        * The start date must come before the end date.
        * The reservation may never overlap existing reservation dates.
        * The start date must be in the future.

####Edit a Reservation
* Edits an existing reservation.

        * Find a reservation.
        * Start and end date can be edited. No other data can be edited.
        * Recalculate the total, display a summary, and ask the user to confirm.

* Validation

        * Guest, host, and start and end dates are required.
        * The guest and host must already exist in the "database". Guests and hosts cannot be created.
        * The start date must come before the end date.
        * The reservation may never overlap existing reservation dates.

####Cancel a Reservation
* Cancel a future reservation
  
        * Find a reservation.
        * Only future reservations are shown.
        * On success, display a message.
  
* Validation

        * You cannot cancel a reservation that's in the past.

###Technical Requirements

* [ ] Must be a Maven project.
* [ ] Spring dependency injection configured with either XML or annotations.
* [ ] All financial math must use BigDecimal.
* [ ] Dates must be LocalDate, never strings.
* [ ] All file data must be represented in models in the application.
* [ ] Reservation identifiers are unique per host, not unique across the entire application. Effectively, the combination of a reservation identifier and a host identifier is required to uniquely identify a reservation.

###File Format
* You may not change the file formats or the file delimiters. You must use the files provided.
    * Guests are stored in their own comma-delimited file, guests.csv, with a header row.
    * Hosts are stored in their own comma-delimited file, hosts.csv, with a header row.
    * Reservations are stored across many files, one for each host. A host reservation file name has the format: {host-identifier}.csv.
      
            Reservations for host c6567347-6c57-4658-a2c7-50040eeeb80f are stored in c6567347-6c57-4658-a2c7-50040eeeb80f.csv
            
            Reservations for host 54508cfa-4f67-4de8-9437-6f27d65b0af0 are stored in 54508cfa-4f67-4de8-9437-6f27d65b0af0.csv
            
            Reservations for host 6a3ef437-289c-40a9-b88a-dd70fad3fdbc are stored in 6a3ef437-289c-40a9-b88a-dd70fad3fdbc.csv
    
###Testing
* All data components must be thoroughly tested.
* Tests must always run successfully regardless of the data starting point, so it's important to establish known good state.
* Never test with "production" data. 
* All domain components must be thoroughly tested using test doubles.
* Do not use file repositories for domain testing.
* User interface testing is not required.

###Deliverables
* [ ] A schedule of concrete tasks. Tasks should contain time estimates and should never be more than 4 hours.
* [ ] Class diagram (informal is okay)
* [ ] Sequence diagrams or flow charts (optional, but encouraged)
* [ ] A Java Maven project that contains everything needed to run without error
* [ ] Test suite

###Stretch Goals
* [ ] Make reservation identifiers unique across the entire application.
* [ ] Allow the user to create, edit, and delete guests.
* [ ] Allow the user to create, edit, and delete hosts.
* [ ] Display reservations for a guest.
* [ ] Display all reservations for a state, city, or zip code.
* [ ] Implement another set of repositories that store data in JSON format. In this approach, you don't want multiple files for reservations. Consider how you might migrate existing data to the JSON format safely.

### High Level Requirements
* The application user is an accommodation administrator. They pair guests to hosts to make reservations.

        * The administrator may view existing reservations for a host.
        * The administrator may create a reservation for a guest with a host.
        * The administrator may edit existing reservations.
        * The administrator may cancel a future reservation.

###Plan
* [x] PLan out structure with layered application and XML injection
  * Models: 
    * Host
    * Guest
    * Reservation
    * is an enum needed?
      * Does not seem like an enum is needed for any fields in Host, Guest, or Reservation
  * Data: 
    * DataException
    * HostFileRepository (Implements interface) HostRepository(Interface)
    * GuestFileRepository (Implements interface) GuestRepository(Interface)
    * ReservationFileRepository (Implements Interface) ReservationRepository(Interface)
  
          Review Sustainable foraging project to see how multiple repositories are utilized and how they interact with each other for Spring injection purposes.
  
    * Each repository needs a Test class and a RepositoryDouble for test data.
  * Domain:
    * Response class
      * This was used in Foraging Assessment and allows multiple types of results to be handled using one class.
      * This will allow us to handle Result<Host>, Result<Guest>, Result<Reservation>
    * Result (extends response)
    * GuestService
    * HostService
    * ReservationService
      * Guest/Host service may be relatively empty as we are not required to offer create/edit/delete guest options (Stretch goals)
 * UI:
  * ConsoleIO
    * Will contain all of the helper methods for reading Strings etc.
  * View
  * Controller
  * MenuOption (enum)
  * App class:
    * Gather Spring XML injections
    * Call controller to run application
  * XML Dependency Injection will occur at each step when a constructor is created, dependency will be added to dependency-configuration.xml file. 
  
* [x] Build Out Models Layer
  * Estimated 1 hour 30 mins
  * Actual: 30 mins
    * Was easier to implement than anticpated with core structure and planning already completed. 
  * Host
    * Fields
      * id (String/GUID), lastName (String), email (String), phoneNumber (String), address (String), city (String), state (String) postalCode (String), standardRate (BigDecimal), weekendRate (BigDecimal)
    * Constructor
    * getters/setters
    * Override equals method
  * Guest
    * Fields
      * guestId (int), firstName (String), lastName (String), email (String), phoneNumber (String), State (String)
    * Constructor
    * getters/setters
    * Override equals method
  * Reservation
    * Fields
      * id (int), guest (Guest), host (Host), startDate (LocalDate), endDate (LocalDate), totalCost (BigDecimal)
    * Constructor
    * getters/setters
    * Override equals method
    * Method Name: makeTotalCost(),  Inputs: None,  Outputs: BigDecimal
  
* [x] Build Data Layer
  * Estimated 3 hours
  * Actual: 2.5 hours
  * Guest / Host were slightly more simple than originally assumed.
  * DataException (extends Exception)
    * Constructor(String message, Throwable rootCause)
      * super(message, rootCause)
      * verify with other examples on past projects
      * overload constructor with message and with rootcause by themselves
  * HostRepository (interface)
    * list methods needed (All throw DataException)
      * Method name: findByEmail(), Inputs: String email,  Outputs: Host
      * Method name: findAll(), Inputs: none,  Outputs: List<Host>
      * This might be all?
        * do not currently need add/update/delete methods (These are stretch goals)
  * HostFileRepository (implements HostRepository)
    * Needs both find methods from HostRepository with logic for handling
      * Refer to past projects for assistance
    * Fields
      * private static final String HEADER = "id,last_name,email,phone,address,city,state,postal_code,standard_rate,weekend_rate";
      * private final String filePath;
      * private static final String DELIMITER = ",";
    * Method Name: deserialize(), Inputs: String[] fields,  Outputs: Host
    * Method Name: preserveString(), Inputs: String string, Outputs String
  * GuestRepository (interface)
    * list methods needed (All throw DataException)
      * Method name: findByEmail(), Inputs: String email,  Outputs: Guest
      * Method name: findAll(), Inputs: none,  Outputs: List<Guest>
      * This might be all?
        * do not currently need add/update/delete methods (These are stretch goals)
  * GuestFileRepository (implements GuestRepository)
    * Needs both find methods from HostRepository with logic for handling
    * Refer to past projects for assistance
    * Fields
      * private static final String HEADER = "guest_id,first_name,last_name,email,phone,state";
      * private final String filePath;
      * private static final String DELIMITER = ",";
    * Method Name: deserialize(), Inputs: String[] fields,  Outputs: Host
    * Method Name: preserveString(), Inputs: String string, Outputs String
  * ReservationRepository (interface)
    * list methods needed (All throw DataException)
      * Method Name: findByHost(),  Inputs: Host host,  Outputs: List<Reservation>
      * Method Name: add() Inputs: Reservation reservation, Outputs: Reservation
      * Method Name: update(), Inputs: Reservation reservation, Outputs: boolean
      * Method Name: deleteById(), Inputs: int id, Host host  Outputs: boolean
  * ReservationFileRepository (implements ReservationRepository)
    * Override methods listed in ReservationRepository with logic for handling.
    * Fields
      * private static final String HEADER = "id,start_date,end_date,guest_id,total";
      * private final String directory;
    * Constructor(String directory)
    * Method Name: getNextId(), Inputs: List<Reservations> reservation, Outputs: int
    * Method Name: getFilePath(), Inputs: String hostId,  Outputs: String
    * Method Name: serialize(), Inputs: Host host,  Outputs: String
    * Method Name: deserialize(), Inputs: String[] fields,  Outputs: Host
    * Method Name: cleanString(), Inputs: String string, Outputs: String
    * Method Name: writeAll(), Inputs: List<Reservation> reservations, String id,  Outputs: VOID
  
* [x] Test Data Layer
  * Estimated: 3 hours ( 1 hour each repository)
  * Actual: 1.5 hours
    * Guest / Host fileRepository were much simpler than anticipated.  There were onyl a few tests for each of them since there are not currently creat/update/delete methods
  * Need SEED_PATH and TEST_PATH
  * Need Instance of each FileRepository with TEST_PATH
  * Need BeforeEach method to set up TEST_PATH properly for known good state.
  * Positive and Negative Tests.
  * Use "run with coverage" to ensure methods and as much code as possible is tested.
  
* [x] Build Out Domain Layer
  * Estimated: 3.5 hours (1 hour ReservationService, 1.5 hour Host/Guest Service, .5 hour result, .5 hour built in bug fixing)
  * Actual: 3.5 hours
  * Result<T> extends Response      (T is an object)
    * Fields
      * private T payload;
    * Method Name: getPayLoad(), Inputs: none,  Outputs: T
    * Method Name: setPayLoad(), Inputs: T payload,  Outputs: VOID
      * These are getters/setters
      * Syntax is a little weird because of 'T'.  Have to make sure this works properly
        * Method Name: isSuccess(), Inputs: none, Outputs: boolean
    * Method Name: getErrorMessages(), Inputs: none, Outputs: List<String>
    * Method Name: addErrorMessages(), Inputs: String message, Outputs: VOID
  * GuestService
    * Fields
      * private final GuestRepository repository
    * Constructor(GuestRepository repository)
    * Method Name: findByEmail(),  Inputs: String email,  Outputs: Guest
    * Method Name: findAll(), Inputs: none,  Outputs: List<Guests>
      * CRUD operations go here. Only needed for stretch goals
      * If stretch goals are added, validation methods wil be needed
  * HostService
    * Fields
      * private final HostRepository repository
    * Method Name: findByEmail(),  Inputs: String email,  Outputs: Host
    * Method Name: findAll(), Inputs: none,  Outputs: List<Host>
  * ReservationService 
    * Fields
      * private final GuestRepository repository
      * private final HostRepository repository
      * private final ReservationRepository repository
    * Method Name: findByHost(),  Inputs: Host host,  Outputs: List<Reservation>
    * Method Name: add(),  Inputs: Reservation reservation,  Outputs: Result<Reservation>
    * Method Name: update(), Inputs: Reservation reservation, Outputs: boolean
    * Method Name: deleteById(), Inputs: int reservationId,  Output: boolean
    * Method Name: findReservationById(), Inputs: Host host, int id,   Outputs: Reservation
    * Method Name: narrowByGuest(),  Inputs: List<Reservation> reservations, Guest guest,  Outputs: List<Reservation>
    * Method Name: makeTotalReservationCost(), Inputs: Reservation reservation  Outputs: BigDecimal
    * Method Name: validate(), Inputs: Reservation reservation, Output: Result<Reservation>
    * Method Name: validateNulls(), Inputs: Reservation reservation, Output: Result<Reservation>
    * Method Name: validateFields(), Inputs: Reservation reservation, Result<Reservation> result  Output: Result<Reservation>
    * Method Name: validateChildrenExist(), Inputs: Reservation reservation, Result<Reservation> result  Output: Result<Reservation>
    * Method Name: validateOverlap(), Inputs: Reservation reservation, Result<Reservation> result  Output: Result<Reservation>
  
* [x] Test Domain Layer
  * Estimated: 3 hours
  * Actual: 2 hours
    * Testing while writing methods makes it a lot  easier to test each case as you go and make sure you are catching each scenario
  * Instantiate a service instance and pass in a RepositoryDouble (implements Repository)
    * in RepositoryDoubles create known good state
      * Override CRUD methods in Double to return testable data.
  * Test each CRUD method from service in positive nad negative cases.
  
* [x] Build Out UI Layer
  * Estimated: each section will have its own estimation
  * Actual: each section will have its own actual
  * Testing the UI happens during coding and development.  This time is built into the estimated time.
  * Controller
    * Estimated: 2 hour
    * Actual: 2 hours
    * Fields
      * private final HostService hostService;
      * private final GuestService guestService;
      * private final ReservationService reservationService;
      * private final View view;
    * Constructor(All Fields)
    * Method Name: run(), Inputs: None,  Outputs: VOID
      * Catches data Exception
    * Method Name: runAppLoop(), Inputs: None,  Outputs: VOID
      * runs menu loop
    * Method Name: viewReservations(), Inputs: None,  Outputs: VOID
    * Method Name: makeReservation(), Inputs: None,  Outputs: VOID
    * Method Name: updateReservation(), Inputs: None,  Outputs: VOID
    * Method Name: deleteReservation(), Inputs: None,  Outputs: VOID
  * View
    * Estimated: 2 hours
    * Actual: 2 hours
    * This will most likely expand beyond initial planning as more methods are needed or methods are split
    * Fields
      * private final ConsoleIO io;
    * Constructor(ConsoleIO io)
    * Method Name: selectMainMenuOption(), Inputs: none,  Outputs: MainMenuOption
    * Method Name: getDate(),  Inputs: None, Outputs: LocalDate
    * Method Name: getHostEmail(), Inputs: None, Outputs: String
    * Method Name: getGuestEmail(), Inputs: None, Outputs: String
    * Method Name: makeReservation(), Inputs: Host host, Guest guest, Outputs: Reservation
    * Method Name: updateReservation(), Inputs: Reservation reservation, Outputs: Reservation
    * Method Name: selectReservation(), Inputs: List<Reservations> reservations, Host host Outputs: int
    * Method Name: displaySummary(), Inputs: Reservation reservation, Outputs: boolean
    * Method Name: displayHeader(), Inputs: String message, Output: VOID
    * Method Name: displayException(), Inputs: Exception ex, Outputs: VOID
    * Method Name: displayStatus(), Inputs: boolean success, String message, Outputs: VOID
      * Overload with List<Strings>
    * Method Name: displayReservations(), Inputs: List<Reservation> reservations, Host host, Outputs: VOID
    * Method Name: displayFutureReservations(), Inputs: List<Reservation> reservations, Host host, Outputs: VOID
    * Method Name: displayReservationHostHeader(), Inputs: Host host,  Outputs: VOID
    * Method Name: displayAllReservations(), List<Reservation> reservations, Host host, Outputs: VOID
      * helper method for printing format for displays
  * ConsoleIO
    * Estimate: 1 hour
    * Actual: .5 hour
      * Was able to use foraging as a quick resource.  Added all methods used in foraging as it seems there may be a use for each of them (maybe not double). Will delete unused methods after UI layer is built.
    * Helper Methods for VIEW
    * Fields
      * private final Scanner scanner
      * private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
      * can hold Strings for easier referencing
      * Examples form foraging
        
            private static final String INVALID_NUMBER = "[INVALID] Enter a valid number.";
            private static final String NUMBER_OUT_OF_RANGE = "[INVALID] Enter a number between %s and %s.";
            private static final String REQUIRED = "[INVALID] Value is required.";
            private static final String INVALID_DATE = "[INVALID] Enter a date in MM/dd/yyyy format.";
    
    * Methods will be referenced here.  Use foraging as a guide for filling out all helper methods
      * print
      * println
      * printF
      * readString
      * readRequiredString
      * readDouble (overload for min/max)
      * readInt (overload for min/max)
      * readBoolean
      * readLocalDate
      * readBigDecimal (overload for min/max)
    * Not all of these will be needed.  Implement as used.
  * MainMenuOption (enum)
    * Estimate: .5 hours
    * Actual: .25 hours
    * Add each MainMenuOption with a value and message
    * Fields
      * private int value;
      * private String message;
    * Constructor(int value, String message)
    * foraging uses a fromValue() method.  Look into if this will be needed with this setup
    * getters for value and message
  
* [x] Build out App class
  * Estimate: 15 mins
  * Actual: 5 mins
  * Ensure XML dependency injection is set up correctly
  * controller.run() to run application
  
* [ ] Final Cleanup - reformatting
  * Estimated: 2 hours
  * Actual:
  * Final walkthrough, reformatting, assess stretch goals and determine what can be implemented
  
* [x] Stretch Goals
  * Will assess as project finishes and will be added based on available time
  * CRUD operations for Host / Guest seem to be first on the list to 'complete' the application.
    * Stretch goals for different view options were added. 
