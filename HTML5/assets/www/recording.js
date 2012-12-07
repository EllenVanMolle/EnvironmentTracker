/**
 * Recording of sound
 */


/*
 * Deze functie past de layout aan van de datacollectiepagina's 
 * en neemt het geluid op, analyseert het en verwijdert het. (Enkel het opnemen van het geluid en 
 * het opslaan gebeurt nu.)
 */
function recordNoise() {
	
	// Aanpassen van de layout aan het feit dat datacollectie na deze stap voltooid is.
	
	// Link de variabelen met een element uit de htmlpagina
	var saveB1 = document.getElementById("saveButton1");
	var saveB2 = document.getElementById("saveButton2");
	var saveB3 = document.getElementById("saveButton3");
	var tab1 = document.getElementById("tab1");
	var tab2 = document.getElementById("tab2");
	
	// Toon de save buttons op elke pagina stepone, steptwo, stepthree
	saveB1.className = saveB1.className.replace("hidden", "" );
	saveB2.className = saveB2.className.replace("hidden", "" );
	saveB3.className = saveB3.className.replace("hidden", "" );
	
	// Enable de step 3 navigatie op stepone en steptwo
	tab1.className = tab1.className.replace("ui-disabled", "");
	tab2.className = tab2.className.replace("ui-disabled", "");
	
	//Start het opnemen van het geluid. Je mag maximum 1 clip opnemen van maximum 5 sec.
	// merk op soms treden er problemen op met limit en duration afhankelijk van het device waarop je ah werken bent. (Quircks)
	var options = {limit:1, duration:5};
	navigator.device.capture.captureAudio(captureSuccess, captureError, options);
}

/*
 * Functie die wordt opgeroepen als het verwijderen van het mediaFile succesvol was.
 */
function removeSuccess(entry) {
    console.log("Removal succeeded");
}

/*
 * Functie die wordt opgeroepen als het verwijderen van het mediaFile niet succesvol was.
 */
function removeFail(error) {
    alert('Error removing file: ' + error.code);
}

/*
 * Deze functie wordt aangeroepen wanneer het geluid succesvol is opgenomen.
 */
var captureSuccess = function(mediaFiles) {
    for (i = 0, len = mediaFiles.length; i < len; i += 1) {
    	mediaFile = mediaFiles[i];
    	// mediaFile is a FileEntrie; an object that represnets a file on a file system.
    	// get the path
		}
        // remove the mediaFile
        mediaFile.remove(removeSuccess, removeFail);
   
};

/*
 * Deze functie wordt aangeroepen wanneer het geluid niet is opgenomen. De gebruiker krijgt dan een foutmelding.
 */
var captureError = function(error) {
    navigator.notification.alert('Error code: ' + error.message, null, 'Capture Error');
};
