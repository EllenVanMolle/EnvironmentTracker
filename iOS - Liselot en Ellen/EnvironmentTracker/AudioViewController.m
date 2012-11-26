//
//  AudioViewController.m
//  EnvironmentTracker
//
//  Created by student on 31/10/12.
//  Copyright (c) 2012 multimedia. All rights reserved.
//

#import "AudioViewController.h"
#import "StartViewController.h"

@implementation AudioViewController

@synthesize startRecordingButton = _startRecordingButton;
@synthesize labelCountingDown = _labelCountingDown;
@synthesize countDownTimer = _countDownTimer;
@synthesize model = _model;

// Private methods

/* Deze methode wordt aangeroepen wanneer men wil stoppen met geluid op te nemen.
 */
-(void)stop
{
    NSLog(@"Stop recording audio");
    //_startRecordingButton.enabled = YES;
    
    // Zorg dat de volgende notificatie wordt ingesteld.
    [self.model startUpNextNotification];
    
    if (audioRecorder.recording)
    {
        // Stop met opnemen als hij aan het opnemen is.
        [audioRecorder stop];
        // Laat de gebruiker teruggaan naar de startpagina.
        [self performSegueWithIdentifier:@"segueAfterRecording" sender:self];
    }
}

/* Deze methode zorgt er voor dat er afgeteld wordt (ook op het scherm) en wanneer 0 bereikt wordt, de opname wordt gestopt door de methode stop aan te roepen.*/
-(void)countDown {
    // Verminder de teller met 1.
    numberDisplayedCountingDown = numberDisplayedCountingDown - 1;
    [_labelCountingDown setText: [NSString stringWithFormat:@"%d", numberDisplayedCountingDown]];
    if (numberDisplayedCountingDown == 0) {
        // Zet the timer af.
        [_countDownTimer invalidate];
        // Stop met opnemen.
        [self stop];
    }
}

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
    [self performSegueWithIdentifier:@"segueAfterRecording" sender:self];
}

-(void) viewDidLoad {

    [super viewDidLoad];
    
    // Verbergen van de back button en een cancel knop in de plaats.
    [self.navigationItem setHidesBackButton:TRUE];
    
    UIBarButtonItem *leftBarButton = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemCancel target:self action:@selector(cancelRecording)];
    [self.navigationItem setLeftBarButtonItem:leftBarButton];
    
    NSArray *dirPaths;
    NSString *docsDir;
    
    dirPaths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    docsDir = [dirPaths objectAtIndex:0];
    NSString *soundFilePath = [docsDir
                               stringByAppendingPathComponent:@"sound.caf"];
    
    // Plaats waar je de geluidsfile gaat opslaan.
    NSURL *soundFileURL = [NSURL fileURLWithPath:soundFilePath];
    
    // De settings waarmee je wil opslaan.
    NSDictionary *recordSettings = [NSDictionary
                                    dictionaryWithObjectsAndKeys:
                                    [NSNumber numberWithInt:kAudioFormatAppleIMA4],
                                    AVFormatIDKey,
                                    [NSNumber numberWithInt: 1],
                                    AVNumberOfChannelsKey,
                                    [NSNumber numberWithFloat:44100.0],
                                    AVSampleRateKey,
                                    nil];
    
    NSError *error = nil;
    
    // Maak een nieuwe audio recorder aan.
    audioRecorder = [[AVAudioRecorder alloc]
                     initWithURL:soundFileURL
                     settings:recordSettings
                     error:&error];
    
    if (error)
    {
        NSLog(@"error: %@", [error localizedDescription]);
        
    } else {
        [audioRecorder prepareToRecord];
    }
}



/* Deze methode wordt aangeroepen net voordat de segue wordt uitgevoerd. We gebruiken deze om het model door te geven aan de volgende controller.
 */
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Als de gebruiker op de knop record heeft gedrukt, moet een viewcontroller aangemaakt worden.
    if ([segue.identifier isEqualToString:@"segueAfterRecording"]) {
        StartViewController *newController = segue.destinationViewController;
        // the segue will do the work of putting the new controller on screen
        // We geven het model door aan de volgende controller.
        newController.model = self.model;
    }
}

/* Deze methode wordt aangeroepen wanneer de gebruiker op start drukt en start het opnemen van het geluid.
 */
-(IBAction)recordAudio
{
    if (!audioRecorder.recording)
    {
        // Start het opnemen.
        [audioRecorder record];
        // Start de timer die aftelt vanaf 5 seconden.
        _countDownTimer = [NSTimer scheduledTimerWithTimeInterval:1.0 target:self selector:@selector(countDown) userInfo:nil repeats:YES];
        numberDisplayedCountingDown = 5;
    }
}

-(void)audioRecorderDidFinishRecording:(AVAudioRecorder *)recorder
                          successfully:(BOOL)flag
{
}
-(void)audioRecorderEncodeErrorDidOccur:(AVAudioRecorder *)recorder
                                  error:(NSError *)error
{
    NSLog(@"Encode Error occurred");
}

@end
