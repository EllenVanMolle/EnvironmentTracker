//
//  AudioViewController.m
//  EnvironmentTracker
//
//  Created by student on 31/10/12.
//  Copyright (c) 2012 multimedia. All rights reserved.
//

#import "AudioViewController.h"
//#import <AVFoundation/AVFoundation.h>
//#import <AVFoundation/AVAudioSettings.h>
//#import <CoreAudio/CoreAudioTypes.h>
//#import <AVFoundation/AVAudioRecorder.h>

@implementation AudioViewController

@synthesize startRecordingButton = _startRecordingButton;
@synthesize labelCountingDown = _labelCountingDown;
@synthesize countDownTimer = _countDownTimer;

// Private methods

-(void)stop
{
    NSLog(@"Stop recording audio");
    //_startRecordingButton.enabled = YES;
    
    if (audioRecorder.recording)
    {
        [audioRecorder stop];
        [self performSegueWithIdentifier:@"segueAfterRecording" sender:self];
    }
}

-(void)countDown {
    numberDisplayedCountingDown = numberDisplayedCountingDown - 1;
    [_labelCountingDown setText: [NSString stringWithFormat:@"%d", numberDisplayedCountingDown]];
    if (numberDisplayedCountingDown == 0) {
        [_countDownTimer invalidate];
        [self stop];
    }
}

// Public methods

-(void)viewDidLoad {

    [super viewDidLoad];
    
    // Verbergen van de back button en eventueel een cancel knop in de plaats.
    
    [self.navigationItem setHidesBackButton:TRUE];
    
    //UIBarButtonItem *leftBarButton = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemCancel target:self action:@selector()];
    //[self.navigationItem setLeftBarButtonItem:leftBarButton];
    //[leftBarButton release];
    
    NSArray *dirPaths;
    NSString *docsDir;
    
    dirPaths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    docsDir = [dirPaths objectAtIndex:0];
    NSString *soundFilePath = [docsDir
                               stringByAppendingPathComponent:@"sound.caf"];
    
    NSURL *soundFileURL = [NSURL fileURLWithPath:soundFilePath];
    
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

-(IBAction)recordAudio
{
    if (!audioRecorder.recording)
    {
        [audioRecorder record];
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
