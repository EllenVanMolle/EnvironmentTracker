//
//  EnvironmentTrackerModel.m
//  EnvironmentTracker
//
//  Created by student on 06/11/12.
//  Copyright (c) 2012 multimedia. All rights reserved.
//

#import "EnvironmentTrackerModel.h"
#import "Observation.h"

@implementation EnvironmentTrackerModel 

@synthesize cameraImage = _cameraImage;
@synthesize analysisQueue = _analysisQueue;

@synthesize biggestHueCategory = _biggestHueCategory;
@synthesize avgSaturation = _avgSaturation;
@synthesize avgBrightness = _avgBrightness;

@synthesize hueHappy = _hueHappy;
@synthesize hueFine = _hueFine;
@synthesize hueUnhappy = _hueUnhappy;
@synthesize hueMood = _hueMood;
@synthesize saturationMood = _saturationMood;
@synthesize brightnessMood = _brightnessMood;
@synthesize dayOfWeekMood = _dayOfWeekMood;

@synthesize database = _database;
@synthesize context = _context;

/* Deze methode zorgt dat de context wordt gedefinieerd.
 */
-(void)databaseIsReady {
    NSLog(@"Database is ready");
    if (self.database.documentState == UIDocumentStateNormal){
        self.context = self.database.managedObjectContext;
        NSLog(@"Context is succesvol gedefinieerd");
    } else {
        NSLog(@"Database not ready");
    }
}

/* Deze methode zorgt er voor dat je de database kan gebruiken.
 */
- (void) useDocument {
    if (![[NSFileManager defaultManager] fileExistsAtPath:[self.database.fileURL path]]) {
        // Het bestaat nog niet op de schijf, dus moeten ze het aanmaken.
        NSLog(@"De database bestaat nog niet.");
        [self.database saveToURL:self.database.fileURL forSaveOperation:UIDocumentSaveForCreating completionHandler:^(BOOL success) {
            if (success) {
                // De database is succesvol aangemaakt.
                NSLog(@"De database is succesvol aangemaakt.");
                [self databaseIsReady];
            }
            if (!success) NSLog(@"De database is niet opgeslagen.");
        }];
    } else if (self.database.documentState == UIDocumentStateClosed) {
        // Het bestaat op de schijf maar het moet nog geopend worden.
        NSLog(@"De database moet nog geopend worden.");
        [self.database openWithCompletionHandler:^(BOOL success) {
            if (success) {
                NSLog(@"De database is succesvol geopend.");
                [self databaseIsReady];
            }
            if (!success) {
                NSLog(@"De database kan niet geopend worden.");
            }
        }];
    } else if (self.database.documentState == UIDocumentStateNormal) {
        // De database is klaar om te gebruiken.
        NSLog(@"De database is klaar om te gebruiken.");
        if (!self.context) {
            [self databaseIsReady];
        }
    }
}

/* Overschrijf de setter van de database, wanneer de database nog niet bestond, roep de methode useDocument aan.
 */
- (void) setDatabase:(UIManagedDocument *)database {
    if (_database != database) {
        _database = database;
        [self useDocument];
    }
}


/* Deze methode wordt aangeroepen vooraleer de database wordt gebruikt.
 */
-(void)openDatabase {
    // Als de database nog niet is aangemaakt, creeer hem.
    if (!self.database) {
        // Slaag de database op in de home/DatabaseEnvironment.
        NSURL *url = [[[NSFileManager defaultManager] URLsForDirectory:NSDocumentDirectory inDomains:NSUserDomainMask] lastObject];
        url = [url URLByAppendingPathComponent:@"TrackerDatabase"];
        self.database = [[UIManagedDocument alloc] initWithFileURL:url]; // setter wordt hoger overschreven
    }
}

/* Deze methode slaat de database op. 
 */
-(void)saveDatabase {
    [self.database saveToURL:self.database.fileURL
            forSaveOperation:UIDocumentSaveForOverwriting
           completionHandler:^(BOOL success) {
               if (!success) NSLog(@"failed to save document %@", self.database.localizedName);
           }];
}
 

/* Deze methode sluit de database. 
 */
-(void)closeDatabase {
    [self.database closeWithCompletionHandler:^(BOOL success) {
        if (!success) NSLog(@"failed to close document %@", self.database.localizedName);
    }];
}

/* Deze methode slaat een observatie op in de database. 
 */
-(void) addObservationToDatabase {
   
    NSManagedObjectContext *context = self.database.managedObjectContext;
    
    Observation *observation =
    [NSEntityDescription insertNewObjectForEntityForName:@"Observation"
                                  inManagedObjectContext:context];
    self.date = [NSDate date];
    observation.observationID = self.identifier;
    observation.mood = self.mood;
    observation.lengteligging = self.lengteligging;
    observation.breedteligging = self.breedteligging;
    observation.date = self.date;
    observation.hue = self.biggestHueCategory;
    observation.saturation = self.avgSaturation;
    observation.brightness = self.avgBrightness;
    
   NSNotificationCenter *center = [NSNotificationCenter defaultCenter];
    [center removeObserver:self
                      name:UIDocumentStateChangedNotification
                    object:self.database];
    
    NSLog(@"Observation saved to database.");
    
}

// Public methods

/* Sla de observatie op in de database met de gegevens die op dit moment zijn opgeslagen in het model. */
-(void) saveObservationToDatabase {
    // Nakijken of de database klaar is.
    if (self.database.documentState == UIDocumentStateNormal) {
        [self addObservationToDatabase];
    } else {
        // Als de database niet klaar is, wachten tot de database klaar is.
        NSNotificationCenter *center = [NSNotificationCenter defaultCenter];
        [center addObserver:self
                   selector:@selector(addObservationToDatabase)
                       name:UIDocumentStateChangedNotification
                     object:self.database];
    }
}


/* Deze methode geeft terug of de database klaar is om te gebruiken.
 */
-(BOOL) isDatabaseReady {
    return self.database.documentState == UIDocumentStateNormal;
}

/*
 Het opstarten van de notificaties. De gebruiker heeft het interval bepaald in zijn instellingen. 
 */
-(void)startUpNextNotification {
    NSLog(@"StartUpNextNotification");
    // We verwijderen eerst alle notificaties die nog eventueel aanwezig zijn.
    [[UIApplication sharedApplication] cancelAllLocalNotifications];
    
    /* We check if the user wants to recieve notifications from our app and how long the interval between two notifactions should be. To retrieve this information we use a NSUserDefaults object. */
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    BOOL notification = [defaults boolForKey:@"notification"];
    int notificationInterval = [defaults integerForKey:@"interval"];
    
    /* Note that boolean variabels are always initialised to false and integers to 0.
     So if the user hasn't set his settings yet he wont receive any notifications */
    
    if(notification){
        // if the user sets the interval to 0 or the usersettings are empty, we set the defaultinterval wich is 120
        if (notificationInterval == 0){
            notificationInterval = 120;
        }
        
        // Nakijken of de juiste classes ondersteund worden.
        Class cls = NSClassFromString(@"UILocalNotification");
        
        if (cls != nil) {
            // We maken een notificatie aan met een
            notificationInterval *= 60; // put the interval value in seconds
            
            UILocalNotification *notif = [[cls alloc] init];
            notif.fireDate = [NSDate dateWithTimeIntervalSinceNow:notificationInterval]; // in seconds
            notif.timeZone = [NSTimeZone defaultTimeZone];
        
            notif.alertBody = @"Time to record your mood";
            notif.alertAction = @"Record now";
            notif.soundName = UILocalNotificationDefaultSoundName;
            //Het huidige badgenummer wordt met 1 verhoogd.
            //notif.applicationIconBadgeNumber = 1 + [[UIApplication sharedApplication] applicationIconBadgeNumber];
        
            [[UIApplication sharedApplication] scheduleLocalNotification:notif];
        } else {
            NSLog(@"De notificaties worden niet ondersteund.");
        }
    }
}

// Macro that selects the maximum of three values
#define MAX3(a,b,c) ( MAX(a, MAX(b,c)) );

//Macro that selects the minimum of three values
#define MIN3(a,b,c) ( MIN (a, MIN(b,c)) );

//Macro that selects the maximum of four values
#define MAX4(a,b,c,d) ( MAX(a, MAX (b, MAX(c,d))) );

/* Method called in a dispatched queque to anayse the image 
 */
-(void)analyseImage {
    
    // sourceImage is the bitmap (rectangular array of pixels)image of the cameraImage
    CGImageRef sourceImage = self.cameraImage.CGImage;
    
    // a copy of the sourceImage data is created, the Data Provider Data objects abstract the data-access task and eliminate the need for applications to manage data through a raw memory buffer.
	CFDataRef theData;
	theData = CGDataProviderCopyData(CGImageGetDataProvider(sourceImage));
    
    // CFDataGetBytePtr returns a read-only pointer to the bytes of a CFData object in this case theData
	UInt8 *pixelData = (UInt8 *) CFDataGetBytePtr(theData);
    
    // CFDataGetLength returns the number of bytes contained by a CFData object in this case theData.
	int dataLength = CFDataGetLength(theData);
    
    // create variables for the total saturation, total brightness, total pixels
    float totalSat = 0;
    float totalBr = 0;
    
    // define an structs (array) for the total pixelcount for every huecat;
    typedef struct {int vigorous; int nature; int ocean; int flower;} pixelcount_hue;
    
    // create an instance of the pixelcount_hue type
    pixelcount_hue pixelcountHue = {0, 0, 0, 0 };
    
    // Loop through every pixel in pixelData until the end (index = dataLength)
	for (int index = 0; index < dataLength; index += 4) {
        
        // define two structs one for the rgb values and one for the hsv values
        typedef struct {float r; float g; float b;} rgb_color;
        typedef struct {float hue; float sat; float br;} hsv_color;
        
        // create an instance of the rgb_color type
        rgb_color rgb;
        
        //get pixelvalue red, green and blue (rgb value) for the current pixel and put them in he rgb array
        rgb.r = (pixelData[index])/255.0;
        rgb.g = (pixelData[index + 1])/255.0;
        rgb.b = (pixelData[index + 2])/255.0;
        
        // create an instance of the hsv_color type
        hsv_color hsv;
        
        // CGfloat is typedef for either float or double.
        CGFloat rgb_min, rgb_max, delta;
        
        // get min of r, g and b value
        rgb_min = MIN3(rgb.r, rgb.g, rgb.b);
        // get max of r, g, and, b value
        rgb_max = MAX3(rgb.r, rgb.g, rgb.b);
        // delta equals the difference between rgb_min and rgb_max
        delta = rgb_max-rgb_min;
        
        // the value for the brightness always equals the max rgb value
        hsv.br = rgb_max;
        
        // calculate saturation value
        if (rgb_max != 0.0){ // if the max rgb value is different from zero
            hsv.sat = delta /rgb_max;
        }
        else{
            hsv.sat = 0.0;
        }
        if (hsv.sat == 0.0) { // if the saturation is zero
            hsv.hue = 0.0;
        }
        else {
            if (rgb.r == rgb_max)// if the red-value is the max
                hsv.hue = (rgb.g - rgb.b) / delta;
            else if (rgb.g == rgb_max)// if the green-value is the max
                hsv.hue = 2 + (rgb.b - rgb.r) / delta;
            else if (rgb.b == rgb_max)// if the blue-value is the max
                hsv.hue = 4 + (rgb.r - rgb.r) / delta;
            
            hsv.hue *= 60.0;
            if (hsv.hue < 0)
                hsv.hue += 360.0;
        }
        
        
        // add a pixel to a pixelcount depending on the huevalue of the pixel
        if (hsv.hue >= 0 && hsv.hue <= 90){
            // if huevalue of pixel is smaller than 90, add one pixel to the vigorous pixelcount
            pixelcountHue.vigorous += 1;
        } else if (hsv.hue > 90 && hsv.hue <= 180){
            // if huevalue of pixel is bigger than 90 and smaller than 180, add one pixel to the nature pixelcount
            pixelcountHue.nature += 1;
        } else if (hsv.hue > 180 && hsv.hue <= 270){
            // if huevalue of pixel is bigger than 180 and smaller than 270, add one pixel to the ocean pixelcount
            pixelcountHue.ocean += 1;
        } else if (hsv.hue > 270 && hsv.hue <= 360){
            // if huevalue of pixel is bigger than 270 and smaller than 360, add one pixel to the flower pixelcount
            pixelcountHue.flower += 1;
        }
        
        // add the saturationvalue to the total saturation
        totalSat = totalSat + hsv.sat;
        // add the brightnessvalue to the total brightness
        totalBr = totalBr + hsv.br;
		
	}
    
    // caculate the value of the category with the most pixels
    int pixelcountHue_max;
    pixelcountHue_max = MAX4(pixelcountHue.vigorous, pixelcountHue.nature, pixelcountHue.ocean, pixelcountHue.flower);
    
    self.biggestHueCategory = [NSNumber numberWithInt:0]; // set value of this variable to 0 which equals the first category, Vigorous
    
    // Check if other category than vigorous has the most pixels
    if (pixelcountHue.nature == pixelcountHue_max){
        self.biggestHueCategory = [NSNumber numberWithInt:1]; // 1 equals Nature
    };
    if (pixelcountHue.ocean == pixelcountHue_max){
        self.biggestHueCategory = [NSNumber numberWithInt:2]; // 2 equals Ocean
    };
    if (pixelcountHue.flower == pixelcountHue_max){
        self.biggestHueCategory = [NSNumber numberWithInt:3]; // 3 equals flower
    }
    // note that we assume that ussualy the amount of pixels in each category will differ. If not the highest category is chosen. This is a result of the sequential if-statements.
    
    // calculate the average Saturation and Brightness by dividing the total saturation and brightness by the total  of pixels, which can be calculated by adding up all the categories
    totalSat = (totalSat / (pixelcountHue.vigorous + pixelcountHue.nature + pixelcountHue.ocean + pixelcountHue.flower))* 100;
    totalBr = (totalBr / (pixelcountHue.vigorous + pixelcountHue.nature + pixelcountHue.ocean + pixelcountHue.flower))* 100;
    
    // Round  the saturation and brightness values so they become integers
    self.avgSaturation = [NSNumber numberWithInt:lroundf(totalSat)];
    self.avgBrightness = [NSNumber numberWithInt:lroundf(totalBr)];
    
    NSLog(@"Saturation is %@", self.avgSaturation);
    NSLog(@"Brightness is %@", self.avgBrightness);
    NSLog(@"Hue Class is %@", self.biggestHueCategory);
    
    NSLog(@"picture analysis is finished");
}


/* This method is called when the user taps the view results button. All observations are retrieved from the database and processed. */
-(void)getDataFromDatabase {
    // Make a instance of NSFetchRequest to pull all the observation out of the database.
    NSFetchRequest *request = [NSFetchRequest fetchRequestWithEntityName:@"Observation"];
    request.fetchBatchSize = 50;
    request.fetchLimit = 200;
    
    //instance of error
    NSError *error;
    
    //make an array of all the observations in the database.
    NSArray *observations = [self.context executeFetchRequest:request error:&error];
    
    // an array for te number of observation in every mood category
    NSMutableArray *moodClassCount = [[NSMutableArray alloc] initWithObjects:[NSNumber numberWithInt:0], [NSNumber numberWithInt:0], [NSNumber numberWithInt:0], nil];
    
    // an array for the number of happy observations for every huecategory
    self.hueHappy = [[NSMutableArray alloc] initWithObjects:[NSNumber numberWithInt:0], [NSNumber numberWithInt:0], [NSNumber numberWithInt:0], [NSNumber numberWithInt:0], nil];
    // an array for the number of fine observations for every huecategory
    self.hueFine = [[NSMutableArray alloc] initWithObjects:[NSNumber numberWithInt:0], [NSNumber numberWithInt:0], [NSNumber numberWithInt:0], [NSNumber numberWithInt:0], nil];
    // an array for the number of unhappy observations for every huecategory
    self.hueUnhappy = [[NSMutableArray alloc] initWithObjects:[NSNumber numberWithInt:0], [NSNumber numberWithInt:0], [NSNumber numberWithInt:0], [NSNumber numberWithInt:0], nil];
    
    // an array for the total mood of all observations for every huecategory
    self.hueMood = [[NSMutableArray alloc] initWithObjects:[NSNumber numberWithInt:0], [NSNumber numberWithInt:0], [NSNumber numberWithInt:0], [NSNumber numberWithInt:0], nil];
    // an array for te number of observation in every hue category
    NSMutableArray *moodHueCount = [[NSMutableArray alloc] initWithObjects:[NSNumber numberWithInt:0], [NSNumber numberWithInt:0], [NSNumber numberWithInt:0], [NSNumber numberWithInt:0], nil];
    
    // an array with the total/average value for the saturation per mood value (0-10)
    self.saturationMood = [[NSMutableArray alloc] initWithObjects:[NSNumber numberWithInt:0], [NSNumber numberWithInt:0], [NSNumber numberWithInt:0], [NSNumber numberWithInt:0], [NSNumber numberWithInt:0], [NSNumber numberWithInt:0], [NSNumber numberWithInt:0], [NSNumber numberWithInt:0], [NSNumber numberWithInt:0], [NSNumber numberWithInt:0], [NSNumber numberWithInt:0], nil];
    
    // an array with the number of observations for every mood value (0-10)
    NSMutableArray *moodCount = [[NSMutableArray alloc] initWithObjects:[NSNumber numberWithInt:0], [NSNumber numberWithInt:0], [NSNumber numberWithInt:0], [NSNumber numberWithInt:0], [NSNumber numberWithInt:0], [NSNumber numberWithInt:0], [NSNumber numberWithInt:0], [NSNumber numberWithInt:0], [NSNumber numberWithInt:0], [NSNumber numberWithInt:0], [NSNumber numberWithInt:0], nil];
    
    // an array with the total/average value for the brightness per mood value (0-10)
    self.brightnessMood = [[NSMutableArray alloc] initWithObjects:[NSNumber numberWithInt:0], [NSNumber numberWithInt:0], [NSNumber numberWithInt:0], [NSNumber numberWithInt:0], [NSNumber numberWithInt:0], [NSNumber numberWithInt:0], [NSNumber numberWithInt:0], [NSNumber numberWithInt:0], [NSNumber numberWithInt:0], [NSNumber numberWithInt:0],[NSNumber numberWithInt:0], nil];
    
    // an array with the total/average value for the mood for every day of the week (1-7 and sunday =1)
    self.dayOfWeekMood = [[NSMutableArray alloc] initWithObjects:[NSNumber numberWithInt:0], [NSNumber numberWithInt:0], [NSNumber numberWithInt:0], [NSNumber numberWithInt:0], [NSNumber numberWithInt:0], [NSNumber numberWithInt:0], [NSNumber numberWithInt:0], nil];
    
    // an array with the number of observations for every day of the week
    NSMutableArray *dayOfWeekCount = [[NSMutableArray alloc] initWithObjects:[NSNumber numberWithInt:0], [NSNumber numberWithInt:0], [NSNumber numberWithInt:0], [NSNumber numberWithInt:0], [NSNumber numberWithInt:0], [NSNumber numberWithInt:0], [NSNumber numberWithInt:0], nil];
    
    // We loop through every observation in the database
    for(Observation *observation in observations) {
        
        // get the integer value of every column for a certain observation
        NSInteger mood = [observation.mood integerValue];
        NSInteger hue = [observation.hue integerValue];
        NSInteger saturation = [observation.saturation integerValue];
        NSInteger brightness = [observation.brightness integerValue];
        
        // make a calendar object
        NSCalendar *gregorian = [[NSCalendar alloc] initWithCalendarIdentifier:NSGregorianCalendar];
        NSDate *date = observation.date;
        NSDateComponents *dateComponents = [gregorian components:NSWeekdayCalendarUnit fromDate:date];
        // get the day of the week for the date the observation was added
        NSInteger weekday = [dateComponents weekday];
        
        NSNumber *dummyHue;
        NSNumber *dummyMood;
        
        // Calculations for pie charts
        if (mood < 4) { //observation is unhappy
            dummyMood = [NSNumber numberWithInt:[[moodClassCount objectAtIndex:2] intValue] + 1];
            [moodClassCount replaceObjectAtIndex:2 withObject: dummyMood]; // add observation to unhappy count
            
            dummyHue = [NSNumber numberWithInt:[[self.hueUnhappy objectAtIndex:hue] intValue] + 1];
            [self.hueUnhappy replaceObjectAtIndex: hue withObject:dummyHue]; // add observation to unhappy count for the right hue categor
           
        } else if (mood < 8){ // observation is neutral
            dummyMood = [NSNumber numberWithInt:[[moodClassCount objectAtIndex:1] intValue] + 1];
            [moodClassCount replaceObjectAtIndex:1 withObject: dummyMood]; // add observation to fine count
            
            dummyHue = [NSNumber numberWithInt:[[self.hueFine objectAtIndex:hue] intValue] + 1];
            [self.hueFine replaceObjectAtIndex: hue withObject:dummyHue]; // add observation to unhappy count for the right hue category
            
        } else { // observation is happy; augment the variabel of the class the observation is part of.
            dummyMood = [NSNumber numberWithInt:[[moodClassCount objectAtIndex:0] intValue] + 1];
            [moodClassCount replaceObjectAtIndex:0 withObject: dummyMood]; // add observation to happy count
            
            dummyHue = [NSNumber numberWithInt:[[self.hueHappy objectAtIndex:hue] intValue] + 1];
            [self.hueHappy replaceObjectAtIndex: hue withObject:dummyHue]; // add observation to unhappy count for the right hue category
        }
    
        // caculations hue barchart
        NSNumber *dummyMoodHueCount = [NSNumber numberWithInt:[[moodHueCount objectAtIndex:hue] intValue] + 1];
        [moodHueCount replaceObjectAtIndex:hue withObject: dummyMoodHueCount];
        
        NSNumber *dummyHueMood = [NSNumber numberWithInt:[[self.hueMood objectAtIndex:hue] intValue] + mood];
        [self.hueMood replaceObjectAtIndex: hue withObject:dummyHueMood]; // add observation to count for the right hue category
       
        NSNumber *dummyMoodCount = [NSNumber numberWithInt:[[moodCount objectAtIndex:mood] intValue] + 1];
        [moodCount replaceObjectAtIndex:mood withObject: dummyMoodCount];
        
        // caculations saturation barchart
        NSNumber *dummySat = [NSNumber numberWithInt:[[self.saturationMood objectAtIndex:mood] intValue] + saturation];
        [self.saturationMood replaceObjectAtIndex: (mood) withObject:dummySat];
        
        // calculations brightness barchart
        NSNumber *dummybr = [NSNumber numberWithInt:[[self.brightnessMood objectAtIndex:mood] intValue] + brightness];
        [self.brightnessMood replaceObjectAtIndex: (mood) withObject:dummybr];
        
        // calculations time barchart
        NSNumber *dayCount = [NSNumber numberWithInt:[[dayOfWeekCount objectAtIndex:weekday-1] intValue] + 1];
        [dayOfWeekCount replaceObjectAtIndex:weekday-1 withObject: dayCount];
        
        NSNumber *day = [NSNumber numberWithInt:[[self.dayOfWeekMood objectAtIndex:weekday-1] intValue] + mood];
        [self.dayOfWeekMood replaceObjectAtIndex: (weekday-1) withObject:day];
        
    }

    NSInteger index;
    
    // for lus that calculates the average mood for every hue category, 
    for (index = 0; index<4 ; index++)
    {   // if moodhuecount for this index is not 0
        if ([[moodHueCount objectAtIndex:index] intValue] != 0){
        NSNumber *avgHueMood = [NSNumber numberWithInt: [[self.hueMood objectAtIndex: index] intValue]/[[moodHueCount objectAtIndex:index] intValue]];
        [self.hueMood replaceObjectAtIndex:index withObject: avgHueMood];
        };
        if ([[moodClassCount objectAtIndex:0] intValue] != 0) {
        NSNumber *perHappy = [NSNumber numberWithFloat: ([[self.hueHappy objectAtIndex: index] intValue]*100)/[[moodClassCount objectAtIndex:0] intValue]];
        [self.hueHappy replaceObjectAtIndex:index withObject: perHappy];
        };
        if ([[moodClassCount objectAtIndex:1] intValue] != 0) {
        NSNumber *perFine = [NSNumber numberWithFloat: ([[self.hueFine objectAtIndex: index] intValue]*100)/[[moodClassCount objectAtIndex:1] intValue]];
        [self.hueFine replaceObjectAtIndex:index withObject: perFine];
        };
        if ([[moodClassCount objectAtIndex:2] intValue] != 0) {
            NSNumber *perUnhappy = [NSNumber numberWithFloat: ([[self.hueUnhappy objectAtIndex: index] intValue]*100)/[[moodClassCount objectAtIndex:2] intValue]];
        [self.hueUnhappy replaceObjectAtIndex:index withObject: perUnhappy];
        }
    };
    
    // for lus that calculates the average saturation and brightness for every mood value  
    for (index = 0; index<11 ; index++)
    {   // if moodcount for this index is not 0
        if ([[moodCount objectAtIndex:index] intValue] != 0){
        NSNumber *avgSat = [NSNumber numberWithInt: [[self.saturationMood objectAtIndex: index] intValue]/[[moodCount objectAtIndex:index] intValue]];
        [self.saturationMood replaceObjectAtIndex:index withObject: avgSat];
        
        NSNumber *avgBr = [NSNumber numberWithInt: [[self.brightnessMood objectAtIndex: index] intValue]/[[moodCount objectAtIndex:index] intValue]];
        [self.brightnessMood replaceObjectAtIndex:index withObject: avgBr];
        }
    }
    
    // for lus that calculates the average mood value for every day of the week
    for (index = 0; index<7 ; index++)
    {   // if dayOfWeekCount for this index is not 0
        if ([[dayOfWeekCount objectAtIndex:index] intValue] != 0){
        NSNumber *avgMood = [NSNumber numberWithInt: [[self.dayOfWeekMood objectAtIndex: index] intValue]/[[dayOfWeekCount objectAtIndex:index] intValue]];
        [self.dayOfWeekMood replaceObjectAtIndex:index withObject: avgMood];
        }
    }
}

@end

