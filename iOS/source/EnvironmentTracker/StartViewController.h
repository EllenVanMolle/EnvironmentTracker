//
//  StartViewController.h
//  EnvironmentTracker
//
//  Created by student on 06/11/12.
//  Copyright (c) 2012 multimedia. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "EnvironmentTrackerModel.h"

@interface StartViewController : UIViewController {
}

@property (nonatomic) IBOutlet UIButton *recordButton;
@property (nonatomic) IBOutlet UIButton *viewResultsButton;
@property (nonatomic) IBOutlet UIButton *settingsButton;
@property (nonatomic) UIManagedDocument *database;
@property (nonatomic) NSManagedObjectContext *context;
@property (nonatomic) EnvironmentTrackerModel *model;


-(IBAction)startUpNextNotification;

@end
