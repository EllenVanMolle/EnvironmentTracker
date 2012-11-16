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
@synthesize database = _database;
//@synthesize context = _context;

// Private methods

- (EnvironmentTrackerModel *) model {
    if (!model) model = [[EnvironmentTrackerModel alloc] init];
    return model;
}

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
    //NSLog(@"Slider bewogen");
    if ([sender value] < 3.0) {
        [_labelMood setText:@"Ongelukkig"];
    } else if ([sender value] < 7.0) {
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

-(void) addObservationToDatabase {
    NSManagedObjectContext *context = self.database.managedObjectContext;
    
    Observation *observation =
    [NSEntityDescription insertNewObjectForEntityForName:@"Observation"
                                  inManagedObjectContext:context];
    observation.observationID = [NSNumber numberWithInt:[self makeIdentifier]];
    observation.mood = [NSNumber numberWithInt:[self mood]];
    observation.lengteligging = [NSNumber numberWithInt:[self determineLocationLengteLigging]];
    observation.breedteligging = [NSNumber numberWithInt:[self determineLocationBreedteLigging]];
    observation.date = [self date];
    
    NSNotificationCenter *center = [NSNotificationCenter defaultCenter];
    [center removeObserver:self
                      name:UIDocumentStateChangedNotification
                    object:self.database];
    
}

-(IBAction)saveMood {
    if (self.database.documentState == UIDocumentStateNormal) {
        [self addObservationToDatabase];
    } else {
        NSNotificationCenter *center = [NSNotificationCenter defaultCenter];
        [center addObserver:self
                   selector:@selector(addObservationToDatabase)
                       name:UIDocumentStateChangedNotification
                     object:self.database];
    }
}

@end
