/*
 * Geolocation
 */

/*
 * De functie die wordt aangeroepen door op de save button te klikken in stepone, steptwo en stepthree.
 * De functie bepaalt de locatie van de gebruiker en slaat deze gegevens vervolgens op in de database. 
 */
function geolocation(){
	
	// verander de tekst op de homepage naar Thank you!
	var text = document.getElementById("homepageText");
        text.innerHTML = "Thank you !";
        	
        	var timeoutVal = 5000;// we wachten max 5 sec
        	var maxAgeVal = 0;
        	if (navigator.geolocation) {// Het is mogelijk om geolocatie te doen.
          		navigator.geolocation.getCurrentPosition(onSuccess, onError, 
            	{enableHighAccuracy: false, timeout: timeoutVal, maximumAge: maxAgeVal}); // Voer geolocatie uit met weinig accuraatheid
            	// note als enableHighAccuracy op true staat werkt de geolocatie niet op een android
        	}
        	else { // Het is niet mogelijk om aan geolocatie te doen omdat dit niet ondersteund wordt.
          		console.log("Geolocation is not supported."); // Geef dit weer in de logs
        	}

        /*
         * Functie die aangeroepen wordt als het localiseren van de gebruiker succesvol was.
         * De locatie wordt opgeslagen in de environmentData tabel van de TrackerDb.
         */				
        function onSuccess (position) {
        	var latitude = position.coords.latitude; // breedteligging
        	var longitude = position.coords.longitude; // lengteligging
        	
        	alert("uw locatie: breedteligging: "+latitude+" en lengteligging: "+longitude);
        	saveObservation(latitude, longitude);
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
        	saveObservation(latitude, longitude);
        	
        }
}