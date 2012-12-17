/*
 * Moodslider
 */

/*
 * Functie die wordt aangesproken op het ogenblik dat de slider van waarde verandert.
 * De functie beschrijft hoe het gezichtje gelukkiger wordt als de mood groter wordt en
 * verdrietiger als de mood kleiner wordt. Daarnaast wordt ook de tekst boven de smiley
 * aangepast; Unhappy, Fine, Happy afhankelijk van de waarde van de mood.
 */
function sliderChange() {
		
		//intialiseer enkele variabelen
		var mood = $("#moodSlider").val(); // stel mood gelik aan de waarde van de moodslider in de html pagina
		var val;
		var newWidth;
		var mouth = $("#mouth"); // koppel mouth met een item mouth uit de html pagina
		var text = document.getElementById("moodText"); // koppel text met een item moodtext uit de html pagina
		
		// zet de tekst van moodText afhankelijk van de waarde van de mood
		if(mood > 7) { //als de mood groter is dan 7
			text.innerHTML = "Happy";
		}else if (mood < 4){ //als de mood kleiner is dan 4
			text.innerHTML = "Unhappy";
		}else{ //als de mood tussen 7 en 4 ligt.
			text.innerHTML = "Fine";
		};
		
		// zet de mond van de smiley afhankelijk van de waarde van de mood
		if (mood > 5) { //als de mood groter is dan 5	
		val = 10-mood;
		newWidth = 90 + (val*20);
       
       	mouth.css({ 
       	// Pas het css document aan zodat glimlach breder wordt als mood groter wordt.
       	top				:"auto",
       	bottom			: val*4,
       	width           : newWidth,
        height          : newWidth,
        "border-radius" : newWidth / 2,
        left            : 3-(val*10)
       })
       .removeClass("straight"); // Verwijder de standaard neutrale, rechte mond.
					
		}
		else if (mood == 5 ) {//als de mood gelijk is aan 5
           
       		mouth.addClass("straight"); // Zet de standaard neutrale, rechte mond.
       
     	}  
     	else {//als de mood kleiner is dan 5  	
       	newWidth = 90 + (mood*20);
			
		mouth.css({
		// Pas het css document aan zodat glimlach omgekeerd staat en breder wordt als mood kleiner wordt.
		top				: mood*4,
		bottom			:"auto",
		width			: newWidth,
		height          : newWidth,
        "border-radius" : newWidth / 2,
        left 			: 3-(mood*10)
        })
		.removeClass("straight"); // verwijder de standaard neutrale, rechte mond.
		
		}
	}