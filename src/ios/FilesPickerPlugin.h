#import <Cordova/CDVPlugin.h>
#import <Photos/Photos.h>
#import <PhotosUI/PhotosUI.h>

@interface FilesPickerPlugin : CDVPlugin <PHPickerViewControllerDelegate>

@property (strong, nonatomic) CDVInvokedUrlCommand *command;

- (void)pickFiles:(CDVInvokedUrlCommand *)command;

@end
