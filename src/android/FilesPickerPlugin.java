package filespickerplugin;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.database.Cursor;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class FilesPickerPlugin extends CordovaPlugin {

    private static final int PICK_FILES = 1;
    private CallbackContext callbackContext;
    private JSONObject options;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("pickFiles")) {
            this.callbackContext = callbackContext;
            this.options = args.getJSONObject(0);
            if (!validateOptions(this.options)) {
                callbackContext.error(getErrorResponse(ErrorCodes.ERROR_INVALID_INPUT));
                return false;
            }
            pickFiles(this.options);
            return true;
        }
        return false;
    }

    private boolean validateOptions(JSONObject options) {
        String type = options.optString("type", "all");
        String input = options.optString("input", "absolutePath");

        boolean validType = type.equals("all") || type.equals("image") || type.equals("video");
        boolean validInput = input.equals("base64") || input.equals("absolutePath");

        return validType && validInput;
    }

    private void pickFiles(JSONObject options) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        String type = options.optString("type", "all");
        if (type.equals("all")) {
            intent.setType("*/*");
        } else if (type.equals("image")) {
            intent.setType("image/*");
        } else if (type.equals("video")) {
            intent.setType("video/*");
        }
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        cordova.startActivityForResult(this, intent, PICK_FILES);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_FILES) {
            if (resultCode == Activity.RESULT_OK) {
                ArrayList<Uri> uris = new ArrayList<>();
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        uris.add(data.getClipData().getItemAt(i).getUri());
                    }
                } else if (data.getData() != null) {
                    uris.add(data.getData());
                }
                processFiles(uris, this.options);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                callbackContext.error(getErrorResponse(ErrorCodes.ERROR_PICKER_CANCELLED));
            } else {
                callbackContext.error(getErrorResponse(ErrorCodes.ERROR_UNKNOWN));
            }
        }
    }

    private void processFiles(ArrayList<Uri> uris, JSONObject options) {
        JSONArray result = new JSONArray();
        try {
            for (Uri uri : uris) {
                String resultString = "";
                if (options.optString("input", "absolutePath").equals("base64")) {
                    resultString = convertToBase64(uri);
                } else {
                    resultString = getPathFromUri(uri);
                }
                result.put(resultString);
            }
            callbackContext.success(result);
        } catch (Exception e) {
            callbackContext.error(getErrorResponse(ErrorCodes.ERROR_PROCESSING_FILES));
        }
    }

    private String getPathFromUri(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = cordova.getActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            String path = cursor.getString(columnIndex);
            cursor.close();
            return path;
        }
        return null;
    }

    private String convertToBase64(Uri uri) throws Exception {
        InputStream inputStream = cordova.getActivity().getContentResolver().openInputStream(uri);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }

    private JSONObject getErrorResponse(int errorCode) {
        JSONObject errorResponse = new JSONObject();
        try {
            errorResponse.put("code", errorCode);
            errorResponse.put("message", ErrorCodes.getErrorMessage(errorCode));
        } catch (JSONException e) {
            // This should never happen
        }
        return errorResponse;
    }
}
