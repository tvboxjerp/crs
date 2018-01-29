package crstv.app.com.crstv;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.afollestad.easyvideoplayer.EasyVideoCallback;
import com.afollestad.easyvideoplayer.EasyVideoPlayer;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class DetailActivity extends AppCompatActivity implements EasyVideoCallback {

    ProgressDialog progressDialog;
    //VideoView player;
    ImageButton btnPlayPause;
    ImageButton btnStop;
    ImageButton btnBack;
    ImageButton btnRefresh;
    LinearLayout playerMenu;
    RelativeLayout playerDisplay;
    Datos datos;
    private AdView adView;
    private EasyVideoPlayer player;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


      //  adView = (AdView) findViewById(R.id.adView);

      //  AdRequest adRequest = new AdRequest.Builder().build();
//        adView.loadAd(adRequest);

      /*  player = (VideoView) findViewById(R.id.videoPlayer);
        btnPlayPause = (ImageButton) findViewById(R.id.btn_play_pause);
        btnStop = (ImageButton) findViewById(R.id.btn_stop);
        btnBack = (ImageButton) findViewById(R.id.btn_back);
        btnRefresh = (ImageButton) findViewById(R.id.btn_refresh);
        playerMenu = (LinearLayout) findViewById(R.id.player_menu);
*/
        datos = (Datos) getIntent().getExtras().getSerializable("datos");

       // progressDialog = new ProgressDialog(DetailActivity.this);
       // progressDialog.setMessage("Cargando...");
       // progressDialog.setCanceledOnTouchOutside(false);
       // progressDialog.show();

        // Grabs a reference to the player view
        player = (EasyVideoPlayer) findViewById(R.id.player);

        // Sets the callback to this Activity, since it inherits EasyVideoCallback
        player.setCallback(this);

        player.setAutoPlay(true);

        // Sets the source to the HTTP URL held in the TEST_URL variable.
        // To play files, you can use Uri.fromFile(new File("..."))

        player.setSource(Uri.parse(datos.getChannel().toString()));


        // playy();

       /* playerDisplay = (RelativeLayout) findViewById(R.id.player_display);
        playerDisplay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int visible = playerMenu.getVisibility();
                if (visible == View.VISIBLE){
                   // animar(false);
                    playerMenu.setVisibility(View.GONE);
                }else {
                   // animar(true);
                    playerMenu.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });
        playerMenu.setVisibility(View.GONE);

        //BUTTONS Actions
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(view.getContext(), MainActivity.class);
                //startActivity(intent);
                onBackPressed();
                //finish();
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(view.getContext(), MainActivity.class);
                //startActivity(intent);
                //finish();
                onBackPressed();
            }
        });

        btnPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (player.isPlaying()){
                    player.pause();
                    btnPlayPause.setImageResource(R.drawable.ic_play);
                }else {
                    player.start();
                    btnPlayPause.setImageResource(R.drawable.ic_pause);
                }
            }
        });

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player.pause();
                playy();
            }
        });
*/


    }

/*
    private void animar(boolean mostrar)
    {
        AnimationSet set = new AnimationSet(true);
        Animation animation = null;
        if (mostrar)
        {
            //desde la esquina inferior derecha a la superior izquierda
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        }
        else
        {    //desde la esquina superior izquierda a la esquina inferior derecha
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        }
        //duración en milisegundos
        animation.setDuration(500);
        set.addAnimation(animation);
        LayoutAnimationController controller = new LayoutAnimationController(set, 0.25f);

        playerMenu.setLayoutAnimation(controller);
        playerMenu.startAnimation(animation);
    }


    void playy(){
        try{
            if (!player.isPlaying()) {
                Uri uri = Uri.parse(datos.getChannel().toString());
                player.setVideoURI(uri);
                player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        btnPlayPause.setImageResource(R.drawable.ic_pause);
                    }
                });

                player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                        Log.d("TAKOTV", "Error al reproducir");
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(DetailActivity.this, R.style.alertDialog));
                        // set title
                        alertDialogBuilder.setTitle("CRS TV");
                        // set dialog message
                        alertDialogBuilder
                                .setMessage("Error al reproducir")
                                .setCancelable(false)
                                .setPositiveButton("Regresar",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        onBackPressed();
                                    }
                                });

                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        //alertDialog.setView();
                        // show it
                        alertDialog.show();
                        return false;
                    }
                });
            }else {
                player.pause();
                btnPlayPause.setImageResource(R.drawable.ic_play);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        player.requestFocus();
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                progressDialog.dismiss();
                mediaPlayer.setLooping(true);
                player.start();
                btnPlayPause.setImageResource(R.drawable.ic_pause);
            }
        });
    }
*/

    @Override
    public void onPause() {
        super.onPause();
        // Make sure the player stops playing if the user presses the home button.
        player.pause();
    }

    // Methods for the implemented EasyVideoCallback

    @Override
    public void onPreparing(EasyVideoPlayer player) {
        // TODO handle if needed

    }

    @Override
    public void onPrepared(EasyVideoPlayer player) {
        // TODO handle
    }

    @Override
    public void onBuffering(int percent) {
        // TODO handle if needed
    }

    @Override
    public void onError(EasyVideoPlayer player, Exception e) {
        // TODO handle
        Toast.makeText(getApplicationContext(), "Error al reproducir", Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    @Override
    public void onCompletion(EasyVideoPlayer player) {
        // TODO handle if needed
    }

    @Override
    public void onRetry(EasyVideoPlayer player, Uri source) {
        // TODO handle if used
    }

    @Override
    public void onSubmit(EasyVideoPlayer player, Uri source) {
        // TODO handle if used
    }

    @Override
    public void onStarted(EasyVideoPlayer player) {
        // TODO handle if needed
    }

    @Override
    public void onPaused(EasyVideoPlayer player) {
        // TODO handle if needed
    }
}
