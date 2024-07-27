#import "FilesPickerPlugin.h"
#import <MobileCoreServices/MobileCoreServices.h>

@implementation FilesPickerPlugin

- (void)pickFiles:(CDVInvokedUrlCommand *)command {
    self.command = command;
    NSDictionary *options = [command.arguments objectAtIndex:0];

    if (![self validateOptions:options]) {
        [self sendError:5 withMessage:@"Invalid input options provided"];
        return;
    }

    PHPickerConfiguration *configuration = [[PHPickerConfiguration alloc] init];
    configuration.selectionLimit = 0; // 0 means no limit, allowing multiple selection

    NSString *type = options[@"type"];
    if ([type isEqualToString:@"image"]) {
        configuration.filter = [PHPickerFilter imagesFilter];
    } else if ([type isEqualToString:@"video"]) {
        configuration.filter = [PHPickerFilter videosFilter];
    } else {
        configuration.filter = [PHPickerFilter anyFilterMatchingSubfilters:@[[PHPickerFilter imagesFilter], [PHPickerFilter videosFilter]]];
    }

    PHPickerViewController *pickerViewController = [[PHPickerViewController alloc] initWithConfiguration:configuration];
    pickerViewController.delegate = self;

    [self.viewController presentViewController:pickerViewController animated:YES completion:nil];
}

- (BOOL)validateOptions:(NSDictionary *)options {
    NSArray *validTypes = @[@"image", @"video", @"all"];
    NSArray *validInputs = @[@"base64", @"absolutePath"];

    NSString *type = options[@"type"];
    NSString *input = options[@"input"];

    BOOL validType = type == nil || [validTypes containsObject:type];
    BOOL validInput = input == nil || [validInputs containsObject:input];

    return validType && validInput;
}

- (void)picker:(PHPickerViewController *)picker didFinishPicking:(NSArray<PHPickerResult *> *)results {
    [picker dismissViewControllerAnimated:YES completion:nil];

    if (results.count == 0) {
        [self sendError:1 withMessage:@"Picker was cancelled"];
        return;
    }

    NSDictionary *options = self.command.arguments[0];
    NSMutableArray *resultArray = [NSMutableArray array];

    dispatch_group_t group = dispatch_group_create();

    for (PHPickerResult *result in results) {
        dispatch_group_enter(group);
        [result.itemProvider loadFileRepresentationForTypeIdentifier:result.itemProvider.registeredTypeIdentifiers.firstObject completionHandler:^(NSURL * _Nullable url, NSError * _Nullable error) {
            if (url) {
                NSString *resultString = @"";
                if ([options[@"input"] isEqualToString:@"base64"]) {
                    resultString = [self encodeURLToBase64:url];
                } else {
                    resultString = [url path];
                }
                [resultArray addObject:resultString];
            }
            dispatch_group_leave(group);
        }];
    }

    dispatch_group_notify(group, dispatch_get_main_queue(), ^{
        CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsArray:resultArray];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:self.command.callbackId];
    });
}

- (NSString *)encodeURLToBase64:(NSURL *)fileURL {
    NSData *fileData = [NSData dataWithContentsOfURL:fileURL];
    return [fileData base64EncodedStringWithOptions:0];
}

- (void)sendError:(int)code withMessage:(NSString *)message {
    NSDictionary *errorDict = @{
        @"code": @(code),
        @"message": message
    };
    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsDictionary:errorDict];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:self.command.callbackId];
}

@end
