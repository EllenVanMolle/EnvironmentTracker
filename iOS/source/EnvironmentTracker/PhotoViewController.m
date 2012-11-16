//
//  PhotoViewController.m
//  EnvironmentTracker
//
//  Created by ariadne on 28/10/12.
//  Copyright (c) 2012 ariadne. All rights reserved.
//

#import "PhotoViewController.h"

@implementation PhotoViewController

-(void) viewDidLoad {
    [super viewDidLoad];
    [self.navigationItem setHidesBackButton:TRUE];
    
    //UIBarButtonItem *leftBarButton = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemCancel target:self action:@selector()];
    //[self.navigationItem setLeftBarButtonItem:leftBarButton];
    //[leftBarButton release];
}

-(IBAction)takeAPicture
    {
        //NSLog(@"Methode takeAPicture");
        UIImagePickerController *  picker = [[UIImagePickerController alloc] init];
        picker.delegate = self;
        // Controleer of de device de benodigde resources heeft 
        if([UIImagePickerController isSourceTypeAvailable:UIImagePickerControllerSourceTypeCamera])
        {
            // We willen de camera openen.
            picker.sourceType = UIImagePickerControllerSourceTypeCamera;
            
            //Then present modal view controller
//            [self presentModalViewController:picker animated:YES];
            [self presentViewController:picker animated:YES completion:nil];
        }
        else
        {
            NSLog(@"Geen camera");
        }
    }

#pragma mark imagePickerDelegate
- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info
{
	// Dismiss imagePickerViewController
	//[picker dismissModalViewControllerAnimated:YES];
    [picker dismissViewControllerAnimated:YES completion:nil];
}
- (void)imagePickerControllerDidCancel:(UIImagePickerController *)picker
{
	// Dismiss the image selection and close the program
	//[picker dismissModalViewControllerAnimated:YES];
    [picker dismissViewControllerAnimated:YES completion:nil];
	
}

@end
