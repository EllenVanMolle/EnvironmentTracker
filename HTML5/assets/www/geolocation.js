/*
 * Geolocation
 */

/*
 * De functie die wordt aangeroepen door op de save button te klikken in stepone, steptwo en stepthree.
 * De functie bepaalt de locatie van de gebruiker en slaat deze gegevens vervolgens op in de database. 
 */
function geolocation(){
	
	// zet de layout van de datacollectiepagina's terug naar de oorspronkelijke toestand
   	resetLayout();
	
	// verander de tekst op de homepage naar Thank you!
	var text = document.getElementById("homepageText");
        text.innerHTML = "Thank you !";
        
	database.getGPSSettings();
}