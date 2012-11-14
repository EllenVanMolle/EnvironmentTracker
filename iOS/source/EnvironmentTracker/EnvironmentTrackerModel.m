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

//private methods

-(void) saveNewObservationWithID:(int) identifier
                        withMood:(int) mood
                        withDate:(NSDate*) date
              withBreedteligging:(int) breedteligging
               withLengteLigging:(int) lengteligging
                     withContext:(NSManagedObjectContext *)context {
    
}

/*
-(void)openDatabase {
    
    NSURL *url = [[[NSFileManager defaultManager] URLsForDirectory:NSDocumentDirectory inDomains:NSUserDomainMask] lastObject];
    url = [url URLByAppendingPathComponent:@"EnvironmentDatabase"];

    self.database = [[UIManagedDocument alloc] initWithFileURL:url];
    if ([[NSFileManager defaultManager] fileExistsAtPath: [url absoluteString]]) {
        [self.database openWithCompletionHandler:^(BOOL success) {
            if (success) [self databaseIsReady];
            if (!success) NSLog(@"couldn’t open document at %@", url);
        }];
    } else {
        [self.database saveToURL:self.database.fileURL
                forSaveOperation:UIDocumentSaveForCreating
               completionHandler:^(BOOL success) {
                   if (success) [self databaseIsReady];
                   if (!success) NSLog(@"couldn’t create document at %@", url);
        }];
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
*/
// Public methods

/*-(void) makeNewObservationWithID:(int) identifier
                       withMood:(int) mood
                       withDate:(NSDate*) date
             withBreedteligging:(int) breedteligging
              withLengteLigging:(int) lengteligging
{
    Observation *observation =
        [NSEntityDescription insertNewObjectForEntityForName:@"Observation"
                                      inManagedObjectContext:context];
    
    observation.observationID = [NSNumber numberWithInt:identifier];
    observation.mood = [NSNumber numberWithInt:mood];
    observation.lengteligging = [NSNumber numberWithInt:lengteligging];
    observation.breedteligging = [NSNumber numberWithInt:breedteligging];
    observation.date = date;
}*/

@end

