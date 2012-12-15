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
	
	var src = 'file:///myrecording.wav';
    var mediaRec = new Media(src, onSuccess, onError);

    // Record audio
    mediaRec.startRecord();

    // Stop recording after 5 sec
    var recTime = 0;
    // The function to execute every sec for 5 sec.
    var recInterval = setInterval(function() {
        recTime = recTime + 1;
        setAudioPosition(recTime);
        if (recTime >= 5) {
            clearInterval(recInterval);
            mediaRec.stopRecord();
            document.getElementById('audio_position').innerHTML = "Finished";
            mediaRec.release();
        }
    }, 1000);
}

// onSuccess Callback Recording
//
function onSuccess() {
    console.log("recordAudio():Audio Success");
    //window.resolveLocalFileSystemURI('file:///myrecording.wav', onResolveSuccess, onResolveFail);
}
/*
// onSuccess resolving URI
//
function onResolveSuccess(fileEntry) {
    console.log(fileEntry.name);
    console.log("File is opened succesfull.")
    fileEntry.remove(succesfullRemove, failedRemove);
}

// onError resolving URI
//
function onResolveFail(evt) {
	console.log("Resolving audio file failed.")
	console.log("there was an error: " + JSON.stringify(evt));
}

// onSuccess remove Callback
//
function succesfullRemove() {
	console.log("Removed succesfull");
}

// onFail remove Callback
//
function failedRemove() {
	console.log("Failed remove");
}

// onError Callback 
//
function onError(error) {
    alert('code: '    + error.code    + '\n' + 
          'message: ' + error.message + '\n');
}
*/
// Set audio position
// 
function setAudioPosition(position) {
    document.getElementById('audio_position').innerHTML = 5 - position;
}