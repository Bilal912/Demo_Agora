package com.edu_touch.edu_hunt;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.Gravity.TOP;
import static com.edu_touch.edu_hunt.MainActivity.MY_PREFS_NAME;

public class Home extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{
    SliderLayout sliderLayout;
    SharedPreferences sharedPreferences;
    TextView Name,Phone;
    CircleImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        imageView = findViewById(R.id.iv_header_img);

        Name = findViewById(R.id.tv_header_name);
        Phone = findViewById(R.id.tv_adres_phone);

        sliderLayout = findViewById(R.id.imageSlider);
        sliderLayout.setIndicatorAnimation(IndicatorAnimations.FILL); //set indicator animation by using SliderLayout.Animations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderLayout.setScrollTimeInSec(1); //set scroll delay in seconds :

        setSliderViews();
        sharedPreferences = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String pic = sharedPreferences.getString("picture","null");
        //Picasso.get().load(pic).into(imageView);
        Glide.with(Home.this)
                .load(pic)
                .centerCrop()
                .placeholder(R.drawable.user2)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(imageView);

        Name.setText(sharedPreferences.getString("name","null"));
        Phone.setText(sharedPreferences.getString("phone","null"));


    }

    private void setSliderViews() {
        for (int i = 0; i <= 3; i++) {

            DefaultSliderView sliderView = new DefaultSliderView(this);

            switch (i) {
                case 0:
                    sliderView.setImageUrl("https://images.pexels.com/photos/547114/pexels-photo-547114.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
                    break;
                case 1:
                    sliderView.setImageUrl("https://images.pexels.com/photos/218983/pexels-photo-218983.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
                    break;
                case 2:
                    sliderView.setImageUrl("https://images.pexels.com/photos/747964/pexels-photo-747964.jpeg?auto=compress&cs=tinysrgb&h=750&w=1260");
                    break;
                case 3:
                    sliderView.setImageUrl("https://images.pexels.com/photos/929778/pexels-photo-929778.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
                    break;
            }

            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
            // sliderView.setDescription(" " + (i + 1));
            final int finalI = i;
            sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(SliderView sliderView) {
//                    final SweetAlertDialog dialog = new SweetAlertDialog(Home.this,SweetAlertDialog.WARNING_TYPE);
//                    dialog.setTitleText("Are you sure?");
//                    dialog.setContentText("you want to logout");
//                    dialog.setConfirmText("Yes");
//                    dialog.setCancelText("No");
//                    dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                        @Override
//                        public void onClick(SweetAlertDialog sweetAlertDialog) {
//                            dialog.dismiss();
//                        }
//                    });
//                    dialog.show();
//                    dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                        @Override
//                        public void onClick(SweetAlertDialog sweetAlertDialog) {
//
//                            SharedPreferences preferences = getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE);
//                            preferences.edit().clear().commit();
//
//                            Intent intent = new Intent(Home.this,MainActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(intent);
//                        }
//                    });

                }
            });

            //at last add this view in your layout :
            sliderLayout.addSliderView(sliderView);

        }

    }

    public void Listing(View view) {
        startActivity(new Intent(Home.this,Listing.class));
    }

    public void back(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Home.this);
        alertDialog.setMessage("Are you sure you want to exit?");
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
                dialogInterface.dismiss();
            }
        });

        alertDialog.show();
    }

    public void update(View view) {
        Intent i = new Intent(Home.this,Update.class);
        startActivity(i);
    }

    public void payment_history(View view) {
        Intent i = new Intent(Home.this,Paymenthistory.class);
        startActivity(i);
    }

    public void Fees(View view) {
        Intent i = new Intent(Home.this,Fees.class);
        startActivity(i);
    }

    public void notificationy(View view) {
        Intent i = new Intent(Home.this,Notification.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Home.this);
        alertDialog.setMessage("Are you sure you want to exit?");
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();

                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

    public void mytecher(View view) {
        Intent i = new Intent(Home.this,My_teacher.class);
        startActivity(i);
    }

    public void logout(View view) {
        final SweetAlertDialog dialog = new SweetAlertDialog(Home.this,SweetAlertDialog.WARNING_TYPE);
        dialog.setTitleText("Are you sure?");
        dialog.setContentText("you want to logout");
        dialog.setConfirmText("Yes");
        dialog.setCancelText("No");
        dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {

                SharedPreferences preferences = getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE);
                preferences.edit().clear().commit();

                Intent intent = new Intent(Home.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }

    public void menu(View view) {

        PopupMenu popup = new PopupMenu(Home.this, view);
        popup.setOnMenuItemClickListener(Home.this);
        popup.inflate(R.menu.menu_res);
        popup.show();

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.edit_profile:

                Intent i = new Intent(Home.this,Update.class);
                startActivity(i);
                return true;

            case R.id.change_password:

                Intent in = new Intent(Home.this,Change_password.class);
                startActivity(in);
                return true;

            case R.id.logout:

                final SweetAlertDialog dialog = new SweetAlertDialog(Home.this,SweetAlertDialog.WARNING_TYPE);
                dialog.setTitleText("Are you sure?");
                dialog.setContentText("you want to logout");
                dialog.setConfirmText("Yes");
                dialog.setCancelText("No");
                dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        SharedPreferences preferences = getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE);
                        preferences.edit().clear().apply();

                        Intent intent = new Intent(Home.this,MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });

                return true;
            default:
                return false;
        }
    }

}