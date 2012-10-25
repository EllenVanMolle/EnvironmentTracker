// Het linken van de knop Start met de methode recordNoise.
$('#buttonStart').click(recordNoise)

/*
 * Deze functie neemt het geluid op, analyseert het en verwijdert het. (Enkel het opnemen van het geluid en 
 * het opslaan gebeurt nu.
 */
function recordNoise() {
	//Start het opnemen van het geluid. Je mag maximum 1 clip opnemen van maximum 5 sec.
	var options = {limit:1,duration:5};
	navigator.device.capture.captureAudio(captureSuccess, captureError, options);
}

/*
 * Deze functie wordt aangeroepen wanneer het geluid succesvol is opgenomen.
 */
var captureSuccess = function(mediaFiles) {
    for (i = 0, len = mediaFiles.length; i < len; i += 1) {
        path = mediaFiles[i].fullPath;
        // do something interesting with the file
        alert(path);
    }
};

/*
 * Deze functie wordt aangeroepen wanneer het geluid niet is opgenomen. De gebruiker krijgt dan een foutmelding.
 */
var captureError = function(error) {
    navigator.notification.alert('Error code: ' + error.message, null, 'Capture Error');
};