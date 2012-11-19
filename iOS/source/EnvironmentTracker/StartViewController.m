//
//  StartViewController.m
//  EnvironmentTracker
//
//  Created by student on 06/11/12.
//  Copyright (c) 2012 multimedia. All rights reserved.
//

#import "StartViewController.h"
#import "EnvironmentTrackerModel.h"
#import "ViewController.h"
#import "ResultsViewController.h"


@implementation StartViewController

@synthesize recordButton = _recordButton;
@synthesize viewResultsButton = _viewResultsButton;
@synthesize settingsButton = _settingsButton;
//@synthesize database = _database;
//@synthesize context = _context;
@synthesize model = _model;



/* Deze methode wordt aangeroepen als de view wordt geinitialiseerd.*/
-(void) viewDidLoad {
    [super viewDidLoad];
    [self.navigationItem setHidesBackButton:TRUE];
    
    //UIBarButtonItem *leftBarButton = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemCancel target:self action:@selector()];
    //[self.navigationItem setLeftBarButtonItem:leftBarButton];
    //[leftBarButton release];
    
    // Het initialiseren en alloceren van het model als het nog niet bestaat.
    if (!self.model) {
        self.model = [[EnvironmentTrackerModel alloc] init];
    }
    
    // Het openen van de database als deze nog niet geopend is.
    [self.model openDatabase];
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    if ([segue.identifier isEqualToString:@"startRecording"]) {
        ViewController *newController = segue.destinationViewController;
        // send messages to newController to prepare it to appear on screen
        // the segue will do the work of putting the new controller on screen
        newController.model = self.model;
    } else if ([segue.identifier isEqualToString:@"viewResults"]) {
        ResultsViewController *newController = segue.destinationViewController;
        newController.model = self.model;
    }
}

-(IBAction)startUpNextNotification {
    NSLog(@"StartUpNextNotification");
    [[UIApplication sharedApplication] cancelAllLocalNotifications];
    
    // Nakijken of de juiste classes ondersteund worden.
    Class cls = NSClassFromString(@"UILocalNotification");
    if (cls != nil) {
        
        UILocalNotification *notif = [[cls alloc] init];
        notif.fireDate = [NSDate dateWithTimeIntervalSinceNow:30];
        notif.timeZone = [NSTimeZone defaultTimeZone];
        
        notif.alertBody = @"Time to record your mood";
        notif.alertAction = @"Record now";
        notif.soundName = UILocalNotificationDefaultSoundName;
        //Het huidige badgenummer wordt met 1 verhoogd.
        //notif.applicationIconBadgeNumber = 1 + [[UIApplication sharedApplication] applicationIconBadgeNumber];
        
        [[UIApplication sharedApplication] scheduleLocalNotification:notif];
    }

 }

@end