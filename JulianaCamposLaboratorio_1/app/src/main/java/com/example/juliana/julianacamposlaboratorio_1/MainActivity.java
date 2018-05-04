package com.example.juliana.julianacamposlaboratorio_1;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1234;
    private static final int IMAGE_CAPTURE = 12;
    EditText editTextRecord,editTextNombre;
    Button buttonSave;
    Button buttonPhoto;
    ArrayList<String> matchesText;
    ArrayList<Persona> listaPersonas = new ArrayList<Persona>();
    ListView listViewPersonas;
    ImageView imageViewTemporal;

    RadioButton radioButtonMujer,radioButtonHombre;
    String uriFoto = "";

    List<String> listaSimple =  Arrays.asList("No hay personas");
    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextNombre = (EditText)findViewById(R.id.editText);
        editTextRecord = (EditText)findViewById(R .id.editText2);
        buttonPhoto = (Button)findViewById(R.id.buttonPhoto);
        buttonSave = (Button) findViewById(R.id.buttonSave);
        listViewPersonas = (ListView)findViewById(R.id.listViewPersonas);
        imageViewTemporal = (ImageView) findViewById(R.id.rowImageTemporal);
        radioButtonHombre = (RadioButton)findViewById(R.id.radioButtonMale);
        radioButtonMujer = (RadioButton)findViewById(R.id.radioButtonFemale);

        imageViewTemporal.setImageDrawable(null);
        editTextRecord.setFocusableInTouchMode(true);
        editTextRecord.requestFocus();
        editTextRecord.setFocusableInTouchMode(false);
        editTextRecord.requestFocus();

        editTextRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE , "es-ES");
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String perfil = editTextRecord.getText().toString();
                String nombre =  editTextNombre.getText().toString();
                String genero;

                if(radioButtonHombre.isChecked()){
                    genero = "Hombre";
                }
                else{
                    genero = "Mujer";
                }
                System.out.println("uri: "+ uriFoto ) ;

                if(nombre.equals("") || uriFoto.equals("") || perfil.equals("")){
                    Toast.makeText(MainActivity.this, "Faltan Datos", Toast.LENGTH_SHORT).show();

                }
                else{
                    imageViewTemporal.setImageDrawable(null);
                    editTextRecord.setFocusableInTouchMode(true);
                    editTextRecord.requestFocus();
                    editTextRecord.setFocusableInTouchMode(false);
                    editTextRecord.requestFocus();

                    Persona persona = new Persona(nombre,genero,uriFoto,perfil);
                    listaPersonas.add(persona);
                    uriFoto = "";
                    editTextRecord.setText("");
                    editTextNombre.setText("");

                    obtenerLista();
                }
            }
        });
        buttonPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Tomar foto");
                Intent intent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE) ;
                startActivityForResult ( intent , IMAGE_CAPTURE ) ;
            }
        });
    }


    @Override
    protected void onResume() {
        obtenerLista();
        super.onResume();
    }
    public void obtenerLista(){
        if(listaPersonas.size()>0) {
            listViewPersonas.setAdapter(new viewAdapter(this));
        }
        else{
            simpleList();
        }

    }
    public void simpleList(){
        adapter = new ArrayAdapter<String>(this,R.layout.simple_linear_layout
                , listaSimple);
        listViewPersonas.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            String palabra = "";
            matchesText = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if(matchesText.size() != 0){
                palabra = matchesText.get(0);
            }
            editTextRecord.setText(palabra);
            System.out.println("Palabra: " + palabra);
        }
        if(requestCode == IMAGE_CAPTURE){
            System.out.println("tomar imagen");

            Uri UriResult = data.getData ();
            if ( resultCode == RESULT_OK ) {
                uriFoto =  UriResult.toString();
                imageViewTemporal.setImageURI(UriResult);
                imageViewTemporal.setRotation(270);

                Toast . makeText (this , "guardado", Toast . LENGTH_LONG ) . show () ;
            } else if ( resultCode == RESULT_CANCELED ) {
                Toast . makeText (this , " cancelado",
                        Toast . LENGTH_LONG ) . show () ;
            } else {
                Toast . makeText (this , " fallo",
                        Toast . LENGTH_LONG ) . show () ;
            }

        }
    }

    public boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = cm.getActiveNetworkInfo();
        if (net!= null && net.isAvailable() && net.isConnected()){
            return true;
        }   else {
            return false;
        }
    }

    public class viewAdapter extends BaseAdapter {
        LayoutInflater layoutInflater;
        public viewAdapter(Context context){
            layoutInflater = layoutInflater.from(context);
        }
        @Override
        public int getCount() {
            return listaPersonas.size();
        }

        @Override
        public Object getItem(int i) {
            return listaPersonas.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = layoutInflater.inflate(R.layout.row, null);
            }
            final int posicionListView = i;
            final TextView titleNombre = (TextView) view.findViewById(R.id.rowTitleNombre);
            final TextView titleGenero = (TextView) view.findViewById(R.id.rowTitleGenero);
            final TextView titlePerfil = (TextView) view.findViewById(R.id.rowTitlePerfil);




            titleNombre.setText("Nombre");
            titleGenero.setText("Genero");
            titlePerfil.setText("Perfil");

            final ImageView foto = (ImageView)view.findViewById(R.id.rowImageView);
            final TextView nombre = (TextView)view.findViewById(R.id.rowNombre);
            final TextView genero = (TextView)view.findViewById(R.id.rowGenero);
            final TextView perfil = (TextView)view.findViewById(R.id.rowPerfil);

            perfil.setText(listaPersonas.get(posicionListView).getPerfil());
            nombre.setText(listaPersonas.get(posicionListView).getNombre());
            genero.setText(listaPersonas.get(posicionListView).getGenero());

            Uri imgUri=Uri.parse(listaPersonas.get(posicionListView).getUriFoto());
            foto.setImageURI(imgUri);

            foto.setRotation(270);


            titleNombre.setTextColor(getResources().getColor(R.color.colorAccent));
            titleGenero.setTextColor(getResources().getColor(R.color.colorAccent));
            titlePerfil.setTextColor(getResources().getColor(R.color.colorAccent));

            nombre.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            genero.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            perfil.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

            return view;
        }
    }

}
