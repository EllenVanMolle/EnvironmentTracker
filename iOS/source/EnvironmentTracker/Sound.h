//
//  Sound.h
//  EnvironmentTracker
//
//  Created by student on 07/11/12.
//  Copyright (c) 2012 multimedia. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class Observation;

@interface Sound : NSManagedObject

@property (nonatomic, retain) Observation *belongsTo;

@end
