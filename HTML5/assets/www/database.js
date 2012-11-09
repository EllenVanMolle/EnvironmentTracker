/**
 * database
 */

$(function(){ initDatabase();

 	// Button and link actions
	$('#buttonMood').click(function(){ saveMood(); });

 	
})

document.addEventListener("deviceready", onDeviceReady, false);

function onDeviceReady() {
	initDatabase()}
	
/* Creëer de database TrackerDb **/
function initDatabase() {
	try {
	    if (!window.openDatabase) {
	        alert('Local Databases are not supported by your browser. Please use a Webkit browser.');
	    } 
	    else {
	    	alert ("make db");
	    	var shortName = 'TrackerDb';
	        var version = '1.0';
	        var displayName = 'Database of the Environment Tracker';
	        var maxSize = (2*1024*1024); // 5MB in bytes
	        TrackerDb = openDatabase(shortName, version, displayName, maxSize);
			createTables();
			//selectAll();
	    }
	} catch(e) {
	    if (e == 2) {
	        // Version mismatch.
	        console.log("Invalid database version.");
	    } else {
	        console.log("Unknown error "+ e +".");
	    }
	    return;
	} 
}

/* Creëer tabellen in de database
 * Tabel: EnvironmentData **/
function createTables(){
	TrackerDb.transaction(
        function (transaction) {
        	transaction.executeSql('CREATE TABLE IF NOT EXISTS EnvironmentData'+
        	'(ID INTEGER PRIMARY KEY ASC, added_on DATETIME, moodrate INTEGER,'+
        	'PosLatitude DOUBLE, PosLongitude DOUBLE)',
        	[], nullDataHandler, errorHandler);
        	
        	transaction.executeSql('CREATE TABLE IF NOT EXISTS PictureData' +
    		'(obsID INTEGER PRIMARY KEY, hue_cat INTEGER, per_vigorous REAL,' +
    		'per_nature REAL, per_ocean REAL, per_flower REAL, per_saturation REAL,' +
    		'per_brightness REAL)', // FOREIGN KEY (obsID) REFERENCES EnvironmentData(ID))',
    		[], nullDataHandler, errorHandler);
        }
    );
}

/* sla de moodrate op als op de continue button wordt geklikt**/
function saveMood(){
	TrackerDb.transaction(
       	function (transaction) {
	
			var dt = new Date();
			var moodrate = $('#moodSlider').val(); 
   	   		var location = get_location();
   	   		
   			if (location[0]){
   				alert("longitude: " + longitude +"latitude: " + latitude);
   				
       				transaction.executeSql('INSERT INTO EnvironmentData(added_on, moodrate, PosLatitude, PosLongitude)' +
	    			'VALUES (?, ?, ?, ?)', [dt, moodrate, location[1], location[2]]); 
        		
	    		} 
   			else
   				{alert('no values for location');
   				transaction.executeSql('INSERT INTO EnvironmentData(added_on, moodrate)' +
	    			'VALUES (?, ?)', [dt, moodrate]);
   				}
   		}
		);
   	}
	  
	  function get_location(){
	  	var success = false;
	  	var latitude = 0;
	  	var longitude = 0;
	  	
				if (navigator.geolocation) {
					var timeoutVal = 5000;// we wachten max 5 sec
					var maximumAgeVal = 0;
					alert('locate');
  					navigator.geolocation.getCurrentPosition(displaySuccess, displayError,
    				{enableHighAccuracy: true, timeout: timeoutVal, maximumAge: maximumAgeVal });
				}
				else {
  				alert("Geolocation is not supported.");
				}
				
				function displaySuccess (position) {
					latitude = position.coords.latitude;
	   				longitude = position.coords.longitude;
	   				success = true;
	   				alert('success/ latitude: ' + latitude + ' longitude: ' + longitude);
	   				return [success, latitude, longitude]; 
	   			};
			
				function displayError(error) {
 				var errors = { 
   					1: 'Permission denied',
   					2: 'Position unavailable',
   					3: 'Request timeout'
   				};
 				alert("Error: " + errors[error.code]);
 				alert('Error/ latitude: ' + latitude + ' longitude: ' + longitude);
 				return [success, latitude, longitude];
			};
			
}

function insertEnvironmentData (dt, moodrate, location){
	
}

function insertPictureData (Data){
	alert ("found data");

	var perPix = Data[1];
	alert("show: " + perPix);
	
	var obsID = findMaxID();
	alert("obsID");
	
	/**TrackerDb.transaction(
       function (transaction) {
       	transaction.executeSql ('INSERT INTO PictureData(obsID, hue_cat, per_vigorous,
    		per_nature, per_ocean, per_flower, per_saturation, per_brightness)' +
	    		'VALUES (?, ?, ?, ?, ?, ?, ?, ?)', 
	    		[obsID, Data[0], perPix[0], perPix[1], perPix[2], perPix[3], Data[2], Data[3]]
	   }
	   );*/
}

function findMaxID() {
	var obsID;
	TrackerDb.transaction(
		function (transaction) {
			transaction.executeSql ('SELECT ID from EnvironmentData', [], 
			function (transaction, results)
				{var len = results.rows.length, i;
  				for (i = 0; i < len; i++) {
  					obsID = results.rows.item(i)['ID'];
  					alert(obsID);}
				
				}
			);
		}
	);
	
	return obsID;
}	 	
	    	
function errorHandler(transaction, error){
 	if (error.code==1){
 		// DB Table already exists
 	} 
 	else {
    	// Error is a human-readable string.
	    console.log('Oops.  Error was '+error.message+' (Code '+error.code+')');
 	}
    return false;
}

function nullDataHandler(){
	console.log("SQL Query Succeeded");
}

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

