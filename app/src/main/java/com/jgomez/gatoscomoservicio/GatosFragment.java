package com.jgomez.gatoscomoservicio;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class GatosFragment extends Fragment {
    /**
     * Declaracion de Variables
     */
    private Button buttonImage;
    private ImageView imageView;
    private CataasService service;
    /**
     * Variable que almacena el estado del menú
     */
    private int menuStateImage = R.id.normal;

    public GatosFragment() {
        // Required empty public constructor
    }

    /**
     * Se llama cuando se ha creado el fragmento y se ha asociado a su actividad. La actividad
     * ya ha recibido el método onCreate().
     *
     * @param menu El menú se recibe como parámetro
     *
     * @param inflater El inflador del menú se recibe como parámetro
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * Este enlace se llama inmediatamente después de que el fragmento se haya asociado a la actividad.
     * Este método se llama después de onCreateView(LayoutInflater, ViewGroup, Bundle) y antes de
     * onCreateView(LayoutInflater, ViewGroup, Bundle).
     * @param item El elemento del menú seleccionado
     *
     * @return devuelve true para consumirlo aquí o false para permitir que continúe la propagación.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        menuStateImage = item.getItemId(); // Actualiza el estado según el elemento del menú seleccionado
        return super.onOptionsItemSelected(item);
    }

    /**
     * Se llama para que el fragmento cree su jerarquía de vistas secundarias dentro de su
     * contenedor, si se devuelve una vista de este método, se agregará a la jerarquía de vistas
     * del fragmento en su raíz.
     * @param inflater El inflador del menú se recibe como parámetro
     * @param container El contenedor del menú se recibe como parámetro
     * @param savedInstanceState El estado del menú se recibe como parámetro
     *
     * @return Devuelve la vista del fragmento
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gatos, container, false);

        buttonImage = view.findViewById(R.id.buttonImage);
        imageView = view.findViewById(R.id.imageView);
        /**
         * Toolbar
         */
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        /**
         * Retrofit
         */
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://cataas.com")
                .build();
        /**
         * Service
         */
        service = retrofit.create(CataasService.class);
        /**
         * Button Listener
         *
         */
        buttonImage.setOnClickListener(new View.OnClickListener() {
            /**
             * onClick, se ejecuta cuando se pulsa el botón.
             * Dependiendo del estado del menú, se carga una imagen u otra.
             *
             * @param v Vista del botón que se ha pulsado
             */
            @Override
            public void onClick(View v) {
                String url = "https://cataas.com/cat";
                if (menuStateImage == R.id.cute) {
                    url += "/cute";
                } else if (menuStateImage == R.id.sleepy) {
                    url += "/sleepy";
                } else if (menuStateImage == R.id.gif) {
                    url += "/gif";
                }
                loadImage(url);
            }
});
        return view;
    }

    //Load Images

    /**
     * Carga la imagen en el ImageView
     *Se utiliza Glide para cargar la imagen en el ImageView
     * @See Glide
     * @param url La url de la imagen
     */
    private void loadImage(String url) {
        service.getImage(url).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Glide.with(getContext())
                            .load(url + "?time=" + System.currentTimeMillis())
                            .into(imageView);
                }
            }

            /**
             * onFailure, se ejecuta cuando la llamada a la API falla.
             * Un mensaje de error se muestra en pantalla y sobra.
             * @param call La llamada a la API
             * @param t El error
             */
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getContext(), "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
            }
        });
    }
}