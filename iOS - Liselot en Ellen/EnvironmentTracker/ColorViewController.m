//
//  ColorViewController.m
//  EnvironmentTracker
//
//  Created by student on 12/12/12.
//  Copyright (c) 2012 multimedia. All rights reserved.
//

#import "ColorViewController.h"
#import "CoreData/CoreData.h"
#import <Foundation/Foundation.h>
#import "Observation.h"
#import "hueChartViewController.h"
#import "saturationChartViewController.h"
#import "BrightnessChartViewController.h"
#import "PieChartHappyViewController.h"
#import "PieChartFineViewController.h"
#import "PieChartUnhappyViewController.h"
#import "TimeChartViewController.h"
#import "StartViewController.h"
#import "SoundChartViewController.h"
#import "MoodViewController.h"

@implementation ColorViewController

@synthesize hueChartButton = _hueChartButton;
@synthesize saturationChartButton = _saturationChartButton;
@synthesize brightnessChartButton = _brightnessChartButton;
@synthesize pieChartsButton = _pieChartsButton;
@synthesize center = _center;
@synthesize model = _model;

/* method to open timechart called from a toolbarbutton time */
-(void) openTimeChart
{
   [self performSegueWithIdentifier:@"openTimeChart" sender:self];
}

/* method to open homepage called from a navigationbutton in the navigationbar Home */
-(void) openHome
{
    [self performSegueWithIdentifier:@"openHome" sender:self];
}

/* method to open soundchart called from a toobarbutton time */
-(void) openSoundChart{
    [self performSegueWithIdentifier:@"openSoundChart" sender:self];
}

/* Deze methode wordt aangeroepen wanneer de database niet klaar is.
 */
-(void) databaseNotReady {
    // Verwijder jezelf als observer van de notificatie dat de database klaar is.
    [self.center removeObserver:self
                           name:UIDocumentStateChangedNotification
                         object:self.model.database];
    // Zorg dat de gebruiker terugkeert naar het scherm van de resultaten.
    [self performSegueWithIdentifier:@"segueDatabaseReady" sender:self];
}

- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    
    // Controleren of de database klaar is, wanneer dat niet het geval is, jezelf toevoegen als observer en vervolgens naar de wachtpagina gaan.
    if (![self.model isDatabaseReady]) {
        self.center = [NSNotificationCenter defaultCenter];
        [self.center addObserver:self
                        selector:@selector(databaseNotReady)
                            name:UIDocumentStateChangedNotification
                          object:self.model.database];
        [self performSegueWithIdentifier:@"segueDatabaseNotReady" sender:self];
    }
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

/* Method called when view is loaded */
- (void)viewDidLoad
{   [self.model getDataFromDatabase];
    
    [super viewDidLoad];
	// Do any additional setup after loading the view.
    
    // make sure the navigatiebar is displaid
    [self.navigationController setNavigationBarHidden:NO animated:YES];
    
    // make sure the toolbar is displaid
    [self.navigationController setToolbarHidden:NO animated:YES];
    
    // make sure backbutton is hidden
    [self.navigationItem setHidesBackButton:YES animated:YES];
    
    // adding a home button to the navigationbar that leads the user back to the homepage
    UIBarButtonItem *backBarButton = [[UIBarButtonItem alloc] initWithTitle:@"home" style:UIBarButtonItemStyleBordered target:self action:@selector(openHome)];
    [self.navigationItem setLeftBarButtonItem:backBarButton];
    
    // add the timeButton to the toolbar to open the timechart
    UIBarButtonItem *timeGraphButton = [[UIBarButtonItem alloc] initWithTitle:@"time" style:UIBarButtonItemStyleBordered target:self action:@selector(openTimeChart)];
    
    // add the soundButton to the toolbar to open the soundchart
    UIBarButtonItem *soundGraphButton = [[UIBarButtonItem alloc] initWithTitle:@"sound" style:UIBarButtonItemStyleBordered target:self action:@selector(openSoundChart)];
    
    [self setToolbarItems:[NSArray arrayWithObjects:timeGraphButton, soundGraphButton, nil]];
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/* Deze methode wordt aangeroepen net voordat de segue wordt uitgevoerd. We gebruiken deze om het model door te geven aan de volgende controller.*/
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Als de gebruiker op de knop Hue Chart heeft gedrukt, moet een viewcontroller aangemaakt worden.
    if ([segue.identifier isEqualToString:@"goToHueChart"]) {
        hueChartViewController *newController = segue.destinationViewController;
        // the segue will do the work of putting the new controller on screen
        // We geven het model door aan de volgende controller.
        newController.model = self.model;
    // Als de gebruiker op de knop Saturation Chart heeft gedrukt, moet een viewcontroller aangemaakt worden.
    } else if ([segue.identifier isEqualToString:@"goToSaturationChart"]) {
        saturationChartViewController *newController = segue.destinationViewController;
        // the segue will do the work of putting the new controller on screen
        // We geven het model door aan de volgende controller.
        newController.model = self.model;
    // Als de gebruiker op de knop Brightness Chart heeft gedrukt, moet een viewcontroller aangemaakt worden
    } else if ([segue.identifier isEqualToString:@"goToBrightnessChart"]) {
        BrightnessChartViewController *newController = segue.destinationViewController;
        // the segue will do the work of putting the new controller on screen
        // We geven het model door aan de volgende controller.
        newController.model = self.model;
    // Als de gebruiker op de knop Pie Charts heeft gedrukt, moeten de viewcontrollers aangemaakt worden. We zorgen ervoor dat alle drie de views of the tabbarcontroller beschikken over het model
    } else if ([segue.identifier isEqualToString:@"goToPieCharts"]) {
        UITabBarController *tabBarController = segue.destinationViewController;
    
        // We geven het model door aan de volgende controller
        PieChartHappyViewController *firstViewController = [[tabBarController viewControllers] objectAtIndex:0];
        firstViewController.model = self.model;
        
        // We geven het model door aan de volgende controller
        PieChartFineViewController *secondViewController = [[tabBarController viewControllers] objectAtIndex:1];
        secondViewController.model = self.model;
        
        // We geven het model door aan de volgende controller
        PieChartUnhappyViewController *thirdViewController = [[tabBarController viewControllers] objectAtIndex:2];
        thirdViewController.model = self.model;
    // Als de gebruiker op de Time knop in de toolbar drukt, moet een viewcontroller aangemaakt worden
    } else if ([segue.identifier isEqualToString:@"openTimeChart"]) {
        TimeChartViewController *newController = segue.destinationViewController;
        // the segue will do the work of putting the new controller on screen
        // We geven het model door aan de volgende controller.
        newController.model = self.model;
    // Als de gebruiker op de Sound knop in de toolbar drukt, moet een viewcontroller aangemaakt worden.
    }else if ([segue.identifier isEqualToString:@"openSoundChart"]) {
        SoundChartViewController *newController = segue.destinationViewController;
        // the segue will do the work of putting the new controller on screen
        // We geven het model door aan de volgende controller.
        newController.model = self.model;
    // Als de gebruiker op de Home knop in de Navigationbar drukt, moet een viewcontroller aangemaakt worden.
    } else if ([segue.identifier isEqualToString:@"openHome"]) {
        StartViewController *newController = segue.destinationViewController;
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
