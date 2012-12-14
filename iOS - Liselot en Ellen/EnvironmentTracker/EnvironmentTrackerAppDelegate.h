//
//  EnvironmentTrackerAppDelegate.h
//  EnvironmentTracker
//
//  Created by student on 31/10/12.
//  Copyright (c) 2012 multimedia. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MoodViewController.h"
#import "AudioViewController.h"

@class PhotoViewController;

@interface EnvironmentTrackerAppDelegate : NSObject <UIApplicationDelegate> {
}

@property (nonatomic) UIWindow *window;
@property (weak, nonatomic) IBOutlet AudioViewController *audioViewController;
@property (nonatomic) MoodViewController *viewController;
//@property (nonatomic) UINavigationController *navigationController;

@end