package es.exitae.gestor_notas;

import java.io.ByteArrayInputStream;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ListaAdapter extends CursorAdapter{


	public ListaAdapter (Context context, Cursor cursor){
		super(context, cursor);
	}

	//Nuestro adaptador tendrá dos textViews para el título y la descripción, y una imageView para la imagen
	@Override
	public void bindView(View view, final Context context, Cursor cursor) {


		TextView idTitulo=(TextView) view.findViewById(R.id.idTitulo);
		TextView idDescripcion=(TextView) view.findViewById(R.id.idDescripcion);
		ImageView idImagen=(ImageView) view.findViewById(R.id.idImagen);
		String titulo=cursor.getString(cursor.getColumnIndexOrThrow("titulo"));
		String descripcion=cursor.getString(cursor.getColumnIndexOrThrow("descripcion"));
		byte[] imagen=cursor.getBlob(cursor.getColumnIndexOrThrow("imagen"));
		idTitulo.setText(titulo);
		idDescripcion.setText(descripcion);
		ByteArrayInputStream imageStream = new ByteArrayInputStream(imagen);
		final Bitmap theImage= BitmapFactory.decodeStream(imageStream);
		idImagen.setImageBitmap(theImage);
	}


	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {

		LayoutInflater inflater=LayoutInflater.from(parent.getContext());
		View retView =inflater.inflate(R.layout.listanotas, parent,false);
		return retView;
	}



}