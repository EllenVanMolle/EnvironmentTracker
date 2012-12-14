//
//  ResultsViewController.m
//  EnvironmentTracker
//
//  Created by student on 13/11/12.
//  Copyright (c) 2012 multimedia. All rights reserved.
//

#import "ResultsViewController.h"
#import "CoreData/CoreData.h"
#import "Foundation/Foundation.h"
#import "Observation.h"

@implementation ResultsViewController

@synthesize labelIdentifier = _labelIdentifier;
@synthesize labelDate = _labelDate;
@synthesize center = _center;
@synthesize model = _model;

/* Deze methode wordt aangeroepen wanneer de database niet klaar is.
 */
-(void) databaseNotReady {
    // Verwider jewelf als observer van de notificatie dat de database klaar is.
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

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(IBAction)viewResults {
    NSFetchRequest *request = [NSFetchRequest fetchRequestWithEntityName:@"Observation"];
    request.fetchBatchSize = 20;
    request.fetchLimit = 100;
    //request.sortDescriptors = [NSArray arrayWithObject:sortDescriptor];

    NSError *error;
    NSArray *observations = [self.model.context executeFetchRequest:request error:&error];
    Observation *lastObservation = [observations lastObject];
    NSDate *date = lastObservation.date;
    // format it
    NSDateFormatter *dateFormat = [[NSDateFormatter alloc]init];
    [dateFormat setDateFormat:@"HH:mm:ss zzz"];
    
    // convert it to a string
    NSString *dateString = [dateFormat stringFromDate:date];
    [_labelDate setText:dateString];
}

@end
