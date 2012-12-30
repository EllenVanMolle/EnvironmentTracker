//
//  hueChartViewController.m
//  EnvironmentTracker
//
//  Created by student on 12/12/12.
//  Copyright (c) 2012 multimedia. All rights reserved.
//

#import "hueChartViewController.h"
#import "MoodViewController.h"

@implementation hueChartViewController

// declaration of a few constant values
CGFloat const CPDBarWidth = 0.65f;
CGFloat const CPDBar = 0.25f;

@synthesize hostView    = _hostView;
@synthesize selectedTheme = _selectedTheme;
@synthesize model = _model;
@synthesize themeButton = _themeButton;

-(void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    // The plot is initialized here, since the view bounds have not transformed for landscape until now
    [self initPlot];
}

/* Method called when screen is rotated. The graph is regenerated
 */
- (void)didRotateFromInterfaceOrientation:(UIInterfaceOrientation)fromInterfaceOrientation
{
    [self initPlot];
}

/* Method called when view is loaded
 */
-(void)viewDidLoad {
    [super viewDidLoad];
    
    // make sure the navigatiebar is displaid
    [self.navigationController setNavigationBarHidden:NO animated:YES];
    
    // make sure the toolbar is displaid
    [self.navigationController setToolbarHidden:NO animated:YES] ;
    
    // add the themeButton to the toolbar
    self.themeButton = [[UIBarButtonItem alloc] initWithTitle:@"theme" style:UIBarButtonItemStyleBordered target:self action:@selector(themeTapped)];
    
    [self setToolbarItems:[NSArray arrayWithObjects:self.themeButton, nil]];
    
}

/* Method called when theme button in the toolbar is tapped. An actionsheet is generated from the tabbar that allows the user to change the theme of the graph
 */
- (void) themeTapped {
    UIActionSheet *actionSheet = [[UIActionSheet alloc] initWithTitle:@"Apply a Theme" delegate:self cancelButtonTitle:@"Cancel" destructiveButtonTitle:nil otherButtonTitles:@"Dark Gradient", @"Plain Black", @"Plain White",@"Slate", @"Name Stocks", nil];
    [actionSheet showFromBarButtonItem:self.themeButton animated:YES];
}

#pragma mark - Chart behavior
-(void)initPlot {
    [self configureHost];
    [self configureGraph];
    [self configurePlots];
    [self configureAxes];
}

-(void) configureHost {
        
    // Set up view frame
    CGRect parentRect = self.view.bounds;

    // Create host view
    self.hostView = [(CPTGraphHostingView *) [CPTGraphHostingView alloc] initWithFrame:parentRect];
    //self.hostView.allowPinchScaling = NO;
    [self.view addSubview:self.hostView];
}

-(void)configureGraph {
    // Create the graph
    CPTGraph *graph = [[CPTXYGraph alloc] initWithFrame:self.hostView.bounds];
    graph.plotAreaFrame.masksToBorder = NO;
    
    // Configure the grap
    self.hostView.hostedGraph = graph;
    graph.paddingLeft = 50.0f;
    graph.paddingTop = 0.0f;
    graph.paddingRight = 0.0f;
    graph.paddingBottom = 70.0f;
    
    self.hostView.hostedGraph = graph;
    
    // Set up plot space
    CGFloat xMin = 0.0f;
    CGFloat xMax = 4.0f;  // maximum value for hue (number of categories)
    CGFloat yMin = 0.0f;
    CGFloat yMax = 11.0f;  // maximum value for the mood
    CPTXYPlotSpace *plotSpace = (CPTXYPlotSpace *) graph.defaultPlotSpace;
    plotSpace.xRange = [CPTPlotRange plotRangeWithLocation:CPTDecimalFromFloat(xMin) length:CPTDecimalFromFloat(xMax)];
    plotSpace.yRange = [CPTPlotRange plotRangeWithLocation:CPTDecimalFromFloat(yMin) length:CPTDecimalFromFloat(yMax)];
    
    // Apply right theme
    self.selectedTheme = [CPTTheme themeNamed:kCPTPlainWhiteTheme];
    if (self.selectedTheme == nil) {
        self.selectedTheme = [CPTTheme themeNamed:kCPTPlainWhiteTheme];
    }
    
    [graph applyTheme:self.selectedTheme];
    // Configure styles
    
}

-(void)configurePlots {
    
    // Set up plots
    CPTBarPlot *barPlot = [CPTBarPlot tubularBarPlotWithColor:[CPTColor cyanColor] horizontalBars:NO];
    
    // Set up line style
    CPTMutableLineStyle *barLineStyle = [[CPTMutableLineStyle alloc] init];
    barLineStyle.lineColor = [CPTColor darkGrayColor];
    barLineStyle.lineWidth = 0.5;
    
        barPlot.dataSource = self;
        barPlot.delegate = self;
        barPlot.barWidth = CPTDecimalFromDouble(CPDBarWidth);
        barPlot.barOffset = CPTDecimalFromDouble(CPDBar);
        barPlot.lineStyle = barLineStyle;
    
    // Add plots to graph
    CPTGraph *graph = self.hostView.hostedGraph;
    
    // Add plot to graph
    [graph addPlot:barPlot toPlotSpace:graph.defaultPlotSpace];
    
}

/* Method called to configure the axes */
-(void)configureAxes {
    CPTMutableTextStyle *axisTitleStyle = [CPTMutableTextStyle textStyle];
    axisTitleStyle.color = [CPTColor grayColor];
    axisTitleStyle.fontName = @"Helvetica-Bold";
    axisTitleStyle.fontSize = 12.0f;
    CPTMutableLineStyle *axisLineStyle = [CPTMutableLineStyle lineStyle];
    axisLineStyle.lineWidth = 2.0f;
    axisLineStyle.lineColor = [[CPTColor grayColor] colorWithAlphaComponent:1];
    
    // Get the graph's axis set
    CPTXYAxisSet *axisSet = (CPTXYAxisSet *) self.hostView.hostedGraph.axisSet;
    
    // Configure the x-axis
    CPTAxis *x = axisSet.xAxis;
    x.title = @"Hue Categories";
    x.titleTextStyle = axisTitleStyle;
    x.titleOffset = 50.0f;
    x.axisLineStyle = axisLineStyle;
    x.labelRotation = M_PI/4;
    x.labelingPolicy = CPTAxisLabelingPolicyNone;
    NSArray *customTickLocations =  [NSArray arrayWithObjects:[NSDecimalNumber numberWithFloat:0.25],[NSDecimalNumber numberWithFloat:1.25], [NSDecimalNumber numberWithFloat:2.25], [NSDecimalNumber numberWithFloat:3.25], nil];
    NSArray *xAxisLabels = [NSArray arrayWithObjects:@"Vigorous", @"Nature", @"Ocean", @"Flower", nil];
    NSUInteger labelLocation = 0;
    NSMutableArray *customLabels = [NSMutableArray arrayWithCapacity:[xAxisLabels count]];
    for (NSNumber *tickLocation in customTickLocations) {
        CPTAxisLabel *newLabel = [[CPTAxisLabel alloc] initWithText: [xAxisLabels objectAtIndex:labelLocation++]
                                                          textStyle:x.labelTextStyle];
        newLabel.tickLocation = [tickLocation decimalValue];
        newLabel.offset = x.labelOffset + x.majorTickLength;
        newLabel.rotation = M_PI/4;
        [customLabels addObject:newLabel];
    };
    x.axisLabels =  [NSSet setWithArray:customLabels];
    
    // Configure the y-axis
    CPTAxis *y = axisSet.yAxis;
    y.labelingPolicy = CPTAxisLabelingPolicyFixedInterval;
    y.title = @"Mood";
    y.titleTextStyle = axisTitleStyle;
    y.titleOffset = 30.0f;
    y.axisLineStyle = axisLineStyle;
    y.majorTickLineStyle = nil;
    y.minorTickLineStyle = nil;
    y.majorIntervalLength = CPTDecimalFromString(@"2");
   
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - CPTPlotDataSource methods
/* Method that returns the number of bars in the plot*/
-(NSUInteger)numberOfRecordsForPlot:(CPTPlot *)plot {
    return 4;
}

/* Method that returns the value of every bar in the plot*/
-(NSNumber *)numberForPlot:(CPTPlot *)plot field:(NSUInteger)fieldEnum recordIndex:(NSUInteger)index {
    NSDecimalNumber *barValue = nil;
    if ((fieldEnum == CPTBarPlotFieldBarTip) && (index < 10)) {
        // NSLog(@"here we are");
        barValue = (NSDecimalNumber *)[self.model.hueMood objectAtIndex:(index)];
        
    } else {
        barValue = (NSDecimalNumber *)[NSDecimalNumber numberWithUnsignedInteger:index];}
    return barValue;
}

/* Method that gives every bar the right color*/
- (CPTFill *)barFillForBarPlot:(CPTBarPlot *)barPlot recordIndex:(NSUInteger)index {
    
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
    [self configureAxes];
    
}

/* Deze methode wordt aangeroepen net voordat de segue wordt uitgevoerd. We gebruiken deze om het model door te geven aan de volgende controller.*/
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    if ([segue.identifier isEqualToString:@"startRecording"]) {
        MoodViewController *newController = segue.destinationViewController;
        // the segue will do the work of putting the new controller on screen
        // We geven het model door aan de volgende controller.
        newController.model = self.model;
    }
}

@end
