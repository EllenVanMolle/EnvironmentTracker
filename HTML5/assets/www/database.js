/*
 * Database
 */

/*
 * Eventlistener die op het ogenblik dat het aparaat klaar is voor gebruik de 
 * de onDeviceReady functie uitvoert.
 */
document.addEventListener("deviceready", onDeviceReady, false);

/*
 * Functie die de database initialiseerd (indien deze nog niet bestaat) en de nodige
 * tabellen creëert indien deze nog niet bestaan.
 */
function onDeviceReady() {
	database= new Database();
	database.initDatabase();
	database.createTables();
	database.createSetTable();
}

/*
 * variabelen
 */	
var database;

var pixData = [0, 0, 0];

var Database = function() {
	var myDb = false;
}

/* 
 * Functie die de database TrackerDb creëert. 
 */
Database.prototype.initDatabase = function() {
	try {
	    if (!window.openDatabase) {	
	    	// Databases worden niet ondersteund. Geef een alert aan de gebruiker.
	        alert('Databases are not supported by your browser. Please use a Webkit browser.');
	    } 
	    else {
	    	// Creëer de database TrackerDB
	    	var shortName = 'TrackerDb';
	        var version = '1.0';
	        var displayName = 'Database of the Environment Tracker app';
	        var maxSize = (2*1024*1024); // de grote van de database TrackerDb in bytes
	        this.myDb = openDatabase(shortName, version, displayName, maxSize);
	    }
	} catch(e) {
	    if (e == 2) {
	        // Foute database versie. Geef dit in de log weer.
	        console.log("Invalid database version.");
	    } 
	    else {
	    	// Geef error weer in de log.
	        console.log("Unknown error "+ e +".");
	    }
	    return;
	} 
}



/********************************* TABELLEN ***********************************************/



/* 
 * Functie die de environmentData tabel in de database creëert door sqlstatements uit te voeren.
 */
Database.prototype.createTables = function(){
	try {// SQL statements voor de creatie van de tabellen
		
		var sqlTabel1 ='CREATE TABLE IF NOT EXISTS environmentData ('
        	+ 'ID INTEGER PRIMARY KEY ASC,'
        	+ 'added_on DATETIME,'
        	+ 'moodrate INTEGER,'
        	+ 'posLatitude DOUBLE,'
        	+ 'posLongitude DOUBLE,'
        	+ 'hue_cat INTEGER,'
        	+ 'per_saturation REAL,' 
        	+ 'per_brightness REAL)';
        
		this.myDb.transaction(
			function (transaction) {
			
			//uitvoeren van sqlTabel1 statement om tabel te creëeren: environmentData
        	transaction.executeSql(sqlTabel1, [], this.nullDataHandler, this.errorHandler);
    		
			}
		);
		
	} catch (e) { 
		console.log("Error processing SQL: "+ e.message +".");
		}
}

/*
 * Functie die nog een vierde tabelsettingsData creëert en igv
 * succes het aantal rijen in de tabel telt.
 */
Database.prototype.createSetTable = function(){
	try {// SQL statements voor de creatie van de tabellen
 		var sqlTabel2 = 'CREATE TABLE IF NOT EXISTS settingsData ('
        	+ 'ID INTEGER PRIMARY KEY,'
        	+ 'notification BOOLEAN,'
        	+ 'monday BOOLEAN,'
        	+ 'tuesday BOOLEAN,'
        	+ 'wednesday BOOLEAN,'
        	+ 'thursday BOOLEAN,'
        	+ 'friday BOOLEAN,'
        	+ 'saturday BOOLEAN,'
        	+ 'sunday BOOLEAN,'
        	+ 'startHour INTEGER,'
        	+ 'startMinute INTEGER,'
        	+ 'stopHour INTEGER,'
        	+ 'stopMinute INTEGER,'
        	+ 'interval INTEGER,'
        	+ 'beep	BOOLEAN,'
        	+ 'nr_beep INTEGER,'
        	+ 'vibrate BOOLEAN,'
        	+ 'min_vibrate INTEGER,'
        	+ 'GPS BOOLEAN)';

	this.myDb.transaction(
			function (transaction) {
			//transaction.executeSql('DROP TABLE settingsData', [], this.nullDataHandler, this.errorHandler);
			//uitvoeren van sqlTabel4 statement om tabel4 te creëeren: settingsData
        	transaction.executeSql(sqlTabel2, [], 
        		function (transaction, resultSet){
        			database.getRowsSetData(); // Count the number of rows in the settingsData tabel 
        	}, this.errorHandler);
        	}
		);
		
	} catch (e) { 
		console.log("Error processing SQL: "+ e.message +".");
	}
}

/****************************** OPSLAG DataCollectie ******************************************/



/*
 * Functie die aangeroepen wordt door geolocation om de locatie, moodrate, foto gegevens en audiogegevens op te slaan.
 */
function saveLocation (latitude, longitude){
	var moodrate = $('#moodSlider').val();
	var on_date = new Date();
	database.saveEnvData(moodrate, on_date, latitude, longitude);
}


/* 
 * Functie die de moodrate, locatie (latitude, longitude), foto gegevens opslaat in de environmentData tabel.
 */
Database.prototype.saveEnvData = function(moodrate, on_date, latitude, longitude){
	try{ // SQL statement voor het opslaan van EnvData
		var sqlInsertEnvData = 'INSERT INTO environmentData (moodrate, added_on, posLatitude, '
			+ 'posLongitude, hue_cat, per_saturation, per_brightness) VALUES (?, ?, ?, ?, ?, ?, ?)';
	
		this.myDb.transaction(
			function(transaction){
			//uitvoeren van sqlInsertEnvData om de moodrate, breedteligging en lengteligging op te slaan in de environmentData tabel.	
			transaction.executeSql(sqlInsertEnvData, [moodrate, on_date, latitude, longitude, pixData[0], pixData[1], pixData[2]], 
				function (transaction, resultSet){
					goHome(); 
				}, this.errorHandler);
			}
		);
	} catch(e) {
		alert("Error Processing SQL: "+ e.message +".");
		return;
	};
}

/*
 * Functie die wordt aangeroepen als de gebruiker op de home of save button klikt om de 
 * moodrate en de layout van de save button en stepthree navigatie terug naar hun oorspronkelijke toestand brengt.
 */
function goHome(){
	$("#moodSlider").val(5).slider("refresh"); // moodSlider waarde wordt op 5 gezet
	
	var saveB1 = document.getElementById("saveButton1");
	var saveB2 = document.getElementById("saveButton2");
	var saveB3 = document.getElementById("saveButton3");
	var tab1 = document.getElementById("tab1");
	var tab2 = document.getElementById("tab2");
	var text = document.getElementById("homepageText");
	
	// verberg de save buttons
	saveB1.className = saveB1.className.replace("", "hidden");
	saveB2.className = saveB2.className.replace("", "hidden");
	saveB3.className = saveB3.className.replace("", "hidden");
	
	// disable step three in de navigatie van stepone en steptwo
	tab1.className = tab1.className.replace("", "ui-disabled");
	tab2.className = tab2.className.replace("", "ui-disabled");
}




/****************************** OPSLAG settings *****************************************************/




/* 
 * Functie die telt hoeveel rijen er in de settingsData tabel zijn. Op die manier kunnen we bepalen of er reeds
 * een set preferenties door de gebruiker werden vastgelegd. Als dit niet het geval is, 
 * voegen we een rij toe met de defaultwaardes.
 */
Database.prototype.getRowsSetData = function(){
	try{// SQL statement die het aantal record telt in de settingsData tabel.
		var sqlCount = 'SELECT Count(*) FROM settingsData';
		this.myDb.transaction(
			function(transaction){
		 	// Het uitvoeren van sqlCount om te weten of reeds set preferenties door de gebruiker werden opgeslagen. 
			transaction.executeSql(sqlCount, [], 
				function (transaction, resultSet){
					var nrRows = resultSet.rows.item(0)["Count(*)"];
					if (nrRows == 0){ // tabel is leeg.
						var settings = [1, false, true, true, true, true, true, true, true, 8, 0, 20, 0, 120, false, 0, false, 0, true];
						database.saveDefaultSettings(settings);
					}
				}, this.errorHandler);
			}
		)
	} catch(e) {
		alert("Error Processing SQL: "+ e.message +".");
	}
}

/* 
 * Functie die de default settings opslaat in de settingsData tabel.
 */
Database.prototype.saveDefaultSettings = function(setData){
	try{// SQL statement die de default settings opslaat in de settingsData tabel
		var sqlInsertDefaultSet = 'INSERT INTO settingsData (ID, notification, monday, tuesday, wednesday, thursday, '
        	+ 'friday, saturday, sunday, startHour, startMinute, stopHour, stopMinute, interval, beep, nr_beep, vibrate, min_vibrate, '
        	+ 'GPS) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)';
     
		this.myDb.transaction(
		function(transaction){
			// Het uitvoeren van sqlInsertDefaultSetData om de default settings in de settingsData tabel op te slaan.
			transaction.executeSql(sqlInsertDefaultSet,
        	[setData[0], setData[1], setData[2], setData[3],setData[4], setData[5], setData[6], setData[7], setData[8],setData[9], setData[10], setData[11], setData[12], setData[13], setData[14], setData[15], setData[16], setData[17], setData[18]],
        	 this.nullDataHandler, this.errorHandler);
			}
		)
	} catch(e) {
		alert("Error Processing SQL: "+ e.message +".");
	}
}

function getSet(){
	database.getSettings();
}

/* 
 * Functie die de default settings opslaat in de settingsData tabel.
 */
Database.prototype.getSettings = function(){
	try{// SQL statement die de default settings opslaat in de settingsData tabel
		var sqlgetSet = 'SELECT * FROM settingsData WHERE ID = ?' 
     
		this.myDb.transaction(
		function(transaction){
			// Het uitvoeren van sqlInsertDefaultSetData om de default settings in de settingsData tabel op te slaan.
			transaction.executeSql(sqlgetSet, [1], function (transaction, resultSet){
				var row = resultSet.rows.item(0);
       			openSet([row.notification, row.monday, row.tuesday, row.wednesday, row.thursday, row.friday, row.saturday, row.sunday, row.startHour, row.startMinute, row.stopHour, row.stopMinute, row.interval, row.beep, row.nr_beep, row.vibrate, row.min_vibrate, row.GPS]);
			}, this.errorHandler);
			}
		)
	} catch(e) {
		alert("Error Processing SQL: "+ e.message +".");
	}
}

/*
 * Functie die de default settings opvraagt.
 
function getSettings() {
	var not = false;
	var beep = false;
	var vibr = false;
	var GPS = false;
	
	if ($('#Notifications').val() == "true"){
		not = true
	} else {};
	if ($('#Beep').val() == "true"){
		beep = true
	} else {};
	if ($('#Vibrate').val() == "true"){
		vibr = true
	} else {};
	if ($('#GPS').val() == "true"){
		GPS = true
	} else {};
	
	return [not, document.getElementById("Monday").checked, document.getElementById("Tuesday").checked, document.getElementById("Wednesday").checked, document.getElementById("Thursday").checked, document.getElementById("Friday").checked, document.getElementById("Saturday").checked, document.getElementById("Sunday").checked, beep, vibr, GPS];
}






/*function loadSetData() {
	database.getSetData();
}

function updateSettings(){
	var setData = getSettings();
	database.updateSetData(setData);
}
*/	

/*
Database.prototype.updateSetData = function(setData){
	try{
		sqlUpdatSetData = 'UPDATE settingsData SET notification = ?, monday = ?, tuesday =?, wednesday = ?, thursday = ?, '
        	+ 'friday = ?, saturday = ?, sunday = ?, startTime = ?, stopTime = ?, interval = ?, beep = ?, nr_beep = ?, vibrate = ?, '
        	+ 'min_vibrate = ?, GPS = ? WHERE ID=1)';
		this.myDb.transaction(
			function(transaction){
				
			transaction.executeSql(sqlUpdateSetData,
        	[setData[0], setData[1], setData[2],setData[3], setData[4], setData[5],setData[7], setData[8],setData[9], setData[10], setData[11], setData[12], setData[13], setData[14], setData[15]],
        	 this.nullDataHandler, this.errorHandler);
			}
		)
		
	}catch (e) {
		alert("Error Processing SQL: "+ e.message +".");
	};
}


/****************************** RESULTS ******************************************/

function getData(){
	database.getResults();
	
  var currentPosition = 0;
  var width = window.innerWidth
  var slideWidth = (width - 20);
  var slides = $('.slide');
  var numberOfSlides = slides.length;

  // Remove scrollbar in JS
  $('#slidesContainer').css('overflow', 'hidden');

  // Wrap all .slides with #slideInner div
  slides
  .wrapAll('<div id="slideInner"></div>')
  // Float left to display horizontally, readjust .slides width
  .css({
    'float' : 'left',
    'width' : slideWidth
  });

  // Set #slideInner width equal to total width of all slides
  $('#slideInner').css('width', slideWidth * numberOfSlides);

  // Insert left and right arrow controls in the DOM
  $('#slideshow')
    .prepend('<span class="control" id="leftControl">Move left</span>')
    .append('<span class="control" id="rightControl">Move right</span>');

  // Hide left arrow control on first load
  manageControls(currentPosition);

  // Create event listeners for .controls clicks
  $('.control')
    .bind('click', function(){
    // Determine new position
      currentPosition = ($(this).attr('id')=='rightControl')
    ? currentPosition+1 : currentPosition-1;

      // Hide / show controls
      manageControls(currentPosition);
      // Move slideInner using margin-left
      $('#slideInner').animate({
        'marginLeft' : slideWidth*(-currentPosition)
      });
    });

  // manageControls: Hides and shows controls depending on currentPosition
  function manageControls(position){
    // Hide left arrow if position is first slide
    if(position==0){ $('#leftControl').hide() }
    else{ $('#leftControl').show() }
    // Hide right arrow if position is last slide
    if(position==numberOfSlides-1){ $('#rightControl').hide() }
    else{ $('#rightControl').show() }
    }
 
}

Database.prototype.getResults = function(){
		
		try{// SQL statement die het aantal record telt in de settingsData tabel.
			var sqlgetEnvData = 'SELECT * FROM environmentData';
			this.myDb.transaction(
				function(transaction){
	 				// Het uitvoeren van sqlCount om te weten hoeveel records er zijn. 
					transaction.executeSql(sqlgetEnvData, [], dataSelectHandler, this.errorHandler);
				}
			)
		} catch(e) {
			alert("Error Processing SQL: "+ e.message +".");
		}

}

function dataSelectHandler(transaction, results) {
 
 	var NrHue = [0, 0, 0, 0];
	var Mood = [0, 0, 0, 0];
	
	var UnhappyHue = [0, 0 , 0, 0];
	var NrUnhappy = 0;
	
	var FineHue = [0, 0 , 0, 0];
	var NrFine = 0;
	
	var HappyHue = [0, 0 , 0, 0];
	var NrHappy = 0;
	
	var MoodWd = [0, 0, 0, 0, 0, 0, 0];
	var HueWd1 = [0, 0, 0, 0, 0, 0, 0];
	var HueWd2 = [0, 0, 0, 0, 0, 0, 0];
	var HueWd3 = [0, 0, 0, 0, 0, 0, 0];
	var HueWd4 = [0, 0, 0, 0, 0, 0, 0];
	var NrWd = [0, 0, 0, 0, 0, 0, 0];
	
	var HueData = [0, 0, 0, 0];
	var UnhappyData = [0, 0, 0, 0];
	var FineData = [0, 0, 0, 0];
	var HappyData = [0, 0, 0, 0];
	var WdMoodData = [0, 0, 0, 0, 0, 0, 0];
	
	// Handle the results
    for (var i=0; i<results.rows.length; i++) {
 
    	var row = results.rows.item(i);
    	var hue_cat = row['hue_cat'];
    	var mood = parseInt(row['moodrate']);
    	var d = new Date(row['added_on']);
    	var weekday = d.getDay();
    	
    	if (hue_cat == 1) // vigorous categorie
    	{ 	NrHue[0] = NrHue[0] + 1;
    		Mood[0] = Mood[0] + mood;}
    	else if (hue_cat == 2) // nature categorie
    	{ 	NrHue[1] = NrHue[1] + 1;
    		Mood[1] = Mood[1] + mood;}
    	else if (hue_cat == 3) // ocean categorie
    	{ 	NrHue[2] = NrHue[2] + 1;
    		Mood[2] = Mood[2] + mood;}
    	else if (hue_cat == 4) // flower categorie
    	{ 	NrHue[3] = NrHue[3] + 1;
    		Mood[3] = Mood[3] + mood;};
    	
    	if (mood <= 3) // Unhappy categorie
    	{ 	NrUnhappy = NrUnhappy + 1;
    		if (hue_cat == 1) // vigorous categorie
    		{ 	UnhappyHue[0] = UnhappyHue[0] + 1;}
    		else if (hue_cat == 2) // nature categorie
    		{ 	UnhappyHue[1] = UnhappyHue[1] + 1;}
    		else if (hue_cat == 3) // ocean categorie
    		{ 	UnhappyHue[2] = UnhappyHue[2] + 1;}
    		else if (hue_cat == 4) // flower categorie
    		{ 	UnhappyHue[3] = UnhappyHue[3] + 1;}
    	}
    	
    	else if (mood >= 8) // Happy categorie
    	{ 	NrHappy = NrHappy + 1;
    		if (hue_cat == 1) // vigorous categorie
    		{ 	HappyHue[0] = HappyHue[0] + 1;}
    		else if (hue_cat == 2) // nature categorie
    		{ 	HappyHue[1] = HappyHue[1] + 1;}
    		else if (hue_cat == 3) // ocean categorie
    		{ 	HappyHue[2] = HappyHue[2] + 1;}
    		else if (hue_cat == 4) // flower categorie
    		{ 	HappyHue[3] = HappyHue[3] + 1;}
    	}
    	
    	else // Fine categorie
    	{ 	NrFine = NrFine + 1;
    		if (hue_cat == 1) // vigorous categorie
    		{ 	FineHue[0] = FineHue[0] + 1;}
    		else if (hue_cat == 2) // nature categorie
    		{ 	FineHue[1] = FineHue[1] + 1;}
    		else if (hue_cat == 3) // ocean categorie
    		{ 	FineHue[2] = FineHue[2] + 1;}
    		else if (hue_cat == 4) // flower categorie
    		{ 	FineHue[3] = FineHue[3] + 1;}
    	};
    
    	
    	NrWd[weekday] = NrWd[weekday] + 1;
    	MoodWd[weekday] = MoodWd[weekday] + mood;
    	if (hue_cat == 1) // vigorous categorie
    	{ 	HueWd1[weekday] = HueWd1[weekday] + 1;}
   		else if (hue_cat == 2) // nature categorie
   		{ 	HueWd2[weekday] = HueWd2[weekday] + 1;}
   		else if (hue_cat == 3) // ocean categorie
   		{ 	HueWd3[weekday] = HueWd3[weekday] + 1;}
   		else if (hue_cat == 4) // flower categorie
    	{ 	HueWd4[weekday] = HueWd4[weekday] + 1;}
    
   }
   
   for (var i=0; i<4; i++){
   		if (Mood[i] != 0)
   		{HueData[i] = Mood[i]/ NrHue[i]};
   		
   		if (NrUnhappy != 0)
   		{UnhappyData[i] = UnhappyHue[i]/ NrUnhappy};
   		
   		if (NrFine != 0)
   		{FineData[i] = FineHue[i]/ NrFine};
   		
   		if (NrHappy != 0)
   		{HappyData[i] = HappyHue[i]/ NrHappy}
   }
   	
   	for(var i=0; i<7; i++){	
   		if(NrWd[i] != 0)
   		{	WdMoodData[i] = MoodWd[i]/ NrWd[i]; }
   		
   	}
   
   openChart(HueData, UnhappyData, FineData, HappyData, WdMoodData);
}
	

/****************************** HANDLERS ******************************************/



/* 
 * Callback handler voor errors bij het uitvoeren van de sqlstatements.
 */
Database.prototype.errorHandler = function(transaction, error){
	// geeft true terug zodat er een rollback van de transactie wordt gedaan
	if (error.code==1){
		// De tabel bestaat al in de database. Geef dit weer in de log.
		console.log('table exists already in de database.');
 	} 
 	else {
    	// Geef error weer in de log
		alert("Error processing SQL: " + error);
		console.log('Error was ' + error.message + ' (Code ' + error.code + ')');
	}
	return true;
}

/*
 * Callback handler voor succes bij het uitvoeren van de sqlstatements
 */
Database.prototype.nullDataHandler = function(transaction, resultSet) {
	console.log("SQL Query Succeeded");
}


/*
function dropTables(){
	TrackerDb.transaction(
	    function (transaction) {
	    	transaction.executeSql("DROP TABLE EnvironmentData;", [], nullDataHandler, errorHandler);
	    }
	);
	console.log("Table 'EnvironmentData' has been dropped.");
	location.reload();
}
	    	
	    	 



/*
function selectAll(){ 
	TrackerDb.transaction(
	    function (transaction) {
	        transaction.executeSql("SELECT * FROM EnvironmentData;", [], dataSelectHandler, errorHandler);    
	    }
	);	
}

/*
function dataSelectHandler(transaction, results){

	// Handle the results
    for (var i=0; i<results.rows.length; i++) {
        
    	var row = results.rows.item(i);
    	
        var newFeature = new Object();
    	
    	newFeature.moodrate  = row['moodrate'];
    }

}
*/


/***
**** Save 'default' data into DB table **
***/

/*function saveAll(){
		prePopulate(1);
}
*/

