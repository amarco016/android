package es.exitae.gestor_notas;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


public class MainActivity extends ListActivity implements AdapterView.OnItemLongClickListener {

	public Uri uriNew = Uri.parse("content://es.exitae.nota/notas");
	private Cursor cursor;

	int cid,ctitulo, cdescripcion,clatitud, clongitud,cimagen;
	Nota nota;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//Cogemos todas las columnas de la base de datos
		final String[] columnas=new String[] {
				NotasProvider._ID,NotasProvider.TITULO, NotasProvider.DESCRIPCION, NotasProvider.LONGITUD, NotasProvider.LATITUD,NotasProvider.IMAGEN

		};

		//Usamos el asterisco para guardar todas las notas
		Uri uriList = Uri.parse("content://es.exitae.nota/notas/*");
		cursor=managedQuery(uriList, columnas,null, null, null);

		//Creamos el adapter para la lista
		ListaAdapter adapter =new ListaAdapter(MainActivity.this,cursor);
		setListAdapter(adapter);


		//Al hacer un click largo en un elemento de la lista daremos la opción de eliminar dicha nota
		getListView().setOnItemLongClickListener(this );
	}


	//creamos un ActionBar
	public boolean onCreateOptionsMenu(Menu menu){
		MenuInflater inflater=getMenuInflater();
		inflater.inflate(R.menu.mainmenu, menu);
		return true;
	}

	//asignamos una acci�n a cada elemento del menu

	public boolean onOptionsItemSelected(MenuItem item){
		int id=item.getItemId();
		switch (id) {
			case R.id.sumar_nota:
				Intent intent2= new Intent();
				intent2.setClass(getApplicationContext(), PantallaEdicionCreacion.class);
				startActivity(intent2);
				break;

			case R.id.mapa:
				Intent intent=new Intent();
				intent.setClass(getApplicationContext(), MapaActivity.class);
				intent.putExtra("nota", nota);

				startActivity(intent);

				break;

		}
		return true;

	}

	//Al clicar en un elemento de la lista, cogemos los datos de esa nota por el cursor y mandamos la nota a la activity "PantallaDetalle"
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		cid=cursor.getColumnIndex(NotasProvider._ID);
		ctitulo=cursor.getColumnIndex(NotasProvider.TITULO);
		cdescripcion=cursor.getColumnIndex(NotasProvider.DESCRIPCION);
		clongitud=cursor.getColumnIndex(NotasProvider.LONGITUD);
		clatitud=cursor.getColumnIndex(NotasProvider.LATITUD);
		cimagen=cursor.getColumnIndex(NotasProvider.IMAGEN);
		nota=new Nota(cursor.getInt(cid),cursor.getString(ctitulo), cursor.getString(cdescripcion), cursor.getDouble(clatitud), cursor.getDouble(clongitud));


		Intent intent3= new Intent();
		intent3.setClass(getApplicationContext(),PantallaDetalle.class);
		intent3.putExtra("nota_main",nota );
		startActivity(intent3);

	}

	//Con un click largo sobre la nota damos la opcion de eliminar
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		Integer id2 = (int) (long) id;
		onCreateDialog(id2);

		showDialog(id2);

		return true;
	}


	//Creamos el dialogo
	protected Dialog onCreateDialog(int id2) {

		switch (id2) {
			default:
				cid=cursor.getColumnIndex(NotasProvider._ID);
				ctitulo=cursor.getColumnIndex(NotasProvider.TITULO);
				cdescripcion=cursor.getColumnIndex(NotasProvider.DESCRIPCION);
				clongitud=cursor.getColumnIndex(NotasProvider.LONGITUD);
				clatitud=cursor.getColumnIndex(NotasProvider.LATITUD);
				cimagen=cursor.getColumnIndex(NotasProvider.IMAGEN);

				nota=new Nota(cursor.getInt(cid),cursor.getString(ctitulo), cursor.getString(cdescripcion), cursor.getDouble(clatitud), cursor.getDouble(clongitud));


				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(getResources().getString(R.string.Eliminando));
				builder.setCancelable(true);
				builder.setPositiveButton(getResources().getString(R.string.Si), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						int id;
						id = nota.getID();
						getContentResolver().delete(uriNew,NotasProvider._ID + " = " + id, null);

					}
				});
				builder.setNegativeButton(getResources().getString(R.string.No), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}

				});
				return builder.create();
		}

	}


}