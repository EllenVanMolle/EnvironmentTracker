//
//  PieChartUnhappyViewController.m
//  EnvironmentTracker
//
//  Created by student on 12/12/12.
//  Copyright (c) 2012 multimedia. All rights reserved.
//

#import "PieChartUnhappyViewController.h"
#import "ColorViewController.h"
#import "MoodViewController.h"

@implementation PieChartUnhappyViewController

@synthesize toolbar = _toolbar;
@synthesize hostView = _hostView;
@synthesize selectedTheme = _selectedTheme;
@synthesize model = _model;

#pragma mark - UIViewController lifecycle methods
-(void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    // The plot is initialized here, since the view bounds have not transformed for landscape until now
    [self initPlot];
}

- (void)didRotateFromInterfaceOrientation:(UIInterfaceOrientation)fromInterfaceOrientation
{
    [self initPlot];
}

-(IBAction)themeTapped:(id)sender {
    UIActionSheet *actionSheet = [[UIActionSheet alloc] initWithTitle:@"Apply a Theme" delegate:self cancelButtonTitle:@"Cancel" destructiveButtonTitle:nil otherButtonTitles:@"Dark Gradient", @"Plain Black", @"Plain White",@"Slate", @"Name Stocks", nil];
    [actionSheet showFromTabBar:self.tabBarController.tabBar];
}

#pragma mark - Chart behavior
-(void)initPlot {
    [self configureHost];
    [self configureGraph];
    [self configureChart];
}

-(void)configureHost {
    
    // Set up view frame
    CGRect parentRect = self.view.bounds;
    CGSize toolbarSize = self.toolbar.bounds.size;
    parentRect = CGRectMake(parentRect.origin.x,
                            (parentRect.origin.y + toolbarSize.height),
                            parentRect.size.width,
                            (parentRect.size.height - toolbarSize.height));
    
    // Create host view
    self.hostView = [(CPTGraphHostingView *) [CPTGraphHostingView alloc] initWithFrame:parentRect];
    // self.hostView.allowPinchScaling = NO;
    [self.view addSubview:self.hostView];
}

-(void)configureGraph {
    
    // Create and initialize graph
    CPTGraph *graph = [[CPTXYGraph alloc] initWithFrame:self.hostView.bounds];
    self.hostView.hostedGraph = graph;
    graph.paddingLeft = 0.0f;
    graph.paddingTop = 0.0f;
    graph.paddingRight = 0.0f;
    graph.paddingBottom = 0.0f;
    graph.axisSet = nil;
    
    // Set up text style
    CPTMutableTextStyle *textStyle = [CPTMutableTextStyle textStyle];
    textStyle.color = [CPTColor grayColor];
    textStyle.fontName = @"Helvetica-Bold";
    textStyle.fontSize = 16.0f;
    
    // Configure title
    NSString *title = @"% of every color when you're unhappy!";
    graph.title = title;
    graph.titleTextStyle = textStyle;
    graph.titlePlotAreaFrameAnchor = CPTRectAnchorTop;
    graph.titleDisplacement = CGPointMake(0.0f, -12.0f);
    
    // Set theme
    if (self.selectedTheme == nil) {
        self.selectedTheme = [CPTTheme themeNamed:kCPTPlainWhiteTheme];
    }
    
    [graph applyTheme:self.selectedTheme];
    
}

-(void)configureChart {
    // 1 - Get reference to graph
    CPTGraph *graph = self.hostView.hostedGraph;
    // 2 - Create chart
    CPTPieChart *pieChart = [[CPTPieChart alloc] init];
    pieChart.dataSource = self;
    pieChart.delegate = self;
    pieChart.pieRadius = (self.hostView.bounds.size.height * 0.65) / 2;
    pieChart.identifier = graph.title;
    pieChart.startAngle = M_PI_4;
    pieChart.sliceDirection = CPTPieDirectionClockwise;
    // 3 - Create gradient
    CPTGradient *overlayGradient = [[CPTGradient alloc] init];
    overlayGradient.gradientType = CPTGradientTypeRadial;
    overlayGradient = [overlayGradient addColorStop:[[CPTColor blackColor] colorWithAlphaComponent:0.0] atPosition:0.9];
    overlayGradient = [overlayGradient addColorStop:[[CPTColor blackColor] colorWithAlphaComponent:0.4] atPosition:1.0];
    pieChart.overlayFill = [CPTFill fillWithGradient:overlayGradient];
    // 4 - Add chart to graph
    [graph addPlot:pieChart];
}


- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

-(void) viewDidLoad {
    [super viewDidLoad];
    // make sure the toolbar is not displaid
    [self.navigationController setToolbarHidden:YES];
    
    // hide the navigationbar because only one can be on top either the UINavigationBarController or either the UITabBarController
    [self.navigationController setNavigationBarHidden:YES];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - CPTPlotDataSource methods
/* Method that returns how many slices that should be displaid
 */
-(NSUInteger)numberOfRecordsForPlot:(CPTPlot *)plot {
    NSUInteger numberSlices = [self.model.hueUnhappy count];
    return numberSlices; // This should always be for because there are four Hue categories
}

/* This method returns a number
 */
-(NSNumber *)numberForPlot:(CPTPlot *)plot field:(NSUInteger)fieldEnum recordIndex:(NSUInteger)index {
    
    // Look for CPTPieChartFieldsSlideWidth
    if (CPTPieChartFieldSliceWidth == fieldEnum)
    {   
        NSNumber *sliceValue = [self.model.hueUnhappy objectAtIndex:(index)];
        return sliceValue;
    }
    return [NSDecimalNumber zero];
}

/* This method gives the right color to every slice
 */
- (CPTFill *) sliceFillForPieChart:(CPTPieChart *)pieChart recordIndex:(NSUInteger)index;
{
    CPTColor *sliceColor;
    if (index == 0){ // yellow (hue=45)
        sliceColor = [CPTColor colorWithComponentRed:255.0f/255.0f green:210.0f/255.0f blue:77.0f/255.0f alpha:1.0f];
    } else if (index == 1){ // green (hue=135)
        sliceColor = [CPTColor colorWithComponentRed:0.0f/255.0f green:128.0f/255.0f blue:32.0f/255.0f alpha:1.0f];
    } else if (index == 2){ // blue (hue=225)
        sliceColor = [CPTColor colorWithComponentRed:31.0f/255.0f green:65.0f/255.0f blue:153.0f/255.0f alpha:1.0f];
    } else { // purple (hue=315)
        sliceColor = [CPTColor colorWithComponentRed:179.0f/255.0f green:36.0f/255.0f blue:143.0f/255.0f alpha:1.0f];
    }
    CPTFill *slicefill = [CPTFill fillWithColor:sliceColor];
    return slicefill;
}

-(CPTLayer *)dataLabelForPlot:(CPTPlot *)plot recordIndex:(NSUInteger)index {
    // Define label text style
    static CPTMutableTextStyle *labelText = nil;
    if (!labelText) {
        labelText= [[CPTMutableTextStyle alloc] init];
        labelText.color = [CPTColor grayColor];
    }
    
    NSDecimalNumber *totalValue = [NSDecimalNumber zero];
    for (NSNumber *sliceValue in self.model.hueUnhappy) {
        totalValue = [totalValue decimalNumberByAdding:[NSDecimalNumber decimalNumberWithString:[sliceValue stringValue]]];
    };
    
    // Calculate percentage value
    NSNumber *sliceValue = [self.model.hueUnhappy objectAtIndex:index];
    NSDecimalNumber *percent;
    if ([totalValue intValue] != 0){
        percent = [[NSDecimalNumber decimalNumberWithString:[sliceValue stringValue]] decimalNumberByDividingBy:totalValue];
    } else {
        percent = [NSDecimalNumber decimalNumberWithString:@"0"];
    }
    
    // Set up display label
    NSString *labelValue = [NSString stringWithFormat:@"%0.1f %%",([percent floatValue] * 100.0f)];
    
    // Create and return layer with label text
    return [[CPTTextLayer alloc] initWithText:labelValue style:labelText];

}

-(NSString *)legendTitleForPieChart:(CPTPieChart *)pieChart recordIndex:(NSUInteger)index {
    return @"";
}


#pragma mark - UIActionSheetDelegate methods
-(void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex {
    // 1 - Get title of tapped button
    NSString *title = [actionSheet buttonTitleAtIndex:buttonIndex];
    // 2 - Get theme identifier based on user tap
    NSString *themeName = kCPTPlainWhiteTheme;
    if ([title isEqualToString:@"Dark Gradient"] == YES) {
        themeName = kCPTDarkGradientTheme;
    } else if ([title isEqualToString:@"Plain Black"] == YES) {
        themeName = kCPTPlainBlackTheme;
    } else if ([title isEqualToString:@"Plain White"] == YES) {
        themeName = kCPTPlainWhiteTheme;
    } else if ([title isEqualToString:@"Slate"] == YES) {
        themeName = kCPTSlateTheme;
    } else if ([title isEqualToString:@"Name Stocks"] == YES) {
        themeName = kCPTStocksTheme;
    }
    // 3 - Apply new theme
    [self.hostView.hostedGraph applyTheme:[CPTTheme themeNamed:themeName]];
    
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Als de gebruiker op de knop Hue Chart heeft gedrukt, moet een viewcontroller aangemaakt worden.
    if ([segue.identifier isEqualToString:@"goToColor"]) {
        ColorViewController *newController = segue.destinationViewController;
        // the segue will do the work of putting the new controller on screen
        // We geven het model door aan de volgende controller.
        newController.model = self.model;
        // Als de gebruiker op de knop Saturation Chart heeft gedrukt, moet een resultsviewcontroller aangemaakt worden.
    } else if ([segue.identifier isEqualToString:@"startRecording"]) {
        MoodViewController *newController = segue.destinationViewController;
        // the segue will do the work of putting the new controller on screen
        // We geven het model door aan de volgende controller.
        newController.model = self.model;
        // Als de gebruiker op de knop Saturation Chart heeft gedrukt, moet een resultsviewcontroller aangemaakt worden.
    }
}


@end

