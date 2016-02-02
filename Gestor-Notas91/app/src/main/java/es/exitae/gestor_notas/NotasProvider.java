package es.exitae.gestor_notas;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class NotasProvider extends ContentProvider{
	public static final String PROVIDER_NAME="es.exitae.nota";
	public static final Uri CONTENT_URI= Uri.parse("content://"+ PROVIDER_NAME +"/notas");

	public static final String _ID= "_id";
	public static final String TITULO="titulo";
	public static final String DESCRIPCION= "descripcion";
	public static final String LONGITUD= "longitud";
	public static final String LATITUD= "latitud";
	public static final String IMAGEN= "imagen";
	private static final String DATABASE_TABLE="notas";

	//Definimos NOTAS(para un listado de notas) y NOTAS_ID(para una sola nota)
	private static final int NOTAS=1;
	private static final int NOTAS_ID=2;

	private static final UriMatcher uriMatcher;
	static {
		uriMatcher =new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(PROVIDER_NAME, "notas", NOTAS);
		uriMatcher.addURI(PROVIDER_NAME, "notas/#", NOTAS_ID);
	}

	private SQLiteDatabase notasDB;

	//Devolvemos true si el ConentProvider se ha cargado correctamente
	@Override
	public boolean onCreate() {
		Context context=getContext();
		NotasSQLHelper dbHelper= new NotasSQLHelper(context);
		notasDB=dbHelper.getWritableDatabase();
		return (notasDB==null) ? false : true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
						String[] selectionArgs, String sortOrder) {

		SQLiteQueryBuilder sqlBuilder=new SQLiteQueryBuilder();
		sqlBuilder.setTables(DATABASE_TABLE);
		if(uriMatcher.match(uri)==NOTAS_ID){
			sqlBuilder.appendWhere(_ID + " = " + uri.getPathSegments().get(1));
		}
		//si el orden es nulo o esta en blanco, utilizaremos el titulo como orden
		if(sortOrder==null || sortOrder ==""){
			sortOrder=TITULO;
		}
		Cursor c= sqlBuilder.query(notasDB, projection, selection, selectionArgs, null, null, sortOrder);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)){

			//Para conjunto de notas
			case NOTAS:
				return "vnd.android.cursor.dir/vnd.exitae.notas";

			//Para una sola nota
			case NOTAS_ID:
				return "vnd.android.cursor.item/vnd.exitae.notas";

			//si recibimos otro caso avisamos al ususrio de que la uri no es correcta
			default:
				throw new IllegalArgumentException("Unsupportes URI: " + uri);
		}

	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {

		//añadimos una nueva nota
		long rowID= notasDB.insert(DATABASE_TABLE, "", values);
		//Si ha ido bien devolvemos su uri
		if(rowID>0){
			Uri _uri= ContentUris.withAppendedId(CONTENT_URI, rowID);
			getContext().getContentResolver().notifyChange(_uri, null);
			return _uri;
		}
		throw new SQLException("Failed to insert row into" + uri);

	}


	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {

		int delCount = 0;
		switch (uriMatcher.match(uri)) {
			//Para un conjunto de notas
			case NOTAS:
				delCount = notasDB.delete(
						DATABASE_TABLE,
						selection,
						selectionArgs);

				break;
			//Para una nota
			case NOTAS_ID:
				String idStr = uri.getLastPathSegment();
				String where = _ID + " = " + idStr;
				if (!TITULO.isEmpty()) {
					where += " AND " + selection;
				}
				delCount = notasDB.delete(
						DATABASE_TABLE,
						where,
						selectionArgs);
				break;
			default:

				throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
		if (delCount > 0 ) {
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return delCount;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
					  String[] selectionArgs) {
		int count = 0;
		int match = uriMatcher.match(uri);

		//Actualizamos los datos introducidos
		switch (match){

			//Para un conjunto de notas
			case NOTAS:

				notasDB.update(DATABASE_TABLE, values, selection, selectionArgs);

				break;

			//Para una nota
			case NOTAS_ID:
				String rowID = uri.getPathSegments().get(1);
				selection = _ID + "=" + rowID
						+ (!TextUtils.isEmpty(selection) ?
						" AND (" + selection + ')' : "");

				notasDB.update(DATABASE_TABLE, values, selection, selectionArgs);

				break;

			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

}