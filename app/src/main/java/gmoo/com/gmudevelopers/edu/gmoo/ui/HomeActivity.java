/*
 * Copyright 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package gmoo.com.gmudevelopers.edu.gmoo.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.transition.TransitionManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import gmoo.com.gmudevelopers.edu.gmoo.R;
import gmoo.com.gmudevelopers.edu.gmoo.adapters.StaggeredAdapter;
import gmoo.com.gmudevelopers.edu.gmoo.auth.UserSignIn;
import gmoo.com.gmudevelopers.edu.gmoo.data.DataManager;
import gmoo.com.gmudevelopers.edu.gmoo.data.Source;
import gmoo.com.gmudevelopers.edu.gmoo.data.api.designernews.PostStoryService;
import gmoo.com.gmudevelopers.edu.gmoo.data.api.designernews.model.Story;
import gmoo.com.gmudevelopers.edu.gmoo.data.prefs.DesignerNewsPrefs;
import gmoo.com.gmudevelopers.edu.gmoo.data.prefs.DribbblePrefs;
import gmoo.com.gmudevelopers.edu.gmoo.data.prefs.SourceManager;
import gmoo.com.gmudevelopers.edu.gmoo.model.AddDetail;
import gmoo.com.gmudevelopers.edu.gmoo.ui.transitions.FabTransform;
import gmoo.com.gmudevelopers.edu.gmoo.util.AnimUtils;
import gmoo.com.gmudevelopers.edu.gmoo.util.glide.CircleTransform;
import ru.dimorinny.floatingtextbutton.FloatingTextButton;


public class HomeActivity extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private static final int RC_SEARCH = 0;
    private static final int RC_AUTH_DRIBBBLE_FOLLOWING = 1;
    private static final int RC_AUTH_DRIBBBLE_USER_LIKES = 2;
    private static final int RC_AUTH_DRIBBBLE_USER_SHOTS = 3;
    private static final int RC_NEW_DESIGNER_NEWS_STORY = 4;
    private static final int RC_NEW_DESIGNER_NEWS_LOGIN = 5;
    private static final String urlNavHeaderBg = "http://api.androidhive.info/images/nav-menu-header-bg.jpg";
    private static final String urlProfileImg = "https://lh3.googleusercontent.com/eCtE_G34M9ygdkmOpYvCag1vBARCmZwnVS6rS5t4JLzJ6QgQSBquM0nuTsCpLhYbKljoyS-txg";

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_POST_ADD = "post_add";
    private static final String TAG_PHOTOS = "photos";
    private static final String TAG_MOVIES = "movies";
    private static final String TAG_NOTIFICATIONS = "notifications";
    private static final String TAG_SETTINGS = "settings";
    public static String CURRENT_TAG = TAG_HOME;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;
    @Nullable
    @BindView(R.id.progress_bar)
    ProgressBar bar;

    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;

    private ActionBarDrawerToggle drawerToggle;
    @BindView(R.id.drawer)
    DrawerLayout drawer;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    /*
        @BindView(R.id.fab)
        ImageButton fab;


        */
    @BindView(R.id.grid)
    RecyclerView recyclerView;
    @Nullable
    @BindView(android.R.id.empty)
    ProgressBar loading;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.post_offer)
    FloatingTextButton postAdd;


    @Nullable
    @BindView(R.id.no_connection)
    ImageView noConnection;
    ImageButton fabPosting;
    GridLayoutManager layoutManager;
    @BindInt(R.integer.num_columns)
    int columns;
    boolean connected = true;
    private TextView noFiltersEmptyText;
    private boolean monitoringConnectivity = false;

    // data
    DataManager dataManager;
    FeedAdapter adapter;

    private DesignerNewsPrefs designerNewsPrefs;
    private DribbblePrefs dribbblePrefs;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);


        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();


        //  StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);

        //  drawer.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        navigationView = findViewById(R.id.nav_view);

        RecyclerView recyclerView = findViewById(R.id.grid);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);

        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        recyclerView.setLayoutManager(layoutManager);
        loading.setIndeterminate(false);

        recyclerView.setHasFixedSize(true);
        ArrayList<AddDetail> addList = getAdds();
        StaggeredAdapter staggeredAdapter = new StaggeredAdapter(addList);
        recyclerView.setAdapter(staggeredAdapter);
        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtWebsite = (TextView) navHeader.findViewById(R.id.website);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);
        setActionBar(toolbar);
        if (savedInstanceState == null) {
            animateToolbar();
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }
        //setExitSharedElementCallback(FeedAdapter.createSharedElementReenterCallback(this));

        dribbblePrefs = DribbblePrefs.get(this);
        designerNewsPrefs = DesignerNewsPrefs.get(this);


        //   adapter = new FeedAdapter(this, dataManager, columns, PocketUtils.isPocketInstalled(this));
        //   adapter = new FeedAdapter(this, dataManager, columns, PocketUtils.isPocketInstalled(this));
        postAdd.setOnClickListener(view -> {

            if (isSignedIn()) {

                Intent intent = new Intent(HomeActivity.this, SelectCategoryActivity.class);
                FabTransform.addExtras(intent,
                        ContextCompat.getColor(HomeActivity.this, R.color.accent), R.drawable.ic_add_dark);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(HomeActivity.this, postAdd,
                        getString(R.string.transition_designer_news_login));
                startActivityForResult(intent, RC_NEW_DESIGNER_NEWS_LOGIN, options.toBundle());
            } else {

                startActivity(new Intent(HomeActivity.this, UserSignIn.class));
                finish();
            }
        });

        //   adapter = new FeedAdapter(this, dataManager, columns, PocketUtils.isPocketInstalled(this));

        setupTaskDescription();
        // load nav menu header data
        loadNavHeader();

        // initializing navigation menu
        setUpNavigationView();
        // dataManager.loadAllDataSources();

        //checkEmptyState();

        // Set Drawerlayout switch indicator that the icon leftmost Toolbar


    }


    private void loadNavHeader() {
        // name, website

        if (isSignedIn()) {

            txtName.setText(mUser.getDisplayName());
            txtWebsite.setText(mUser.getEmail());

        } else {

            txtName.setText("Ravi Tamada");
            txtWebsite.setText("www.androidhive.info");

        }

        // loading header background image
        Glide.with(this).load(urlNavHeaderBg)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgNavHeaderBg);

        // Loading profile image
        Glide.with(this).load(urlProfileImg)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfile);

        // showing dot next to notifications label
        navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot);
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;

                    case R.id.nav_post_add:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_POST_ADD;

                        if (isSignedIn()) {
                            startActivity(new Intent(HomeActivity.this, SelectCategoryActivity.class));
                            finish();
                        } else {
                            startActivity(new Intent(HomeActivity.this, UserSignIn.class));
                            finish();
                        }

                        break;

                    case R.id.nav_category:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_PHOTOS;

                        if (isSignedIn()) {
                            startActivity(new Intent(HomeActivity.this, SelectCategoryActivity.class));
                            finish();
                        } else {
                            startActivity(new Intent(HomeActivity.this, UserSignIn.class));
                            finish();
                        }

                        break;
                    case R.id.nav_sell_your_stuff:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_MOVIES;
                        break;
                    case R.id.nav_chat:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_NOTIFICATIONS;
                        break;
                    case R.id.nav_settings:
                        navItemIndex = 5;
                        CURRENT_TAG = TAG_SETTINGS;
                        break;
                    case R.id.help:
                        // launch new intent instead of loading fragment
                        // startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.logout:
                        // launch new intent instead of loading fragment

                        if ((mUser != null) && (mAuth != null)) {
                            mAuth.signOut();
                        }

                        drawer.closeDrawers();
                        Toast.makeText(HomeActivity.this, "Signed Out", Toast.LENGTH_LONG).show();
                        return true;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.addDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        //setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer


    }

    @Override
    protected void onResume() {
        super.onResume();
        //  dribbblePrefs.addLoginStatusListener(filtersAdapter);
        checkConnectivity();
    }

    @Override
    protected void onPause() {

        if (monitoringConnectivity) {
            final ConnectivityManager connectivityManager
                    = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            connectivityManager.unregisterNetworkCallback(connectivityCallback);
            monitoringConnectivity = false;
        }
        super.onPause();
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return true;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_filter:
                drawer.openDrawer(GravityCompat.END);
                return true;
            case R.id.menu_search:
                View searchMenuView = toolbar.findViewById(R.id.menu_search);
                Bundle options = ActivityOptions.makeSceneTransitionAnimation(this, searchMenuView,
                        getString(R.string.transition_search_back)).toBundle();
                startActivityForResult(new Intent(this, SearchActivity.class), RC_SEARCH, options);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private ArrayList<AddDetail> getAdds() {
        ArrayList<AddDetail> details = new ArrayList<>();
        for (int index = 0; index < getFakeList().size(); index++) {
            details.add(new AddDetail(getFakeList().get(index)));
        }
        return details;
    }

    private ArrayList<String> getFakeList() {
        ArrayList<String> imagesList = new ArrayList<>();
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/12/23193634/tumblr_oiboua3s6F1slhhf0o1_500.jpg");
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/12/16094159/tumblr_o2z8oh0Ntt1ted1sho1_1280-683x1024.jpg");
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/11/08192732/tumblr_oev1qbnble1ted1sho1_500.jpg");
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/11/02093356/DSF1919-768x512.jpg");
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/12/16094159/tumblr_o2z8oh0Ntt1ted1sho1_1280-683x1024.jpg");
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/12/16094159/tumblr_o2z8oh0Ntt1ted1sho1_1280-683x1024.jpg");
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/11/18184202/tumblr_ntyttsx2Y51ted1sho1_500.jpg");
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/11/25093310/2016-03-01-roman-drits-barnimages-008-768x512.jpg");
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/11/02093356/DSF1919-768x512.jpg");
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/12/16094159/tumblr_o2z8oh0Ntt1ted1sho1_1280-683x1024.jpg");
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/11/25093310/2016-03-01-roman-drits-barnimages-008-768x512.jpg");
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/11/25093331/tumblr_ofz20toUqd1ted1sho1_500.jpg");
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/12/16094159/tumblr_o2z8oh0Ntt1ted1sho1_1280-683x1024.jpg");
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/12/16094159/tumblr_o2z8oh0Ntt1ted1sho1_1280-683x1024.jpg");
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/11/02093356/DSF1919-768x512.jpg");
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/12/16094159/tumblr_o2z8oh0Ntt1ted1sho1_1280-683x1024.jpg");
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/12/23193617/2016-11-13-barnimages-igor-trepeshchenok-01-768x509.jpg");
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/11/08192739/tumblr_ofem6n49F61ted1sho1_500.jpg");
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/11/02093356/DSF1919-768x512.jpg");
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/12/16094159/tumblr_o2z8oh0Ntt1ted1sho1_1280-683x1024.jpg");
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/11/25093334/2016-11-21-roman-drits-barnimages-003-768x512.jpg");
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/11/02093356/DSF1919-768x512.jpg");
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/12/16094159/tumblr_o2z8oh0Ntt1ted1sho1_1280-683x1024.jpg");
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/11/02093347/2016-11-21-roman-drits-barnimages-009-768x512.jpg");
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/12/16094158/2016-12-05-roman-drits-barnimages-011-768x512.jpg");
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/11/02093356/DSF1919-768x512.jpg");
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/12/16094159/tumblr_o2z8oh0Ntt1ted1sho1_1280-683x1024.jpg");
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/11/02093347/2016-11-21-roman-drits-barnimages-009-768x512.jpg");
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/12/16094158/2016-12-05-roman-drits-barnimages-011-768x512.jpg");
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/11/02093356/DSF1919-768x512.jpg");
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/11/02093356/DSF1919-768x512.jpg");
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/12/16094159/tumblr_o2z8oh0Ntt1ted1sho1_1280-683x1024.jpg");
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/11/02093347/2016-11-21-roman-drits-barnimages-009-768x512.jpg");
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/12/16094158/2016-12-05-roman-drits-barnimages-011-768x512.jpg");
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/11/02093356/DSF1919-768x512.jpg");
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/12/16094159/tumblr_o2z8oh0Ntt1ted1sho1_1280-683x1024.jpg");
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/11/02093347/2016-11-21-roman-drits-barnimages-009-768x512.jpg");
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/12/16094158/2016-12-05-roman-drits-barnimages-011-768x512.jpg");
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/11/02093356/DSF1919-768x512.jpg");
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/11/02093347/2016-11-21-roman-drits-barnimages-009-768x512.jpg");
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/12/16094158/2016-12-05-roman-drits-barnimages-011-768x512.jpg");

        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/11/02093356/DSF1919-768x512.jpg");
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/11/02093347/2016-11-21-roman-drits-barnimages-009-768x512.jpg");
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/12/16094158/2016-12-05-roman-drits-barnimages-011-768x512.jpg");
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/11/02093356/DSF1919-768x512.jpg");
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/11/02093347/2016-11-21-roman-drits-barnimages-009-768x512.jpg");
        imagesList.add("http://static0.passel.co/wp-content/uploads/2016/12/16094158/2016-12-05-roman-drits-barnimages-011-768x512.jpg");

        return imagesList;
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }


    private RecyclerView.OnScrollListener toolbarElevation = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            // we want the grid to scroll over the top of the toolbar but for the toolbar items
            // to be clickable when visible. To achieve this we play games with elevation. The
            // toolbar is laid out in front of the grid but when we scroll, we lower it's elevation
            // to allow the content to pass in front (and reset when scrolled to top of the grid)
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && layoutManager.findFirstVisibleItemPosition() == 0
                    && layoutManager.findViewByPosition(0).getTop() == recyclerView.getPaddingTop()
                    && toolbar.getTranslationZ() != 0) {
                // at top, reset elevation
                toolbar.setTranslationZ(0f);
            } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING
                    && toolbar.getTranslationZ() != -1f) {
                // grid scrolled, lower toolbar to allow content to pass in front
                toolbar.setTranslationZ(-1f);
            }
        }
    };

    private boolean isSignedIn() {

        // Get Logged in User instance and check if logged in.
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mUser != null) {
            // Name, email address, and profile photo Url
            String name = mUser.getDisplayName();
            String email = mUser.getEmail();
            Uri photoUrl = mUser.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = mUser.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            String uid = mUser.getUid();

            return true;

        } else {

            return false;
        }

    }

    BroadcastReceiver postStoryResultReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ensurePostingProgressInflated();
            switch (intent.getAction()) {
                case PostStoryService.BROADCAST_ACTION_SUCCESS:
                    // success animation
                    AnimatedVectorDrawable complete =
                            (AnimatedVectorDrawable) getDrawable(R.drawable.avd_upload_complete);
                    if (complete != null) {
                        fabPosting.setImageDrawable(complete);
                        complete.start();
                        fabPosting.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                fabPosting.setVisibility(View.GONE);
                            }
                        }, 2100); // length of R.drawable.avd_upload_complete
                    }

                    // actually add the story to the grid
                    Story newStory = intent.getParcelableExtra(PostStoryService.EXTRA_NEW_STORY);
                    adapter.addAndResort(Collections.singletonList(newStory));
                    break;
                case PostStoryService.BROADCAST_ACTION_FAILURE:
                    // failure animation
                    AnimatedVectorDrawable failed =
                            (AnimatedVectorDrawable) getDrawable(R.drawable.avd_upload_error);
                    if (failed != null) {
                        fabPosting.setImageDrawable(failed);
                        failed.start();
                    }
                    // remove the upload progress 'fab' and reshow the regular one
                    fabPosting.animate()
                            .alpha(0f)
                            .rotation(90f)
                            .setStartDelay(2000L) // leave error on screen briefly
                            .setDuration(300L)
                            .setInterpolator(AnimUtils.getFastOutSlowInInterpolator(HomeActivity
                                    .this))
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    fabPosting.setVisibility(View.GONE);
                                    fabPosting.setAlpha(1f);
                                    fabPosting.setRotation(0f);
                                }
                            });
                    break;
            }
            unregisterPostStoryResultListener();
        }
    };

    void registerPostStoryResultListener() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(PostStoryService.BROADCAST_ACTION_SUCCESS);
        intentFilter.addAction(PostStoryService.BROADCAST_ACTION_FAILURE);
        LocalBroadcastManager.getInstance(this).
                registerReceiver(postStoryResultReceiver, intentFilter);
    }

    void unregisterPostStoryResultListener() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(postStoryResultReceiver);
    }

    void revealPostingProgress() {
        Animator reveal = ViewAnimationUtils.createCircularReveal(fabPosting,
                (int) fabPosting.getPivotX(),
                (int) fabPosting.getPivotY(),
                0f,
                fabPosting.getWidth() / 2)
                .setDuration(600L);
        reveal.setInterpolator(AnimUtils.getFastOutLinearInInterpolator(this));
        reveal.start();
        AnimatedVectorDrawable uploading =
                (AnimatedVectorDrawable) getDrawable(R.drawable.avd_uploading);
        if (uploading != null) {
            fabPosting.setImageDrawable(uploading);
            uploading.start();
        }
    }

    void ensurePostingProgressInflated() {
        if (fabPosting != null) return;
        fabPosting = (ImageButton) ((ViewStub) findViewById(R.id.stub_posting_progress)).inflate();
    }


    int getAuthSourceRequestCode(Source filter) {
        switch (filter.key) {
            case SourceManager.SOURCE_DRIBBBLE_FOLLOWING:
                return RC_AUTH_DRIBBBLE_FOLLOWING;
            case SourceManager.SOURCE_DRIBBBLE_USER_LIKES:
                return RC_AUTH_DRIBBBLE_USER_LIKES;
            case SourceManager.SOURCE_DRIBBBLE_USER_SHOTS:
                return RC_AUTH_DRIBBBLE_USER_SHOTS;
        }
        throw new InvalidParameterException();
    }

    private void showPostingProgress() {
        ensurePostingProgressInflated();
        fabPosting.setVisibility(View.VISIBLE);
        // if stub has just been inflated then it will not have been laid out yet
        if (fabPosting.isLaidOut()) {
            revealPostingProgress();
        } else {
            fabPosting.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int l, int t, int r, int b,
                                           int oldL, int oldT, int oldR, int oldB) {
                    fabPosting.removeOnLayoutChangeListener(this);
                    revealPostingProgress();
                }
            });
        }
    }

    private void setNoFiltersEmptyTextVisibility(int visibility) {
        if (visibility == View.VISIBLE) {
            if (noFiltersEmptyText == null) {
                // create the no filters empty text
                ViewStub stub = findViewById(R.id.stub_no_filters);
                noFiltersEmptyText = (TextView) stub.inflate();
                String emptyText = getString(R.string.no_filters_selected);
                int filterPlaceholderStart = emptyText.indexOf('\u08B4');
                int altMethodStart = filterPlaceholderStart + 3;
                SpannableStringBuilder ssb = new SpannableStringBuilder(emptyText);
                // show an image of the filter icon
                ssb.setSpan(new ImageSpan(this, R.drawable.ic_filter_small,
                                ImageSpan.ALIGN_BASELINE),
                        filterPlaceholderStart,
                        filterPlaceholderStart + 1,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                // make the alt method (swipe from right) less prominent and italic
                ssb.setSpan(new ForegroundColorSpan(
                                ContextCompat.getColor(this, R.color.text_secondary_light)),
                        altMethodStart,
                        emptyText.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ssb.setSpan(new StyleSpan(Typeface.ITALIC),
                        altMethodStart,
                        emptyText.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                noFiltersEmptyText.setText(ssb);
                noFiltersEmptyText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        drawer.openDrawer(GravityCompat.END);
                    }
                });
            }
            noFiltersEmptyText.setVisibility(visibility);
        } else if (noFiltersEmptyText != null) {
            noFiltersEmptyText.setVisibility(visibility);
        }

    }

    private void setupTaskDescription() {

    }

    private void animateToolbar() {
        // this is gross but toolbar doesn't expose it's children to animate them :(
        View t = toolbar.getChildAt(0);
        if (t != null && t instanceof TextView) {
            TextView title = (TextView) t;

            // fade in and space out the title.  Animating the letterSpacing performs horribly so
            // fake it by setting the desired letterSpacing then animating the scaleX ¯\_(ツ)_/¯
            title.setAlpha(0f);
            title.setScaleX(0.8f);

            title.animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .setStartDelay(300)
                    .setDuration(900)
                    .setInterpolator(AnimUtils.getFastOutSlowInInterpolator(this));
        }
    }

    /**
     * Highlight the new source(s) by:
     * 1. opening the drawer
     * 2. scrolling new source(s) into view
     * 3. flashing new source(s) background
     * 4. closing the drawer (if user hasn't interacted with it)
     */
    private void highlightNewSources(final Source... sources) {
        final Runnable closeDrawerRunnable = new Runnable() {
            @Override
            public void run() {
                drawer.closeDrawer(GravityCompat.END);
            }
        };
        drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {

            // if the user interacts with the filters while it's open then don't auto-close
            private final View.OnTouchListener filtersTouch = new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    drawer.removeCallbacks(closeDrawerRunnable);
                    return false;
                }
            };

            @Override
            public void onDrawerOpened(View drawerView) {
                // scroll to the new item(s) and highlight them
                List<Integer> filterPositions = new ArrayList<>(sources.length);
                for (Source source : sources) {
                    if (source != null) {
                        // filterPositions.add(filtersAdapter.getFilterPosition(source));
                    }
                }
                int scrollTo = Collections.max(filterPositions);
                //  filtersList.smoothScrollToPosition(scrollTo);
                for (int position : filterPositions) {
                    //   filtersAdapter.highlightFilter(position);
                }
                //filtersList.setOnTouchListener(filtersTouch);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                // reset
                //  filtersList.setOnTouchListener(null);
                drawer.removeDrawerListener(this);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                // if the user interacts with the drawer manually then don't auto-close
                if (newState == DrawerLayout.STATE_DRAGGING) {
                    drawer.removeCallbacks(closeDrawerRunnable);
                }
            }
        });
        drawer.openDrawer(GravityCompat.END);
        drawer.postDelayed(closeDrawerRunnable, 2000L);
    }

    private void checkConnectivity() {
        final ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        connected = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        if (!connected) {
            loading.setVisibility(View.GONE);
            if (noConnection == null) {
                final ViewStub stub = (ViewStub) findViewById(R.id.stub_no_connection);
                noConnection = (ImageView) stub.inflate();
            }
            final AnimatedVectorDrawable avd =
                    (AnimatedVectorDrawable) getDrawable(R.drawable.avd_no_connection);
            if (noConnection != null && avd != null) {
                noConnection.setImageDrawable(avd);
                avd.start();
            }

            connectivityManager.registerNetworkCallback(
                    new NetworkRequest.Builder()
                            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build(),
                    connectivityCallback);
            monitoringConnectivity = true;
        }
    }

    private ConnectivityManager.NetworkCallback connectivityCallback
            = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(Network network) {
            connected = true;
            if (adapter.getDataItemCount() != 0) return;
            runOnUiThread(() -> {
                TransitionManager.beginDelayedTransition(drawer);
                noConnection.setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE);
                dataManager.loadAllDataSources();
            });
        }

        @Override
        public void onLost(Network network) {
            connected = false;
        }
    };

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.post_offer:
                Intent intent = new Intent(this, SelectCategoryActivity.class);
                FabTransform.addExtras(intent,
                        ContextCompat.getColor(this, R.color.accent), R.drawable.ic_add_dark);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, postAdd,
                        getString(R.string.transition_designer_news_login));
                startActivityForResult(intent, RC_NEW_DESIGNER_NEWS_LOGIN, options.toBundle());
        }
    }
}
