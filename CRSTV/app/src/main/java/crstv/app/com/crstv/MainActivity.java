package crstv.app.com.crstv;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.google.android.gms.ads.MobileAds;
import cz.msebera.android.httpclient.Header;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private AdView mAdView;
    GridView grid;
    ArrayList<Datos> datos = new ArrayList<Datos>();
    private static final String URL_CHANNEL_LIST = "https://raw.githubusercontent.com/tvboxjerp/tako/master/list.txt";
   private static final String GROUP = "GROUP:";
    private static final String EOF = "<<EOF>>";
    private BufferedReader in;
    private InterstitialAd mInterstitialAd;

    private static final int INTERVALO = 2000; //2 segundos para salir
    private long tiempoPrimerClick;
    private String appVersion;
    private String dialogMessage;
    private Message dialog;
    private String buttonStyle = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Notifications
        Intent i = new Intent(this, TokenRefreshListenerService.class);
        startService(i);
        setContentView(R.layout.activity_main);

        appVersion = BuildConfig.VERSION_NAME;
        dialogMessage = "";
        validateVersion();

        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, getString(R.string.admob_id));
        // Create the InterstitialAd and set the adUnitId.
        mInterstitialAd = new InterstitialAd(this);
        //ID for testing
       //mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        // Defined in res/values/strings.xml
        mInterstitialAd.setAdUnitId(getString(R.string.ad_unit_id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        grid = (GridView) findViewById(R.id.grid);
        downloadList();
        showInterstitial();

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                mInterstitialAd.show();
            }
        });

    }

    private void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and restart the game.
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            //showInterstitial();
           // Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (tiempoPrimerClick + INTERVALO > System.currentTimeMillis()){
                super.onBackPressed();
                return;
            }else {
                Toast.makeText(this, "Vuelve a presionar para salir", Toast.LENGTH_SHORT).show();
            }
            tiempoPrimerClick = System.currentTimeMillis();
        }


    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
      //  menu.clear();

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setQueryHint(getText(R.string.action_search));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(MainActivity.this, R.string.submitted, Toast.LENGTH_SHORT).show();
                //se oculta el EditText
                searchView.setQuery("", false);
                searchView.setIconified(true);
                searchItems(query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                //textView.setText(newText);
                //Toast.makeText(MainActivity.this, searchItem.getTitle().toString(), Toast.LENGTH_LONG).show();
                searchItems(newText);
                return true;
            }
        });

        MenuItem reloadItem = menu.findItem(R.id.action_refresh);
        reloadItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                downloadList();
                return true;
            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.tv) {
            showInterstitial();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.movies) {
            showInterstitial();
            Intent intent = new Intent(getApplicationContext(), MovieActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void downloadList(){
        datos = new ArrayList<Datos>();
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Cargando lista...");
        progressDialog.show();

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(URL_CHANNEL_LIST, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                BufferedReader br = null;
                long count = 0;
                if (statusCode == 200){

                    InputStream is = new ByteArrayInputStream(responseBody);
                    try {
                        in = new BufferedReader(new InputStreamReader(is));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    String inputLine;
                    int index = 0;

                    try {
                        while ((inputLine = in.readLine()) != null) {
                            if (inputLine.contains(GROUP)) {

                            }else{
                                if (inputLine.contains(EOF)) {

                                }else{
                                    String[] data = inputLine.split("<>");
                                    String group = (data[0] != null) ? data[0] : "";
                                    String logo = (data[1] != null) ? data[1] : "";
                                    String name = (data[2] != null) ? data[2] : "";
                                    String channel = (data[3] != null) ? data[3] : "";
                                    datos.add(new Datos(index, group, logo, name, channel));
                                    index++;
                                }
                            }
                        }

                        // Sorting
                        Collections.sort(datos, new Comparator<Datos>() {
                            @Override
                            public int compare(Datos item2, Datos item1)
                            {
                                return  item2.getName().compareTo(item1.getName());
                            }
                        });

                        Adaptador adaptador;
                        adaptador = new Adaptador(MainActivity.this, datos);
                        grid.setAdapter(adaptador);
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this,  datos.size() + " Canales cargados", Toast.LENGTH_SHORT).show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

    }

    private void searchItems(String name){

        ArrayList<Datos> items = new ArrayList<Datos>();

        if (!name.equals("")){
            for (int i = 0; i < datos.size(); i++) {
                Datos item = (Datos) datos.get(i);
                String itemName = item.getName().toUpperCase();

                if (itemName.contains(name.toUpperCase())){
                    items.add(item);
                }else{

                }
            }
            if (items.size() > 0) {
                Adaptador adaptador;
                adaptador = new Adaptador(MainActivity.this, items);
                grid.setAdapter(adaptador);
            }else {
                Toast.makeText(MainActivity.this, R.string.no_found, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void validateVersion(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference().getRoot().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String currentVersion = dataSnapshot.child("currentVersion").getValue().toString();
                String supportedVersion = dataSnapshot.child("supportedVersion").getValue().toString();
                boolean showMessage = (boolean) dataSnapshot.child("showCurrentMessage").getValue();
                if (appVersion.equals(currentVersion)){
                    dialogMessage = dataSnapshot.child("messageCurrentVersion").getValue().toString();
                    buttonStyle = Message.BUTTON_OK;
                }else{
                    if (supportedVersion.contains(appVersion)){
                        dialogMessage = dataSnapshot.child("messageSupportedVersion").getValue().toString();
                        buttonStyle = Message.BUTTON_OK_UPDATE;
                        showMessage = true;
                    }else{
                        dialogMessage = dataSnapshot.child("messageFailedVersion").getValue().toString();
                        buttonStyle = Message.BUTTON_EXIT_UPDATE;
                        showMessage = true;
                    }
                }

                if (dialogMessage != null) {
                    if (showMessage) {
                        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                        dialog = new Message("Informacion", dialogMessage, buttonStyle);
                        dialog.show(fragmentManager, "Info");
                    }else {
                        Toast.makeText(MainActivity.this , "Gracias por usar CRSTV", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

}


