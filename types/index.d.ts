export type FileType = 'image' | 'video' | 'all';

export type InputType = 'base64' | 'absolutePath';

export interface IFilesPickerOptions {
    type?: FileType; // Type of files to pick
    input?: InputType; // Desired input format
    quality?: number; // Quality for image compression (0-100)
}

export interface IPickerFile {
    path: string; // Absolute path of the picked file
    base64: string; // Base64 representation of the picked file
}

export default class FilesPickerManager {
    pickFiles(options: IFilesPickerOptions): Promise<string[]>;
}
