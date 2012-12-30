//
//  ColorViewController.h
//  EnvironmentTracker
//
//  Created by student on 12/12/12.
//  Copyright (c) 2012 multimedia. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "EnvironmentTrackerModel.h"


@interface ColorViewController : UIViewController

@property (nonatomic) IBOutlet UIButton *hueChartButton;
@property (nonatomic) IBOutlet UIButton *saturationChartButton;
@property (nonatomic) IBOutlet UIButton *brightnessChartButton;
@property (nonatomic) IBOutlet UIButton *pieChartsButton;
@property (nonatomic) NSNotificationCenter *center;
@property (nonatomic) EnvironmentTrackerModel *model;

@end
