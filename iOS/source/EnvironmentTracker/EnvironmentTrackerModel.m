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

-(void)databaseIsReady {
    NSLog(@"Database is ready");
    if (self.database.documentState == UIDocumentStateNormal){
        self.context = self.database.managedObjectContext;
        NSLog(@"Context is succesvol gedefinieerd");
    } else {
        NSLog(@"Database not ready");
    }
}

- (void) useDocument {
    if (![[NSFileManager defaultManager] fileExistsAtPath:[self.database.fileURL path]]) {
        // Het bestaat nog niet op de schijf, dus moeten ze het aanmaken.
        NSLog(@"De database bestaat nog niet.");
        [self.database saveToURL:self.database.fileURL forSaveOperation:UIDocumentSaveForCreating completionHandler:^(BOOL success) {
            if (success) {
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

- (void) setDatabase:(UIManagedDocument *)database {
    if (_database != database) {
        _database = database;
        [self useDocument];
    }
}


-(void)openDatabase {
    if (!self.database) {
        NSURL *url = [[[NSFileManager defaultManager] URLsForDirectory:NSDocumentDirectory inDomains:NSUserDomainMask] lastObject];
        url = [url URLByAppendingPathComponent:@"EnvironmentDatabase"];
        self.database = [[UIManagedDocument alloc] initWithFileURL:url]; // setter wordt hoger overschreven
    }
}

-(void)saveDatabase {
    [self.database saveToURL:self.database.fileURL
            forSaveOperation:UIDocumentSaveForOverwriting
           completionHandler:^(BOOL success) {
               if (!success) NSLog(@"failed to save document %@", self.database.localizedName);
           }];
}
 

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
    observation.observationID = identifier;
    observation.mood = mood;
    observation.lengteligging = lengteligging;
    observation.breedteligging = breedteligging;
    observation.date = date;
    
    NSNotificationCenter *center = [NSNotificationCenter defaultCenter];
    [center removeObserver:self
                      name:UIDocumentStateChangedNotification
                    object:self.database];
    
}

// Public methods

-(IBAction)saveObservationWithID:(int) newIdentifier
                        WithMood:(int) newMood
                        WithDate:(NSDate *) newDate
              WithBreedteligging:(int) newBreedteligging
               WithLengteligging:(int) newLengteligging
{
    identifier = [NSNumber numberWithInt:newIdentifier];
    mood = [NSNumber numberWithInt:newMood];
    breedteligging = [NSNumber numberWithInt:newBreedteligging];
    lengteligging = [NSNumber numberWithInt:newLengteligging];
    date = newDate;
    
    if (self.database.documentState == UIDocumentStateNormal) {
        [self addObservationToDatabase];
    } else {
        NSNotificationCenter *center = [NSNotificationCenter defaultCenter];
        [center addObserver:self
                   selector:@selector(addObservationToDatabase)
                       name:UIDocumentStateChangedNotification
                     object:self.database];
    }
}

-(BOOL) isDatabaseReady {
    return self.database.documentState == UIDocumentStateNormal;
}

@end

