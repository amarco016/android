package es.exitae.gestor_notas;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NotasSQLHelper extends SQLiteOpenHelper{
	final static String DB_NAME="notas";
	private final static int DB_VERSION=1;

	public NotasSQLHelper(Context context){
		super(context, DB_NAME,null,DB_VERSION);
	}

	//Creamos la base de datos. Si está en modo lectura, la ponemos también en modo escritura
	@Override
	public void onCreate(SQLiteDatabase db) {
		if(db.isReadOnly()){
			db=getWritableDatabase();

		}
		db.execSQL("CREATE TABLE notas (_id INTEGER PRIMARY KEY AUTOINCREMENT, titulo TEXT, descripcion TEXT, latitud DOUBLE, longitud DOUBLE, imagen BLOB);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}



}