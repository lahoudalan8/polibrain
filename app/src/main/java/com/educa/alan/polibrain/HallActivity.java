package com.educa.alan.polibrain;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HallActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Botões das camadas de cada matéria
    private LinearLayout mat, fis;
    private ImageView matCam[] = new ImageView[3];


    // Dados do usuário no Drawer
    private TextView nomeDrawer, emailDrawer;
    private ImageView imgDrawer;

    // Dados do usuário
    private String nome, email;
    private Uri photoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hall);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in

            // Name, email address, and profile photo Url
            nome = user.getDisplayName();
            email = user.getEmail();
            photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            String uid = user.getUid();


        } else {
            // No user is signed in
        }

        mat = (LinearLayout) findViewById(R.id.mat);
        fis = (LinearLayout) findViewById(R.id.fis);

        int temp;
        String[] id = new String[]{"mat1", "mat2", "mat3"};

        for(int i=0; i<id.length; i++){
            temp = getResources().getIdentifier(id[i], "id", getPackageName());
            matCam[i] = (ImageView) findViewById(temp);
        }

        // Inserindo dados do usuário no Drawer
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        nomeDrawer = (TextView) headerView.findViewById(R.id.nomeUsuarioDrawer);
        nomeDrawer.setText(nome);
        emailDrawer = (TextView) headerView.findViewById(R.id.emailUsuarioDrawer);
        emailDrawer.setText(email);
        imgDrawer = (ImageView) headerView.findViewById(R.id.imgUsuarioDrawer);
        imgDrawer.setImageURI(photoUrl);
        Uri a = photoUrl;

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { switch(item.getItemId()) {
        case R.id.add:
            //add the function to perform here
            return(true);
        case R.id.reset:
            //add the function to perform here
            return(true);
        case R.id.about:
            //add the function to perform here
            return(true);
        case R.id.exit:
            //add the function to perform here
            return(true);
    }
        return(super.onOptionsItemSelected(item));
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.matematica) {
            mat.setVisibility(View.VISIBLE);
            fis.setVisibility(View.GONE);
        } else if (id == R.id.fisica) {
            mat.setVisibility(View.GONE);
            fis.setVisibility(View.VISIBLE);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void clicaMat1(View view) {
        Intent intent = new Intent(this, NiveisActivity.class);
        intent.putExtra("camada", 1);
        startActivity(intent);
    }
}
