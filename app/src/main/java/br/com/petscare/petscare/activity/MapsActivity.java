package br.com.petscare.petscare.activity;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.security.Permission;
import java.util.ArrayList;
import java.util.List;

import br.com.petscare.petscare.Manifest;
import br.com.petscare.petscare.R;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    protected Location mLastKnowLocation;
    GoogleApiClient.Builder mGoogleApiClient;
    private LocationRequest locationRequest;

    //variaveis latitude e longitude clinicas veterinarias
    private static final LatLng cliniBonsAmigos = new LatLng(-19.959325, -44.043551);
    private static final LatLng cliniConsulveter = new LatLng(-19.946157, -44.041317);
    private static final LatLng cliniTerraDosBichos = new LatLng(-19.935188, -44.050843);
    private static final LatLng cliniCamargos = new LatLng(-19.936544, -44.019687);
    private static final LatLng cliniCliveco = new LatLng(-19.918454, -44.079410);
    private static final LatLng cliniCenterVet = new LatLng(-19.970732, -44.031607);

    private MarkerOptions mBonsAmigos = new MarkerOptions();
    private MarkerOptions mConsulveter = new MarkerOptions();
    private MarkerOptions mTerraDosBichos = new MarkerOptions();
    private MarkerOptions mCliveco = new MarkerOptions();
    private MarkerOptions mCamargosMarker= new MarkerOptions();
    private MarkerOptions mCenterVet = new MarkerOptions();

    private String[] permissoesNecessarias = new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if(!Permissao.validaPermissoes(1, this, permissoesNecessarias)) {
            finish();
            Toast.makeText(this, "Não foi possível obter sua localização", Toast.LENGTH_SHORT).show();

        }

        Toast.makeText(this, "Protótipo de localização de veterinários", Toast.LENGTH_SHORT).show();

        mFusedLocationClient = LocationServices. getFusedLocationProviderClient(this);
        obtainLastKnowLocation();

        locationRequest = new LocationRequest() ;
        locationRequest.setInterval( 10000 ) ;
        locationRequest.setFastestInterval( 5000 ) ;
        locationRequest.setPriority(LocationRequest. PRIORITY_HIGH_ACCURACY ) ;

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for(int resultados: grantResults){
            if(resultados == PackageManager.PERMISSION_DENIED){
                Toast.makeText(this, "Não será possível utilizar o serviço negando sua localização", Toast.LENGTH_SHORT).show();

            }
        }

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(mLastKnowLocation != null){
            zoomMapToMyLocation();
        }
        markerVeterinarios();

    }

    @SuppressWarnings("MissingPermission")
    private void obtainLastKnowLocation() {
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {

                            //obtém a última localização conhecida
                            mLastKnowLocation = task.getResult();
                            //Se o mapa está disponível
                            //coloca uma marca e faz zoom
                            if(mMap != null){
                                zoomMapToMyLocation();
                            }

                        } else {

                            //Não há localização conhecida ou houve uma excepção
                            //A excepção pode ser obtida com task.getException()
                            Toast.makeText(MapsActivity.this,
                                    "Não há localização conhecida ou houve uma excepção",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void zoomMapToMyLocation(){
        final LatLng google = new LatLng(mLastKnowLocation.getLatitude(), mLastKnowLocation.getLongitude());
        // Configuração da câmera
        final CameraPosition position = new CameraPosition.Builder()
                .target( google )     //  Localização
                .bearing( 45 )        //  Rotação da câmera
                .tilt( 90 )             //  Ângulo em graus
                .zoom( 17 )           //  Zoom
                .build();
        CameraUpdate update = CameraUpdateFactory.newCameraPosition( position );
        mMap.animateCamera( update );

        // Criando um objeto do tipo MarkerOptions

        final MarkerOptions markerOptions = new MarkerOptions();

        // Configurando as propriedades do marker

        markerOptions.position( google )	// Localização
                .title("Casa")	// Título
                .snippet("Seu local atual") // Descrição
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        // Adicionando marcador ao mapa

        mMap.addMarker( markerOptions );

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Double latitude = latLng.latitude;
                Double longitude = latLng.longitude;
                Toast.makeText(MapsActivity.this, "Latitude: " + latitude + ", Longitude: "+ longitude, Toast.LENGTH_SHORT).show();
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                // Criar marker no marker
                MarkerOptions options = new MarkerOptions();
                options.position( latLng );
                mMap.addMarker( options );

                // Configurando as propriedades da Linha
                PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions.add( google );
                polylineOptions.add( latLng );
                polylineOptions.color( Color.BLUE );
                // Adiciona a linha no mapa
                mMap.addPolyline( polylineOptions );
            }
        });

    }

    private void markerVeterinarios(){
        mBonsAmigos.position( cliniBonsAmigos )	// Localização
                .title("Veterinária,Pet Shop e Hotelzinho - Bons Amigos")	// Título
                .snippet("TELEFONE:(31)3364-4631"); // Descrição
        // Adicionando marcador ao mapa
        mMap.addMarker( mBonsAmigos );

        mConsulveter.position(cliniConsulveter)	// Localização
                .title("Clínica Veterinária Consulveter ")// Título
                .snippet("TELEFONE:(31)3392-7000");
        mMap.addMarker( mConsulveter );

        mCamargosMarker.position(cliniCamargos)	// Localização
                .title("Clínica Veterinária Camargos")// Título
                .snippet("TELEFONE:(31)3047-6241");
        mMap.addMarker( mCamargosMarker );

        mTerraDosBichos.position(cliniTerraDosBichos)	// Localização
                .title("Clínica Vetrinária Terra dos Bichos ")// Título
                .snippet("TELEFONE:(31) 2568-1751");
        mMap.addMarker( mTerraDosBichos );

        mCliveco.position(cliniCliveco)	// Localização
                .title("Cliveco ")// Título
                .snippet("TELEFONE:(31)3398-2960");
        mMap.addMarker( mCliveco );

        mCenterVet.position(cliniCenterVet)	// Localização
                .title("CenterVet Hospital Veterinário 24 Horas")// Título
                .snippet("TELEFONE: (31)3333-6475");
        mMap.addMarker( mCenterVet );
    }

}

class Permissao {
    public static boolean validaPermissoes(int requestCode, Activity activity, String permissoes[]){
        if(Build.VERSION.SDK_INT >= 23){
            List<String> listaPermissoes = new ArrayList<String>();

            //Percorre o array de permissões
            for(String permissao: permissoes){
                //Verifica se possui a permissão e é igual ao nível do pacote
                Boolean validaPermissao = ContextCompat.checkSelfPermission(activity, permissao) == PackageManager.PERMISSION_GRANTED;

                if(!validaPermissao)
                listaPermissoes.add(permissao);

            }

            if(listaPermissoes.isEmpty()) return true;

            String[] novasPermissoes = new String[listaPermissoes.size()];
            listaPermissoes.toArray(novasPermissoes);
            ActivityCompat.requestPermissions(activity, novasPermissoes, requestCode);

        }

        return true;
    }

}
