//
//  GraphMoodVersusColorViewController.h
//  EnvironmentTracker
//
//  Created by student on 11/12/12.
//  Copyright (c) 2012 multimedia. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "EnvironmentTrackerModel.h"
#import "CorePlot-CocoaTouch.h"

/*
 * Declare GraphMoodVersusColorViewController as a delegate of a delegate for CPTPlotDataSource and UIActionSheetDelegate. 
 * The former notes that the view controller will serve as the data source for your plot.
 */
@interface PieChartHappyViewController : UIViewController <CPTPlotDataSource , UIActionSheetDelegate>

@property (nonatomic, strong) CPTGraphHostingView *hostView;
@property (nonatomic, strong) CPTTheme *selectedTheme;
@property (nonatomic) IBOutlet UIToolbar *toolbar;
@property (nonatomic) IBOutlet UIBarButtonItem *themeBarButton;
@property (nonatomic) IBOutlet UIBarButtonItem *BackBarButton;
@property EnvironmentTrackerModel *model;

- (IBAction) themeTapped: (id)sender;

@end
