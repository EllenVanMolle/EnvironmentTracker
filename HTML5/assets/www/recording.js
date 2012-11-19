/**
 * Recording of sound
 */


/*
 * Deze functie neemt het geluid op, analyseert het en verwijdert het. (Enkel het opnemen van het geluid en 
 * het opslaan gebeurt nu.
 */
function recordNoise() {
	
	// Aanpassen van de layout aan het feit dat datacollectie na deze stap voltooid is.
	var saveB1 = document.getElementById("saveButton1");
	var saveB2 = document.getElementById("saveButton2");
	var saveB3 = document.getElementById("saveButton3");
	var tab1 = document.getElementById("tab1");
	var tab2 = document.getElementById("tab2");
	
	// Toon de save buttons op elke pagina stepone, steptwo, stepthree
	saveB1.className = saveB1.className.replace("hidden", "" );
	saveB2.className = saveB2.className.replace("hidden", "" );
	saveB3.className = saveB3.className.replace("hidden", "" );
	
	// Enable de step three navigatie op stepone en steptwo
	tab1.className = tab1.className.replace("ui-disabled", "");
	tab2.className = tab2.className.replace("ui-disabled", "");
	
	//Start het opnemen van het geluid. Je mag maximum 1 clip opnemen van maximum 5 sec.
	var options = {limit:1, duration:5};
	navigator.device.capture.captureAudio(captureSuccess, captureError, options);
}

/*
 * Deze functie wordt aangeroepen wanneer het geluid succesvol is opgenomen.
 */
var captureSuccess = function(mediaFiles) {
    for (i = 0, len = mediaFiles.length; i < len; i += 1) {
        path = mediaFiles[i].fullPath;
        console.log(path); // voorlopig log het pad
   
    }
};

/*
 * Deze functie wordt aangeroepen wanneer het geluid niet is opgenomen. De gebruiker krijgt dan een foutmelding.
 */
var captureError = function(error) {
    navigator.notification.alert('Error code: ' + error.message, null, 'Capture Error');
};
