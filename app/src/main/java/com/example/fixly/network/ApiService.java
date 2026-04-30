package com.example.fixly.network;

import com.example.fixly.models.catalogos.CategoriasResponse;
import com.example.fixly.models.catalogos.ServiciosResponse;
import com.example.fixly.models.editarPerfilCliente.EditarPerfilClienteRequest;
import com.example.fixly.models.editarPerfilProveedor.EditarPerfilProveedorRequest;
import com.example.fixly.models.login.LoginRequest;
import com.example.fixly.models.login.LoginResponse;
import com.example.fixly.models.registroCliente.RegistroRequest;
import com.example.fixly.models.registroCliente.RegistroResponse;
import com.example.fixly.models.registroProveedor.RegistroProveedorRequest;
import com.example.fixly.models.registroProveedor.RegistroProveedorResponse;
import com.example.fixly.models.verPerfilCliente.PerfilClienteResponse;
import com.example.fixly.models.verPerfilProveedor.PerfilProveedorResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ApiService {
    @GET("/")
    Call<ResponseBody> checkStatus();

    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("users/registrarCliente")
    Call<RegistroResponse> registrarCliente(@Body RegistroRequest registroRequest);

    @GET("catalogs/categorias")
    Call<CategoriasResponse> getCategorias();

    @GET("catalogs/categorias/servicios")
    Call<ServiciosResponse> getServiciosPorCategoria(@Query("id") int categoriaId);

    @POST("users/registrarPrestador")
    Call<RegistroProveedorResponse> registrarPrestador(
            @Header("Authorization") String token,
            @Body RegistroProveedorRequest request
    );

    @GET("users/verPerfil")
    Call<PerfilClienteResponse> verPerfilCliente(@Header("Authorization") String token);

    @GET("users/verPerfil")
    Call<PerfilProveedorResponse> verPerfilProveedor(@Header("Authorization") String token);

    @PUT("users/editarPerfil")
    Call<RegistroResponse> editarPerfil(
            @Header("Authorization") String token,
            @Body EditarPerfilClienteRequest request
    );

    @PUT("users/editarPerfil")
    Call<RegistroProveedorResponse> editarPerfilProveedor(
            @Header("Authorization") String token,
            @Body EditarPerfilProveedorRequest request
    );
}
