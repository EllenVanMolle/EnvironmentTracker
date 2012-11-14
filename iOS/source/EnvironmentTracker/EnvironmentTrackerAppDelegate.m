//
//  EnvironmentTrackerAppDelegate.m
//  EnvironmentTracker
//
//  Created by student on 31/10/12.
//  Copyright (c) 2012 multimedia. All rights reserved.
//

#import "EnvironmentTrackerAppDelegate.h"

@implementation EnvironmentTrackerAppDelegate

@synthesize window = _window;
@synthesize audioViewController = _audioViewController;
@synthesize viewController = _viewController;
//@synthesize navigationController = _navigationController;


#pragma mark -
#pragma mark Application lifecycle

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    
    // Add the view controller's view to the window and display.
    [_window addSubview:self.viewController.view];
    [_window makeKeyAndVisible];
    
    // Override point for customization after application launch.
    // Wordt aangeroepen als de applicatie niet aan het lopen is en de notificatie komt en de gebruiker drukt ok.
    // Controleren of de notificaties ondersteund worden.
    Class cls = NSClassFromString(@"UILocalNotification");
    if (cls) {
        UILocalNotification *notification = [launchOptions objectForKey:
                                             UIApplicationLaunchOptionsLocalNotificationKey];
        
        if (notification) {
            NSLog(@"Notificatie na afsluiten");
            UINavigationController *navigationController = self.window.rootViewController;
            [navigationController.topViewController performSegueWithIdentifier:@"startRecording" sender:self];
        }
    }
    
    application.applicationIconBadgeNumber = 0;
    

    
    return YES;
}

- (void)applicationWillResignActive:(UIApplication *)application {
    /*
     Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
     Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
     */
}


- (void)applicationDidEnterBackground:(UIApplication *)application {
    /*
     Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
     If your application supports background execution, called instead of applicationWillTerminate: when the user quits.
     */
}


- (void)applicationWillEnterForeground:(UIApplication *)application {
    /*
     Called as part of  transition from the background to the inactive state: here you can undo many of the changes made on entering the background.
     */
}


- (void)applicationDidBecomeActive:(UIApplication *)application {
    /*
     Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
     */
}


- (void)applicationWillTerminate:(UIApplication *)application {
    /*
     Called when the application is about to terminate.
     See also applicationDidEnterBackground:.
     */
}

// Applicatie is op de achtergrond of de voorgrond wanneer de notificatie fired.
- (void)application:(UIApplication *)application didReceiveLocalNotification:(UILocalNotification *)notification {
    NSLog(@"Applicatie op voorgrond of achtergrond bij ontvangen van notificatie");
    application.applicationIconBadgeNumber = 0;
    
    UINavigationController *navigationController = self.window.rootViewController;
    UIViewController *topViewController = navigationController.topViewController;
    UIView *topView = topViewController.view;
    if (![topView.accessibilityLabel isEqual:@"recordMoodView"]
        && ![topView.accessibilityLabel isEqual:@"recordPhoto"]
        && ![topView.accessibilityLabel isEqualToString:@"recordSound"]) {
        NSLog(@"Niet op record mood");
        [topViewController performSegueWithIdentifier:@"startRecording" sender:self];
    } else {
        NSLog(@"Geen notificatie want al aan het opnemen.");
    }
    
}


#pragma mark -
#pragma mark Memory management

- (void)applicationDidReceiveMemoryWarning:(UIApplication *)application {
    /*
     Free up as much memory as possible by purging cached data objects that can be recreated (or reloaded from disk) later.
     */
}

@end
