
function sliderChange() {
		
		var mood = $("#moodSlider").val();
		var val;
		var newWidth;
		var mouth = $("#mouth");
			
		if (mood > 5) {
		val = 10-mood;
		newWidth = 90 + (val*20);
       
       	mouth.css({
       	top				:"auto",
       	bottom			: val*4,
       	width           : newWidth,
        height          : newWidth,
        "border-radius" : newWidth / 2,
        left            : 3-(val*10)
       })
       .removeClass("straight");
					
		}
		else if (mood == 5 ) {
           
       		mouth.addClass("straight");
       
     	}  
     	else {     	
       	newWidth = 90 + (mood*20);
			
		mouth.css({
		top				: mood*4,
		bottom			:"auto",
		width			: newWidth,
		height          : newWidth,
        "border-radius" : newWidth / 2,
        left 			: 3-(mood*10)
        })
		.removeClass("straight");
			
       
		}
	}