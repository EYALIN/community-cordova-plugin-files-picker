# Community Cordova Plugin Files Picker

I dedicate a considerable amount of my free time to developing and maintaining many cordova plugins for the community ([See the list with all my maintained plugins][community_plugins]).
To help ensure this plugin is kept updated,
new features are added and bugfixes are implemented quickly,
please donate a couple of dollars (or a little more if you can stretch) as this will help me to afford to dedicate time to its maintenance.
Please consider donating if you're using this plugin in an app that makes you money,
or if you're asking for new features or priority bug fixes. Thank you!

[![](https://img.shields.io/static/v1?label=Sponsor%20Me&style=for-the-badge&message=%E2%9D%A4&logo=GitHub&color=%23fe8e86)](https://github.com/sponsors/eyalin)

## community-cordova-plugin-files-picker

A Cordova plugin for selecting multiple files (images and videos) on Android and iOS, with support for different input formats (base64 and absolute path).

## Features

- Supports multiple file selection
- Handles images and videos
- Returns files in base64 or absolute path format
- Compatible with Android and iOS

## Installation

To install the plugin, use the following command:

```sh
cordova plugin add community-cordova-plugin-files-picker
```

## Usage

### TypeScript

To use the plugin in your Angular application, you need to declare the plugin and then use it in your component or service.

#### Angular Declaration

```typescript
declare var FilesPickerPlugin: FilesPickerManager;
```

#### Example Usage

```typescript
import { Component } from '@angular/core';
import FilesPickerManager, { IFilesPickerOptions } from 'community-cordova-plugin-files-picker';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'File Picker Example';

  constructor() {}

  pickFiles() {
    const options: IFilesPickerOptions = {
      type: 'image', // Can be 'image', 'video', or 'all'
      input: 'base64', // Can be 'base64' or 'absolutePath'
      quality: 100 // Quality for image compression (0-100)
    };

    FilesPickerPlugin.pickFiles(options)
      .then((files: string[]) => {
        console.log('Selected Files:', files);
      })
      .catch((err: any) => {
        this.handleError(err);
      });
  }

  handleError(err: any) {
    if (err?.toString().includes('Access has been denied')) {
      alert('Storage access denied. Please enable storage access in settings.');
    } else if ([2, 3, 4, 5].includes(err?.code)) {
      alert(`Failed to upload: ${err.message}`);
    } else if (err?.code === 1) {
      alert('File picker was cancelled.');
    } else {
      alert(`An unknown error occurred: ${err}`);
    }
  }
}
```

### JavaScript

To use the plugin in a plain JavaScript application, you can directly call the plugin methods after the `deviceready` event.

#### Example Usage

```html
<!DOCTYPE html>
<html>
  <head>
    <title>File Picker Example</title>
    <script type="text/javascript" src="cordova.js"></script>
    <script type="text/javascript">
      document.addEventListener('deviceready', function () {
        var options = {
          type: 'image', // Can be 'image', 'video', or 'all'
          input: 'base64', // Can be 'base64' or 'absolutePath'
          quality: 100 // Quality for image compression (0-100)
        };

        FilesPickerPlugin.pickFiles(options)
          .then(function (files) {
            console.log('Selected Files:', files);
          })
          .catch(function (err) {
            handleError(err);
          });

        function handleError(err) {
          if (err?.toString().includes('Access has been denied')) {
            alert('Storage access denied. Please enable storage access in settings.');
          } else if ([2, 3, 4, 5].includes(err?.code)) {
            alert('Failed to upload: ' + err.message);
          } else if (err?.code === 1) {
            alert('File picker was cancelled.');
          } else {
            alert('An unknown error occurred: ' + err);
          }
        }
      }, false);
    </script>
  </head>
  <body>
    <h1>File Picker Example</h1>
    <button onclick="pickFiles()">Pick Files</button>
  </body>
</html>
```

## Plugin Options

The `IFilesPickerOptions` interface defines the options you can pass to the `pickFiles` method:

- `type`: The type of files to pick. Can be 'image', 'video', or 'all'.
- `input`: The desired input format. Can be 'base64' or 'absolutePath'.
- `quality`: The quality for image compression (0-100).

### Example

```typescript
const options: IFilesPickerOptions = {
  type: 'image',
  input: 'base64',
  quality: 100
};
```

## Error Handling

The plugin returns errors with a code and a message. The following error codes are defined:

- `1`: Picker was cancelled
- `2`: Invalid file type provided
- `3`: Error processing files
- `4`: Unknown error
- `5`: Invalid input options provided

## Native Code Implementation

### Android

The Android implementation uses the `Intent.ACTION_GET_CONTENT` for file selection and supports multiple selection through `ClipData`.

### iOS

The iOS implementation uses the `PHPickerViewController` for file selection, which supports multiple selection and was introduced in iOS 14.

## License

This project is licensed under the MIT License.

## Contributing

Contributions are welcome! Please open an issue or submit a pull request.

## Issues

If you encounter any issues, please open a bug report at [GitHub Issues](https://github.com/eyalin/community-cordova-plugin-files-picker/issues).

## Repository

For more information and the latest updates, visit the [GitHub repository](https://github.com/eyalin/community-cordova-plugin-files-picker).



[w3c_spec]: https://www.w3.org/TR/battery-status/
[status_object]: #status-object
[community_plugins]: https://github.com/EYALIN?tab=repositories&q=community&type=&language=&sort=
