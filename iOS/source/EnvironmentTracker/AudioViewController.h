//
//  AudioViewController.h
//  EnvironmentTracker
//
//  Created by student on 31/10/12.
//  Copyright (c) 2012 multimedia. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <AVFoundation/AVFoundation.h>
#import <AVFoundation/AVAudioRecorder.h>

@interface AudioViewController : UIViewController <AVAudioRecorderDelegate>
{
    AVAudioRecorder *audioRecorder;
    int numberDisplayedCountingDown;
}

@property (weak, nonatomic) IBOutlet UIButton *startRecordingButton;
@property (nonatomic) IBOutlet UILabel *labelCountingDown;
@property (nonatomic) NSTimer *countDownTimer;

-(IBAction)recordAudio;

@end
