//
//  PhotoViewController.m
//  EnvironmentTracker
//
//  Created by ariadne on 28/10/12.
//  Copyright (c) 2012 ariadne. All rights reserved.
//

#import "PhotoViewController.h"
#import "AudioViewController.h"
#import "StartViewController.h"

@implementation PhotoViewController

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
    [self performSegueWithIdentifier:@"backToStartFromPhoto" sender:self];
}

-(void) viewDidLoad {
    [super viewDidLoad];
    
    // Het verbergen van de back button.
    [self.navigationItem setHidesBackButton:TRUE];
    
    // Het weergeven van de cancel button. Wanneer de gebruiker hierop drukt wordt de methode cancelRecording aangeroepen.
    UIBarButtonItem *leftBarButton = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemCancel target:self action:@selector(cancelRecording)];
    [self.navigationItem setLeftBarButtonItem:leftBarButton];
}

/* Deze methode wordt aangeroepen net voordat de segue wordt uitgevoerd. We gebruiken deze om het model door te geven aan de volgende controller.
 */
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Als de gebruiker op de knop record heeft gedrukt, moet een viewcontroller aangemaakt worden.
    if ([segue.identifier isEqualToString:@"goToAudio"]) {
        AudioViewController *newController = segue.destinationViewController;
        // the segue will do the work of putting the new controller on screen
        // We geven het model door aan de volgende controller.
        newController.model = self.model;
    } else if ([segue.identifier isEqualToString:@"backToStartFromPhoto"]) {
        StartViewController *newController = segue.destinationViewController;
        newController.model = self.model;
    }
}

-(IBAction)takeAPicture
    {
        //NSLog(@"Methode takeAPicture");
        UIImagePickerController *  picker = [[UIImagePickerController alloc] init];
        picker.delegate = self;
        // Controleer of de device de benodigde resources heeft 
        if([UIImagePickerController isSourceTypeAvailable:UIImagePickerControllerSourceTypeCamera])
        {
            // We willen de camera openen.
            picker.sourceType = UIImagePickerControllerSourceTypeCamera;
            
            //Then present modal view controller
            [self presentViewController:picker animated:YES completion:nil];
        }
        else
        {
            NSLog(@"Geen camera");
        }
    }

#pragma mark imagePickerDelegate
- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info
{
	// Dismiss imagePickerViewController
    [picker dismissViewControllerAnimated:YES completion:nil];
}
- (void)imagePickerControllerDidCancel:(UIImagePickerController *)picker
{
	// Dismiss the image selection and close the program
    [picker dismissViewControllerAnimated:YES completion:nil];
	
}

@end
