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
}

/*
 * algemene variabelen
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
        	+ 'GPS BOOLEAN)';

	this.myDb.transaction(
			function (transaction) {
			transaction.executeSql('DROP TABLE settingsData', [], this.nullDataHandler, this.errorHandler);
			//uitvoeren van sqlTabel2 statement om tabel4 te creëeren: settingsData
        	transaction.executeSql(sqlTabel2, [], this.nullDataHandler, this.errorHandler);
        	}
		);
		
	} catch (e) { 
		console.log("Error processing SQL: "+ e.message +".");
	}
}


/****************************** OPSLAG DataCollectie ******************************************/


/*
 * Functie die aangeroepen wordt door geolocation om moodrate en datum te bepalen en vervolgens de observatie data op te slaan.
 */
function saveObservation (latitude, longitude){
	// bepaald de waarde van het html item met id moodSlider, maw bepaald de mood
	var moodrate = $('#moodSlider').val();
	
	// reset de waarde van de moodslider naar de default waarde 5
	resetMoodSlider();
	
	// bepaal huidige datum en tijd
	var on_date = new Date();
	
	// sla de data op in de database
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

/****************************** resetfunctions ******************************************/

/*
 * Functie die aangeroepen wordt door de goHome methode en door de saveObservation methode om de waarde van de moodSlider 
 * terug op 5 te zetten.
 */
function resetMoodSlider(){
	$("#moodSlider").val(5).slider("refresh"); // moodSlider waarde wordt op 5 gezet
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
	 resetLayout();
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
/*
 * Functie die wordt aangesproken door te klikken op de view results button in de homepage.
 * De functie haalt alle observaties uit de database en berekent een aantal waarden die dan door de graph.js
 * gebruikt worden om in de resultpagina's een aantal samenvattende grafieken weer te geven.
 */
function getData(){
	// haal alle observaties uit de database.
	database.getResults();
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
	var lastLocation = [Math.round(results.rows.item(AantalObs-1)['posLatitude']*1000), Math.round(results.rows.item(AantalObs-1)['posLongitude']*1000)];
	var lastLocationAantal = 0;
	var lastLocationMood = 0;
	
	// verwerking van de resultaten
	// we overlopen alle rijen in het resultaat, dus in de gevalle alle rijen uit de tabel
    for (var i=0; i< AantalObs; i++) {
 
    	// Elke rij in tabel is een observatie. Hier definieren we een variabele row gelijk aan observatie i
    	var row = results.rows.item(i);
    	
    	// De hue van observatie i
    	var hue_cat = row['hue_cat'];
    	// De mood van observatie i (moet nog omgezet worden naar een integer)
    	var mood = parseInt(row['moodrate']);
    	// een datum variabele voor de added_on van de observatie i
    	var d = new Date(row['added_on']);
    	// De dag waarop een observatie i werd toegevoegd (ma=0, di=1, woe=2, do=3, vrij=4, zat=5, zon=6)
    	var weekday = d.getDay();
    	// De saturatie van observatie i
    	var sat = row['per_saturation'];
    	// De brightness van observatie i
    	var bright = row['per_brightness'];
    	// De locatie van observatie i
    	var location = [Math.round(row['posLatitude'] *1000), Math.round(row['posLongitude'] *1000)]; // afgerond op drie cijfers na de komma
    	
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
    
    // berekenen van gemiddelde gemoedstoestand op een bepaalde dag van de week
    	// weekday waarde van 0-6 afh van de dag.
    	NrWd[weekday] += 1;
    	MoodWd[weekday] += mood;
    	
    // berekenen van de gemiddelde saturation per gemoedstoestand (1-10)
    	satNr[mood-1] += sat;
    // berekenen van de gemiddelde brightness per gemoedstoestand (1-10)
    	brightNr[mood-1] += bright ;
   	// aantal metingen per gemoedstoestand
    	NrM [mood-1] += 1;
    
    // vergelijken locatie observatie i met laatste locatie
    if (lastLocation == location) {
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
   	lastLocationMood /= lastLocationAantal;
   	
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
   	
   
   openChart(hueMood, UnhappyHue, FineHue, HappyHue, MoodWd, satNr, brightNr, avgMood, mostHueCat, lastLocationMood, lastLocationAantal, lastLocation);
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


/***
**** Save 'default' data into DB table **
***/

/*function saveAll(){
		prePopulate(1);
}
*/

