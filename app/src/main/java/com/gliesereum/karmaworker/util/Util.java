package com.gliesereum.karmaworker.util;

import android.app.Activity;
import android.util.Log;

import com.gliesereum.karmaworker.network.json.carwash.AllCarWashResponse;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import androidx.appcompat.widget.Toolbar;

public class Util {
    private Activity activity;
    private Toolbar toolbar;
    private int identifier;


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

//    public void addNavigation() {
//        new DrawerBuilder().withActivity(activity).build();
//        PrimaryDrawerItem mapsItem = new PrimaryDrawerItem().withName("Карта").withIdentifier(1).withTag("maps").withIcon(R.drawable.map_icon);
//        SecondaryDrawerItem car_listItem = new SecondaryDrawerItem().withName("Список авто").withIdentifier(2).withTag("car_list").withSelectable(false).withIcon(R.drawable.my_cars);
//        SecondaryDrawerItem record_listItem = new SecondaryDrawerItem().withName("Список заказов").withIdentifier(3).withTag("record_list").withSelectable(false).withIcon(R.drawable.orders);
//        SecondaryDrawerItem profileItem = new SecondaryDrawerItem().withName("Мой Профиль").withIdentifier(4).withTag("profile").withSelectable(false).withIcon(R.drawable.profile);
//        SecondaryDrawerItem logoutItem = new SecondaryDrawerItem().withName("Выйти").withIdentifier(5).withSelectable(false).withTag("logout").withSelectable(false).withIcon(R.drawable.logout);
//        SecondaryDrawerItem loginItem = new SecondaryDrawerItem().withName("Вход").withIdentifier(6).withSelectable(false).withTag("login").withSelectable(false).withIcon(R.drawable.login);
//
//        if (!FastSave.getInstance().getBoolean(IS_LOGIN, false)) {
//            car_listItem.withEnabled(false);
//            record_listItem.withEnabled(false);
//            profileItem.withEnabled(false);
//        }
//
//        AccountHeader headerResult = new AccountHeaderBuilder()
//                .withActivity(activity)
//                .withHeaderBackground(R.drawable.cover_karma)
//                .addProfiles(
//                        new ProfileDrawerItem().withName(FastSave.getInstance().getString(USER_NAME, "") + " " + FastSave.getInstance().getString(USER_SECOND_NAME, ""))
//                                .withIcon(activity.getResources().getDrawable(R.mipmap.ic_launcher_round))
//                )
////                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
////                    @Override
////                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
////                        return false;
////                    }
////                })
//                .build();
//
//        DrawerBuilder drawerBuilder = new DrawerBuilder();
//        drawerBuilder.withAccountHeader(headerResult);
//        drawerBuilder.withActivity(activity);
//        drawerBuilder.withToolbar(toolbar);
//        drawerBuilder.withSelectedItem(identifier);
//        drawerBuilder.addDrawerItems(
//                mapsItem,
//                new DividerDrawerItem(),
//                car_listItem,
//                record_listItem,
//                profileItem
//        );
//        Drawer result = drawerBuilder.build();
//        drawerBuilder.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
//            @Override
//            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
//                switch (drawerItem.getTag().toString()) {
//                    case "maps":
//                        activity.startActivity(new Intent(activity.getApplicationContext(), MapsActivity.class));
//                        activity.finish();
//                        result.closeDrawer();
//                        break;
//                    case "car_list":
//                        activity.startActivity(new Intent(activity.getApplicationContext(), CarListActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
//                        result.closeDrawer();
//                        break;
//                    case "record_list":
//                        activity.startActivity(new Intent(activity.getApplicationContext(), RecordListActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
//                        result.closeDrawer();
//                        break;
//                    case "logout":
//                        FastSave.getInstance().saveBoolean(IS_LOGIN, false);
//                        FastSave.getInstance().deleteValue(USER_NAME);
//                        FastSave.getInstance().deleteValue(USER_SECOND_NAME);
//                        FastSave.getInstance().deleteValue(CAR_ID);
//                        FastSave.getInstance().deleteValue(CAR_BRAND);
//                        FastSave.getInstance().deleteValue(CAR_SERVICE_CLASS);
//                        FastSave.getInstance().deleteValue(CAR_MODEL);
//                        FastSave.getInstance().deleteValue(CAR_FILTER_LIST);
//                        activity.startActivity(new Intent(activity.getApplicationContext(), LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
//                        activity.finish();
//                        break;
//                    case "profile":
//                        activity.startActivity(new Intent(activity.getApplicationContext(), ProfileActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
//                        result.closeDrawer();
//                        break;
//                    case "login":
//                        activity.startActivity(new Intent(activity.getApplicationContext(), LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
//                        activity.finish();
//                        break;
//                }
//
//                return true;
//            }
//        });
//
//
//        if (FastSave.getInstance().getBoolean(IS_LOGIN, false)) {
//            result.addItem(logoutItem);
//        } else {
//            result.addItem(loginItem);
//        }
//
//    }

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

    public static String getStringDate(Long millisecond) {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format.format(new Date(millisecond));
    }

    public static long startOfDay(Long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        cal.set(Calendar.HOUR_OF_DAY, 0); //set hours to zero
        cal.set(Calendar.MINUTE, 0); // set minutes to zero
        cal.set(Calendar.SECOND, 0); //set seconds to zero
        return cal.getTimeInMillis();
    }

    public static long endOfDay(Long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        cal.set(Calendar.HOUR_OF_DAY, 23); //set hours to zero
        cal.set(Calendar.MINUTE, 59); // set minutes to zero
        cal.set(Calendar.SECOND, 59); //set seconds to zero
        return cal.getTimeInMillis();
    }

}
