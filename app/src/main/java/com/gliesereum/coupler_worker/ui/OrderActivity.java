package com.gliesereum.coupler_worker.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.gliesereum.coupler_worker.R;
import com.gliesereum.coupler_worker.network.APIClient;
import com.gliesereum.coupler_worker.network.APIInterface;
import com.gliesereum.coupler_worker.network.CustomCallback;
import com.gliesereum.coupler_worker.network.json.carwash.AllCarWashResponse;
import com.gliesereum.coupler_worker.network.json.carwash.PackagesItem;
import com.gliesereum.coupler_worker.network.json.carwash.ServicePricesItem;
import com.gliesereum.coupler_worker.network.json.carwash.ServicesItem;
import com.gliesereum.coupler_worker.network.json.order.OrderBody;
import com.gliesereum.coupler_worker.network.json.order.OrderResponse;
import com.gliesereum.coupler_worker.network.json.record.AllRecordResponse;
import com.gliesereum.coupler_worker.util.FastSave;
import com.gliesereum.coupler_worker.util.Util;
import com.gohn.nativedialog.ButtonType;
import com.gohn.nativedialog.NDialog;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Response;

import static com.gliesereum.coupler_worker.util.Constants.ACCESS_TOKEN;

public class OrderActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "TAG";
    private APIInterface API;
    private CustomCallback customCallback;
    private OrderBody orderBody;
    private AllCarWashResponse carWash;
    private Long begin = 0L;
    private Map<String, PackagesItem> packageMap;
    private Map<String, ServicePricesItem> servicePriceMap;
    private Map<String, ServicesItem> serviceMap;
    private List<String> nameOfServiceList;
    private Calendar date;
    private ImageView backBtn;
    private ConstraintLayout packageBlock;
    private LinearLayout packageLianer;
    private TextView durationLabel;
    private TextView priceLabel;
    private TextView discountTextView;
    private LinearLayout serviceLianear;
    private Button orderButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_new);
        initData();
        initView();
        setPackages(carWash);
    }


    private void initData() {
        FastSave.init(getApplicationContext());
        API = APIClient.getClient().create(APIInterface.class);
        customCallback = new CustomCallback(this, this);
        carWash = FastSave.getInstance().getObject("carWash", AllCarWashResponse.class);
        orderBody = new OrderBody();
        packageMap = new HashMap<>();
        servicePriceMap = new HashMap<>();
        serviceMap = new HashMap<>();
        nameOfServiceList = new ArrayList<>();
        fillServicePackageMap();
    }

    private void initView() {
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        packageBlock = findViewById(R.id.packageBlock);
        packageLianer = findViewById(R.id.packageLianer);
        durationLabel = findViewById(R.id.durationLabel);
        priceLabel = findViewById(R.id.priceLabel);
        discountTextView = findViewById(R.id.discountTextView);
        serviceLianear = findViewById(R.id.serviceLianear);
        orderButton = findViewById(R.id.orderButton);
        orderButton.setOnClickListener(this);
    }


    private void setServicePrices(AllCarWashResponse carWash) {
        Log.d(TAG, "setServicePrices: ");
        serviceLianear.removeAllViews();
        View layout2;
        CheckBox checkBox;
        TextView timeServiceLabel;
        TextView priceServiceLabel;
        int index = 0;
        for (int i = 0; i < carWash.getServicePrices().size(); i++) {
            if (!serviceMap.containsKey(carWash.getServicePrices().get(i).getId())) {
                layout2 = LayoutInflater.from(this).inflate(R.layout.service_order_item, serviceLianear, false);
                checkBox = layout2.findViewById(R.id.nameServiceLabel);
                timeServiceLabel = layout2.findViewById(R.id.timeServiceLabel);
                priceServiceLabel = layout2.findViewById(R.id.priceServiceLabel);
                checkBox.setText(carWash.getServicePrices().get(i).getName());
                timeServiceLabel.setText(String.valueOf(carWash.getServicePrices().get(i).getDuration()));
                priceServiceLabel.setText(String.valueOf(carWash.getServicePrices().get(i).getPrice()));
                checkBox.setTag(carWash.getServicePrices().get(i).getId());
                checkBox.setTag(R.string.tagKeyDuration, carWash.getServicePrices().get(i).getDuration());
                checkBox.setTag(R.string.tagKeyPrice, carWash.getServicePrices().get(i).getPrice());
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            durationLabel.setText(String.valueOf(Integer.parseInt(durationLabel.getText().toString()) + ((int) buttonView.getTag(R.string.tagKeyDuration))));
                            priceLabel.setText(String.valueOf(Integer.parseInt(priceLabel.getText().toString()) + ((int) buttonView.getTag(R.string.tagKeyPrice))));
                        } else {
                            durationLabel.setText(String.valueOf(Integer.parseInt(durationLabel.getText().toString()) - ((int) buttonView.getTag(R.string.tagKeyDuration))));
                            priceLabel.setText(String.valueOf(Integer.parseInt(priceLabel.getText().toString()) - ((int) buttonView.getTag(R.string.tagKeyPrice))));
                        }
                    }
                });
                serviceLianear.addView(layout2);
            } else {
                layout2 = LayoutInflater.from(this).inflate(R.layout.service_order_item, serviceLianear, false);
                checkBox = layout2.findViewById(R.id.nameServiceLabel);
                timeServiceLabel = layout2.findViewById(R.id.timeServiceLabel);
                priceServiceLabel = layout2.findViewById(R.id.priceServiceLabel);
                checkBox.setText(carWash.getServicePrices().get(i).getName());
                timeServiceLabel.setText(String.valueOf(carWash.getServicePrices().get(i).getDuration()));
                priceServiceLabel.setText(String.valueOf(carWash.getServicePrices().get(i).getPrice()));
                checkBox.setTag(carWash.getServicePrices().get(i).getId());
                checkBox.setTag(R.string.tagKeyDuration, carWash.getServicePrices().get(i).getDuration());
                checkBox.setTag(R.string.tagKeyPrice, carWash.getServicePrices().get(i).getPrice());
                checkBox.setChecked(true);
//                checkBox.setClickable(false);
                checkBox.setEnabled(false);
                serviceLianear.addView(layout2, index++);
            }
        }

    }

    private void fillServicePackageMap() {
        for (int i = 0; i < carWash.getPackages().size(); i++) {
            packageMap.put(carWash.getPackages().get(i).getId(), carWash.getPackages().get(i));
        }
        for (int i = 0; i < carWash.getServicePrices().size(); i++) {
            servicePriceMap.put(carWash.getServicePrices().get(i).getId(), carWash.getServicePrices().get(i));
        }
    }

    private void setPackages(AllCarWashResponse carWash) {
        if (carWash.getPackages() != null && carWash.getPackages().size() != 0) {
            View layout2 = LayoutInflater.from(this).inflate(R.layout.package_btn2, packageLianer, false);
            MaterialButton packageBtn = layout2.findViewById(R.id.packageBtn);
            packageBtn.setText("Не выбран");
            packageBtn.setTag("default");
            packageBtn.setOnClickListener(v -> {
                for (int j = 0; j < packageLianer.getChildCount(); j++) {
                    ConstraintLayout constraintLayout = ((ConstraintLayout) packageLianer.getChildAt(j));
                    if (constraintLayout.getChildAt(0).getTag().equals(v.getTag())) {
                        ((MaterialButton) constraintLayout.getChildAt(0)).setBackgroundTintList(ContextCompat.getColorStateList(OrderActivity.this, R.color.grey));
                    } else {
                        ((MaterialButton) constraintLayout.getChildAt(0)).setBackgroundTintList(ContextCompat.getColorStateList(OrderActivity.this, R.color.blackPrimary));
                    }
                }
                serviceMap.clear();
                nameOfServiceList.clear();
                orderBody.setPackageId(null);
                priceLabel.setText("0");
                durationLabel.setText("0");
                discountTextView.setText("0%");
                Log.d(TAG, "setPackages: ");
                setServicePrices(carWash);
            });
            packageLianer.addView(layout2);
            for (int i = 0; i < carWash.getPackages().size(); i++) {
                layout2 = LayoutInflater.from(this).inflate(R.layout.package_btn2, packageLianer, false);
                packageBtn = layout2.findViewById(R.id.packageBtn);
                packageBtn.setText(carWash.getPackages().get(i).getName());
                packageBtn.setTag(carWash.getPackages().get(i).getId());
                packageBtn.setOnClickListener(v -> {
                    serviceMap.clear();
                    nameOfServiceList.clear();
                    orderBody.setPackageId((String) v.getTag());
                    for (int j = 0; j < packageLianer.getChildCount(); j++) {
                        ConstraintLayout constraintLayout = ((ConstraintLayout) packageLianer.getChildAt(j));
                        if (constraintLayout.getChildAt(0).getTag().equals(v.getTag())) {
                            ((MaterialButton) constraintLayout.getChildAt(0)).setBackgroundTintList(ContextCompat.getColorStateList(OrderActivity.this, R.color.grey));
                        } else {
                            ((MaterialButton) constraintLayout.getChildAt(0)).setBackgroundTintList(ContextCompat.getColorStateList(OrderActivity.this, R.color.blackPrimary));
                        }
                    }
                    for (int j = 0; j < packageMap.get(v.getTag()).getServices().size(); j++) {
                        nameOfServiceList.add(packageMap.get(v.getTag()).getServices().get(j).getName());
                        serviceMap.put(packageMap.get(v.getTag()).getServices().get(j).getId(), packageMap.get(v.getTag()).getServices().get(j));
                    }
                    discountTextView.setText(String.valueOf(packageMap.get(v.getTag()).getDiscount()) + "%");
                    setServicePrices(carWash);
                    String duration = String.valueOf(packageMap.get(v.getTag()).getDuration());
                    durationLabel.setText(duration);
                    double price = 0;
                    for (int j = 0; j < packageMap.get(v.getTag()).getServices().size(); j++) {
                        price += packageMap.get(v.getTag()).getServices().get(j).getPrice();
                    }
                    priceLabel.setText(String.valueOf((int) (price - ((price / 100) * packageMap.get(v.getTag()).getDiscount()))));
                    packageBlock.setVisibility(View.VISIBLE);
                });
                packageLianer.addView(layout2);
                ConstraintLayout constraintLayout = ((ConstraintLayout) packageLianer.getChildAt(0));
                ((MaterialButton) constraintLayout.getChildAt(0)).performClick();
            }
            setServicePrices(carWash);
        } else {
            priceLabel.setText("0");
            durationLabel.setText("0");
            discountTextView.setText("0%");
            packageBlock.setVisibility(View.GONE);
            setServicePrices(carWash);
        }
    }

    public void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        new DatePickerDialog(OrderActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(OrderActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        begin = date.getTimeInMillis();
                        getRecordFreeTime(false);
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), true).show();
            }
        },
                currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.orderButton:
                openPreOrderDialog();
                break;
        }
    }

    private boolean checkCarWashWorkTime() {
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
            if (carWash.getWorkTimes().get(i).getDayOfWeek().equals(dayOfWeek)) {
                if (carWash.getWorkTimes().get(i).getFrom() < (System.currentTimeMillis() + (carWash.getTimeZone() * 60000)) && carWash.getWorkTimes().get(i).getTo() > (System.currentTimeMillis() + (carWash.getTimeZone() * 60000))) {
                    Log.d("test_log", "openPreOrderDialog: " + carWash.getWorkTimes().get(i).getFrom());
                    Log.d("test_log", "openPreOrderDialog: " + (System.currentTimeMillis() + (carWash.getTimeZone() * 60000)));
                    Log.d("test_log", "openPreOrderDialog: " + carWash.getWorkTimes().get(i).getTo());
                    return true;
                }
            }
        }
        return false;
    }

    private void openPreOrderDialog() {
        NDialog preOrderNewDialog = new NDialog(OrderActivity.this, ButtonType.NO_BUTTON);
        preOrderNewDialog.isCancelable(true);
        preOrderNewDialog.setCustomView(R.layout.pre_order_new_dialod);
        List<View> childViews = preOrderNewDialog.getCustomViewChildren();
        for (View childView : childViews) {
            switch (childView.getId()) {
                case R.id.preOrderLabel1:
                    TextView preOrderLabel1 = childView.findViewById(R.id.preOrderLabel1);
                    if (Util.checkCarWashWorkTime(carWash)) {
                        preOrderLabel1.setText("У Вас есть возможность заказать мойку на ближайшее или на выбраное время");
                    } else {
                        preOrderLabel1.setText("Мойка сейчас не работает. у  Вас есть возможность заказать мойку только на выбранное время");
                    }
                    break;
                case R.id.preOrderLabel2:
                    TextView preOrderLabel2 = childView.findViewById(R.id.preOrderLabel2);
                    if (Util.checkCarWashWorkTime(carWash)) {
                        preOrderLabel2.setText("Пожайлуста, сделайте свой выбор");
                    } else {
                        preOrderLabel2.setText("");
                    }
                    break;
                case R.id.timeOrderBtn:
                    Button okBtn = childView.findViewById(R.id.timeOrderBtn);
                    okBtn.setOnClickListener(v -> {
                        preOrderNewDialog.dismiss();
                        showDateTimePicker();
                    });
                    break;
                case R.id.nowOrderBtn:
                    if (Util.checkCarWashWorkTime(carWash)) {
                        Button nowOrderBtn = childView.findViewById(R.id.nowOrderBtn);
                        nowOrderBtn.setOnClickListener(v -> {
                            preOrderNewDialog.dismiss();
                            getRecordFreeTime(true);
                        });
                    } else {
                        Button nowOrderBtn = childView.findViewById(R.id.nowOrderBtn);
                        nowOrderBtn.setEnabled(false);
                    }
                    break;
            }
        }
        preOrderNewDialog.show();
    }

    private void getRecordFreeTime(boolean nowFlag) {
//        orderBody.setWorkingSpaceId(null);
//        orderBody.setTargetId(FastSave.getInstance().getString(CAR_ID, ""));
        orderBody.setBusinessId(carWash.getId());
        if (nowFlag) {
            orderBody.setBegin(System.currentTimeMillis() + (carWash.getTimeZone() * 60000));
        } else {
            orderBody.setBegin(begin);
        }
        orderBody.setDescription("Android");
        List<String> list = new ArrayList<>();
        for (int i = 0; i < serviceLianear.getChildCount(); i++) {
            if (((CheckBox) ((ConstraintLayout) serviceLianear.getChildAt(i)).getChildAt(0)).isChecked()) {
                list.add((String) ((CheckBox) ((ConstraintLayout) serviceLianear.getChildAt(i)).getChildAt(0)).getTag());
            }
            Log.d(TAG, "getRecordFreeTime: ");
        }
        orderBody.setServicesIds(list);
        API.preOrder(FastSave.getInstance().getString(ACCESS_TOKEN, ""), orderBody)
                .enqueue(customCallback.getResponseWithProgress(new CustomCallback.ResponseCallback<OrderResponse>() {
                    @Override
                    public void onSuccessful(Call<OrderResponse> call, Response<OrderResponse> response) {
                        orderBody.setWorkingSpaceId(response.body().getWorkingSpaceId());
                        orderBody.setBegin(response.body().getBegin());
                        NDialog nDialog = new NDialog(OrderActivity.this, ButtonType.NO_BUTTON);
                        nDialog.isCancelable(false);
                        nDialog.setCustomView(R.layout.order_view);
                        List<View> childViews = nDialog.getCustomViewChildren();
                        for (View childView : childViews) {
                            switch (childView.getId()) {
                                case R.id.time:
                                    TextView time = childView.findViewById(R.id.time);
                                    time.setText(Util.getStringTime(response.body().getBegin()));
                                    break;
                                case R.id.okBtn:
                                    Button okBtn = childView.findViewById(R.id.okBtn);
                                    okBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            API.doOrder(FastSave.getInstance().getString(ACCESS_TOKEN, ""), orderBody)
                                                    .enqueue(customCallback.getResponseWithProgress(new CustomCallback.ResponseCallback<AllRecordResponse>() {
                                                        @Override
                                                        public void onSuccessful(Call<AllRecordResponse> call, Response<AllRecordResponse> response) {
                                                            nDialog.dismiss();
                                                            Toast.makeText(OrderActivity.this, "Запись добавленна в список", Toast.LENGTH_SHORT).show();
//                                                            startActivity(new Intent(OrderActivity.this, RecordListActivity.class));
                                                            FastSave.getInstance().saveObject("RECORD", response.body());
                                                            startActivity(new Intent(OrderActivity.this, SingleRecordActivity.class));
                                                            finish();
                                                        }

                                                        @Override
                                                        public void onEmpty(Call<AllRecordResponse> call, Response<AllRecordResponse> response) {

                                                        }
                                                    }));
                                        }
                                    });
                                    break;
                                case R.id.backBtn:
                                    Button backBtn = childView.findViewById(R.id.backBtn);
                                    backBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            nDialog.dismiss();
                                        }
                                    });
                                    break;
                            }
                        }
                        nDialog.show();
                    }

                    @Override
                    public void onEmpty(Call<OrderResponse> call, Response<OrderResponse> response) {

                    }
                }));
    }
}
