//
//  PhotoViewController.h
//  EnvironmentTracker
//
//  Created by ariadne on 28/10/12.
//  Copyright (c) 2012 ariadne. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "EnvironmentTrackerModel.h"

@interface PhotoViewController : UIViewController <UIImagePickerControllerDelegate>{
    
}

@property EnvironmentTrackerModel *model;

-(IBAction)takeAPicture;


@end
