//
//  SoundChartViewController.m
//  EnvironmentTracker
//
//  Created by student on 16/12/12.
//  Copyright (c) 2012 multimedia. All rights reserved.
//

#import "SoundChartViewController.h"
#import "ColorViewController.h"
#import "TimeChartViewController.h"
#import "MoodViewController.h"

@interface SoundChartViewController ()

@end

@implementation SoundChartViewController

@synthesize model = _model;
@synthesize avgDecibelsLabel = _avgDecibelsLabel;
@synthesize DecibelRemark = _DecibelRemark;

/* Method called when the time button is tapped*/
- (void) openTimeChart {
    [self performSegueWithIdentifier:@"openTimefromSound" sender:self];
}

/* Method called when the color button is tapped*/
- (void) openColorChart {
    [self performSegueWithIdentifier:@"openColorfromSound" sender:self];
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

/* Method called when the view is loaded*/
- (void)viewDidLoad
{
    [super viewDidLoad];
        
    // make sure the navigatiebar is displaid
    [self.navigationController setNavigationBarHidden:NO animated:YES];
        
    // make sure the toolbar is displaid
    [self.navigationController setToolbarHidden:NO animated:YES] ;
        
    // make sure backbutton is hidden
    [self.navigationItem setHidesBackButton:YES animated:YES];
    
    // adding a time button to the navigationbar that leads the user to the timeChartpage
    UIBarButtonItem *TimeBarButton = [[UIBarButtonItem alloc] initWithTitle:@"time" style:UIBarButtonItemStyleBordered target:self action:@selector(openTimeChart)];
    [self.navigationItem setRightBarButtonItem:TimeBarButton];
    
    // adding a color button to the navigationbar that leads the user to the colorpage
    UIBarButtonItem *colorBarButton = [[UIBarButtonItem alloc] initWithTitle:@"color" style:UIBarButtonItemStyleBordered target:self action:@selector(openColorChart)];
    [self.navigationItem setLeftBarButtonItem:colorBarButton];
    
    if ([self.model.avgDecibels intValue] != 0) {
        [self.avgDecibelsLabel setText: [NSString stringWithFormat:@" %@ dB", self.model.avgDecibels]];
        if ([self.model.avgDecibels intValue] < 40) {
            [self.DecibelRemark setText:(@"Quiet")];
        } else if ([self.model.avgDecibels intValue] < 80) {
            [self.DecibelRemark setText:(@"Noisy")];
        } else if  ([self.model.avgDecibels intValue] < 100) {
            [self.DecibelRemark setText:(@"Loud")];
        } else {
            [self.DecibelRemark setText:(@"Dangerous")];
        }
    } else {
        [self.DecibelRemark setText:(@"Last record not found")];
    }
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/* Deze methode wordt aangeroepen net voordat de segue wordt uitgevoerd. We gebruiken deze om het model door te geven aan de volgende controller.*/
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Als de gebruiker op de color knop in de navigationbar drukt, moet een viewcontroller aangemaakt worden.
    if ([segue.identifier isEqualToString:@"openColorfromSound"]) {
        ColorViewController *newController = segue.destinationViewController;
        // the segue will do the work of putting the new controller on screen
        // We geven het model door aan de volgende controller.
        newController.model = self.model;
    // Als de gebruiker op de time knop in de navigationbar heeft gedrukt, moet een resultsviewcontroller aangemaakt worden.
    } else if ([segue.identifier isEqualToString:@"openTimefromSound"]) {
        TimeChartViewController *newController = segue.destinationViewController;
        // the segue will do the work of putting the new controller on screen
        // We geven het model door aan de volgende controller.
        newController.model = self.model;
    } else if ([segue.identifier isEqualToString:@"startRecording"]) {
        MoodViewController *newController = segue.destinationViewController;
        // the segue will do the work of putting the new controller on screen
        // We geven het model door aan de volgende controller.
        newController.model = self.model;
    }
}

@end
