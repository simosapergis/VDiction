package com.sapergis.vdiction.helper;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import androidx.core.content.ContextCompat;

public class GrantPermission {
    private static final int REQUEST_READ_PERMISSION = 786;
    private static boolean result = false;

    public static boolean check(String permissionFor, Activity activity){
        if(ContextCompat.checkSelfPermission(activity,permissionFor) == PackageManager.PERMISSION_GRANTED ){
            result = true;
        }
        else{
            String [] perm = new String[2];
            perm[0] = permissionFor;
            activity.requestPermissions(perm, codeForPermission(permissionFor));
            result = false;
        }
        return result;
    }

    private static int codeForPermission(String permissionFor) {
        switch (permissionFor){
            case Manifest.permission.READ_EXTERNAL_STORAGE:
                return REQUEST_READ_PERMISSION;
                default:
                    return  0;
        }
    }
}
