var PLUGIN_NAME = 'FilesPickerPlugin';

var FilesPickerPlugin = {
    pickFiles: function(options) {
        return new Promise(function (resolve, reject) {
            cordova.exec(resolve, reject, PLUGIN_NAME, 'pickFiles', [options]);
        });
    },
};

module.exports = FilesPickerPlugin;
