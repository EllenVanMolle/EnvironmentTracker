//
//  StartViewController.m
//  EnvironmentTracker
//
//  Created by student on 06/11/12.
//  Copyright (c) 2012 multimedia. All rights reserved.
//

#import "StartViewController.h"
#import "EnvironmentTrackerModel.h"
#import "MoodViewController.h"
#import "ColorViewController.h"
#import "SettingsViewController.h"


@implementation StartViewController

@synthesize recordButton = _recordButton;
@synthesize viewResultsButton = _viewResultsButton;
@synthesize settingsButton = _settingsButton;
@synthesize model = _model;


/* Deze methode wordt aangeroepen als de view wordt geinitialiseerd.
 */
-(void) viewDidLoad {
    [super viewDidLoad];
    
    // make sure the navigatiebar is displaid
    [self.navigationController setNavigationBarHidden:NO animated:YES];
    
    // make sure the toolbar is not displaid
    [self.navigationController setToolbarHidden:YES animated:YES];
    
    // De back-button wordt verborgen.
    [self.navigationItem setHidesBackButton:TRUE];
    
    // Het initialiseren en alloceren van het model als het nog niet bestaat.
    if (!self.model) {
        self.model = [[EnvironmentTrackerModel alloc] init];
    }
    
    // Het openen van de database.
    [self.model openDatabase];
}

/* Deze methode wordt aangeroepen net voordat de segue wordt uitgevoerd. We gebruiken deze om het model door te geven aan de volgende controller.
 */
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Als de gebruiker op de knop record heeft gedrukt, moet een viewcontroller aangemaakt worden.
    if ([segue.identifier isEqualToString:@"startRecording"]) {
        MoodViewController *newController = segue.destinationViewController;
        // the segue will do the work of putting the new controller on screen
        // We geven het model door aan de volgende controller.
        newController.model = self.model;
    // Als de gebruiker op de knop view results heeft gedrukt, moet een resultsviewcontroller aangemaakt worden.
    } else if ([segue.identifier isEqualToString:@"viewResults"]) {
        ColorViewController *newController = segue.destinationViewController;
        // the segue will do the work of putting the new controller on screen
        // We geven het model door aan de volgende controller.
        newController.model = self.model;
    } else if ([segue.identifier isEqualToString:@"openSettings"]) {
        SettingsViewController *newController = segue.destinationViewController;
        // the segue will do the work of putting the new controller on screen
        // We geven het model door aan de volgende controller.
        newController.model = self.model;
    }

}

@end