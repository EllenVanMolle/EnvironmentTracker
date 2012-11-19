//
//  EnvironmentTrackerModel.h
//  EnvironmentTracker
//
//  Created by student on 06/11/12.
//  Copyright (c) 2012 multimedia. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Observation.h"

@interface EnvironmentTrackerModel : NSObject {
    NSNumber *identifier;
    NSNumber *mood;
    NSNumber *breedteligging;
    NSNumber *lengteligging;
    NSDate *date;
}

@property (nonatomic) UIManagedDocument *database;
@property (nonatomic) NSManagedObjectContext *context;


-(void)openDatabase;
-(IBAction)saveObservationWithID:(int) newIdentifier
                        WithMood:(int) newMood
                        WithDate:(NSDate *) newDate
              WithBreedteligging:(int) newBreedteligging
               WithLengteligging:(int) newLengteligging;
-(BOOL) isDatabaseReady;

@end
