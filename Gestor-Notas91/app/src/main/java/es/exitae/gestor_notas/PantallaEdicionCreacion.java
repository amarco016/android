package es.exitae.gestor_notas;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;


public class PantallaEdicionCreacion extends Activity implements OnClickListener{
    private static final int dialogo = 0;
    private static final int dialogoImagen = 1;

    static final int REQUEST_CAMARA = 1;
    static final int REQUEST_GALERIA = 2;

    private Bitmap imagen;

    EditText titulo1, descripcion1;
    Double longitud=1000.0;
    Double latitud=1000.0;
    Button btn_guardar;
    Nota nota1;

    //creamos una variable para a la hora de guardar los datos saber si es una nueva nota.
    Boolean nuevanota=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantallaedicioncreacion);

        nota1=(Nota) getIntent().getSerializableExtra("nota1");

        titulo1= (EditText) findViewById(R.id.titulo1);
        descripcion1= (EditText) findViewById(R.id.descripcion1);
        btn_guardar=(Button) findViewById(R.id.btn_guardar);

        btn_guardar.setOnClickListener(this);

        //En el caso de edición en el layout deberan de salir los datos de la nota que vamos a editar
        if(nota1!=null){
            nuevanota=false;
            latitud=nota1.getLatitud();
            longitud=nota1.getLongitud();

            titulo1.setText(nota1.getTitulo());
            descripcion1.setText(nota1.getDescripcion());
        }
    }

    //creamos un ActionBar
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.sumar_nota_menu, menu);
        if(!nuevanota) {
            getActionBar().setTitle(getResources().getString(R.string.EditarTitulo));
        }else{
            getActionBar().setTitle(getResources().getString(R.string.CrearTitulo));
        }
        return true;
    }

    //asignamos una acción a cada elemento del menu

    public boolean onOptionsItemSelected(MenuItem item){
        int id=item.getItemId();
        switch (id) {
            case R.id.coordenadas:
                //Llamamos a la función que se encarga de crear un diálogo para introducir las coordenadas
                onCreateDialog(id);
                showDialog(dialogo);
                break;

            case R.id.camara:
                //LLamamos a la función que se encarga de crear un diálogo para elegir la imagen
                onCreateDialog(id);
                showDialog(dialogoImagen);
                break;

        }
        return true;

    }

    @Override
    public void onClick(View v) {

        Uri uriNew = Uri.parse("content://es.exitae.nota/notas");
        ContentValues values=new ContentValues();

        //comprobamos que el título y la descripción no estan vacíos
        if(titulo1.getText().toString().matches("")|| descripcion1.getText().toString().matches("")){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.Tit_Desc_vacio), Toast.LENGTH_SHORT).show();
            return;
        }

        //Si no es una nueva nota editaremos los datos que tenemos. En caso contrario añadiremos los datos

        if(!nuevanota){
            int id=nota1.getID();
            String where = "_id" + "=" + id;
            values.put("titulo", titulo1.getText().toString());
            values.put("descripcion", descripcion1.getText().toString());
            values.put("_id", nota1.getID());
            values.put("latitud", latitud);
            values.put("longitud", longitud);
            //Pasamos la imagen a un array para meterla en la base de datos
            if(imagen !=null){
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                imagen.compress(Bitmap.CompressFormat.PNG, 100, bos);
                byte[] bArray = bos.toByteArray();
                values.put("imagen", bArray);
            }
            getContentResolver().update(uriNew, values,where ,null);
        }else{
            values.put("titulo", titulo1.getText().toString());
            values.put("descripcion", descripcion1.getText().toString());
            values.put("latitud", latitud);
            values.put("longitud", longitud);
            //si no hemos elegido un dibujo pondremos uno por defecto. Pasamos la imagen a un Array para meterlo
            //en la base de datos.
            if(imagen !=null){
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                imagen.compress(Bitmap.CompressFormat.PNG, 100, bos);
                byte[] bArray = bos.toByteArray();
                values.put("imagen", bArray);
            }else{
                //si el usuario no pone una foto ponemos una por defecto
                Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                largeIcon.compress(Bitmap.CompressFormat.PNG, 100, bos);
                byte[] bArray2 = bos.toByteArray();
                values.put("imagen", bArray2);
            }
            getContentResolver().insert(uriNew, values);
        }

        finish();
    }


    protected Dialog onCreateDialog(int id) {

        switch (id) {
            case dialogo:
                return DialogoCoordenadas();
            case dialogoImagen:
                return DialogoImagen();
            default:
                return null;
        }
    }

    private Dialog DialogoCoordenadas() {
        LinearLayout layout=new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.Coordenadas));

        final EditText input = new EditText(this);
        input.setHint(getResources().getString(R.string.Latitud));
        layout.addView(input);

        final EditText input2 = new EditText(this);
        input2.setHint(getResources().getString(R.string.Longitud));
        layout.addView(input2);

        builder.setView(layout);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            //asignamos los valores correspondientes para la latitud y longitud
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                //Hacemos tratamiento de errores para asegurar que la longitud y la latitud son números
                try{
                    latitud =Double.parseDouble(input.getText().toString());
                    longitud =Double.parseDouble(input2.getText().toString());

                }catch(Exception ex){
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.Lat_Long_error), Toast.LENGTH_SHORT).show();

                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        return builder.create();
    }
    private Dialog DialogoImagen() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.Sumar_imagen));

        //Si el usuario escoge la opción "galeria", dejamos que escoja una imagen de su galeria
        builder.setPositiveButton(getResources().getString(R.string.Galeria), new DialogInterface.OnClickListener() {

            //asignamos los valores correspondientes para la latitud y longitud
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                Intent PhotoPickerIntent= new Intent(Intent.ACTION_PICK);
                PhotoPickerIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(Intent.createChooser(PhotoPickerIntent, "galeria"), REQUEST_GALERIA);
            }
        });

        //Si el usuario escoge "cámara" mandamos un intent para ir a la cámara de su dispositivo
        builder.setNegativeButton(getResources().getString(R.string.Camara), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CAMARA);
            }
        });

        return builder.create();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode==RESULT_OK){
            if(requestCode==REQUEST_CAMARA){

                Bitmap reslizedBitmap=(Bitmap) data.getExtras().get("data");
                imagen =reslizedBitmap;

            }else if(requestCode==REQUEST_GALERIA){

                try{
                    Uri selectedImage= data.getData();
                    Bitmap yourSelectedImage=MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    //ajustamos la imagen
                    Bitmap reslizedBitmap= Bitmap.createScaledBitmap(yourSelectedImage, 400, 400, false);
                    yourSelectedImage.recycle();
                    imagen =reslizedBitmap;

                }catch(FileNotFoundException ex){
                    Toast.makeText(this, "ha habido un error", Toast.LENGTH_LONG).show();
                }catch (IOException e) {
                    Toast.makeText(this, "ha habido un error", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
            Toast.makeText(this, getResources().getString(R.string.Imagen_añadido), Toast.LENGTH_SHORT).show();

        }
    };
}