//
//  Observation.h
//  EnvironmentTracker
//
//  Created by student on 07/11/12.
//  Copyright (c) 2012 multimedia. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class Photo, Sound;

@interface Observation : NSManagedObject

@property (nonatomic, retain) NSDate * date;
@property (nonatomic, retain) NSNumber * mood;
@property (nonatomic, retain) NSNumber * breedteligging;
@property (nonatomic, retain) NSNumber * observationID;
@property (nonatomic, retain) NSNumber * lengteligging;
@property (nonatomic, retain) Photo *photo;
@property (nonatomic, retain) Sound *sound;

@end
