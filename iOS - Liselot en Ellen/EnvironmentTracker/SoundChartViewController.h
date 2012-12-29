//
//  SoundChartViewController.h
//  EnvironmentTracker
//
//  Created by student on 16/12/12.
//  Copyright (c) 2012 multimedia. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "EnvironmentTrackerModel.h"

@interface SoundChartViewController : UIViewController

@property EnvironmentTrackerModel *model;
@property (nonatomic) IBOutlet UILabel *avgDecibelsLabel;
@property (nonatomic) IBOutlet UILabel *DecibelRemark;

@end
