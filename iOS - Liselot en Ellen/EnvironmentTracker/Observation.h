//
//  Observation.h
//  EnvironmentTracker
//
//  Created by student on 21/11/12.
//  Copyright (c) 2012 multimedia. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>


@interface Observation : NSManagedObject

@property (nonatomic, retain) NSNumber * breedteligging;
@property (nonatomic, retain) NSDate * date;
@property (nonatomic, retain) NSNumber * lengteligging;
@property (nonatomic, retain) NSNumber * mood;
@property (nonatomic, retain) NSNumber * observationID;
@property (nonatomic, retain) NSNumber * brightness;
@property (nonatomic, retain) NSNumber * saturation;
@property (nonatomic, retain) NSNumber * hue;

@end
