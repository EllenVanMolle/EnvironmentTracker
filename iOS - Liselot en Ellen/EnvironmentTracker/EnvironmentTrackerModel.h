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
}

@property NSNumber *identifier;
@property NSNumber *mood;
@property NSNumber *breedteligging;
@property NSNumber *lengteligging;
@property NSDate *date;

@property (nonatomic) UIManagedDocument *database;
@property (nonatomic) NSManagedObjectContext *context;


-(void)openDatabase;
-(IBAction)saveObservationToDatabase;
-(BOOL) isDatabaseReady;
-(void)startUpNextNotification;

@end
