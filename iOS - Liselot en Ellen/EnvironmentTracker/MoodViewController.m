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


/*
 * Deze code wordt aangeroepen wanneer de gebruiker op de cancel button drukt.
 */
-(void) cancelRecording {
    // Geef een alert zodanig dat de gebruiker weet dat de gegevens niet worden opgeslagen>
    // Eventueel: Maak ook een cancel button.
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Cancel recording"
                                                    message:@"You cancelled the recording. The current data is not recorded."
                                                   delegate:nil
                                          cancelButtonTitle:@"OK"
                                          otherButtonTitles:nil];
    [alert show];
    // Start de volgende notificatie op.
    [self.model startUpNextNotification];
    // Zorg dat de gebruiker terug naar de startpagina gaat.
    [self performSegueWithIdentifier:@"backToStartFromMood" sender:self];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
    
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
        PhotoViewController *newController = segue.destinationViewController;
        // the segue will do the work of putting the new controller on screen
        // We geven het model door aan de volgende controller.
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
        [_labelMood setText:@"Ongelukkig"];
    } else if ([sender value] < 8.0) {
        [_labelMood setText:@"Neutraal"];
    } else {
        [_labelMood setText:@"Gelukkig"];
    }
}
/*
-(int) makeIdentifier {
    return 1;
}

-(int) mood {
    return [self.moodSlider value];
}

-(NSDate *) date {
    return [NSDate date];
}

-(int) determineLocationLengteLigging {
    return 1;
}

-(int) determineLocationBreedteLigging {
    return 1;
}
 */

/* Deze methode wordt aangeroepen wanneer de gebruiker op de knop continue drukt en zorgt dat de mood wordt doorgegeven aan het model zodanig dat het in het model kan worden opgeslagen.
 */
-(void) saveMood {
    [self.model setMood:[NSNumber numberWithInt:[self.moodSlider value]]];
    //[self.model saveObservationWithID:[self makeIdentifier] WithMood:[self mood] WithDate:[self date] WithBreedteligging:[self determineLocationBreedteLigging] WithLengteligging:[self determineLocationLengteLigging]];
}

@end
