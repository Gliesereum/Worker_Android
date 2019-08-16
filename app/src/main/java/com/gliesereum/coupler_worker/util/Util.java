package com.gliesereum.coupler_worker.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.chaos.view.PinView;
import com.gliesereum.coupler_worker.BuildConfig;
import com.gliesereum.coupler_worker.R;
import com.gliesereum.coupler_worker.network.APIClient;
import com.gliesereum.coupler_worker.network.APIInterface;
import com.gliesereum.coupler_worker.network.CustomCallback;
import com.gliesereum.coupler_worker.network.json.carwash.AllCarWashResponse;
import com.gliesereum.coupler_worker.network.json.notificatoin.RegistrationTokenDeleteResponse;
import com.gliesereum.coupler_worker.network.json.pin.PinBody;
import com.gliesereum.coupler_worker.network.json.pin.PinResponse;
import com.gliesereum.coupler_worker.ui.ClientsListActivity;
import com.gliesereum.coupler_worker.ui.LockActivity;
import com.gliesereum.coupler_worker.ui.LoginActivity;
import com.gliesereum.coupler_worker.ui.RecordListActivity;
import com.gohn.nativedialog.ButtonType;
import com.gohn.nativedialog.NDialog;
import com.labters.lottiealertdialoglibrary.ClickListener;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Response;

import static com.gliesereum.coupler_worker.util.Constants.ACCESS_EXPIRATION_DATE;
import static com.gliesereum.coupler_worker.util.Constants.ACCESS_TOKEN;
import static com.gliesereum.coupler_worker.util.Constants.ACCESS_TOKEN_WITHOUT_BEARER;
import static com.gliesereum.coupler_worker.util.Constants.FIREBASE_TOKEN;
import static com.gliesereum.coupler_worker.util.Constants.IS_EXIST_PIN;
import static com.gliesereum.coupler_worker.util.Constants.IS_LOCK;
import static com.gliesereum.coupler_worker.util.Constants.IS_LOGIN;
import static com.gliesereum.coupler_worker.util.Constants.ONLY_CLIENT;
import static com.gliesereum.coupler_worker.util.Constants.PIN_CODE;
import static com.gliesereum.coupler_worker.util.Constants.REFRESH_EXPIRATION_DATE;
import static com.gliesereum.coupler_worker.util.Constants.REFRESH_TOKEN;
import static com.gliesereum.coupler_worker.util.Constants.USER_AVATAR;
import static com.gliesereum.coupler_worker.util.Constants.USER_ID;
import static com.gliesereum.coupler_worker.util.Constants.USER_NAME;
import static com.gliesereum.coupler_worker.util.Constants.USER_SECOND_NAME;

public class Util {
    private Activity activity;
    private Toolbar toolbar;
    private int identifier;
    private Drawer result;

    private PinView codeView;
    private PinView newPinCode;
    private PinView confirmPinCode;
    private String pin = "";
    private String newPin = "";
    private String confirmPin = "";
    private Button createPinBtn;

    private APIInterface API;
    private CustomCallback customCallback;
    private LottieAlertDialog alertDialog;



    public Util(Activity activity, Toolbar toolbar, int identifier) {
        this.activity = activity;
        this.toolbar = toolbar;
        this.identifier = identifier;
    }

    public Util() {
    }

    public void lockScreen(Context context, Activity activity, ImageButton lockBtn) {
        Log.d("TAG", "lockScreen: ");
        API = APIClient.getClient().create(APIInterface.class);
        customCallback = new CustomCallback(context, activity);
        if (FastSave.getInstance().getBoolean(IS_EXIST_PIN, false)) {
            FastSave.getInstance().saveBoolean(IS_LOCK, true);
            activity.startActivity(new Intent(context, LockActivity.class));
        } else {
            NDialog newPinCodeDialog = new NDialog(context, ButtonType.NO_BUTTON);
            newPinCodeDialog.isCancelable(true);
            newPinCodeDialog.setCustomView(R.layout.create_pin);
            List<View> childViews = newPinCodeDialog.getCustomViewChildren();
            for (View childView : childViews) {
                switch (childView.getId()) {
                    case R.id.confirmPinCode:
                        confirmPinCode = childView.findViewById(R.id.confirmPinCode);
                        confirmPinCode.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                confirmPin = String.valueOf(s);
                                Log.d("PIN", "onTextChanged: " + confirmPin);
                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });
                        break;
                    case R.id.newPinCode:
                        newPinCode = childView.findViewById(R.id.newPinCode);
                        newPinCode.setFocusable(true);
                        newPinCode.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                newPin = String.valueOf(s);
                                Log.d("PIN", "onTextChanged: " + newPin);
                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });
                        break;

                    case R.id.createPinBtn:
                        createPinBtn = childView.findViewById(R.id.createPinBtn);
                        createPinBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                createPinBtn.setEnabled(false);
                                if (newPin.equals(confirmPin)) {
                                    API.savePinCode(FastSave.getInstance().getString(ACCESS_TOKEN, ""), new PinBody(newPin))
                                            .enqueue(customCallback.getResponseWithProgress(new CustomCallback.ResponseCallback<PinResponse>() {
                                                @Override
                                                public void onSuccessful(Call<PinResponse> call, Response<PinResponse> response) {
                                                    newPinCodeDialog.dismiss();
                                                    Toast.makeText(context, "PIN код установлен", Toast.LENGTH_SHORT).show();
                                                    FastSave.getInstance().saveBoolean(IS_EXIST_PIN, true);
                                                    FastSave.getInstance().saveString(PIN_CODE, confirmPin);
                                                    FastSave.getInstance().saveBoolean(IS_LOCK, true);
                                                    lockScreen(context, activity, lockBtn);
                                                }

                                                @Override
                                                public void onEmpty(Call<PinResponse> call, Response<PinResponse> response) {

                                                }
                                            }));

                                } else {
                                    createPinBtn.setEnabled(true);
                                    newPin = "";
                                    confirmPin = "";
                                    newPinCode.setText("");
                                    confirmPinCode.setText("");
                                    Toast.makeText(context, "PIN коды не совпадают", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        break;
                }
            }
            newPinCodeDialog.show();
        }

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
        SecondaryDrawerItem clients = new SecondaryDrawerItem().withName("Клиенты").withIdentifier(2).withTag("clients").withSelectable(false).withIcon(R.drawable.ic_outline_contacts_24px).withIconTintingEnabled(true);
        SecondaryDrawerItem logoutItem = new SecondaryDrawerItem().withName("Выйти").withIdentifier(3).withSelectable(false).withTag("logout").withSelectable(false).withIcon(R.drawable.ic_outline_exit_to_app_24px).withIconTintingEnabled(true);
//        SecondaryDrawerItem loginItem = new SecondaryDrawerItem().withName("Вход").withIdentifier(4).withSelectable(false).withTag("login").withSelectable(false).withIcon(R.drawable.ic_outline_monetization_on_24px).withIconTintingEnabled(true);
        SecondaryDrawerItem versionItem = new SecondaryDrawerItem().withName("v" + BuildConfig.VERSION_NAME + " beta").withIdentifier(5).withSelectable(false).withTag("version").withSelectable(false);

//        if (!FastSave.getInstance().getBoolean(IS_LOGIN, false)) {
//            myBusinesses.withEnabled(false);
//            analytics.withEnabled(false);
//            orders.withEnabled(false);
//            settings.withEnabled(false);
//            profileItem.withEnabled(false);
//        }

        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                Picasso.get().load(uri).placeholder(placeholder).transform(new CircleTransform()).into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                Picasso.get().cancelRequest(imageView);
            }
        });

        ProfileDrawerItem profileDrawerItem = new ProfileDrawerItem();
        profileDrawerItem.withName(FastSave.getInstance().getString(USER_NAME, "") + " " + FastSave.getInstance().getString(USER_SECOND_NAME, ""));
        if (FastSave.getInstance().getString(USER_AVATAR, "").equals("")) {
            profileDrawerItem.withIcon(activity.getResources().getDrawable(R.mipmap.ic_launcher_round));
        } else {
            profileDrawerItem.withIcon(FastSave.getInstance().getString(USER_AVATAR, ""));
        }

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(activity)
                .withTextColorRes(R.color.white)
                .withHeaderBackground(R.drawable.account_switcher_custom)
                .withSelectionListEnabledForSingleProfile(false)
                .withCompactStyle(true)
                .addProfiles(profileDrawerItem)
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
                new DividerDrawerItem(),
                logoutItem
        );
        result = drawerBuilder.build();
        drawerBuilder.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                switch (drawerItem.getTag().toString()) {
                    case "orders":
                        activity.startActivity(new Intent(activity.getApplicationContext(), RecordListActivity.class));
                        result.closeDrawer();
                        break;
                    case "clients":
                        FastSave.getInstance().saveBoolean(ONLY_CLIENT, true);
                        activity.startActivity(new Intent(activity.getApplicationContext(), ClientsListActivity.class));
                        result.closeDrawer();
                        break;
                    case "logout":
                        if (result.isDrawerOpen()) {
                            result.closeDrawer();
                        }
                        alertDialog = new LottieAlertDialog.Builder(activity, DialogTypes.TYPE_QUESTION)
                                .setTitle("Выход")
                                .setDescription("Вы действительно хотите выйти из Coupler Worker?")
                                .setPositiveText("Да")
                                .setNegativeText("Нет")
                                .setPositiveButtonColor(activity.getResources().getColor(R.color.md_red_A200))
                                .setPositiveListener(new ClickListener() {
                                    @Override
                                    public void onClick(@NotNull LottieAlertDialog lottieAlertDialog) {
                                        deleteRegistrationToken();
                                        FastSave.getInstance().deleteValue(IS_LOGIN);
                                        FastSave.getInstance().deleteValue(USER_ID);
                                        FastSave.getInstance().deleteValue(ACCESS_TOKEN);
                                        FastSave.getInstance().deleteValue(ACCESS_TOKEN_WITHOUT_BEARER);
                                        FastSave.getInstance().deleteValue(REFRESH_TOKEN);
                                        FastSave.getInstance().deleteValue(ACCESS_EXPIRATION_DATE);
                                        FastSave.getInstance().deleteValue(REFRESH_EXPIRATION_DATE);
                                        activity.startActivity(new Intent(activity.getApplicationContext(), LoginActivity.class));
                                        activity.finish();
                                    }
                                })
                                .setNegativeListener(new ClickListener() {
                                    @Override
                                    public void onClick(@NotNull LottieAlertDialog lottieAlertDialog) {
                                        alertDialog.dismiss();
                                    }
                                })
                                .build();
                        alertDialog.setCancelable(false);
                        alertDialog.show();
                        break;
                }

                return true;
            }
        });


//        result.addItem(new DividerDrawerItem());
//        if (FastSave.getInstance().getBoolean(IS_LOGIN, false)) {
//        result.addItem(logoutItem);
//        } else {
//            result.addItem(loginItem);
//        }
        result.addItem(versionItem);
    }

    private void deleteRegistrationToken() {
        API = APIClient.getClient().create(APIInterface.class);
        customCallback = new CustomCallback(activity.getApplicationContext(), activity);
        API.deleteRegistrationToken(FastSave.getInstance().getString(ACCESS_TOKEN, ""), FastSave.getInstance().getString(FIREBASE_TOKEN, ""))
                .enqueue(customCallback.getResponse(new CustomCallback.ResponseCallback<RegistrationTokenDeleteResponse>() {
                    @Override
                    public void onSuccessful(Call<RegistrationTokenDeleteResponse> call, Response<RegistrationTokenDeleteResponse> response) {

                    }

                    @Override
                    public void onEmpty(Call<RegistrationTokenDeleteResponse> call, Response<RegistrationTokenDeleteResponse> response) {

                    }
                }));

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
