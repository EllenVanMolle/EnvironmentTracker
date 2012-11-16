//
//  StartViewController.m
//  EnvironmentTracker
//
//  Created by student on 06/11/12.
//  Copyright (c) 2012 multimedia. All rights reserved.
//

#import "StartViewController.h"
#import "EnvironmentTrackerModel.h"
#import "ViewController.h"
#import "ResultsViewController.h"


@implementation StartViewController

@synthesize recordButton = _recordButton;
@synthesize viewResultsButton = _viewResultsButton;
@synthesize settingsButton = _settingsButton;
@synthesize database = _database;
@synthesize context = _context;
@synthesize model = _model;

-(void)databaseIsReady {
    NSLog(@"Database is ready");
    if (self.database.documentState == UIDocumentStateNormal){
        self.context = self.database.managedObjectContext;
        NSLog(@"Context is succesvol gedefinieerd");
    } else {
        NSLog(@"Database not ready");
    }
}

- (void) useDocument {
    if (![[NSFileManager defaultManager] fileExistsAtPath:[self.database.fileURL path]]) {
        // Het bestaat nog niet op de schijf, dus moeten ze het aanmaken.
        NSLog(@"De database bestaat nog niet.");
        [self.database saveToURL:self.database.fileURL forSaveOperation:UIDocumentSaveForCreating completionHandler:^(BOOL success) {
            if (success) {
                NSLog(@"De database is succesvol aangemaakt.");
                [self databaseIsReady];
            }
            if (!success) NSLog(@"De database is niet opgeslagen.");
        }];
    } else if (self.database.documentState == UIDocumentStateClosed) {
        // Het bestaat op de schijf maar het moet nog geopend worden.
        NSLog(@"De database moet nog geopend worden.");
        [self.database openWithCompletionHandler:^(BOOL success) {
            if (success) {
                NSLog(@"De database is succesvol geopend.");
                [self databaseIsReady];
            }
            if (!success) {
                NSLog(@"De database kan niet geopend worden.");
            }
        }];
    } else if (self.database.documentState == UIDocumentStateNormal) {
        // De database is klaar om te gebruiken.
        NSLog(@"De database is klaar om te gebruiken.");
        if (!self.context) {
            [self databaseIsReady];
        }
    }
}

- (void) setDatabase:(UIManagedDocument *)database {
    if (_database != database) {
        _database = database;
        [self useDocument];
    }
}

/* Deze methode wordt aangeroepen als de view wordt geinitialiseerd.*/
-(void) viewDidLoad {
    [super viewDidLoad];
    [self.navigationItem setHidesBackButton:TRUE];
    
    //UIBarButtonItem *leftBarButton = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemCancel target:self action:@selector()];
    //[self.navigationItem setLeftBarButtonItem:leftBarButton];
    //[leftBarButton release];
    
    // Het initialiseren en alloceren van het model als het nog niet bestaat.
    
    
    // Het openen van de database als deze nog niet geopend is.
    if (!self.database) {
        NSURL *url = [[[NSFileManager defaultManager] URLsForDirectory:NSDocumentDirectory inDomains:NSUserDomainMask] lastObject];
        url = [url URLByAppendingPathComponent:@"EnvironmentDatabase"];
        self.database = [[UIManagedDocument alloc] initWithFileURL:url]; // setter wordt hoger overschreven
    }
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    if ([segue.identifier isEqualToString:@"startRecording"]) {
        ViewController *newController = segue.destinationViewController;
        // send messages to newController to prepare it to appear on screen
        // the segue will do the work of putting the new controller on screen
        newController.database = self.database;
    } else if ([segue.identifier isEqualToString:@"viewResults"]) {
        ResultsViewController *newController = segue.destinationViewController;
        newController.database = self.database;
    }
}

-(IBAction)startUpNextNotification {
    NSLog(@"StartUpNextNotification");
    [[UIApplication sharedApplication] cancelAllLocalNotifications];
    
    // Nakijken of de juiste classes ondersteund worden.
    Class cls = NSClassFromString(@"UILocalNotification");
    if (cls != nil) {
        
        UILocalNotification *notif = [[cls alloc] init];
        notif.fireDate = [NSDate dateWithTimeIntervalSinceNow:30];
        notif.timeZone = [NSTimeZone defaultTimeZone];
        
        notif.alertBody = @"Time to record your mood";
        notif.alertAction = @"Record now";
        notif.soundName = UILocalNotificationDefaultSoundName;
        //Het huidige badgenummer wordt met 1 verhoogd.
        //notif.applicationIconBadgeNumber = 1 + [[UIApplication sharedApplication] applicationIconBadgeNumber];
        
        [[UIApplication sharedApplication] scheduleLocalNotification:notif];
    }

 }

@end