

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
/*import android.support.design.widget.NavigationView;
import androidx.core.app.Fragment;
import androidx.core.app.FragmentTransaction;*/
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
/*import androidx.core.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;*/
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.navigation.NavigationView;
import com.google.ads.consent.ConsentForm;
import com.google.ads.consent.ConsentFormListener;
import com.google.ads.consent.ConsentInfoUpdateListener;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/*import xebia.ismail.e_learning.fragment.HomeFragment;
import xebia.ismail.e_learning.fragment.VolumeFragment;*/

/* Ismail Xebia */
//////////////////////////////////////////////////////////////////////////////////////////////////////////
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MaxAdListener {
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static final String TAG = MainActivity.class.getSimpleName();
    ConsentForm form;
    private static final String SELECTED_ITEM_ID = "SELECTED_ITEM_ID";
    CheckBox terms;
    private final Handler mDrawerHandler = new Handler();
    private DrawerLayout mDrawerLayout;
    private int mPrevSelectedId;
    private NavigationView mNavigationView;
    private int mSelectedId;
    private Toolbar mToolbar;
    private static InterstitialAd mInterstitialAd;
    CardView imageid1, imageid2, imageid3, imageid4, imageid5;
    Intent i;
    ///////////////////////////////////////////////////////////
    private AdRequest.Builder Builder;
    private MaxInterstitialAd interstitialAd;
    private int retryAttempt;
    private MaxAdView lovinAdView;
    /////////////////////////////////////////////////////////////////
    private AdView mAdView;
    private AdView adView;
    CardView mycard1;
    ActivityType activityType;
    public enum ActivityType {
        LIST1, LIST2,LIST3,LIST4
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_awal);
        /* Membuat Alert Dialog */
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);
        if (firstStart) {
            showStartDialog();
        }
        /*mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        AppLovinSdk.getInstance(this).setMediationProvider("MAX");
        AppLovinSdk.initializeSdk(this, new AppLovinSdk.SdkInitializationListener() {
            @Override
            public void onSdkInitialized(final AppLovinSdkConfiguration configuration) {
                // AppLovin SDK is initialized, start loading ads
                Builder = new AdManagerAdRequest.Builder().addKeyword("Lawyer, Mesothelioma, insurance");
            }
        });
        lovincreateInterstitialAd();
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        createPersonalizedAd();
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        ////// GDPR
        ConsentInformation consentInformation = ConsentInformation.getInstance(MainActivity.this);
        String[] publisherIds = {getString(R.string.pub_admob)};

        consentInformation.requestConsentInfoUpdate(publisherIds, new
                ConsentInfoUpdateListener() {
                    @Override
                    public void onConsentInfoUpdated(ConsentStatus consentStatus) {
                        Log.d(TAG, "onConsentInfoUpdated");
                        switch (consentStatus) {
                            case PERSONALIZED:
                                Log.d(TAG, "PERSONALIZED");
                                ConsentInformation.getInstance(MainActivity.this)
                                        .setConsentStatus(ConsentStatus.PERSONALIZED);
                                break;
                            case NON_PERSONALIZED:
                                Log.d(TAG, "NON_PERSONALIZED");
                                ConsentInformation.getInstance(MainActivity.this)
                                        .setConsentStatus(ConsentStatus.NON_PERSONALIZED);
                                break;


                            case UNKNOWN:
                                Log.d(TAG, "UNKNOWN");
                                if
                                (ConsentInformation.getInstance(MainActivity.this).isRequestLocationInEeaOrUnknown
                                        ()) {
                                    URL privacyUrl = null;
                                    try {

                                        privacyUrl = new URL(getString(R.string.privacy_police));

                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();

                                    }
                                    form = new ConsentForm.Builder(MainActivity.this,
                                            privacyUrl)
                                            .withListener(new ConsentFormListener() {
                                                @Override

                                                public void onConsentFormLoaded() {
                                                    Log.d(TAG, "onConsentFormLoaded");

                                                    showform();
                                                }

                                                @Override
                                                public void onConsentFormOpened() {
                                                    Log.d(TAG, "onConsentFormOpened");

                                                }

                                                @Override
                                                public void onConsentFormClosed(

                                                        ConsentStatus consentStatus, Boolean
                                                        userPrefersAdFree) {
                                                    Log.d(TAG, "onConsentFormClosed");

                                                }

                                                @Override
                                                public void onConsentFormError(String

                                                                                       errorDescription) {


                                                    Log.d(TAG, "onConsentFormError");
                                                    Log.d(TAG, errorDescription);
                                                }
                                            })

                                            .withPersonalizedAdsOption()
                                            .withNonPersonalizedAdsOption()

                                            .build();
                                    form.load();
                                } else {
                                    Log.d(TAG, "PERSONALIZED else");
                                    ConsentInformation.getInstance(MainActivity.this)
                                            .setConsentStatus(ConsentStatus.PERSONALIZED);
                                }
                                break;


                            default:
                                break;
                        }

                    }

                    private void showform() {
                        if (form != null) {
                            Log.d(TAG, "show ok");
                            form.show();
                        }
                    }

                    @Override
                    public void onFailedToUpdateConsentInfo(String errorDescription) {
// User's consent status failed to update.
                        Log.d(TAG, "onFailedToUpdateConsentInfo");
                        Log.d(TAG, errorDescription);
                    }
                });


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        assert mNavigationView != null;
        mNavigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout, mToolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                super.onDrawerSlide(drawerView, 0);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, 0);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        /* SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);*/
        mSelectedId = mNavigationView.getMenu().getItem(prefs.getInt("default_view", 0)).getItemId();
        mSelectedId = savedInstanceState == null ? mSelectedId : savedInstanceState.getInt(SELECTED_ITEM_ID);
        mPrevSelectedId = mSelectedId;
        mNavigationView.getMenu().findItem(mSelectedId).setChecked(true);

        if (savedInstanceState == null) {
            mDrawerHandler.removeCallbacksAndMessages(null);
            mDrawerHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    navigate(mSelectedId);
                }
            }, 250);

            boolean openDrawer = prefs.getBoolean("open_drawer", false);

            if (openDrawer)
                mDrawerLayout.openDrawer(GravityCompat.START);
            else
                mDrawerLayout.closeDrawers();
        }
    }

    private void bindAdaptiveBanner() {
        FrameLayout adViewContainer=findViewById(R.id.adViewContainer);
        AdView adaptiveAdView = new AdView(this);
        adaptiveAdView.setAdSize(getAdSize());
        adaptiveAdView.setAdUnitId(getString(R.string.id_banner));
        adViewContainer.addView(adaptiveAdView);
        AdRequest adRequest=new AdRequest.Builder().build();
        adaptiveAdView.loadAd(adRequest);
        adaptiveAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }
            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
                lovinAdView = new MaxAdView( getString(R.string.lovin_banner), MainActivity.this);
                FrameLayout lovinAdViewContainer = findViewById(R.id.adViewContainer);
                // Stretch to the width of the screen for banners to be fully functional
                int width = ViewGroup.LayoutParams.MATCH_PARENT;
                // Banner height on phones and tablets is 50 and 90, respectively
                int heightPx = getResources().getDimensionPixelSize( R.dimen.banner_height );
                lovinAdView.setLayoutParams( new FrameLayout.LayoutParams( width, heightPx ) );
                // Set background or background color for banners to be fully functional
                /*lovinAdView.setBackgroundColor( Color.WHITE );*/
                ViewGroup rootView = findViewById( android.R.id.content );
                /*rootView.addView( lovinAdView );*/
                lovinAdViewContainer.addView(lovinAdView);
                // Load the ad
                lovinAdView.loadAd();
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });
    }

    private AdSize getAdSize() {
        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;
        int adWidth = (int) (widthPixels / density);
        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }

    private void showStartDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Term And Condition")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("firstStart", false);
                        editor.apply();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, "KINDLY ACCEPT TERMS AND CONDITIONS", Toast.LENGTH_SHORT).show();
                        terms.setChecked(false);
                    }
                })
                .setMessage(Html.fromHtml(getString(R.string.syarat_ketentuan)))
                .create().show();

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void lovincreateInterstitialAd() {
        interstitialAd = new MaxInterstitialAd(getString(R.string.lovin_inter), this);
        interstitialAd.setListener(this);
        // Load the first ad
        interstitialAd.loadAd();
    }

    // MAX Ad Listener
    @Override
    public void onAdLoaded(final MaxAd maxAd) {
        // Interstitial ad is ready to be shown. interstitialAd.isReady() will now return 'true'
        // Reset retry attempt
        retryAttempt = 0;
    }

    @Override
    public void onAdLoadFailed(final String adUnitId, final MaxError error) {
        // Interstitial ad failed to load
        // We recommend retrying with exponentially higher delays up to a maximum delay (in this case 64 seconds)
        retryAttempt++;
        long delayMillis = TimeUnit.SECONDS.toMillis((long) Math.pow(2, Math.min(6, retryAttempt)));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                interstitialAd.loadAd();
            }
        }, delayMillis);
    }

    @Override
    public void onAdDisplayFailed(final MaxAd maxAd, final MaxError error) {
        // Interstitial ad failed to display. We recommend loading the next ad
        interstitialAd.loadAd();
    }

    @Override
    public void onAdDisplayed(final MaxAd maxAd) {
    }

    @Override
    public void onAdClicked(final MaxAd maxAd) {
    }

    @Override
    public void onAdHidden(final MaxAd maxAd) {
        // Interstitial ad is hidden. Pre-load the next ad
        interstitialAd.loadAd();
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////lovin

    public void switchFragment(int itemId) {
        mSelectedId = mNavigationView.getMenu().getItem(itemId).getItemId();
        mNavigationView.getMenu().findItem(mSelectedId).setChecked(true);
        mDrawerHandler.removeCallbacksAndMessages(null);
        mDrawerHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                navigate(mSelectedId);
            }
        }, 250);
        mDrawerLayout.closeDrawers();
    }

    private void navigate(final int itemId) {
        final View elevation = findViewById(R.id.elevation);
        Fragment navFragment = null;
        switch (itemId) {
            case R.id.menu_shareapp:
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + getPackageName());
                startActivity(Intent.createChooser(share, "Share App Via"));
                break;

            case R.id.menu_rateapp:
                Uri uri = Uri.parse("market://details?id=" + getApplication().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getApplication().getPackageName())));
                }
                break;

            case R.id.menu_disclaimer:
                startActivity(new Intent(this, Disclaimer.class));
                mNavigationView.getMenu().findItem(mPrevSelectedId).setChecked(true);
                break;
            case R.id.menu_privacypolicy:
                startActivity(new Intent(this, Privacy.class));
                mNavigationView.getMenu().findItem(mPrevSelectedId).setChecked(true);
                break;
            case R.id.menu_tnc:
                startActivity(new Intent(this, TermAndCon.class));
                mNavigationView.getMenu().findItem(mPrevSelectedId).setChecked(true);
                break;

            case R.id.nav_6:
                startActivity(new Intent(this, AboutActivity.class));
                mNavigationView.getMenu().findItem(mPrevSelectedId).setChecked(true);
                return;
        }

        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(4));

    }

    public int dp(float value) {
        float density = getApplicationContext().getResources().getDisplayMetrics().density;

        if (value == 0) {
            return 0;
        }
        return (int) Math.ceil(density * value);
    }

    public void list_A(View view) {
        if (mInterstitialAd != null) {
            activityType = ActivityType.LIST1;
            mInterstitialAd.show(this);
////////////////////////////////////////////////////////////////lovin
        } else {
            Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
            createPersonalizedAd();
            Intent intent = new Intent(MainActivity.this, Playlist1.class);
            startActivity(intent);
            interstitialAd.showAd();
            interstitialAd.loadAd();
        }
    }

    //////////////////////////////////////////////////////////////////lovin
    public void list_B(View view) {
        if (mInterstitialAd != null) {
            activityType = ActivityType.LIST2;
            mInterstitialAd.show(this);
////////////////////////////////////////////////////////////////lovin
        } else {
            Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
            createPersonalizedAd();
            Intent intent = new Intent(MainActivity.this, Playlist2.class);
            startActivity(intent);
            interstitialAd.showAd();
            interstitialAd.loadAd();
        }
    }

    public void list_C(View view) {
        if (mInterstitialAd != null) {
            activityType = ActivityType.LIST3;
            mInterstitialAd.show(this);
////////////////////////////////////////////////////////////////lovin
        } else {
            Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
            createPersonalizedAd();
            Intent intent = new Intent(MainActivity.this, Playlist3.class);
            startActivity(intent);
            interstitialAd.showAd();
            interstitialAd.loadAd();
        }
    }

    public void list_D(View view) {
        if (mInterstitialAd != null) {
            activityType = ActivityType.LIST4;
            mInterstitialAd.show(this);
////////////////////////////////////////////////////////////////lovin
        } else {
            Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
            createPersonalizedAd();
            Intent intent = new Intent(MainActivity.this, Playlist4.class);
            startActivity(intent);
            interstitialAd.showAd();
            interstitialAd.loadAd();
        }
    }

    public void list_E(View view) {
        Uri otherapp = Uri.parse("https://play.google.com/store/apps/developer?id=" + getString(R.string.MoreAppKeyWord)); // missing 'http://' will cause crashed
        Intent goToMoreapps = new Intent(Intent.ACTION_VIEW, otherapp);
        startActivity(goToMoreapps);
    }

    private void createPersonalizedAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        createInterstitialAd(adRequest);
    }

    private void createInterstitialAd(AdRequest adRequest) {
        InterstitialAd.load(this, getString(R.string.id_interstitial), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;
                Log.i("...Admob", "onAdLoaded");

                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when fullscreen content is dismissed.
                        Log.d("Admob", "The ad was dismissed.");
                        Intent intent;
                        switch (activityType) {
                            case LIST1:
                                intent = new Intent(MainActivity.this, Playlist1.class);
                                break;
                            case LIST2:
                                intent = new Intent(MainActivity.this, Playlist2.class);
                                break;
                            case LIST3:
                                intent = new Intent(MainActivity.this, Playlist3.class);
                                break;
                            case LIST4:
                                intent = new Intent(MainActivity.this, Playlist4.class);
                                break;
                            default:
                                return;
                        }
                        startActivity(intent);
                    }


                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        // Called when fullscreen content failed to show.
                        Log.d("...Admob", "The ad failed to show.");
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when fullscreen content is shown.
                        // Make sure to set your reference to null so you don't
                        // show it a second time.
                        mInterstitialAd = null;
                        Log.d("Admob", "The ad was shown.");
                    }
                });
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                Log.i("...Admob", loadAdError.getMessage());
                mInterstitialAd = null;
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        menuItem.setChecked(true);
        mSelectedId = menuItem.getItemId();
        mDrawerHandler.removeCallbacksAndMessages(null);
        mDrawerHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                navigate(mSelectedId);
            }
        }, 250);
        mDrawerLayout.closeDrawers();
        return true;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SELECTED_ITEM_ID, mSelectedId);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to Exit?");
            builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    System.exit(0);
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog d = builder.create();
            d.show();

        }
    }
}