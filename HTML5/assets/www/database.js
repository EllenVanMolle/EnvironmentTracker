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

var pixData = [0, 0, 0, 0, 0, 0, 0];

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
 * Functie die de nodige tabellen in de database creëert door sqlstatements uit te voeren.
 * Tabel1: environmentData; Tabel2: pictureData; Tabel3: audioData
 */
Database.prototype.createTables = function(){
	try {// SQL statements voor de creatie van de tabellen
		
		var sqlTabel1 ='CREATE TABLE IF NOT EXISTS environmentData ('
        	+ 'ID INTEGER PRIMARY KEY ASC,'
        	+ 'added_on DATETIME,'
        	+ 'moodrate INTEGER,'
        	+ 'posLatitude DOUBLE,'
        	+ 'posLongitude DOUBLE)';
        
        var sqlTabel2 = 'CREATE TABLE IF NOT EXISTS pictureData ('
        	+ 'obsID INTEGER PRIMARY KEY,'
        	+ 'hue_cat INTEGER,'
        	+ 'per_vigorous REAL,'
        	+ 'per_nature REAL,'
        	+ 'per_ocean REAL,'
        	+ 'per_flower REAL,'
        	+ 'per_saturation REAL,' 
        	+ 'per_brightness REAL,'
        	+ 'FOREIGN KEY (obsID) REFERENCES environmentData(ID))';
        
        var sqlTabel3 = 'CREATE TABLE IF NOT EXISTS audioData ('
        	+ 'obsID INTEGER PRIMARY KEY,'
        	+ 'FOREIGN KEY (obsID) REFERENCES environmentData(ID))'; 
        
		this.myDb.transaction(
			function (transaction) {
			
			//uitvoeren van sqlTabel1 statement om tabel1 te creëeren: environmentData
        	transaction.executeSql(sqlTabel1, [], this.nullDataHandler, this.errorHandler);
        	
        	//uitvoeren van sqlTabel2 statement om tabel2 te creëeren: pictureData
        	transaction.executeSql(sqlTabel2, [], this.nullDataHandler, this.errorHandler);

			//uitvoeren van sqlTabel3 statement om tabel3 te creëeren: autioData
        	transaction.executeSql(sqlTabel3, [], this.nullDataHandler, this.errorHandler);
    		
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
 		var sqlTabel4 = 'CREATE TABLE IF NOT EXISTS settingsData ('
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
			transaction.executeSql('DROP TABLE settingsData', [], this.nullDataHandler, this.errorHandler);
			//uitvoeren van sqlTabel4 statement om tabel4 te creëeren: settingsData
        	transaction.executeSql(sqlTabel4, [], 
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
 * Functie die aangeroepen wordt door geolocation om de locatie en moodrate op te slaan.
 */
function saveLocation (latitude, longitude){
	var moodrate = $('#moodSlider').val();
	var on_date= new Date();
	database.saveEnvData(moodrate, on_date, latitude, longitude);
}


/* 
 * Functie die de moodrate en locatie (latitude, longitude) opslaat in de environmentData tabel.
 */
Database.prototype.saveEnvData = function(moodrate, on_date, latitude, longitude){
	try{ // SQL statement voor het opslaan van EnvData
		var sqlInsertEnvData = 'INSERT INTO environmentData (moodrate, added_on, posLatitude, '
			+ 'posLongitude) VALUES (?, ?, ?, ?)';
	
		this.myDb.transaction(
			function(transaction){
			//uitvoeren van sqlInsertEnvData om de moodrate, breedteligging en lengteligging op te slaan in de environmentData tabel.	
			transaction.executeSql(sqlInsertEnvData, [moodrate, on_date, latitude, longitude], 
				function (transaction, resultSet){
					database.getRowsEnvData(); // Als dit statement is uitgevoerd, tel het aantal rijen in de environment tabel.
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
 * Functie die telt hoeveel records er in de environmentData tabel zijn en
 * op die manier weten we wat de laatste record ID is.
 */
Database.prototype.getRowsEnvData = function(){
	try{// SQL statement die het aantal record telt in de settingsData tabel.
		var sqlCount = 'SELECT Count(*) FROM environmentData';
		this.myDb.transaction(
			function(transaction){
		 	// Het uitvoeren van sqlCount om te weten of reeds set preferenties door de gebruiker werden opgeslagen. 
			transaction.executeSql(sqlCount, [], 
				function (transaction, resultSet){
					var nrRows = resultSet.rows.item(0)["Count(*)"]; // het aantal rijen in de environmentData tabel is gelijk aan het laatste ingegeven id.
					database.savePicData(nrRows, pixData[0], pixData[1], pixData[2], pixData[3], pixData[4], pixData[5], pixData[6]);
					//voer de functie uit die de fotogegevens aan de tabel toevoegt, met hetzelfde id als de laatste rij in environmentData tabel.
					database.saveAudioData(nrRows);
					//voer de functie uit die de audiogegevens aan de tabel toevoegt, met hetzelfde id als de laatste rij in environmentData tabel.
					
					}
				, this.errorHandler);
			}
		)
	} catch(e) {
		alert("Error Processing SQL: "+ e.message +".");
	}
}

/* 
 * Functie die de gegevens uit de foto analyse opslaat in de pictureData tabel.
 */
Database.prototype.savePicData = function(obsId, HueClass, vigorous, nature, ocean, flower, saturation, brightness)
{
	try{// SQL statement voor het opslaan van PicData
		var sqlInsertPicData = 'INSERT INTO pictureData (obsID, hue_cat, per_vigorous, '
			+'per_nature, per_ocean, per_flower, per_saturation, per_brightness) '
			+ 'VALUES (?, ?, ?, ?, ?, ?, ?, ?)';
		this.myDb.transaction(
			function(transaction){
			//uitvoeren van sqlInsertPicData om de Hue, Saturation, Brightness op te slaan in de pictureData tabel.	
			transaction.executeSql(sqlInsertPicData, 
			[obsId, HueClass, vigorous, nature, ocean, flower,
			saturation, brightness], this.nullDataHandler, this.errorHandler);
			}
		);
	} catch(e){
		alert("Error Processing SQL: "+ e.message +".");
	};
}

/* 
 * Functie die de gegevens uit de audio analyse opslaat in de audioData tabel.
 */
Database.prototype.saveAudioData = function(obsId)
{
	try{// SQL statement voor het opslaan van AudioData
		var sqlInsertAudioData = 'INSERT INTO audioData (obsID) '
			+ 'VALUES (?)';
		this.myDb.transaction(
			function(transaction){
			//uitvoeren van sqlInsertAudioData om de audiodata in de audioData tabel op te slaan.	
			transaction.executeSql(sqlInsertAudioData, 
			[obsId], this.nullDataHandler, this.errorHandler);
			}
		);
	} catch(e){
		alert("Error Processing SQL: "+ e.message +".");
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
        	+ 'GPS) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)';
     
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

