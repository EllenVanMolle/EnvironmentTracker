/**
 * Recording of sound
 */


/*
 * Deze functie past de layout aan van de datacollectiepagina's 
 * en neemt het geluid op, analyseert het en verwijdert het.
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
	
	// Aanmaken van een nieuwe audio file (noodzakelijk voor iOS)
	window.requestFileSystem(LocalFileSystem.PERSISTENT, 0, onSuccessRequestingFileSystem, onErrorRequestingFileSystem);


// on Success Requesting File System Callback
//
function onSuccessRequestingFileSystem(filesystem) {
	// Create a new recording file in the root directory
	filesystem.root.getFile("myRecording.wav", {create: true, exclusive: true}, onSuccessCreateFile, onFailCreateFile);
}

// on Success Creating File Callback
//
function onSuccessCreateFile(fileEntry) {
	fileEntry.file(onSuccessFileInformation, onErrorFileInformation);
}

// on Success Requesting File Information
//
function onSuccessFileInformation(file) {
	var src = file.fullpath;
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

//on Success Callback Recording
//
function onSuccess() {
    console.log("recordAudio():Audio Success");
    window.resolveLocalFileSystemURI("file:///sdcard/myrecording.wav", onResolveSuccess, onResolveFail);
}

// onSuccess resolving URI
//
function onResolveSuccess(fileEntry) {
    console.log(fileEntry.name);
    console.log("File is opened succesfull.")
    fileEntry.remove(succesfullRemove, failedRemove);
}

// on Error Requesting File System Callback
//
function onErrorRequestingFileSystem(error) {
	console.log("Couldn't find file system");
}

// on Error Creating File Callback
//
function onFailCreateFile(error) {
	console.log("Failed to create or retrieve file: " + error.code);
}

// on Error Requesting File Information
//
function onErrorFileInformation(error) {
	console.log("Couldn't find file information.")
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

// onError make Media object Callback 
//
function onError(error) {
    alert('code: '    + error.code    + '\n' + 
          'message: ' + error.message + '\n');
}

// Set audio position
// 
function setAudioPosition(position) {
    document.getElementById('audio_position').innerHTML = 5 - position;
}
}