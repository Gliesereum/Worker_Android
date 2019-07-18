package com.gliesereum.coupler_worker.network;

import com.gliesereum.coupler_worker.network.json.car.AllCarResponse;
import com.gliesereum.coupler_worker.network.json.car.BrandResponse;
import com.gliesereum.coupler_worker.network.json.car.CarDeleteResponse;
import com.gliesereum.coupler_worker.network.json.carwash.AllCarWashResponse;
import com.gliesereum.coupler_worker.network.json.carwash.CommentsItem;
import com.gliesereum.coupler_worker.network.json.carwash.Rating;
import com.gliesereum.coupler_worker.network.json.classservices.ClassServiceResponse;
import com.gliesereum.coupler_worker.network.json.client.ClientResponse;
import com.gliesereum.coupler_worker.network.json.client_new.NewClientResponse;
import com.gliesereum.coupler_worker.network.json.client_record_new.ClientRecordNewResponse;
import com.gliesereum.coupler_worker.network.json.code.CodeResponse;
import com.gliesereum.coupler_worker.network.json.code.SigninBody;
import com.gliesereum.coupler_worker.network.json.filter.FilterResponse;
import com.gliesereum.coupler_worker.network.json.notificatoin.NotificatoinBody;
import com.gliesereum.coupler_worker.network.json.notificatoin.RegistrationTokenDeleteResponse;
import com.gliesereum.coupler_worker.network.json.notificatoin.UserSubscribe;
import com.gliesereum.coupler_worker.network.json.order.OrderBody;
import com.gliesereum.coupler_worker.network.json.order.OrderResponse;
import com.gliesereum.coupler_worker.network.json.pin.PinResponse;
import com.gliesereum.coupler_worker.network.json.record.AllRecordResponse;
import com.gliesereum.coupler_worker.network.json.record.RecordsSearchBody;
import com.gliesereum.coupler_worker.network.json.service.ServiceResponse;
import com.gliesereum.coupler_worker.network.json.status.StatusRegistration;
import com.gliesereum.coupler_worker.network.json.status.StatusResponse;
import com.gliesereum.coupler_worker.network.json.user.User;
import com.gliesereum.coupler_worker.network.json.user.UserResponse;
import com.gliesereum.coupler_worker.network.json.worker.WorkerResponse;

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

    @GET("karma/v1/user-pin-code")
    Call<PinResponse> getPinCode(@Header("Authorization") String accessToken);

    @GET("karma/v1/working-space/workers/by-business")
    Call<List<WorkerResponse>> getAllWorkersByBusiness(@Header("Authorization") String accessToken, @Query("businessId") String businessId);

    @GET("karma/v1/business/customers")
    Call<List<ClientResponse>> getAllClientsByBusiness(@Header("Authorization") String accessToken, @Query("ids") List<String> ids);

    @GET("karma/v1/business/customers/by-corporation-ids")
    Call<NewClientResponse> getAllClientsByCorporation(@Header("Authorization") String accessToken, @Query("ids") List<String> ids);

    @GET("karma/v1/record/business/by-client")
    Call<ClientRecordNewResponse> getClientsRecord(@Header("Authorization") String accessToken, @Query("corporationIds") List<String> corporationIds, @Query("clientId") String clientId);

    @GET("karma/v1/business/customers/by-corporation-ids")
    Call<NewClientResponse> searchClients(@Header("Authorization") String accessToken, @Query("ids") List<String> ids, @Query("query") String query);

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
    @GET("karma/v1/working-space/worker/exist/byPhone")
    Call<StatusRegistration> checkExist(@Query("phone") String phone);

    @GET("account/v1/phone/code")
    Call<CodeResponse> getPhoneCode(@Query("phone") String phone);

    @POST("account/v1/auth/signin")
    Call<UserResponse> signIn(@Body SigninBody signinBody);

    @GET("account/v1/user/me")
    Call<User> getUser(@Header("Authorization") String accessToken);

    @PUT("account/v1/user")
    Call<User> updateUser(@Header("Authorization") String accessToken, @Body User user);

    @POST("account/v1/auth/refresh")
    Call<UserResponse> refreshAccessToken(@Query("refreshToken") String refreshToken);

    @GET("account/v1/auth/check")
    Call<UserResponse> checkAccessToken(@Query("accessToken") String accessToken);


    //CAR
    @GET("karma/v1/car/user/as-worker")
    Call<List<AllCarResponse>> getClientCar(@Header("Authorization") String accessToken, @Query("clientId") String clientId, @Query("corporationId") String corporationId);


    @GET("karma/v1/car/brands")
    Call<List<BrandResponse>> getBrands(@Header("Authorization") String accessToken);

    @GET("karma/v1/car/models/by-brand/{brandId}")
    Call<List<BrandResponse>> getModels(@Header("Authorization") String accessToken, @Path("brandId") String id);

    @GET("karma/v1/car/years")
    Call<List<BrandResponse>> getYears(@Header("Authorization") String accessToken);

    @GET("karma/v1/car/user")
    Call<List<AllCarResponse>> getAllCars(@Header("Authorization") String accessToken);

    @GET("karma/v1/car/{carId}")
    Call<AllCarResponse> getCarById(@Header("Authorization") String accessToken, @Path("carId") String id);

    @POST("karma/v1/car")
    Call<AllCarResponse> addCar(@Header("Authorization") String accessToken, @Body AllCarResponse object);

    @DELETE("karma/v1/car/{idCar}")
    Call<CarDeleteResponse> deleteCar(@Header("Authorization") String accessToken, @Path("idCar") String idCar);

    @GET("karma/v1/class")
    Call<List<ClassServiceResponse>> getAllClassService(@Header("Authorization") String accessToken);

    @POST("karma/v1/car/service/{idCar}/{idService}")
    Call<CarDeleteResponse> addClassService(@Path("idCar") String idCar, @Path("idService") String idService, @Header("Authorization") String accessToken);

    @POST("karma/v1/car/filter-attribute/{idCar}/{idAttribute}")
    Call<CarDeleteResponse> addCarFilter(@Path("idCar") String idCar, @Path("idAttribute") String idAttribute, @Header("Authorization") String accessToken);

    @POST("karma/v1/car/set-favorite/{idCar}")
    Call<AllCarResponse> setFavoriteCar(@Header("Authorization") String accessToken, @Path("idCar") String idCar);




    //FILTER
    @GET("karma/v1/filter/by-service-type")
    Call<List<FilterResponse>> getFilters(@Query("serviceType") String serviceType);


    //CARWASH
    @GET("karma/v1/business/full-model/by-current-user")
    Call<List<AllCarWashResponse>> getAllCarWash(@Header("Authorization") String accessToken);

    @GET("karma/v1/business/{carwashId}/full-model")
    Call<AllCarWashResponse> getCarWashFull(@Header("Authorization") String accessToken, @Path("carwashId") String id);

//    @GET("karma/v1/business/{carwashId}")
//    Call<AllCarWashResponse> getCarWash(@Path("carwashId") String id);

    @GET("karma/v1/service")
    Call<List<ServiceResponse>> getAllService();

    @GET("karma/v1/business/{carwashId}/rating")
    Call<Rating> getRating(@Header("Authorization") String accessToken, @Path("carwashId") String id);




    //RECORD
    @POST("karma/v1/record/free-time")
    Call<OrderResponse> preOrder(@Header("Authorization") String accessToken, @Body OrderBody orderBody);

    @POST("karma/v1/record/create/from-business")
    Call<AllRecordResponse> doOrder(@Header("Authorization") String accessToken, @Body OrderBody orderBody);

    @POST("karma/v1/record/business/params")
    Call<List<AllRecordResponse>> getAllRecord(@Header("Authorization") String accessToken, @Body RecordsSearchBody recordsSearchBody);

    @GET("karma/v1/record/{recordId}")
    Call<AllRecordResponse> getSingleRecord(@Header("Authorization") String accessToken, @Path("recordId") String recordId);

    @PUT("karma/v1/record/record/canceled")
    Call<AllRecordResponse> canceleRecord(@Header("Authorization") String accessToken, @Query("idRecord") String idRecord, @Query("message") String message);

    @PUT("karma/v1/record/status/process")
    Call<AllRecordResponse> changeRecordStatus (@Header("Authorization") String accessToken, @Query("idRecord") String idRecord, @Query("status") String status);

    //COMMENT
    @POST("karma/v1/business/{carwashId}/comment")
    Call<CommentsItem> sendComment(@Header("Authorization") String accessToken, @Path("carwashId") String carwashId, @Body CommentsItem orderBody);

    @GET("karma/v1/business/{carwashId}/comment/current-user")
    Call<CommentsItem> getMyComment(@Header("Authorization") String accessToken, @Path("carwashId") String id);

    @PUT("karma/v1/business/comment")
    Call<CommentsItem> editComment(@Header("Authorization") String accessToken, @Body CommentsItem orderBody);

    @DELETE("karma/v1/business/comment/{commentId}")
    Call<CarDeleteResponse> deleteComment(@Header("Authorization") String accessToken, @Path("commentId") String id);


}