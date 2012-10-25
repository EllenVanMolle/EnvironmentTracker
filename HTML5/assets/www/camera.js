//Het linken van de knop Take a picture met de methode takeAPicture
$('#buttonTakeAPicture').click(takeAPicture)


/*Deze functie wordt aangesproken wanneer de gebruiker in stap 3 op de knop 'Take A picture' drukt. Deze functie
 * moet er voor zorgen dat de foto gemaakt wordt, tijdelijk opgeslagen en verwerkt wordt en nadien terug verwijderd
 * wordt.*/
function takeAPicture() {
	navigator.camera.getPicture(onSuccess, onFail, { quality: 50, 
	    destinationType: Camera.DestinationType.FILE_URI });
}

/*
 * Deze functie wordt aangesproken als het nemen van de foto succesvol is.
 */
function onSuccess(imageURI) {
    var image = document.getElementById('myImage');
    image.src = imageURI;
    localStorage.setItem("placeImage", image.src);
    alert(localStorage.getItem("placeImage"));
    //analyseImage;
}

/*
 * Deze functie wordt aangesproken als het nemen van de foto niet gelukt is. De gebruiker krijgt dan een foutmelding.
 */
function onFail(message) {
    alert('Failed because: ' + message);
}

/*
 * Deze functie moet de kleuren van de foto analyseren. Deze werkt nog niet.
 */
function analyseImage() {
	alert('analyseImage');
	//var img = new Image();
	//img.src = localStorage.getItem("placeImage");
	//var context = document.getElementById('canvas').getContext('2d');
	//context.drawImage(img, 0, 0);
	//var x=5;
	//var y=5;
	//alert('voor getImageData');
	//data = context.getImageData(x, y, 1, 1).data;
	//alert(data);
}