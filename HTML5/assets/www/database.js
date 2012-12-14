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
	database = new Database();
	database.initDatabase();// initialiseer de database
	database.createTable(); // creëer de environmenttabel
	database.createSettingsTable();// creëer de settingstabel
	
	// registreer de eventlistener zodat je weet wanneer de gebruiker opde backbutton clickt (enkel android!)
	document.addEventListener("backbutton", onBackKeyDown, false);
}

/*
 * Functie die wordt aangeroepen als de backbutton wordt geklikt
 */
function onBackKeyDown() {
	var activePage = window.location.href;
	console.log ("actieve pagina: " + activePage);
	if (activePage == "file:///android_asset/www/index.html#stepone"){
		$("#backButtonDialog").click();
   } else if (activePage == "file:///android_asset/www/index.html#steptwo"){
   		$.mobile.changePage("#stepone", {reverse: true})
   } else if (activePage == "file:///android_asset/www/index.html#stepthree"){
   		$.mobile.changePage("#steptwo", {reverse: true})
   } else if (activePage == "file:///android_asset/www/index.html#settings"){
   		$.mobile.changePage("#homepage", {reverse: true})
   } else if (activePage == "file:///android_asset/www/index.html#colorResults"){
   		$.mobile.changePage("#homepage", {reverse: true})
   } else if (activePage == "file:///android_asset/www/index.html#averages"){
   		$.mobile.changePage("#colorResults", {reverse: true, transition: turn})
   } else if (activePage == "file:///android_asset/www/index.html#hueGraphPage"){
   		$.mobile.changePage("#colorResults", {reverse: true, transition: turn})
   } else if (activePage == "file:///android_asset/www/index.html#happyGraphPage"){
   		$.mobile.changePage("#colorResults", {reverse: true, transition: turn})
   } else if (activePage == "file:///android_asset/www/index.html#fineGraphPage"){
   		$.mobile.changePage("#colorResults", {reverse: true, transition: turn})
   } else if (activePage == "file:///android_asset/www/index.html#unhappyGraphPage"){
   		$.mobile.changePage("#colorResults", {reverse: true, transition: turn})
   } else if (activePage == "file:///android_asset/www/index.html#saturationResults"){
   		$.mobile.changePage("#colorResults", {reverse: true, transition: turn})
   } else if (activePage == "file:///android_asset/www/index.html#brightnessResults"){
   		$.mobile.changePage("#colorResults", {reverse: true, transition: turn})
   } else if (activePage == "file:///android_asset/www/index.html#timeResults"){
   		$.mobile.changePage("#homepage", {reverse: true})
   } else if (activePage == "file:///android_asset/www/index.html#colorResults"){
   		$.mobile.changePage("#homepage", {reverse: true})
   }
}

/*
 * algemene variabelen
 */	
var database;

var analysisIsFinished;

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
Database.prototype.createTable = function(){
	try {// SQL statements voor de creatie van de environmenttabel: environmentData
		
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
 * Functie die nog een tabel settingsData creëert.
 */
Database.prototype.createSettingsTable = function(){
	try {// SQL statements voor de creatie van de tabellen
 		var sqlTabel2 = 'CREATE TABLE IF NOT EXISTS settingsData ('
        	+ 'ID INTEGER PRIMARY KEY,'
        	+ 'notification BOOLEAN,'
        	+ 'interval INTEGER,'
        	+ 'GPS BOOLEAN)';

	this.myDb.transaction(
			function (transaction) {
			//transaction.executeSql('DROP TABLE settingsData', [], this.nullDataHandler, this.errorHandler);
			//uitvoeren van sqlTabel2 statement om tabel4 te creëeren: settingsData
        	transaction.executeSql(sqlTabel2, [], function (transaction, resultSet){
        		// na het creëren van de tabel wordt een eerste record met de default settings opgeslagen
        		saveDefaultSettings();
        	}, this.errorHandler);
        	}
		);
		
	} catch (e) { 
		console.log("Error processing SQL: "+ e.message +".");
	}
}


/****************************** OPSLAG DataCollectie ******************************************/

function getUserLocation(GPSsetting) {
	console.log(GPSsetting);
	
if (GPSsetting){
	console.log("user wants GPS location"); 
	  	
	if (navigator.geolocation) {// Het is mogelijk om geolocatie te doen.
  		navigator.geolocation.getCurrentPosition(onSuccess, onError, 
    	{enableHighAccuracy: false, timeout: timeoutVal, maximumAge: maxAgeVal}); // Voer geolocatie uit met weinig accuraatheid
    	// note als enableHighAccuracy op true staat werkt de geolocatie niet op een android
	}
	else { // Het is niet mogelijk om aan geolocatie te doen omdat dit niet ondersteund wordt.
  		console.log("Geolocation is not supported."); // Geef dit weer in de logs
	}
}	else 
{	console.log ("User does not want GPSlocation");
	var latitude = 0;
	var longitude = 0;
	getMoodRate(latitude, longitude);
}

/*
 * Functie die aangeroepen wordt als het localiseren van de gebruiker succesvol was.
 * De locatie wordt opgeslagen in de environmentData tabel van de TrackerDb.
 */				
function onSuccess (position) {
	var latitude = position.coords.latitude; // breedteligging
	var longitude = position.coords.longitude; // lengteligging
	
	getMoodRate(latitude, longitude);
}

/*
 * Functie die aangeroepen wordt als het localiseren van de gebruiker faalt. De error wordt gelogd en 
 * twee nullen worden opgeslagen in de environmentData tabel van de TrackerDb in de kolommen van lengte- en breedteligging.
 */					
function onError (error) {
 	var errors = { 
		1: 'Permission denied',
		2: 'Position unavailable',
		3: 'Request timeout'
	}; 
	console.log("Error: " + errors[error.code]); //Geef error weer in de logs.
		 				
	var latitude = 0;
	var longitude = 0;
	getMoodRate(latitude, longitude);
	
}

}

function getMoodRate (latitude, longitude){
	// bepaald de waarde van het html item met id moodSlider, maw bepaald de mood
	var moodrate = parseInt($('#moodSlider').val(), 10);
	
	// reset de waarde van de moodslider naar de default waarde 5
	resetMoodSlider();
	console.log('values: ' + latitude + ' ' +longitude + ' '+moodrate);
	saveObservation(latitude, longitude, moodrate) // Sla de gegevens op in de db
}

/*
 * Functie die aangeroepen wordt door geolocation om moodrate en datum te bepalen en vervolgens de observatie data op te slaan.
 */
function saveObservation (latitude, longitude, moodrate){
	// bepaal huidige datum en tijd
	var on_date = new Date();
	
	console.log(analysisIsFinished);
	
	if (analysisIsFinished){
		// sla de data op in de database
		
		console.log (moodrate,' en ', on_date,' en ', latitude, ' en ', longitude);
		
		// haal de waarden voor hueclass, saturation and brightness uit de HTML pagina
		var hueClass = parseInt($("#HueClass").text(), 10) ;
        var saturation = parseInt($("#Saturation").text(), 10);
        var brightness = parseInt($("#Brightness").text(), 10);
        
        brightness += 1;
        console.log(brightness);
        
		resetSpanElementen();
		
		console.log (moodrate+' en '+ on_date+' en '+ latitude+ ' en '+ longitude);
		database.saveEnvData(moodrate, on_date, latitude, longitude, hueClass, saturation, brightness);
	} else {
		var myVar = setTimeout(function(){retrySaveObservation}, 3000);
	}
	
	
}

function retrySaveObservation (latitude, longitude, moodrate)
{	
	saveObservation (latitude, longitude, moodrate);
} 


/* 
 * Functie die de moodrate, locatie (latitude, longitude), foto gegevens opslaat in de environmentData tabel.
 */
Database.prototype.saveEnvData = function(moodrate, on_date, latitude, longitude, hueClass, Saturation, Brightness){
	try{ // SQL statement voor het opslaan van EnvData
		var sqlInsertEnvData = 'INSERT INTO environmentData (moodrate, added_on, posLatitude, '
			+ 'posLongitude, hue_cat, per_saturation, per_brightness) VALUES (?, ?, ?, ?, ?, ?, ?)';
	
		this.myDb.transaction(
			function(transaction){
			//uitvoeren van sqlInsertEnvData om de moodrate, breedteligging en lengteligging op te slaan in de environmentData tabel.	
			transaction.executeSql(sqlInsertEnvData, [moodrate, on_date, latitude, longitude, hueClass, Saturation, Brightness], 
				this.nullDataHandler, this.errorHandler);
			}
		);
	} catch(e) {
		alert("Error Processing SQL: "+ e.message +".");
		return;
	};
}

/****************************** resetfunctions ******************************************/

/*
 * Functie die aangeroepen wordt door de goHome methode en door de saveObservation methode om de waarde van de moodSlider 
 * terug op 5 te zetten.
 */
function resetMoodSlider(){
	$("#moodSlider").val(5).slider("refresh"); // moodSlider waarde wordt op 5 gezet
}

/*
 * Functie die aangeroepen wordt door de goHome methode en door de saveObservation methode om de waarde van de spanElementen 
 * terug op 0 te zetten.
 */
function resetSpanElementen(){
		var hueClass = $("#HueClass");
        var saturation = $("#Saturation");
        var brightness = $("#Brightness");
        
		hueClass.text (0); 
        saturation.text (0);
        brightness.text (0);
}

/*
 * Functie die wordt aangeroepen door de geolocation methode en door de goHome methode om 
 * de layout van de save button en stepthree navigatie terug naar hun oorspronkelijke toestand brengt.
 */
function resetLayout(){
	// link de variabelen met de gepaste html items
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

/*
 * Functie die wordt aangeroepen als de gebruiker op de home button klikt om de 
 * moodrate en de layout van de save button en stepthree navigatie terug naar hun oorspronkelijke toestand brengt.
 */
function goHome(){
	 resetMoodSlider();
	 resetSpanElementen();
	 resetLayout();
}



/****************************** OPSLAG settings *****************************************************/


/*
 * Bewaar de defaultdata in de tabel. Deze methode wordt aangeroepen na de creatie van de tabel en door de knop
 * default settings.
 */

function saveDefaultSettings(){
	
	database.countNumberRows();
}


Database.prototype.countNumberRows = function () {
	try {// SQL statement dat het aantal rijen telt in de settingsData tabel
		var sqlCount = 'SELECT Count(*) FROM settingsData';
		this.myDb.transaction(
			function(transaction){
		 	// Het uitvoeren van sqlCount om te weten of reeds set preferenties door de gebruiker werden opgeslagen. 
			transaction.executeSql(sqlCount, [], 
				function (transaction, resultSet){
					var nrRows = resultSet.rows.item(0)["Count(*)"];
					//console.log(nrRows);
					if (nrRows == 0){
					 // tabel is leeg.
					 var defaultSettingsData = [1, false, 120, true];
					 database.saveDefaultSettings(defaultSettingsData);
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
Database.prototype.saveDefaultSettings = function (SettingsData){
	try {// SQL statement die de defaultwaardes van de settings opslaat in de settingsData tabel
		var sqlSaveDefaultSettings = 'INSERT INTO settingsData (ID, notification, interval, GPS) VALUES (?, ?, ?, ?)';
     	
		this.myDb.transaction(
		function(transaction){
			// Het uitvoeren van sqlSaveDefaultSettings om de default settings in de settingsData tabel op te slaan.
			transaction.executeSql(sqlSaveDefaultSettings,
        	[SettingsData[0], SettingsData[1], SettingsData[2], SettingsData[3]],
        	 this.nullDataHandler, this.errorHandler);
			}
		)
	} catch(e) {
		alert("Error Processing SQL: "+ e.message +".");
	}	
}

/*
 * Functie die door de knop save in de settingspagina wordt aangeroepen en die de settings uit de html pagina gaat en
 * vervolgens het record in de database update
 */
function saveSettings(){
	var settingsData = [true, 120, true];
	
	// get value of notification checkbox
	settingsData[0]= $('input[type="checkbox"]:first').is(':checked');
	console.log($('input[type="checkbox"]:first').is(':checked'));
	
	// save the value of the number inputfield
	settingsData[1]= $("input[type='number']").val();
	
	// get value of GPS checkbox
	settingsData[2]=  $('input[type="checkbox"]:last').is(':checked');
	
	console.log (settingsData);
	
	// save everything in the database
	database.saveSettings (settingsData);
}

/*
 * Functie die de settings in de database update
 */

Database.prototype.saveSettings = function (SettingsData){
	try {// SQL statement die de defaultwaardes van de settings opslaat in de settingsData tabel
		var sqlSaveSettings = 'UPDATE settingsData SET notification = ?, interval = ?, GPS = ? WHERE ID = 1';
     	
		this.myDb.transaction(
		function(transaction){
			// Het uitvoeren van sqlSaveDefaultSettings om de default settings in de settingsData tabel op te slaan.
			transaction.executeSql(sqlSaveSettings,
        	[SettingsData[0], SettingsData[1], SettingsData[2]],
        	 this.nullDataHandler, this.errorHandler);
			}
		)
	} catch(e) {
		alert("Error Processing SQL: "+ e.message +".");
	}	
}

/*
 * Funcite die wordt aangesproken bij het openen van de settings
 */
function getSettingsData(){
	database.getSettings();
	
	// verander de tekst op de homepage naar Thank you!
	var text = document.getElementById("homepageText");
        text.innerHTML = "Welcome !";
}

/* 
 * Functie aangeroepen door geolocation methode om de GPS settings uit de settingsData tabel te halen
 * en deze effectief te kunnen toepassen.
 */
Database.prototype.getGPSSettings = function(){
	try{// SQL statement die de default settings opslaat in de settingsData tabel
		var sqlGetGPSSettings = 'SELECT (GPS) FROM settingsData WHERE ID = ?' 
     
		this.myDb.transaction(
		function(transaction){
			// Het uitvoeren van sqlInsertDefaultSetData om de default settings in de settingsData tabel op te slaan.
			transaction.executeSql(sqlGetGPSSettings, [1], function (transaction, resultSet){
				var row = resultSet.rows.item(0);
				GPSsettings = row['GPS'];
				console.log ("gps settings " + GPSsettings);
       			getUserLocation(GPSsettings);
			}, this.errorHandler);
			}
		)
	} catch(e) {
		alert("Error Processing SQL: "+ e.message +".");
	}
}


/* 
 * Functie die de default settings uit de settingsData tabel haalt.
 */
Database.prototype.getSettings = function(){
	try{// SQL statement die de default settings opslaat in de settingsData tabel
		var sqlGetSettings = 'SELECT * FROM settingsData WHERE ID = ?' 
     
		this.myDb.transaction(
		function(transaction){
			// Het uitvoeren van sqlInsertDefaultSetData om de default settings in de settingsData tabel op te slaan.
			transaction.executeSql(sqlGetSettings, [1], function (transaction, resultSet){
				var row = resultSet.rows.item(0);
       			setSettings([row['notification'], row['interval'], row['GPS']]);
			}, this.errorHandler);
			}
		)
	} catch(e) {
		alert("Error Processing SQL: "+ e.message +".");
	}
}

/*
 * Functie die ervoor zorgt dat de waardes voor de settings uit de database correct worden weergegeven
 */
 
function setSettings(settingsData) {
	//console.log (settingsData);	
	
	// zet de waarde van number gelijk met de waarde opgeslagen in de settingstabel
	$("input[type='number']").val(settingsData[1]);
	
	// idem voor de notification checkbox
	if (settingsData[0] == "true")
	{	
		$("input[type='checkbox']:first").attr("checked",true).checkboxradio("refresh");
		$("input[type='number']").prop('disabled', false);
	}
	else {
		$("input[type='checkbox']:first").attr("checked",false).checkboxradio("refresh");
		$("input[type='number']").prop('disabled', true);
	};
	
	// idem voor de GPS checkbox
	if (settingsData[2] == "true"){
		$("input[type='checkbox']:last").attr("checked",true).checkboxradio("refresh");
	}
	else {
		$("input[type='checkbox']:last").prop("checked",false).checkboxradio("refresh");
		}
}

/*
 * Functie die wordt aangeroepen als de checkbox voor notificaties wordt gewijzigd.
 * Als notificaties worden aangevinkt, wordt het interval inputvakje enabled of disabled
 */
function changeCheckboxNotifications(){
	console.log ("we zijn er");
	if ($('input[type="checkbox"]:first').is(':checked')){
		$("input[type='number']").prop('disabled', false);
	} else {
		$("input[type='number']").prop('disabled', true);
	}
}

/*
 * Functie die wordt aangeroepen als de gebruiker op de checkbox klikt
 */
function showPopup(){
	$( "#NotificationPopup" ).popup( "open" );
	// zorg ervoor dat de notificatiecheckbox niet wordt aangevinkt
	$("input[type='checkbox']:first").attr("checked",true).checkboxradio("refresh");
	
}



/****************************** RESULTS ******************************************/
/*
 * Functie die wordt aangesproken door te klikken op de view results button in de homepage.
 * De functie haalt alle observaties uit de database en berekent een aantal waarden die dan door de graph.js
 * gebruikt worden om in de resultpagina's een aantal samenvattende grafieken weer te geven.
 */
function getResultData(){
	// haal alle observaties uit de database.
	database.getResults();
	
	// verander de tekst op de homepage naar Thank you!
	var text = document.getElementById("homepageText");
        text.innerHTML = "Welcome !";
}

/*
 * Functie die alle observaties uit de database haalt.
 */
Database.prototype.getResults = function(){
		
		try{// SQL statement dat alle observaties uit de environmentData tabel haalt
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

/*
 * Functie die de resultaten van de uitvoering van het sql statement uit de getResults methode verwerkt.
 */
function dataSelectHandler(transaction, results) {
 	
 	// variabele voor het aantal observaties
 	var AantalObs = results.rows.length;
 	
 	// variabelen voor het berekenen van de gemiddelde gemoedstoestand per hue categorie
 	var NrHue = [0, 0, 0, 0];
	var hueMood = [0, 0, 0, 0];
	
	// variabelen voor het berekenen van % per hue_cat als gebruiker ongelukkig is	
	var UnhappyHue = [0, 0 , 0, 0];
	var NrUnhappy = 0;
	
	// variabelen voor het berekenen van % per hue_cat als gebruiker oke is
	var FineHue = [0, 0 , 0, 0];
	var NrFine = 0;
	
	// variabelen voor het berekenen van % per hue_cat als gebruiker gelukkig is
	var HappyHue = [0, 0 , 0, 0];
	var NrHappy = 0;
	
	// variabelen voor het berekenen van de gemiddelde gemoedstoestand per dag van de week
	var MoodWd = [0, 0, 0, 0, 0, 0, 0];
	var NrWd = [0, 0, 0, 0, 0, 0, 0];
	
	// variabelen voor het berekenen van de saturation per gemoedstoestand
	var satNr = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
	// variabelen voor het berekenen van de brightness per gemoedstoestand
	var brightNr = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
	// variabele die het aantal meting per gemoedstoestand weergeeft.
	var NrM = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
	
	// variabelen voor het berekenen van de gemiddelde gemoedstoestand en meest voorkomende hue categorie
	var avgMood = 0;
	var mostHueCat = 0;
	
	// variabelen voor het berekenene van de gemiddelde gemoedstoestand op de laatste location
	//var lastLocationNotRound = [results.rows.item(AantalObs-1)['posLatitude'], results.rows.item(AantalObs-1)['posLongitude']]
	var lastLocationNotRound = [64,422006, 2,084095];
	console.log('lastlocation ' + lastLocationNotRound);
	var lastLocation = [Math.round(lastLocationNotRound[0]*10000), Math.round(lastLocationNotRound[1]*10000)];
	var lastLocationAantal = 0;
	var lastLocationMood = 0;
	console.log('lastlocationMood');
	
	// verwerking van de resultaten
	// we overlopen alle rijen in het resultaat, dus in de gevalle alle rijen uit de tabel
    for (var i=0; i< AantalObs; i++) {
 
    	// Elke rij in tabel is een observatie. Hier definieren we een variabele row gelijk aan observatie i
    	var row = results.rows.item(i);
    	
    	// De hue van observatie i
    	var hue_cat = row['hue_cat'];
    	// De mood van observatie i (moet nog omgezet worden naar een integer)
    	var mood = parseInt(row['moodrate'], 10);
    	// een datum variabele voor de added_on van de observatie i
    	var d = new Date(row['added_on']);
    	// De dag waarop een observatie i werd toegevoegd (ma=0, di=1, woe=2, do=3, vrij=4, zat=5, zon=6)
    	var weekday = d.getDay();
    	// De saturatie van observatie i
    	var sat = row['per_saturation'];
    	// De brightness van observatie i
    	var bright = row['per_brightness'];
    	// De locatie van observatie i
    	var location = [Math.round(row['posLatitude'] *10000), Math.round(row['posLongitude'] *10000)]; // afgerond op drie cijfers na de komma
    	
    if (hue_cat != 0) // als een foto genomen werd in deze observatie
    {
    	// berekenen van de gemiddelde gemoedstoestand per hue categorie
       	if (hue_cat == 1) 
    	{ 	//als i tot de vigorous categorie
    		NrHue[0] += 1;
    		hueMood[0] += mood;}
    	else if (hue_cat == 2)
    	{ 	//als i tot de nature categorie
    		NrHue[1] += 1;
    		hueMood[1] += mood;}
    	else if (hue_cat == 3) 
    	{ 	//als i tot de ocean categorie
    		NrHue[2] += 1;
    		hueMood[2] += mood;}
    	else if (hue_cat == 4)
    	{ 	//als i tot de flower categorie
    		NrHue[3] += 1;
    		hueMood[3] += mood;};
    		
     	// berekenen van % per hue_cat als gebruiker ongelukkig is	
    	if (mood <= 3) // Unhappy categorie
    	{ 	NrUnhappy = NrUnhappy + 1;
    		if (hue_cat == 1) // vigorous categorie
    		{ 	UnhappyHue[0] += 1;}
    		else if (hue_cat == 2) // nature categorie
    		{ 	UnhappyHue[1] += 1;}
    		else if (hue_cat == 3) // ocean categorie
    		{ 	UnhappyHue[2] += 1;}
    		else if (hue_cat == 4) // flower categorie
    		{ 	UnhappyHue[3] += 1;}
    	}
    	
    	// berekenen van % per hue_cat als gebruiker gelukkig is	
    	else if (mood >= 8) // Happy categorie
    	{ 	NrHappy = NrHappy + 1;
    		if (hue_cat == 1) // vigorous categorie
    		{ 	HappyHue[0] += 1;}
    		else if (hue_cat == 2) // nature categorie
    		{ 	HappyHue[1] += 1;}
    		else if (hue_cat == 3) // ocean categorie
    		{ 	HappyHue[2] += 1;}
    		else if (hue_cat == 4) // flower categorie
    		{ 	HappyHue[3] += 1;}
    	}
    	
    	// berekenen van % per hue_cat als gebruiker oke is	
    	else // Fine categorie
    	{ 	NrFine = NrFine + 1;
    		if (hue_cat == 1) // vigorous categorie
    		{ 	FineHue[0] += 1;}
    		else if (hue_cat == 2) // nature categorie
    		{ 	FineHue[1] += 1;}
    		else if (hue_cat == 3) // ocean categorie
    		{ 	FineHue[2] += 1;}
    		else if (hue_cat == 4) // flower categorie
    		{ 	FineHue[3]+= 1;}
    	};
    	
    	// berekenen van de gemiddelde saturation per gemoedstoestand (1-10)
    	satNr[mood-1] += sat;
    	// berekenen van de gemiddelde brightness per gemoedstoestand (1-10)
    	brightNr[mood-1] += bright ;
   		// aantal metingen per gemoedstoestand
    	NrM [mood-1] += 1;
    };
    
    // berekenen van gemiddelde gemoedstoestand op een bepaalde dag van de week
    	// weekday waarde van 0-6 afh van de dag. heeft niet te maken met foto analyse dus die mag nul zijn
    	NrWd[weekday] += 1;
    	MoodWd[weekday] += mood;
    	
    
    // vergelijken locatie observatie i met laatste locatie
    if (location == lastLocation) {
    	lastLocationAantal += 1;
    	lastLocationMood += mood;
    }
    
    
   }; // einde forlus
   
   // voor elke hue categorie
   for (var i=0; i<4; i++){
   	
   		if (hueMood[i] != 0)
   		// gemiddelde gemoedstoestand
   		{	avgMood += hueMood[i]; // totale gemoed
   			hueMood[i] /= NrHue[i];
   			hueMood[i] = Math.round(hueMood[i])// afronden
   		};
   		
   		if (NrUnhappy != 0)
   		// % wanneer gebruiker ongelukkig is
   		{	UnhappyHue[i] /= NrUnhappy;
   			UnhappyHue[i] = (Math.round(UnhappyHue[i]*100)) /100 // afronden tot op twee cijfers na de komma
   		};
   		
   		if (NrFine != 0)
   		// % wanneer gebruiker oke is
   		{	FineHue[i] /= NrFine;
   			FineHue[i] = (Math.round(FineHue[i]*100)) /100 // afronden tot op twee cijfers na de komma
   		};
   		
   		if (NrHappy != 0)
   		// % wanneer gebruiker ok is
   		{	HappyHue[i] /= NrHappy;
   			HappyHue[i] = (Math.round(HappyHue[i]*100)) /100// afronden tot op twee cijfers na de komma
   		}
   		
   };
   	
   	// voor elke dag van de week
   	for(var i=0; i<7; i++){	
   		if(NrWd[i] != 0)
   		{	//gemiddeld gemoedstoestand
   			MoodWd[i] /= NrWd[i];
   			MoodWd[i] = Math.round(MoodWd[i])// afronden
   		}
   		
   	};
   	
   	// voor elke gemoedstoestand tussen 1-10
   	for (var i=0; i<10; i++){
   		if(NrM[i] != 0)
   		{ 	// gemiddelde saturation
   			satNr[i] /= NrM[i];
   			satNr[i] = Math.round(satNr[i]); // afronden
   			
   			// gemiddelde brightness
   			brightNr[i] /= NrM[i];
   			brightNr[i] = Math.round(brightNr[i]); // afronden
   			
   		}
   	};
   	
   	// gemiddelde mood op de laatste locatie
   	//lastLocationMood /= lastLocationAantal;
   	
   	// gemiddelde gemoedstoestand
   	avgMood /= (NrHue[0]+ NrHue[1]+ NrHue[2] + NrHue[3]);
   	
   	// bepaal het hoogste aantal observaties voor een hue category
    var MaxNrObs = Math.max.apply(null, NrHue);
       
   	// loop door alle klassen (0-3)	
    for (var n=0; n < 4; n++ )
    {	
       if (NrHue[n] == MaxNrObs) // vergelijk het aantal observaties in elke klasse met de maximumwaarde
       {// als het maximum gelijk is aan het aantal pixels in die klasse, stel hueclass gelijk aan die categorie
         	mostHueCat = (n+1);
        }
    }
   	
   	console.log('lastlocationMood ' + lastLocationMood);
   	console.log('lastlocationAantal ' + lastLocationAantal);
   
   openChart(hueMood, UnhappyHue, FineHue, HappyHue, MoodWd, satNr, brightNr, avgMood, mostHueCat, lastLocationNotRound, lastLocationAantal, lastLocationMood);
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



