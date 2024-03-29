package com.gliesereum.coupler_worker.network;

import com.gliesereum.coupler_worker.network.json.car.AllCarResponse;
import com.gliesereum.coupler_worker.network.json.carwash.AllCarWashResponse;
import com.gliesereum.coupler_worker.network.json.client_new.NewClientResponse;
import com.gliesereum.coupler_worker.network.json.client_record_new.ClientRecordNewResponse;
import com.gliesereum.coupler_worker.network.json.code.CodeResponse;
import com.gliesereum.coupler_worker.network.json.code.SigninBody;
import com.gliesereum.coupler_worker.network.json.notificatoin.NotificatoinBody;
import com.gliesereum.coupler_worker.network.json.notificatoin.RegistrationTokenDeleteResponse;
import com.gliesereum.coupler_worker.network.json.notificatoin.UserSubscribe;
import com.gliesereum.coupler_worker.network.json.order.OrderBody;
import com.gliesereum.coupler_worker.network.json.order.OrderResponse;
import com.gliesereum.coupler_worker.network.json.pin.PinBody;
import com.gliesereum.coupler_worker.network.json.pin.PinResponse;
import com.gliesereum.coupler_worker.network.json.pin.RemindPinCodeResponse;
import com.gliesereum.coupler_worker.network.json.record.AllRecordResponse;
import com.gliesereum.coupler_worker.network.json.record.PaymentResponse;
import com.gliesereum.coupler_worker.network.json.record.RecordsSearchBody;
import com.gliesereum.coupler_worker.network.json.status.StatusRegistration;
import com.gliesereum.coupler_worker.network.json.status.StatusResponse;
import com.gliesereum.coupler_worker.network.json.user.User;
import com.gliesereum.coupler_worker.network.json.user.UserResponse;
import com.gliesereum.coupler_worker.network.json.worker_new.WorkerResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIInterface {

    @GET("karma/v1/business-administrator/for-business")
    Call<RemindPinCodeResponse> isAdministrator(@Header("Authorization") String accessToken, @Query("businessId") String businessId);

    @PUT("karma/v1/record/update-time")
    Call<AllRecordResponse> updateTimeRecord(@Header("Authorization") String accessToken, @Query("beginTime") Long beginTime, @Query("idRecord") String idRecord);

    @POST("karma/v1/user-pin-code/remind-me")
    Call<RemindPinCodeResponse> remindPinCode(@Header("Authorization") String accessToken);

    @GET("karma/v1/user-pin-code")
    Call<PinResponse> getPinCode(@Header("Authorization") String accessToken);

    @POST("karma/v1/user-pin-code")
    Call<PinResponse> savePinCode(@Header("Authorization") String accessToken, @Body PinBody pinBody);

    @GET("karma/v1/worker/by-business")
    Call<WorkerResponse> getAllWorkersByBusiness(@Header("Authorization") String accessToken, @Query("businessId") String businessId);


    @GET("karma/v1/business/customers")
    Call<NewClientResponse> getAllClientsByCorporation(@Header("Authorization") String accessToken, @Query("corporationId") String corporationId, @Query("page") Integer page, @Query("size") Integer size);

//    @GET("karma/v1/record/by-client-for-corporation")
//    Call<ClientRecordNewResponse> getClientsRecord(@Header("Authorization") String accessToken, @Query("corporationIds") List<String> corporationIds, @Query("clientId") String clientId);

    @GET("karma/v1/business/customers")
    Call<NewClientResponse> searchClients(@Header("Authorization") String accessToken, @Query("corporationId") String corporationId, @Query("query") String query);

    //STATUS
    @GET("status")
    Call<StatusResponse> checkStatus();

    //NOTIFICATION
    @POST("notification/v1/user-device")
    Call<NotificatoinBody> sendRegistrationToken(@Header("Authorization") String accessToken, @Body NotificatoinBody notificatoinBody);

    @POST("notification/v1/user-subscribe/list")
    Call<List<UserSubscribe>> subscribeToChanel(@Header("Authorization") String accessToken, @Body NotificatoinBody notificatoinBody, @Query("overrideExistedDestination") Boolean overrideExistedDestination);

    @DELETE("notification/v1/user-device")
    Call<RegistrationTokenDeleteResponse> deleteRegistrationToken(@Header("Authorization") String accessToken, @Query("registrationToken") String registrationToken);

    //ACCOUNT
    @GET("karma/v1/worker/exist/byPhone")
    Call<StatusRegistration> checkExist(@Query("phone") String phone);

    @GET("account/v1/phone/code")
    Call<CodeResponse> getPhoneCode(@Query("phone") String phone);

    @POST("account/v1/auth/signin")
    Call<UserResponse> signIn(@Body SigninBody signinBody);

    @PUT("account/v1/user")
    Call<User> updateUser(@Header("Authorization") String accessToken, @Body User user);

    @POST("account/v1/auth/refresh")
    Call<UserResponse> refreshAccessToken(@Query("refreshToken") String refreshToken);

    @GET("account/v1/auth/check")
    Call<UserResponse> checkAccessToken(@Query("accessToken") String accessToken);

    //CAR
    @GET("karma/v1/car/user/as-worker")
    Call<List<AllCarResponse>> getClientCar(@Header("Authorization") String accessToken, @Query("clientId") String clientId, @Query("corporationId") String corporationId);

    @GET("karma/v1/car/{carId}")
    Call<AllCarResponse> getCarById(@Header("Authorization") String accessToken, @Path("carId") String id);

    //CARWASH
    @GET("karma/v1/business/by-current-user/like-worker/full-model")
    Call<List<AllCarWashResponse>> getAllBusiness(@Header("Authorization") String accessToken);

    //RECORD
    @POST("karma/v1/record/free-time")
    Call<OrderResponse> preOrder(@Header("Authorization") String accessToken, @Body OrderBody orderBody);

    @POST("karma/v1/record/create-for-business")
    Call<AllRecordResponse> doOrder(@Header("Authorization") String accessToken, @Body OrderBody orderBody);

    @POST("karma/v1/record/by-params-for-business/payment-info")
    Call<PaymentResponse> getAllRecordPayment(@Header("Authorization") String accessToken, @Body RecordsSearchBody recordsSearchBody);

    @POST("karma/v1/record/by-params-for-business")
    Call<ClientRecordNewResponse> getAllRecord(@Header("Authorization") String accessToken, @Body RecordsSearchBody recordsSearchBody);

    @PUT("karma/v1/record/canceled-record")
    Call<AllRecordResponse> canceleRecord(@Header("Authorization") String accessToken, @Query("idRecord") String idRecord, @Query("message") String message);

    @PUT("karma/v1/record/update-status-process")
    Call<AllRecordResponse> changeRecordStatus (@Header("Authorization") String accessToken, @Query("idRecord") String idRecord, @Query("status") String status);

}