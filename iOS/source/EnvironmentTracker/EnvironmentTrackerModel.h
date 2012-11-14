//
//  EnvironmentTrackerModel.h
//  EnvironmentTracker
//
//  Created by student on 06/11/12.
//  Copyright (c) 2012 multimedia. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Observation.h"

@interface EnvironmentTrackerModel : NSObject

-(void) addNewObservationWithID:(int) identifier
                       withMood:(int) mood
                       withDate:(NSDate*) date
             withBreedteligging:(int) breedteligging
              withLengteLigging:(int) lengteligging
                    withContext:(NSManagedObjectContext *) context;

@end
