package crstv.app.com.crstv;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

/**
 * Created by eutiquio on 1/22/18.
 */

public class Message extends DialogFragment {

    private String title;
    private String message;
    private String buttonStyle;
    public static final String BUTTON_OK = "BUTTON_OK";
    public static final String BUTTON_OK_UPDATE = "BUTTON_OK_UPDATE";
    public static final String BUTTON_EXIT_UPDATE = "BUTTON_EXIT_UPDATE";
    //public static final String URL_UPDATE = "https://drive.google.com/file/d/1vcX2KTm7gqnsTuCZ7sbnCl01eoS-VlUo/view?usp=sharing";
    Intent intent;
    FirebaseRemoteConfig mFirebaseRemoteConfig;
    //private final String urlNewVersion = "";


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(message)
                .setTitle(title);

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);

        String urlNewVersion = "";
        mFirebaseRemoteConfig.fetch()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mFirebaseRemoteConfig.activateFetched();

                        }
                    }
                })
                .addOnFailureListener(getActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);
                    }
                });
        urlNewVersion = mFirebaseRemoteConfig.getString("url_new_version");

        intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(urlNewVersion));

        if (buttonStyle.equals(BUTTON_OK_UPDATE)) {
                    builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    })
                    .setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(intent);
                        }
                    });
        }else{
            if (buttonStyle.equals(BUTTON_EXIT_UPDATE)) {
                        setCancelable(false);
                        builder.setNegativeButton("SALIR", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                System.exit(0);
                            }
                        })
                        .setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(intent);
                                System.exit(0);
                            }
                        });
            }else{
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

            }
        }
        return builder.create();
    }

   @SuppressLint("ValidFragment")
   public Message(String title, String message, String buttonStyle) {
        this.title = title;
        this.message = message;
        this.buttonStyle = buttonStyle;
    }

    public Message(){

    }
       public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
