/*
 * het analyseren van de foto
 */

/*
 * Methode om een canvas te creëren om vervolgens de pixeldata van de afbeelding op te vragen
 */
function colorAnalysis(imageSource){ 
	
	console.log("start");
    
	// Link de variable canvas met een item MyCanvas uit de htmlfile
	var canvas = document.getElementById('MyCanvas');
   
   	// check of een canvas ondersteund wordt
    if (canvas.getContext){
  		var ctx = canvas.getContext('2d');
  		
  		//	creëer een nieuw afbeelding element
  		var img = new Image();
  		
  		// volgende functie wordt uitgevoerd bij het laden van de afbeelding
		img.onload = function(){
				
			var maxWidth = window.innerWidth;
			var maxHeight = window.innerHeight;
			var imgWidth = img.width;
			var imgHeight = img.height;
			var ratio = 1;
				
			if(imgWidth > maxWidth)
           	ratio = maxWidth / imgWidth;
        	else if(imgHeight > maxHeight)
           	ratio = maxHeight / imgHeight;
            	
           	var Width = imgWidth*ratio;
           	var Height = imgHeight*ratio;
  				
			canvas.width = Width;
			canvas.height= Height;
  			
  			// teken de afbeelding in het canvas	
  			ctx.drawImage(img,0,0); // execute drawImage statements here
  			
  			// haal de data van de afbeelding uit de context	
  			var imageData = ctx.getImageData(0, 0, Width, Height);	
			
			// voer de pixalAnalyse methode uit met de gevonden data
			analysisOfPixels (imageSource, imageData.data, Width, Height);
		};
		// definieer de source van de afbeelding
		//img.src = localStorage.getItem("placeImage"); 
		img.src = imageSource;
      		
	}
	else {alert("Canvas is not supported.") // canvas wordt niet ondersteund
	}
    
   
}

/*
 * Methode om de kleuren van de pixels te analyseren
 */
function analysisOfPixels(imageSource, Data, Width, Height){
	
	// initialiseren van enkele variabelen
	var nrPixelsHueCategory = [0, 0, 0, 0];
	var totalSat = 0;
	var totalBr = 0;
					
		// loop door elke rij
		for (var j = 0; j < Height; j++)
		{
   			//loop door elke kolom
     		for (var i = 0; i < Width; i++)
     		{ 	
     			var index = (j * Width + i) * 4;
      			var red = Data[index];	// red
      			var green = Data[++index];	// green
        		var blue = Data[++index];  //blue
       			
       			// zet de rgb waarden om naar hsv waarden
       			var hsv = rgb2hsv (red, green, blue);
         		
         		// Bepaal tot welke hue categorie een pixel behoort		
         		if (hsv[0] <= 90) // verhoog het aantal pixel is in de vigorous categorie met 1
         		nrPixelsHueCategory[0] += 1;
         		else if (hsv[0] <= 180) // verhoog het aantal pixel is in de nature categorie met 1
         		nrPixelsHueCategory[1] += 1;
         		else if (hsv[0] <= 270) // verhoog het aantal pixel is in de ocean categorie met 1
         		nrPixelsHueCategory[2] += 1;
         		else if (hsv[0] <= 360) // verhoog het aantal pixel is in de flower categorie met 1
         		nrPixelsHueCategory[3] += 1;
         		else {console.log('hue is incorrect')};
         		// als het pixel tot geen enkele categorie behoort log dat de hue waarschijnlijk incorrect is.
         		
         		// verhoog de totale saturatie met de saturatie van deze pixel		
         		totalSat += hsv[1];
         		// verhoog de totale brightness met de brightness van deze pixel		
         		totalBr += hsv[2];  
         	}
        }
         
        // bepaal het totale aantal pixel door lengt maal breedte te doen	
        var totalPixels = Width * Height;
        // bepaal het hoogste pixel in een hue category
        var MaxPixels = Math.max.apply(null, nrPixelsHueCategory);
       
       	// initialiseer een nieuwe variabele 
        var HueClass = 0;
        
        // loop door alle klassen (0-3)	
        for (var n=0; n < 4; n++ )
        {	
        	if (nrPixelsHueCategory[n] == MaxPixels) // vergelijk het aantal pixels in elke klasse met de maximumwaarde
         	{// als het maximum gelijk is aan het aantal pixels in die klasse, stel hueclass gelijk aan die categorie
         		HueClass = (n+1);
         	}
  		/*als twee categorieën allebij evenveel pixels hebben en dit aantal het maximum is over de vier categorieën, 
  		 * wordt de categorie met het hoogste nummer opgeslagen als HueClass. Merk op dat de kans dat zo'n situatie zich voordoet heel klein is.
  		 */
        }
        console.log(HueClass);
        
        // Bereken de procentuele waarde van de saturatie en de brightness en rond deze af op een eenheid. 	
        var Saturation = Math.round((totalSat / totalPixels)*100);
        var Brightness = Math.round((totalBr / totalPixels)*100);
        console.log(Saturation + ' ' + Brightness);
        
        // geef de waarde van de HueClass, Staturation and Brightness door aan de overeenstemmende span elementen
        var hueClass = $("#HueClass");
        var saturation = $("#Saturation");
        var brightness = $("#Brightness");
        
		hueClass.text (HueClass); 
        saturation.text (Saturation);
        brightness.text (Brightness);
        
        console.log (hueClass.text() +' en ' + saturation.text() + ' en '+ brightness.text());
        analysisIsFinished = true;
        //console.log(pixData);
         console.log("Start removal image file.");
    
    // Removing picture again
   window.resolveLocalFileSystemURI(imageSource, onResolveSuccess, onResolveFail);
	
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
		console.log("Resolving image file failed.")
		console.log("there was an error: " + JSON.stringify(evt));
	}
	
	// onSuccess remove Callback
	//
	function succesfullRemove() {
		console.log("Removed image succesfull");
	}
	
	// onFail remove Callback
	//
	function failedRemove() {
		console.log("Failed image remove");
	}
}


/*
 * Methode om de rgb waarden om te zetten naar hsvwaarden.
 */
function rgb2hsv (r,g,b) {
 var computedH = 0;
 var computedS = 0;
 var computedV = 0;

 //remove spaces from input RGB values, convert to int
 var r = parseInt( (''+r).replace(/\s/g,''),10 ); 
 var g = parseInt( (''+g).replace(/\s/g,''),10 ); 
 var b = parseInt( (''+b).replace(/\s/g,''),10 ); 

 if ( r==null || g==null || b==null ||
     isNaN(r) || isNaN(g)|| isNaN(b) ) {
   alert ('Please enter numeric RGB values!');
   return;
 }
 
 if (r<0 || g<0 || b<0 || r>255 || g>255 || b>255) {
   alert ('RGB values must be in the range 0 to 255.');
   return;
 }
 
 r=r/255; g=g/255; b=b/255;
 var minRGB = Math.min(r,Math.min(g,b));
 var maxRGB = Math.max(r,Math.max(g,b));

 // Black-gray-white
 if (minRGB==maxRGB) {
  computedV = minRGB;
  return [0,0,computedV];
 }

 // Colors other than black-gray-white:
 var d = (r==minRGB) ? g-b : ((b==minRGB) ? r-g : b-r);
 var h = (r==minRGB) ? 3 : ((b==minRGB) ? 1 : 5);
 computedH = 60*(h - d/(maxRGB - minRGB));
 computedS = (maxRGB - minRGB)/maxRGB;
 computedV = maxRGB;
 return [computedH,computedS,computedV]; 
 
 }    