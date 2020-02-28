package com.sbsj.ordermatstaboss.DB;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface DB_Service {
	@FormUrlEncoded
	@POST("insert_masterinfo.php")
	Call<String> insertMaster(@Field("directory") String directory, @Field("sns_id") String sns_id);

	@GET("update_masterinfo.php")
	Call<String> updateMaster(@QueryMap Map<String, String> map);

	@FormUrlEncoded
	@POST("get_masterinfo.php")
	Call<MasterInfo> getMasterInfo(@Field("directory") String directory, @Field("sns_id") String sns_id);

	@FormUrlEncoded
	@POST("insert_masterdeviceinfo.php")
	Call<String> insertMasterDeviceInfo(@FieldMap Map<String, String> map);

	@FormUrlEncoded
	@POST("get_masterdeviceinfo.php")
	Call<MasterDeviceInfo> getMasterDeviceInfo(@Field("master_id") int master_id);


	@GET("get_storeinfobymasterid.php")
	Call<StoreInfo> getstoreinfobymasterid(@Query("master_id") int master_id);

	@FormUrlEncoded
	@POST("get_storeinfobystoreid.php")
	Call<StoreInfo> getstoreinfobystoreid(@Field("store_id") int store_id);

	@FormUrlEncoded
	@POST("insert_storeinfo.php")
	Call<String> insertstoreinfo(@FieldMap Map<String, String> map);

	@FormUrlEncoded
	@POST("update_storeinfo.php")
	Call<String> updatestoreinfo(@FieldMap Map<String, String> map);

	@FormUrlEncoded
	@POST("update_storeinfo_open.php")
	Call<String> updatestoreinfo_open(@Field("store_id") int store_id, @Field("open") int open);

//    @FormUrlEncoded
//    @POST("get_menucateinfo.php")
//    Call<MenuCateInfo> getCategoryInfo(@Field("store_id") int store_id);
//
//    @FormUrlEncoded
//    @POST("get_menucateinfo.php")
//    Call<String> getCategoryInfo_string(@Field("store_id") int store_id);

	//    @FormUrlEncoded
//    @POST("insert_menucateinfo.php")
//    Call<String> insertCategoryInfo(@Field("store_id") int store_id, @Field("name[]") ArrayList<String> name, @Field("sequence") int count);
//
//    @FormUrlEncoded
//    @POST("update_menucateinfo.php")
//    Call<String> updateCategoryInfo(@Field("id[]") ArrayList<Integer> id, @Field("name[]") ArrayList<String> name);
//
//    @FormUrlEncoded
//    @POST("get_menucateinfo.php")
//    Call<MenuCateInfo> getCategoryInfo(@Field("store_id") int store_id);
	@FormUrlEncoded
	@POST("get_goodinfo.php")
	Call<GoodsInfo> getGoodInfo(@Field("goods_id") int goods_id);

	@FormUrlEncoded
	@POST("get_goodsinfo.php")
	Call<GoodsInfo> getGoodsInfo(@Field("store_id") int store_id);

//    @FormUrlEncoded
//    @POST("get_goodsinfo.php")
//    Call<String> getGoodsInfo(@Field("store_id") int store_id);


	@FormUrlEncoded
	@POST("insert_goodsinfo.php")
	Call<String> insertgoodsinfo(@FieldMap Map<String, String> map);

	@FormUrlEncoded
	@POST("update_goodsinfo.php")
	Call<String> updategoodsinfo(@FieldMap Map<String, String> map);
}
