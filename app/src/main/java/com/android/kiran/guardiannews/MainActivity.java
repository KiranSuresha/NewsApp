package com.android.kiran.guardiannews;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {

  private static final String NEWS_SECTION = "section";
  private static final String MENU_ID = "menuId";
  private static final String SELECTED_MENU_POSITION = "menuPosition";

  private String mNewsSection;
  private int mMenuId;
  private int mSelectedPosition;
  private NewsFragment mNewsFragment;
  private FragmentManager mFragmentManager;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    DrawerLayout drawer = findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle =
        new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open,
            R.string.navigation_drawer_close);
    drawer.addDrawerListener(toggle);
    toggle.syncState();

    NavigationView navigationView = findViewById(R.id.navigation_view);
    navigationView.setNavigationItemSelectedListener(this);

    mFragmentManager = getSupportFragmentManager();

    if (savedInstanceState == null) {
      mMenuId = R.id.nav_home;
      displaySectionContent();
    }
  }

  @Override protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt(MENU_ID, mMenuId);
    outState.putString(NEWS_SECTION, mNewsSection);
    outState.putInt(SELECTED_MENU_POSITION, mSelectedPosition);
  }

  @Override protected void onRestoreInstanceState(Bundle inState) {
    super.onRestoreInstanceState(inState);

    if (inState != null) {
      mMenuId = inState.getInt(MENU_ID);
      mSelectedPosition = inState.getInt(SELECTED_MENU_POSITION);
      mNewsSection = inState.getString(NEWS_SECTION);
      displaySectionContent();
    }
  }

  @Override public void onBackPressed() {
    DrawerLayout drawer = findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }

  @SuppressWarnings("StatementWithEmptyBody") @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    mMenuId = item.getItemId();

    displaySectionContent();

    DrawerLayout drawer = findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;
  }

  public void displaySectionContent() {
    switch (mMenuId) {
      case R.id.nav_home:
        mNewsSection = getString(R.string.menu_home).toLowerCase();
        mSelectedPosition = 1;
        break;
      case R.id.nav_business:
        mNewsSection = getString(R.string.menu_business).toLowerCase();
        mSelectedPosition = 2;
        break;
      case R.id.nav_education:
        mNewsSection = getString(R.string.menu_education).toLowerCase();
        mSelectedPosition = 3;
        break;
      case R.id.nav_environment:
        mNewsSection = getString(R.string.menu_environment).toLowerCase();
        mSelectedPosition = 4;
        break;
      case R.id.nav_fashion:
        mNewsSection = getString(R.string.menu_fashion).toLowerCase();
        mSelectedPosition = 5;
        break;
      case R.id.nav_film:
        mNewsSection = getString(R.string.menu_film).toLowerCase();
        mSelectedPosition = 6;
        break;
      case R.id.nav_money:
        mNewsSection = getString(R.string.menu_money).toLowerCase();
        mSelectedPosition = 7;
        break;
      case R.id.nav_politics:
        mNewsSection = getString(R.string.menu_politics).toLowerCase();
        mSelectedPosition = 8;
        break;
      case R.id.nav_sport:
        mNewsSection = getString(R.string.menu_sport).toLowerCase();
        mSelectedPosition = 9;
        break;
      case R.id.nav_technology:
        mNewsSection = getString(R.string.menu_technology).toLowerCase();
        mSelectedPosition = 10;
        break;
      case R.id.nav_weather:
        mNewsSection = getString(R.string.menu_weather).toLowerCase();
        mSelectedPosition = 11;
        break;
      case R.id.nav_world:
        mNewsSection = getString(R.string.menu_world).toLowerCase();
        mSelectedPosition = 12;
        break;
      default:
        mNewsSection = getString(R.string.menu_home).toLowerCase();
        mSelectedPosition = 1;
        break;
    }

    mNewsFragment = NewsFragment.newInstance(mNewsSection, mSelectedPosition);
    mFragmentManager.beginTransaction()
        .replace(R.id.frame_content, mNewsFragment)
        .addToBackStack(null)
        .commit();
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.about_menu, menu);

    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();

    switch (id) {
      case R.id.about:
        // call the about screen
        Intent aboutScreenIntent = new Intent(this, AboutPageActivity.class);
        startActivity(aboutScreenIntent);
        break;
    }
    return true;
  }
}
