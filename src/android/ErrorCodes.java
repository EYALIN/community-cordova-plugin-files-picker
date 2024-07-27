package filespickerplugin;

public class ErrorCodes {
    public static final int ERROR_PICKER_CANCELLED = 1;
    public static final int ERROR_INVALID_TYPE = 2;
    public static final int ERROR_PROCESSING_FILES = 3;
    public static final int ERROR_UNKNOWN = 4;
    public static final int ERROR_INVALID_INPUT = 5;

    public static String getErrorMessage(int errorCode) {
        switch (errorCode) {
            case ERROR_PICKER_CANCELLED:
                return "Picker was cancelled";
            case ERROR_INVALID_TYPE:
                return "Invalid file type provided";
            case ERROR_PROCESSING_FILES:
                return "Error processing files";
            case ERROR_INVALID_INPUT:
                return "Invalid input options provided";
            case ERROR_UNKNOWN:
            default:
                return "Unknown error";
        }
    }
}
