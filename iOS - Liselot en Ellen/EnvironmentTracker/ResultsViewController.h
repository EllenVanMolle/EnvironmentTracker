//
//  ResultsViewController.h
//  EnvironmentTracker
//
//  Created by student on 13/11/12.
//  Copyright (c) 2012 multimedia. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "EnvironmentTrackerModel.h"

@interface ResultsViewController : UIViewController

@property (nonatomic) IBOutlet UILabel *labelIdentifier;
@property (nonatomic) IBOutlet UILabel *labelDate;
//@property (nonatomic) UIManagedDocument *database;
//@property (nonatomic) NSManagedObjectContext *context;
@property (nonatomic) NSNotificationCenter *center;
@property (nonatomic) EnvironmentTrackerModel *model;


-(IBAction)viewResults;

@end
