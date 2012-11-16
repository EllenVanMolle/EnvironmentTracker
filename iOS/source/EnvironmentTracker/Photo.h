//
//  Photo.h
//  EnvironmentTracker
//
//  Created by student on 07/11/12.
//  Copyright (c) 2012 multimedia. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class Observation;

@interface Photo : NSManagedObject

@property (nonatomic, retain) NSNumber * hue0_90;
@property (nonatomic, retain) NSNumber * hue90_180;
@property (nonatomic, retain) NSNumber * hue180_270;
@property (nonatomic, retain) NSNumber * hue270_360;
@property (nonatomic, retain) NSNumber * saturation;
@property (nonatomic, retain) NSNumber * brightness;
@property (nonatomic, retain) Observation *belongsTo;

@end
