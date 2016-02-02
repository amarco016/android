package es.exitae.gestor_notas;

import android.database.Cursor;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class MapaActivity extends FragmentActivity implements OnMarkerClickListener {

	Nota nota;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapa);


		GoogleMap map=((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

		Uri uriNew = Uri.parse("content://es.exitae.nota/notas");
		Cursor c=getContentResolver().query(uriNew, null,null,null,null);

		//Nos aseguramos de que existe al menos un registro

		if (c.moveToFirst()) {
			//Recorremos el cursor hasta que no haya más registros
			do {
				String titulo = c.getString(1);
				Double latitud=c.getDouble(3);
				Double longitud=c.getDouble(4);
				//si el valos no es menor de 1000 sabemos que no ha sido asignado ningún valor a las coordenadas.
				//Por lo tanto no aparecerán en el mapa
				if(latitud<1000 && longitud<1000){
					MarkerOptions marcador= new MarkerOptions();
					marcador.title(titulo);
					marcador.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
					LatLng ubicacion_nota= new LatLng(latitud,longitud);
					marcador.position(ubicacion_nota);
					map.addMarker(marcador);
					map.setOnMarkerClickListener(this);
				}
			} while(c.moveToNext());
		}

	}

	//Si pulsamos en el marcador de una nota, mandamos un intent con la nota a la pantalla de detalle de la nota
	@Override
	public boolean onMarkerClick(Marker marker) {

		marker.getTitle();
		Uri uriNew = Uri.parse("content://es.exitae.nota/notas");
		Cursor c=getContentResolver().query(uriNew, null,null,null,null);

		//Nos aseguramos de que existe al menos un registro

		if (c.moveToFirst()) {
			//Recorremos el cursor hasta que no haya más registros
			do {
				int id= c.getInt(0);
				String titulo = c.getString(1);
				String descripcion=c.getString(2);
				Double latitud=c.getDouble(3);
				Double longitud=c.getDouble(4);
				//si el valos no es menor de 1000 sabemos que no ha sido asignado ningún valor a las coordenadas.
				//Por lo tanto no aparecerán en el mapa
				if(titulo.equals(marker.getTitle())){
					nota= new Nota(id,titulo,descripcion,latitud,longitud);
				}
			} while(c.moveToNext());
		}


		Intent intent=new Intent();
		intent.setClass(getApplicationContext(), PantallaDetalle.class);
		intent.putExtra("nota_map", nota);
		startActivity(intent);
		finish();
		return false;


	}

}