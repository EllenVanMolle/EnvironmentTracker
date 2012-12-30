//
//  SettingsViewController.m
//  EnvironmentTracker
//
//  Created by student on 08/12/12.
//  Copyright (c) 2012 multimedia. All rights reserved.
//

#import "SettingsViewController.h"
#import "StartViewController.h"
#import "MoodViewController.h"

@implementation SettingsViewController

@synthesize notificationSwitch = _notificationSwitch;
@synthesize intervalTextField = _intervalTextField;
@synthesize model = _model;

/* Method to make sure the keyboard dissapears when the background is touched.
 * Necessary because there is no BACK button on this type of keyboard
 */
- (void)touchesEnded:(NSSet *)touches withEvent:(UIEvent *)event
{
    [self.intervalTextField resignFirstResponder];
}

/* This method is called when the user hits the save button. It uses a NSUserDefaults object
 * to save the user's preferences.
 */
-(void) save {
    
    // First we need to hide the keyboard of the intervalTextField
    [self.intervalTextField resignFirstResponder];
    
    // Create an boolean to store the value of the notificationSwitch
    BOOL notification = self.notificationSwitch.on;
    NSLog(@"notification: %@\n", (notification ? @"YES" : @"NO"));
    
    // Create an integer to store the text info from the intervalTextField
    int interval = [[self.intervalTextField text] integerValue];
    NSLog(@"interval is %i", interval);
    
    /* Store the data with an NSUserDefaults object, which stores the preferences in
     * the IOS default system. This way it's made persistent through sessions and
     * available throughout the code
     */
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    [defaults setBool:notification forKey:@"notification"];
    [defaults setInteger:interval forKey:@"interval"];
    [defaults synchronize];
    
    // after saving the data we startup a first notification taking into account the user settings.
    [self.model startUpNextNotification];
    
    [self performSegueWithIdentifier:@"openHomeFromSettings" sender:self];
}

/* Method called when the switch is touched to enable the IntervalTextField
 */
-(void) toggleEnableIntervalTextField:(id)sender{
    
    // disable/ enable the interval textfield depending on the value of notification
    if (self.notificationSwitch.on) {
        [self.intervalTextField setUserInteractionEnabled:YES];
    } else {
        [self.intervalTextField setUserInteractionEnabled:NO];
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

/* Method called when the view is loaded. Here we will set the intervalTextField and
 * notificationSwitch in the UI to the right, stored user preferences.
 */
- (void)viewDidLoad
{
    // Get the stored data before the view loads by calling the standard UserDefault function
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    BOOL notification = [defaults boolForKey:@"notification"];
    int interval = [defaults integerForKey:@"interval"];
    
    // Because we're working with an TextField the int should be transformed to a string
    NSString *intervalString = [NSString stringWithFormat:@"%i",interval];
    
    /* Note that boolean variabels are always initialised to false and integers to 0.
     So if the user hasn't saved any settings before, the values will be false and 0. */
    
    // change the value of the interval it it's 0 to the default 120
    if (interval == 0) {
        interval = 120;
    };
    
    // Update the UI elements with the saved data
    self.notificationSwitch.on = notification;
    self.intervalTextField.text = intervalString;
    
    // we set the keypad so the user can only enter integers
    [self.intervalTextField setKeyboardType:UIKeyboardTypeNumberPad];
    /* Because in the NumberPad keyboard there's no return button we implemented a method
     that hides the keyboard when the background is touched, when save button is hit */
    
    // disable/ enable the interval textfield depending on the value of notification
    if (notification) {
        [self.intervalTextField setUserInteractionEnabled:YES];
    } else {
        [self.intervalTextField setUserInteractionEnabled:NO];
    }
    
    [super viewDidLoad];
    
    // make sure the navigatiebar is displaid
    [self.navigationController setNavigationBarHidden:NO animated:YES];
    
    // make sure the toolbar is displaid
    [self.navigationController setToolbarHidden:NO animated:YES];
    
    // add the saveButton to the toolbar
    UIBarButtonItem *saveButton = [[UIBarButtonItem alloc] initWithTitle:@"Save" style:UIBarButtonItemStyleBordered  target:self action:@selector(save)];
    
    [self setToolbarItems:[NSArray arrayWithObjects:saveButton, nil]];
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/* Deze methode wordt aangeroepen net voordat de segue wordt uitgevoerd. We gebruiken deze om het model door te geven aan de volgende controller.*/
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Als de gebruiker op de save button klikt wordt de home pagina geopend
    if ([segue.identifier isEqualToString:@"openHomeFromSettings"]) {
        StartViewController *newController = segue.destinationViewController;
        // the segue will do the work of putting the new controller on screen
        // We geven het model door aan de volgende controller.
        newController.model = self.model;
    } else if ([segue.identifier isEqualToString:@"startRecording"]) {
        MoodViewController *newController = segue.destinationViewController;
        // the segue will do the work of putting the new controller on screen
        // We geven het model door aan de volgende controller.
        newController.model = self.model;
    }
}

@end
