/*
 * Taking a picture
 */

/*
 * Deze functie wordt aangesproken wanneer de gebruiker in stap 2 op de knop 'Take A picture' drukt. Deze functie
 * moet er voor zorgen dat de foto gemaakt wordt, tijdelijk opgeslagen en verwerkt wordt en nadien terug verwijderd
 * wordt.
 */
function takeAPicture() {
	analysisIsFinished = false;
	navigator.camera.getPicture(onSuccess, onFail, { quality: 50, 
		 destinationType: Camera.DestinationType.FILE_URI});
}

/*
 * Deze functie wordt aangesproken als het nemen van de foto succesvol is.
 */
function onSuccess(imageURI) {
	// sla de foto locaal op
	//localStorage.setItem("placeImage", imageURI);
	// analyseer de foto
	//imageSrc =  'images/Autumn_Leaves.jpg'
	imageSrc = imageURI;
	colorAnalysis(imageSrc);
   }

/*
 * Deze functie wordt aangesproken als het nemen van de foto niet gelukt is.
 * De gebruiker krijgt dan een foutmelding.
 */
function onFail(message) {
	// log de reden waarom het nemen van de foto gefaald heeft.
    console.log('Failed because: ' + message);  
    // geef een 0 waardes aan alle fotovariabelen
    analysisIsFinished = true;
}