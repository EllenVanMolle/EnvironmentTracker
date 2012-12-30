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
@property NSDate *date;
@property NSNumber *biggestHueCategory;
@property NSNumber *avgSaturation;
@property NSNumber *avgBrightness;
@property NSNumber *lengteligging;
@property NSNumber *breedteligging;

@property UIImage *cameraImage;
@property dispatch_queue_t analysisQueue;

@property NSNumber *avgDecibels;

@property NSMutableArray *hueHappy;
@property NSMutableArray *hueFine;
@property NSMutableArray *hueUnhappy;
@property NSMutableArray *hueMood;
@property NSMutableArray *saturationMood;
@property NSMutableArray *brightnessMood;
@property NSMutableArray *dayOfWeekMood;

@property (nonatomic) UIManagedDocument *database;
@property (nonatomic) NSManagedObjectContext *context;


-(void) openDatabase;
-(IBAction) saveObservationToDatabase;
-(BOOL) isDatabaseReady;
-(void) startUpNextNotification;
-(void) getDataFromDatabase;
-(void) analyseImage;

@end
