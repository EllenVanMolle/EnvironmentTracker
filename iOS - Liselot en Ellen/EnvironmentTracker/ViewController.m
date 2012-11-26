//
//  ViewController.m
//  EnvironmentTracker
//
//  Created by ariadne on 28/10/12.
//  Copyright (c) 2012 ariadne. All rights reserved.
//

#import "ViewController.h"

@implementation ViewController

@synthesize moodSlider = _moodSlider;
@synthesize continueFromMood = _continueFromMood;
@synthesize labelMood = _labelMood;
@synthesize model = _model;
//@synthesize database = _database;
//@synthesize context = _context;

// Private methods

// Public methods

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
    
    [self.navigationItem setHidesBackButton:TRUE];
    
   // UIBarButtonItem *leftBarButton = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemCancel target:self action:@selector()];
    //[self.navigationItem setLeftBarButtonItem:leftBarButton];
    //[leftBarButton release];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(IBAction)changeLabel:(UISlider *)sender {
    if ([sender value] < 4.0) {
        [_labelMood setText:@"Ongelukkig"];
    } else if ([sender value] < 8.0) {
        [_labelMood setText:@"Neutraal"];
    } else {
        [_labelMood setText:@"Gelukkig"];
    }
}

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

-(void) saveMood {
    [self.model saveObservationWithID:[self makeIdentifier] WithMood:[self mood] WithDate:[self date] WithBreedteligging:[self determineLocationBreedteLigging] WithLengteligging:[self determineLocationLengteLigging]];
}

@end
