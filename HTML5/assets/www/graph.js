/**
 * Representation of the results
 */

/*
 * Functie die de grafieken cre�ert
 */
function openChart(HueData, UnhappyData, FineData, HappyData, WdMoodData, SatData, BrightData){
	
	// initialiseer variabelen voor alle grafieken die zullen worden opgesteld.
	var chartHue;
	var chartUnhappy;
	var chartFine;
	var chartHappy;
	var chartWd;
	var chartSat;
	var chartBright;
	
	var width = window.innerWidth;
	var heigth = window.innerHeigth;
	var graphWidth= width-30;
	var graphHeigth= heigth-100;
	
/*
 * eerste grafiek, barchart die voor elke hue categorie de gemiddelde mood geeft.
 */
      chartHue = new Highcharts.Chart({
         chart: {
            renderTo: 'graphHue', // container in html waar de grafiek moet komen
            type: 'column', // barchart
            width: graphWidth,
            heigth: graphHeigth // breedte en lengte van de grafiek
         },
         title: {
            text: 'Mood versus Colors'
         },
         subtitle: {
                text: 'Influence of colors on your mood'
         },
         xAxis: {
            categories: ['Vigorous', 'Nature', 'Ocean', 'Flower']
         },
         yAxis: {
            title: {
            	min: 1,
            	max: 10,
               text: 'mood'
            }
         },
         legend: {
                enabled: false
            },
         tooltip: { // als de gebruiker op een kolom klikt komt dit stukje tekst te voorschijn
                formatter: function() {
                	var mood;
                	if (this.y <=3 )
                	{mood = 'Unhappy'}
                	else if (this.y <8)
                	{mood= 'Fine'}
                	else {mood= 'Happy'};
                    return 'When '+
                        this.x +' colors prevail you feel '+ mood +' !';
                }
            },
         plotOptions: {
         	column: { // toon voor elke kolom wat de waarde is.
         		dataLabels: {
         			enabled: true
         		}
         	},
            series: {
                colorByPoint: true
            }
        },
         series: [{
           data: HueData
         }]
      });
      // einde eerste grafiek

/*
 * tweede grafiek, piechart die de verdeling van de hue categorie�n geeft wanneer de gebruiker ongelukkig is.
 */
      chartUnhappy = new Highcharts.Chart({
            chart: {
            	width: graphWidth,
            	heigth: graphHeigth, // breedte en lengte van de grafiek
                renderTo: 'graphUnhappy',  // container in html waar de grafiek moet komen
              	plotBorderWidth: null, // geen rand
              	plotShadow: false // geen schaduw
            },
            title: {
                text: 'Colors when you are Unhappy'
            },
            tooltip: {
                pointFormat: '{series.name}: <b>{point.percentage}%</b>',
                percentageDecimals: 1
            },
            plotOptions: {
                pie: {
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: false,
                        }
                    }
            },
            series: [{
                type: 'pie',
                name: 'Tint share',
                data: [
                    ['Vigorous', UnhappyData[0]],
                    ['Nature', UnhappyData[1]],
                   	{	name: 'Ocean', 
                    	y: UnhappyData[2],
                        sliced: true,
                        selected: true
                    },
                    ['Flower', UnhappyData[3]],
                ]
         }]
    });

/*
 * derde grafiek, piechart die de verdeling van de hue categorie�n geeft wanneer de gebruiker ok� is.
 */   
    chartFine = new Highcharts.Chart({
            chart: {
            	width: graphWidth,
            	heigth: graphHeigth,// breedte en lengte van de grafiek
                renderTo: 'graphFine',  // container in html waar de grafiek moet komen
               	plotBorderWidth: null, // geen rand
                plotShadow: false // geen schaduw
            },
            title: {
                text: 'colors when you are Fine'
            },
            tooltip: {
                pointFormat: '{series.name}: <b>{point.percentage}%</b>',
                percentageDecimals: 1
            },
            plotOptions: {
                pie: {
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: false,
                    }
                }
            },
            
            series: [{
                type: 'pie',
                name: 'Tint share',
                data: [
                    ['Vigorous', FineData[0]],
                    ['Nature', FineData[1]],
                   	{	name: 'Ocean', 
                    	y: FineData[2],
                        sliced: true,
                        selected: true
                    },
                    ['Flower', FineData[3]],
                ]
         }]
    });

/*
 * vierde grafiek, piechart die de verdeling van de hue categorie�n geeft wanneer de gebruiker gelukkig is.
 */    
    chartHappy = new Highcharts.Chart({
            chart: {
            	width: graphWidth,
            	heigth: graphHeigth, // breedte en lengte van de grafiek
                renderTo: 'graphHappy',  // container in html waar de grafiek moet komen
               	plotBorderWidth: null, // geen rand
                plotShadow: false // geen schaduw
            },
            title: {
                text: 'Colors when you are Happy'
            },
            tooltip: {
                pointFormat: '{series.name}: <b>{point.percentage}%</b>',
                percentageDecimals: 1
            },
            plotOptions: {
                pie: {
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: false,
                    }
                }
            },
            series: [{
                type: 'pie',
                name: 'Tint share',
                data: [
                    ['Vigorous', HappyData[0]],
                    ['Nature', HappyData[1]],
                   	{	name: 'Ocean', 
                    	y: HappyData[2],
                        sliced: true,
                        selected: true
                    },
                    ['Flower', HappyData[3]],
                ]
         }]
    });

/*
 * vijfde grafiek, horizontale barchart die de gemiddelde mood geeft van de gebruiker voor elke dag van de week.
 */  
    chartWd = new Highcharts.Chart({
         chart: {
        	width: graphWidth,
        	heigth: graphHeigth, // breedte en lengte van de grafiek
            renderTo: 'graphWd',  // container in html waar de grafiek moet komen
            type: 'bar'
         },
         title: {
            text: 'Mood versus Days of the week'
         },
         xAxis: {
            categories: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
         },
         yAxis: {
            title: {
            	min: 1,
            	max: 10,
               text: 'mood'
            }
         },
         legend: {
                enabled: false
            },
         tooltip: {
                formatter: function() {
                	var mood;
                	if (this.y <=3 )
                	{mood = 'Unhappy'}
                	else if (this.y <8)
                	{mood= 'Fine'}
                	else {mood= 'Happy'};
                    return 'on '+
                        this.x + ' you feel '+ mood +' !';
                }
           }, 
         plotOptions: {
         	bar: {
         		dataLabels: {
         			enabled: true
         		}
         	},
            series: {
                colorByPoint: true
            }
        },
        
         series: [{
            data: [WdMoodData[1], WdMoodData[2], WdMoodData[3], WdMoodData[4], WdMoodData[5], WdMoodData[6], WdMoodData[0]]
         }]
      });

/*
 * zesde grafiek, horizontale barchart die de gemiddelde brightness geeft voor elke gemoedstoestand.
 */     
    chartBright = new Highcharts.Chart({
        chart: {
       		width: graphWidth,
       		heigth: graphHeigth, // breedte en lengte van de grafiek
       		renderTo: 'graphBright',  // container in html waar de grafiek moet komen
       		type: 'bar'
        },
        title: {
           text: 'Mood versus Brightness of the colors'
        },
        xAxis: {
        	categories: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10']
        },
        yAxis: {
        	title: {
        	min: 1,
        	max: 100,
           text: 'brightness'}
        },
        legend: {
               enabled: false
           },
        tooltip: {
               formatter: function() {
               		var brightness;
               		if (this.y < 33) {
               			brightness = "low"
               		} else if (this.y < 66){
               			brightness = "average"
               		} else {
               			brightness =  "high"
               		};
                   return 'if the brightness is '+
                       brightness + ' your mood is '+ this.x +' !';
               }
          }, 
        plotOptions: {
        	bar: {
        		dataLabels: {
        			enabled: true
        		}
        	},
           series: {
               colorByPoint: true
           }
       },
       
        series: [{
           data: BrightData
        }]
     });

/*
 * zevende grafiek, horizontale barchart die de gemiddelde brightness geeft voor elke gemoedstoestand.
 */  
    chartSat = new Highcharts.Chart({
        chart: {
       		width: graphWidth,
       		heigth: graphHeigth, // breedte en lengte van de grafiek
       		renderTo: 'graphSat',  // container in html waar de grafiek moet komen
       		type: 'bar'
        },
        title: {
           text: 'Mood versus Saturation of the colors'
        },
        xAxis: {
        	categories: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10']
        },
        yAxis: {
        	title: {
        	min: 1,
        	max: 100,
           text: 'saturation'}
        },
        legend: {
               enabled: false
           },
        tooltip: {
               formatter: function() {
                   var saturation;
               		if (this.y < 33) {
               			saturation = "low"
               		} else if (this.y < 66){
               			saturation = "average"
               		} else {
               			saturation = "high"
               		};
                   return 'if the saturation is '+
                       saturation + ' your mood is '+ this.x +' !';
               }
          }, 
        plotOptions: {
        	bar: {
        		dataLabels: {
        			enabled: true
        		}
        	},
           series: {
               colorByPoint: true
           }
       },
       
        series: [{
           data: SatData
        }]
     });

}