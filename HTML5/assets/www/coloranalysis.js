

function pictureAnalysis(){ 
	var canvas = document.getElementById('MyCanvas');
    
    if (canvas.getContext){
  		var ctx = canvas.getContext('2d');
  			
  		var img = new Image();   // Create new img element
		img.onload = function(){
				
			var maxWidth = window.innerWidth;
			var maxHeight = window.innerHeight
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
  				
  			ctx.drawImage(img,0,0); // execute drawImage statements here
  				
  			var imageData = ctx.getImageData(0, 0, Width, Height);	

			pixalAnalysis (imageData.data, Width, Height);
		};
		img.src = 'images/Autumn_Leaves.jpg'; 
      		
	}
	else {alert("no canvas")
 		 // canvas-unsupported code here
	}	
}

function pixalAnalysis(Data, Width, Height){
	
	var nrPix = [0, 0, 0, 0];
	var totalS = 0;
	var totalB = 0;
					
		// loop through each row
		for (var j = 0; j < Height; j++)
		{
   			//loop through each column
     		for (var i = 0; i < Width; i++)
     		{ 	
     			var index=(j * Width + i) * 4;
      			var red = Data[index];	// red
      			var green = Data[++index];	// green
        		var blue = Data[++index];  //blue
       			
       			var hsv = rgb2hsv (red, green, blue);
         				
         		if (hsv[0] <= 90)
         		nrPix[0] = nrPix[0] + 1;
         		else if (hsv[0] <= 180)
         		nrPix[1] = nrPix[1] + 1;
         		else if (hsv[0] <= 270)
         		nrPix[2] = nrPix[2] + 1;
         		else if (hsv[0] <= 360)
         		nrPix[3] = nrPix[3] + 1;
         		else {alert('hue is incorrect')};
         				
         		totalS = totalS + hsv[1];
         				
         		totalB = totalB + hsv[2];  
         	}
        }
         	
        var totalPix = Width * Height;
        var percentPix =[0, 0, 0, 0];
        var MaxPix = Math.max.apply(null, nrPix);
        
        var HueClass = 0;
         	
        for (var n=0; n < 4; n++ )
        {	
        	if (nrPix[n] == MaxPix)
         	{HueClass = (n+1);}
         	percentPix[n] = (nrPix[n]/totalPix)*100;
        }
         	
        var Saturation = (totalS / totalPix)*100;
        var Brightness = (totalB / totalPix)*100;
        pixData = [HueClass, percentPix[0], percentPix[1], percentPix[2], percentPix[3], Saturation, Brightness];
}


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
    