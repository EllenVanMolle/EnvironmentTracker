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
    //  log which button was pressed
    NSLog(@"Button: %i, was pressed.", buttonIndex);
    //  if YES button was pressed
    if (buttonIndex == 0){
        // Start de volgende notificatie op.
        [self.model startUpNextNotification];
        // Zorg dat de gebruiker terug naar de startpagina gaat.
        [self performSegueWithIdentifier:@"backToStartFromPhoto" sender:self];
    }
}

/* Method called when view is loaded
 */
-(void) viewDidLoad {
    [super viewDidLoad];
    // make sure the navigatiebar is displaid
    [self.navigationController setNavigationBarHidden:NO animated:YES];
    
    // make sure the toolbar is not displaid
    [self.navigationController setToolbarHidden:YES animated:YES];
    
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

/* This method is called when the button in the view is pressed to take a picture
 */
-(IBAction)takeAPicture
    {
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

/* Methode die aangeroepen wordt als de foto genomen is.
 */
#pragma mark imagePickerDelegate
- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info
{
    // cameraImage is the UIimage that represents the picture captured by the camera.
    self.model.cameraImage = (UIImage *) [info objectForKey:UIImagePickerControllerOriginalImage];
    
    // Dismiss imagePickerViewController
    [picker dismissViewControllerAnimated:YES completion:nil];
    
    // First create a queue to make sure the analysis happens asynchronously
    self.model.analysisQueue = dispatch_queue_create("analysisQueue", NULL);
    
    // Now you can put things in the FIFO queue
    dispatch_async(self.model.analysisQueue, ^{
        NSLog(@" Start QueQue");
        [self.model analyseImage];
    });
}

/* Methode die aangeroepen wordt als het nemen vam de foto gecanceld is
 */
- (void)imagePickerControllerDidCancel:(UIImagePickerController *)picker
{
	// Dismiss the image selection and close the program
    [picker dismissViewControllerAnimated:YES completion:nil];
}

@end
