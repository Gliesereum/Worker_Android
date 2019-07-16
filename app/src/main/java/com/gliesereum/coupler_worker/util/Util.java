package com.gliesereum.coupler_worker.util;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.gliesereum.coupler_worker.BuildConfig;
import com.gliesereum.coupler_worker.R;
import com.gliesereum.coupler_worker.network.json.carwash.AllCarWashResponse;
import com.gliesereum.coupler_worker.ui.ClientsListActivity;
import com.gliesereum.coupler_worker.ui.RecordListActivity;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static com.gliesereum.coupler_worker.util.Constants.ONLY_CLIENT;

public class Util {
    private Activity activity;
    private Toolbar toolbar;
    private int identifier;
    private Drawer result;


    public Util(Activity activity, Toolbar toolbar, int identifier) {
        this.activity = activity;
        this.toolbar = toolbar;
        this.identifier = identifier;

    }

    public static boolean checkCarWashWorkTime(AllCarWashResponse carWash) {
        String dayOfWeek = "";
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case Calendar.MONDAY:
                dayOfWeek = "MONDAY";
                break;
            case Calendar.TUESDAY:
                dayOfWeek = "TUESDAY";
                break;
            case Calendar.WEDNESDAY:
                dayOfWeek = "WEDNESDAY";
                break;
            case Calendar.THURSDAY:
                dayOfWeek = "THURSDAY";
                break;
            case Calendar.FRIDAY:
                dayOfWeek = "FRIDAY";
                break;
            case Calendar.SATURDAY:
                dayOfWeek = "SATURDAY";
                break;
            case Calendar.SUNDAY:
                dayOfWeek = "SUNDAY";
                break;
        }

        for (int i = 0; i < carWash.getWorkTimes().size(); i++) {
            if (carWash.getWorkTimes().get(i).getDayOfWeek().equals(dayOfWeek) && carWash.getWorkTimes().get(i).isIsWork()) {
                if (carWash.getWorkTimes().get(i).getFrom() < (System.currentTimeMillis() + (carWash.getTimeZone() * 60000)) && carWash.getWorkTimes().get(i).getTo() > (System.currentTimeMillis() + (carWash.getTimeZone() * 60000))) {
                    Log.d("test_log", "car wash work");
                    return true;
                }
            }
        }
        Log.d("test_log", "car wash not work: ");
        return false;
    }

    public void addNavigation() {
        new DrawerBuilder().withActivity(activity).build();
        PrimaryDrawerItem orders = new PrimaryDrawerItem().withName("Заказы").withIdentifier(1).withTag("orders").withIcon(R.drawable.ic_outline_monetization_on_24px).withIconTintingEnabled(true);
        SecondaryDrawerItem clients = new SecondaryDrawerItem().withName("Клиенты").withIdentifier(2).withTag("clients").withSelectable(false).withIcon(R.drawable.ic_outline_monetization_on_24px).withIconTintingEnabled(true);
        SecondaryDrawerItem logoutItem = new SecondaryDrawerItem().withName("Выйти").withIdentifier(3).withSelectable(false).withTag("logout").withSelectable(false).withIcon(R.drawable.ic_outline_monetization_on_24px).withIconTintingEnabled(true);
        SecondaryDrawerItem loginItem = new SecondaryDrawerItem().withName("Вход").withIdentifier(4).withSelectable(false).withTag("login").withSelectable(false).withIcon(R.drawable.ic_outline_monetization_on_24px).withIconTintingEnabled(true);
        SecondaryDrawerItem versionItem = new SecondaryDrawerItem().withName("v" + BuildConfig.VERSION_NAME + " beta").withIdentifier(5).withSelectable(false).withTag("version").withSelectable(false);

//        if (!FastSave.getInstance().getBoolean(IS_LOGIN, false)) {
//            myBusinesses.withEnabled(false);
//            analytics.withEnabled(false);
//            orders.withEnabled(false);
//            settings.withEnabled(false);
//            profileItem.withEnabled(false);
//        }

//        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
//            @Override
//            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
//                Picasso.get().load(uri).placeholder(placeholder).transform(new CircleTransform()).into(imageView);
//            }
//
//            @Override
//            public void cancel(ImageView imageView) {
//                Picasso.get().cancelRequest(imageView);
//            }
//        });

//        ProfileDrawerItem profileDrawerItem = new ProfileDrawerItem();
//        profileDrawerItem.withName(FastSave.getInstance().getString(USER_NAME, "") + " " + FastSave.getInstance().getString(USER_SECOND_NAME, ""));
//        if (FastSave.getInstance().getString(USER_AVATAR, "").equals("")) {
//            profileDrawerItem.withIcon(activity.getResources().getDrawable(R.mipmap.ic_launcher_round));
//        } else {
//            profileDrawerItem.withIcon(FastSave.getInstance().getString(USER_AVATAR, ""));
//        }

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(activity)
                .withTextColorRes(R.color.black)
                .withHeaderBackground(R.drawable.account_switcher_custom)
                .withSelectionListEnabledForSingleProfile(false)
                .withCompactStyle(true)
//                .addProfiles(profileDrawerItem)
                .build();

        DrawerBuilder drawerBuilder = new DrawerBuilder();
        drawerBuilder.withAccountHeader(headerResult);
        drawerBuilder.withActivity(activity);
        drawerBuilder.withToolbar(toolbar);
        drawerBuilder.withActionBarDrawerToggle(true);
        drawerBuilder.withSelectedItem(identifier);
        drawerBuilder.addDrawerItems(
                orders,
                clients,
                logoutItem
        );
        result = drawerBuilder.build();
        drawerBuilder.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                switch (drawerItem.getTag().toString()) {
                    case "orders":
                        Toast.makeText(activity, "orders", Toast.LENGTH_SHORT).show();
                        activity.startActivity(new Intent(activity.getApplicationContext(), RecordListActivity.class));
                        result.closeDrawer();
                        break;
                    case "clients":
                        FastSave.getInstance().saveBoolean(ONLY_CLIENT, true);
                        activity.startActivity(new Intent(activity.getApplicationContext(), ClientsListActivity.class));
                        result.closeDrawer();
                        break;
                    case "logout":
//                        if (result.isDrawerOpen()) {
//                            result.closeDrawer();
//                        }
//                        alertDialog = new LottieAlertDialog.Builder(activity, DialogTypes.TYPE_QUESTION)
//                                .setTitle("Выход")
//                                .setDescription("Вы действительно хотите выйти со своего профиля?")
//                                .setPositiveText("Да")
//                                .setNegativeText("Нет")
//                                .setPositiveButtonColor(activity.getResources().getColor(R.color.md_red_A200))
//                                .setPositiveListener(new ClickListener() {
//                                    @Override
//                                    public void onClick(@NotNull LottieAlertDialog lottieAlertDialog) {
//                                        deleteRegistrationToken();
//                                        FastSave.getInstance().deleteValue(IS_LOGIN);
//                                        FastSave.getInstance().deleteValue(USER_NAME);
//                                        FastSave.getInstance().deleteValue(USER_SECOND_NAME);
//                                        activity.startActivity(new Intent(activity.getApplicationContext(), LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
//                                        activity.finish();
//                                    }
//                                })
//                                .setNegativeListener(new ClickListener() {
//                                    @Override
//                                    public void onClick(@NotNull LottieAlertDialog lottieAlertDialog) {
//                                        alertDialog.dismiss();
//                                    }
//                                })
//                                .build();
//                        alertDialog.setCancelable(false);
//                        alertDialog.show();
                        break;
                }

                return true;
            }
        });


        result.addItem(new DividerDrawerItem());
//        if (FastSave.getInstance().getBoolean(IS_LOGIN, false)) {
        result.addItem(logoutItem);
//        } else {
//            result.addItem(loginItem);
//        }
        result.addItem(versionItem);
    }

    public static boolean checkExpirationToken(Long localDateTime) {
        if (localDateTime > System.currentTimeMillis()) {
            return true;
        } else {
            return false;
        }
    }

    public static String getStringTimeTrue(Long millisecond) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(new Date(millisecond));

    }

    public static String getStringTime(Long millisecond) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format.format(new Date(millisecond));

    }

    public static String getStringDateTrue(Long millisecond) {
//        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
//        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
//        calendar.setTimeInMillis(millisecond);
//        return format.format(calendar.getTime());
        SimpleDateFormat format = new SimpleDateFormat("dd:MM");
        return format.format(new Date(millisecond));

    }

    public static String getStringFullDateTrue(Long millisecond) {
//        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
//        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
//        calendar.setTimeInMillis(millisecond);
//        return format.format(calendar.getTime());
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format.format(new Date(millisecond));

    }

    public static String getStringDate(Long millisecond) {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format.format(new Date(millisecond));
    }

    public static long startOfDay(Long time) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTimeInMillis(time);
        cal.set(Calendar.HOUR_OF_DAY, 0); //set hours to zero
        cal.set(Calendar.MINUTE, 0); // set minutes to zero
        cal.set(Calendar.SECOND, 0); //set seconds to zero
        return cal.getTimeInMillis();
    }

    public static long endOfDay(Long time) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTimeInMillis(time);
        cal.set(Calendar.HOUR_OF_DAY, 23); //set hours to zero
        cal.set(Calendar.MINUTE, 59); // set minutes to zero
        cal.set(Calendar.SECOND, 59); //set seconds to zero
        return cal.getTimeInMillis();
    }

}
