
# community-cordova-plugin-files-picker

I dedicate a considerable amount of my free time to developing and maintaining many cordova plugins for the community ([See the list with all my maintained plugins][community_plugins]).
To help ensure this plugin is kept updated,
new features are added and bugfixes are implemented quickly,
please donate a couple of dollars (or a little more if you can stretch) as this will help me to afford to dedicate time to its maintenance.
Please consider donating if you're using this plugin in an app that makes you money,
or if you're asking for new features or priority bug fixes. Thank you!

[![](https://img.shields.io/static/v1?label=Sponsor%20Me&style=for-the-badge&message=%E2%9D%A4&logo=GitHub&color=%23fe8e86)](https://github.com/sponsors/eyalin)

[community_plugins]: https://github.com/EYALIN?tab=repositories&q=community&type=&language=&sort=

## Description

`community-cordova-plugin-files-picker` is a Cordova plugin that allows users to select files from their device (images, videos, or any files) and retrieve either the file's absolute path or a Base64-encoded representation. The plugin supports both Android and iOS platforms.

## Features

- Pick multiple or single files
- Support for different file types (images, videos, all)
- Return files as absolute paths or Base64 strings
- Support for Android and iOS

## Installation

To install the plugin, run:

```bash
cordova plugin add community-cordova-plugin-files-picker
```

## Usage

To use the plugin, simply call the `pickFiles` method with the desired options.

```typescript
const options: IFilesPickerOptions = {
    type: 'image',         // 'image', 'video', or 'all'
    input: 'base64',       // 'base64' or 'absolutePath'
    multiple: true         // true for multiple files, false for a single file
};

FilesPickerManager.pickFiles(options).then((files) => {
    console.log(files); // Process the selected file(s)
}).catch((error) => {
    console.error(error);
});
```

## Options

- **type**: The type of files to pick. Options are:
    - `'image'`: Pick images only.
    - `'video'`: Pick videos only.
    - `'all'`: Pick any file type.

- **input**: The format in which the files are returned. Options are:
    - `'base64'`: Return the files as Base64-encoded strings.
    - `'absolutePath'`: Return the files as absolute paths.

- **multiple**: Whether to allow selecting multiple files. Defaults to `true`.

## Platform Support

- **Android**
- **iOS**

## Contributing

Feel free to submit issues or pull requests. Contributions are always welcome!

## License

This plugin is licensed under the MIT License.
