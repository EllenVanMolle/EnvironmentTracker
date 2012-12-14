//
//  EnvironmentTrackerModel.m
//  EnvironmentTracker
//
//  Created by student on 06/11/12.
//  Copyright (c) 2012 multimedia. All rights reserved.
//

#import "EnvironmentTrackerModel.h"
#import "Observation.h"


@implementation EnvironmentTrackerModel {
    
}

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
        url = [url URLByAppendingPathComponent:@"DatabaseEnvironment"];
        self.database = [[UIManagedDocument alloc] initWithFileURL:url]; // setter wordt hoger overschreven
    }
}

/* Deze methode slaat de database op. */
-(void)saveDatabase {
    [self.database saveToURL:self.database.fileURL
            forSaveOperation:UIDocumentSaveForOverwriting
           completionHandler:^(BOOL success) {
               if (!success) NSLog(@"failed to save document %@", self.database.localizedName);
           }];
}
 

/* Deze methode sluit de database. */
-(void)closeDatabase {
    [self.database closeWithCompletionHandler:^(BOOL success) {
        if (!success) NSLog(@"failed to close document %@", self.database.localizedName);
    }];
}

-(void) addObservationToDatabase {
    NSManagedObjectContext *context = self.database.managedObjectContext;
    
    Observation *observation =
    [NSEntityDescription insertNewObjectForEntityForName:@"Observation"
                                  inManagedObjectContext:context];
    observation.observationID = self.identifier;
    observation.mood = self.mood;
    observation.lengteligging = self.lengteligging;
    observation.breedteligging = self.breedteligging;
    observation.date = self.date;
    
    NSNotificationCenter *center = [NSNotificationCenter defaultCenter];
    [center removeObserver:self
                      name:UIDocumentStateChangedNotification
                    object:self.database];
    
}

// Public methods

// Sla de observatie op in de database met de gegevens die op dit moment zijn opgeslagen in het model.
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
 Het opstarten van de notificaties. De gebruiker heeft het interval bepaald in zijn instellingen. Later moet deze code uitgevoerd worden wanneer de gebruiker zijn instellingen bevestigd, zodanig dat dat het moment is dat de code zordt aangeroepen.
 */
-(void)startUpNextNotification {
    NSLog(@"StartUpNextNotification");
    // We verwijderen eerst alle notificaties die nog eventueel aanwezig zijn.
    [[UIApplication sharedApplication] cancelAllLocalNotifications];
    
    // Nakijken of de juiste classes ondersteund worden.
    Class cls = NSClassFromString(@"UILocalNotification");
    if (cls != nil) {
        // We maken een notificatie aan met een
        UILocalNotification *notif = [[cls alloc] init];
        notif.fireDate = [NSDate dateWithTimeIntervalSinceNow:10];
        notif.timeZone = [NSTimeZone defaultTimeZone];
        
        notif.alertBody = @"Time to record your mood";
        notif.alertAction = @"Record now";
        notif.soundName = UILocalNotificationDefaultSoundName;
        //Het huidige badgenummer wordt met 1 verhoogd.
        //notif.applicationIconBadgeNumber = 1 + [[UIApplication sharedApplication] applicationIconBadgeNumber];
        
        [[UIApplication sharedApplication] scheduleLocalNotification:notif];
    } else {
        NSLog(@"De notificaties zorden niet ondersteund.");
    }
    
}

@end

