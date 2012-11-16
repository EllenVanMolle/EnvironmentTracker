/*
 * Taking a picture
 */

/*
 * Deze functie wordt aangesproken wanneer de gebruiker in stap 2 op de knop 'Take A picture' drukt. Deze functie
 * moet er voor zorgen dat de foto gemaakt wordt, tijdelijk opgeslagen en verwerkt wordt en nadien terug verwijderd
 * wordt.
 */
function takeAPicture() {
	navigator.camera.getPicture(onSuccess, onFail, { quality: 50, 
	    destinationType: Camera.DestinationType.DATA_URL });
}

/*
 * Deze functie wordt aangesproken als het nemen van de foto succesvol is.
 */
function onSuccess(imageData) {
	//var image = document.getElementById('myImage');
	/*alert('image received');
	/*image.src = "data:image/jpeg;base64," + imageData;
	/*image.src = imageURI// ;
    localStorage.setItem("placeImage", image.src);
    alert(localStorage.getItem("placeImage"));*/
   	pictureAnalysis(); // analyseer de foto
   }

/*
 * Deze functie wordt aangesproken als het nemen van de foto niet gelukt is.
 * De gebruiker krijgt dan een foutmelding.
 */
function onFail(message) {
    alert('Failed because: ' + message);  
}
 