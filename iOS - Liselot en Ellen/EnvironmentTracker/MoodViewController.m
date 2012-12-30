//
//  ViewController.m
//  EnvironmentTracker
//
//  Created by ariadne on 28/10/12.
//  Copyright (c) 2012 ariadne. All rights reserved.
//

#import "MoodViewController.h"
#import "PhotoViewController.h"
#import "StartViewController.h"

@implementation MoodViewController

@synthesize moodSlider = _moodSlider;
@synthesize continueFromMood = _continueFromMood;
@synthesize labelMood = _labelMood;
@synthesize model = _model;


/* Deze code wordt aangeroepen wanneer de gebruiker op de cancel button drukt.
 */
-(void) cancelRecording {
    // Geef een alert zodanig dat de gebruiker weet dat de gegevens niet worden opgeslagen>
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Cancel recording"
                                                    message:@"Do you want to cancel the recording? The current data will not be saved."
                                                   delegate:self
                                          cancelButtonTitle:@"YES"
                                          otherButtonTitles:@"NO", nil];
    [alert show];
    
}

/* This method is called when one of the buttons in the alert is ticked. Depending on the index of the button certain actions are taken. */
- (void)alertView:(UIAlertView *)alertView didDismissWithButtonIndex:(NSInteger)buttonIndex
{
    // log which button is pressed
    NSLog(@"Button: %i, was pressed.", buttonIndex);
    //if the YES button is pressed
    if (buttonIndex == 0){
        // Start de volgende notificatie op.
        [self.model startUpNextNotification];
        // Zorg dat de gebruiker terug naar de startpagina gaat.
        [self performSegueWithIdentifier:@"backToStartFromMood" sender:self];
    }
}

/* Method called when the view is loaded
 */
- (void)viewDidLoad
{
    [super viewDidLoad];
	
    // make sure the navigatiebar is displaid
    [self.navigationController setNavigationBarHidden:NO animated:YES];
    
    // make sure the toolbar is not displaid
    [self.navigationController setToolbarHidden:YES animated:YES];
    
    // Zorg dat de back button niet getoond wordt.
    [self.navigationItem setHidesBackButton:TRUE];
    
    // Geef een cancel button in de plaats en roep cancelrecording op wanneer men er op drukt.
    UIBarButtonItem *leftBarButton = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemCancel target:self action:@selector(cancelRecording)];
     [self.navigationItem setLeftBarButtonItem:leftBarButton];
}

/* Deze methode wordt aangeroepen net voordat de segue wordt uitgevoerd. We gebruiken deze om het model door te geven aan de volgende controller.
 */
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Als de gebruiker op de knop continue heeft gedrukt, moet een photoviewcontroller aangemaakt worden.
    if ([segue.identifier isEqualToString:@"goToPhoto"]) {
        // the segue will do the work of putting the new controller on screen
        // We geven het model door aan de volgende controller.
        PhotoViewController *newController = segue.destinationViewController;
        newController.model = self.model;
    // als de gebruiker canceld gaat ij terug naar het begin.
    } else if ([segue.identifier isEqualToString:@"backToStartFromMood"]) {
        // the segue will do the work of putting the new controller on screen
        // We geven het model door aan de volgende controller.
        StartViewController *newController = segue.destinationViewController;
        newController.model = self.model;
    } 
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/* Deze methode zorgt er voor dat de tekst boven de slider wijzigt wanneer de gebruiker de slider beweegt.
 */
-(IBAction)changeLabel:(UISlider *)sender {
    if ([sender value] < 4.0) {
        [_labelMood setText:@"Unhappy"];
    } else if ([sender value] < 8.0) {
        [_labelMood setText:@"Neutral"];
    } else {
        [_labelMood setText:@"Happy"];
    }
}

/* Deze methode wordt aangeroepen wanneer de gebruiker op de knop continue drukt en zorgt dat de mood wordt doorgegeven aan het model zodanig dat het in het model kan worden opgeslagen.
 */
-(void) saveMood {
    [self.model setMood:[NSNumber numberWithInt:[self.moodSlider value]]];
}

@end
