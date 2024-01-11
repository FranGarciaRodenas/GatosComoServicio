package com.jgomez.gatoscomoservicio;

 import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
 import retrofit2.http.Url;

public interface CataasService {
    @GET
    Call<ResponseBody> getImage(@Url String url);
}