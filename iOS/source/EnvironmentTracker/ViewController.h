//
//  ViewController.h
//  EnvironmentTracker
//
//  Created by ariadne on 28/10/12.
//  Copyright (c) 2012 ariadne. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "EnvironmentTrackerModel.h"

@interface ViewController : UIViewController {
        EnvironmentTrackerModel *model;
}

@property (weak, nonatomic) IBOutlet UISlider *moodSlider;
@property (weak, nonatomic) IBOutlet UIButton *continueFromMood;
@property (nonatomic) IBOutlet UILabel *labelMood;
@property (nonatomic) UIManagedDocument *database;
//@property (nonatomic) NSManagedObjectContext *context;

-(IBAction)saveMood;

-(IBAction)changeLabel:(UISlider *)sender;

@end
