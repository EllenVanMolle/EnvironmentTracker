//
//  BrightnessChartViewController.m
//  EnvironmentTracker
//
//  Created by student on 12/12/12.
//  Copyright (c) 2012 multimedia. All rights reserved.
//

#import "BrightnessChartViewController.h"
#import "MoodViewController.h"

@implementation BrightnessChartViewController

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

/* Method called after view is loaded
 */
-(void)viewDidLoad {
    [super viewDidLoad];
    
    // make sure the navigatiebar is displaid
    [self.navigationController setNavigationBarHidden:NO];
    
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

/* Method called to configure the hostview for the graph*/
-(void) configureHost {
    
    // Set up view frame
    CGRect parentRect = self.view.bounds;
    
    // Create host view
    self.hostView = [(CPTGraphHostingView *) [CPTGraphHostingView alloc] initWithFrame:parentRect];
    //self.hostView.allowPinchScaling = NO;
    [self.view addSubview:self.hostView];
}

/* Method called to configure the graph*/
-(void)configureGraph {
    // Create the graph
    CPTGraph *graph = [[CPTXYGraph alloc] initWithFrame:self.hostView.bounds];
    graph.plotAreaFrame.masksToBorder = NO;
    
    // Configure the grap
    self.hostView.hostedGraph = graph;
    graph.paddingLeft = 50.0f;
    graph.paddingTop = 0.0f;
    graph.paddingRight = 0.0f;
    graph.paddingBottom = 50.0f;
    
    self.hostView.hostedGraph = graph;
    
    // Set up plot space
    CGFloat xMin = 0.0f;
    CGFloat xMax = 100.0f; // maximum value for brightness
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
   
    
}

/* Method called to configure the plot */
-(void)configurePlots {
    
    // Set up plots
    CPTBarPlot *barPlot = [CPTBarPlot tubularBarPlotWithColor:[CPTColor cyanColor] horizontalBars:YES];
    
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

/* Method called to create and configure the axes */
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
    x.title = @"Brightness";
    x.titleTextStyle = axisTitleStyle;
    x.titleOffset = 30.0f;
    x.axisLineStyle = axisLineStyle;
    x.labelingPolicy = CPTAxisLabelingPolicyFixedInterval;
    x.majorIntervalLength = CPTDecimalFromString(@"20");
      
    // Configure the x-axis
    CPTAxis *y = axisSet.yAxis;
    y.title = @"Mood";
    y.titleTextStyle = axisTitleStyle;
    y.titleOffset = 30.0f;
    y.axisLineStyle = axisLineStyle;
    y.labelRotation = M_PI/4;
    y.labelingPolicy = CPTAxisLabelingPolicyNone;
    NSArray *customTickLocations = [NSArray arrayWithObjects:[NSDecimalNumber numberWithFloat:0.25],[NSDecimalNumber numberWithFloat:1.25], [NSDecimalNumber numberWithFloat:2.25], [NSDecimalNumber numberWithFloat:3.25], [NSDecimalNumber numberWithFloat:4.25], [NSDecimalNumber numberWithFloat:5.25], [NSDecimalNumber numberWithFloat:6.25], [NSDecimalNumber numberWithFloat:7.25], [NSDecimalNumber numberWithFloat:8.25], [NSDecimalNumber numberWithFloat:9.25],[NSDecimalNumber numberWithFloat:10.25], nil];
    NSArray *xAxisLabels = [NSArray arrayWithObjects:@"0", @"1", @"2", @"3", @"4", @"5", @"6", @"7", @"8", @"9", @"10", nil];
    NSUInteger labelLocation = 0;
    NSMutableArray *customLabels = [NSMutableArray arrayWithCapacity:[xAxisLabels count]];
    for (NSNumber *tickLocation in customTickLocations) {
        CPTAxisLabel *newLabel = [[CPTAxisLabel alloc] initWithText: [xAxisLabels objectAtIndex:labelLocation++]
                                                          textStyle:x.labelTextStyle];
        newLabel.tickLocation = [tickLocation decimalValue];
        newLabel.offset = y.labelOffset + y.majorTickLength;
        [customLabels addObject:newLabel];
    };
    y.axisLabels =  [NSSet setWithArray:customLabels];
    
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
/* Method that returns the amount of bars in the barchart*/
-(NSUInteger)numberOfRecordsForPlot:(CPTPlot *)plot {
    return 11;
}

/* Method that returns a value for every bar in teh plot*/
-(NSNumber *)numberForPlot:(CPTPlot *)plot field:(NSUInteger)fieldEnum recordIndex:(NSUInteger)index {
    NSDecimalNumber *barValue = nil;
    if ((fieldEnum == CPTBarPlotFieldBarTip) && (index < 11)) {
        barValue = (NSDecimalNumber *)[self.model.brightnessMood objectAtIndex:(index)];
        
    } else {
        barValue = (NSDecimalNumber *)[NSDecimalNumber numberWithUnsignedInteger:index];}
    return barValue;
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
