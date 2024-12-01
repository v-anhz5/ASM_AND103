package fpoly.account.asm_and103.services;

import java.util.ArrayList;

import fpoly.account.asm_and103.model.District;
import fpoly.account.asm_and103.model.DistrictRequest;
import fpoly.account.asm_and103.model.GHNOrderRequest;
import fpoly.account.asm_and103.model.GHNOrderRespone;
import fpoly.account.asm_and103.model.Order;
import fpoly.account.asm_and103.model.Product;
import fpoly.account.asm_and103.model.Province;
import fpoly.account.asm_and103.model.Response;
import fpoly.account.asm_and103.model.ResponseGHN;
import fpoly.account.asm_and103.model.Ward;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiServices {



    @GET("api/fruits")
    Call<Response<ArrayList<Product>>> getAllListFruit();

    @GET("api/fruits")
    Call<Response<ArrayList<Product>>> getListFruit(@Query("categoryid") String categoryId);

    @GET("api/search-fruits")
    Call<Response<ArrayList<Product>>> searchFruits(@Query("keyword") String keyword);

    @POST("api/fruits")
    Call<Response<Product>> addFruit(@Body Product product);

    @POST("add-order")
    Call<Response<Order>> order (@Body Order order);

    @PUT("api/fruits/{productid}")
    Call<Response<Void>> updateFruitByProductId(@Path("productid") String productId, @Body Product product);

    @DELETE("api/fruits/{productid}")
    Call<Response<Void>> deleteFruitByProductId(@Path("productid") String productid); // Đổi tên phương thức

    @GET("/shiip/public-api/master-data/province")
    Call<ResponseGHN<ArrayList<Province>>> getListProvince();

    @POST("/shiip/public-api/master-data/district")
    Call<ResponseGHN<ArrayList<District>>> getListDistrict(@Body DistrictRequest districtRequest);

    @GET("/shiip/public-api/master-data/ward")
    Call<ResponseGHN<ArrayList<Ward>>> getListWard(@Query("district_id") int district_id);

    @POST("/shiip/public-api/v2/shipping-order/create")
    Call<ResponseGHN<GHNOrderRespone>> GHNOrder (@Body GHNOrderRequest ghnOrderRequest);

}