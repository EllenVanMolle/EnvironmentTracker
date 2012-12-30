//
//  TimeChartViewController.h
//  EnvironmentTracker
//
//  Created by student on 12/12/12.
//  Copyright (c) 2012 multimedia. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "EnvironmentTrackerModel.h"
#import "CorePlot-CocoaTouch.h"

@interface TimeChartViewController : UIViewController  <CPTBarPlotDataSource, UIActionSheetDelegate>

@property EnvironmentTrackerModel *model;
@property (nonatomic, strong) CPTGraphHostingView *hostView;
@property (nonatomic, strong) CPTTheme *selectedTheme;
@property UIBarButtonItem *themeButton;

@end
