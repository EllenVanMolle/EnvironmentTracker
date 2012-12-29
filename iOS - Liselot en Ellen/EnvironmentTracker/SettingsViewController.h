//
//  SettingsViewController.h
//  EnvironmentTracker
//
//  Created by student on 08/12/12.
//  Copyright (c) 2012 multimedia. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "EnvironmentTrackerModel.h"


@interface SettingsViewController : UIViewController

@property (nonatomic) IBOutlet UISwitch *notificationSwitch;
@property (nonatomic) IBOutlet UITextField *intervalTextField;
@property (nonatomic) EnvironmentTrackerModel *model;

- (IBAction)toggleEnableIntervalTextField: (id)sender;

@end
