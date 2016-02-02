package es.exitae.gestor_notas;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class PantallaDetalle extends Activity implements OnClickListener{

	Nota nota_main, nota_map,nota;
	TextView titulo, descripcion;
	Button btn_eliminar, btn_editar;
	Cursor cursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pantalladetalle);

		titulo= (TextView) findViewById(R.id.titulo);
		descripcion= (TextView) findViewById(R.id.descripcion);
		btn_eliminar= (Button) findViewById(R.id.btn_eliminar);
		btn_editar= (Button) findViewById(R.id.btn_editar);

		nota_main=(Nota) getIntent().getSerializableExtra("nota_main");
		nota_map=(Nota) getIntent().getSerializableExtra("nota_map");

		//La nota puede proceder de "MainActivity" o "MapaActivity"
		if (nota_main==null){
			nota=nota_map;
		}else{
			nota=nota_main;
		}
		titulo.setText(nota.getTitulo());
		descripcion.setText(nota.getDescripcion());

		btn_eliminar.setOnClickListener(this);
		btn_editar.setOnClickListener(this);
	}

	//creamos un ActionBar
	public boolean onCreateOptionsMenu(Menu menu){
		MenuInflater inflater=getMenuInflater();
		inflater.inflate(R.menu.detalles_nota_menu, menu);
		getActionBar().setTitle(getResources().getString(R.string.DetalleTitulo));
		return true;
	}


	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.btn_eliminar:
				onCreateDialog(R.id.btn_eliminar);
				showDialog(R.id.btn_eliminar);

				break;

			case R.id.btn_editar:

				//Vamos a la pantalla de edición
				Intent intent5= new Intent();
				intent5.setClass(getApplicationContext(), PantallaEdicionCreacion.class);
				intent5.putExtra("nota1", nota);
				startActivity(intent5);

				finish();
				break;
		}
	}

	//Creamos el dialogo
	protected Dialog onCreateDialog(int id2) {

		switch (id2) {
			default:


				final Uri uriNew = Uri.parse("content://es.exitae.nota/notas");
				final String[] columnas=new String[] {
						NotasProvider._ID,NotasProvider.TITULO, NotasProvider.DESCRIPCION, NotasProvider.LONGITUD, NotasProvider.LATITUD,NotasProvider.IMAGEN
				};

				cursor=managedQuery(uriNew, columnas,null, null, null);



				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(getResources().getString(R.string.Eliminando));
				builder.setCancelable(true);
				builder.setPositiveButton(getResources().getString(R.string.Si), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						int id ;
						id=nota.getID();
						//Eliminamos la nota
						getContentResolver().delete(uriNew,NotasProvider._ID+ " = " + id , null);
						finish();

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