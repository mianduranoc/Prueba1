package mx.edu.ittepic.tpdm_u3_practica1_delarosaguerrerogabriela;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText nc_alu, nom_alu,ape_alu,carrera;
    Button consultar,insertar,actualizar,eliminar;
    DatabaseReference servicioRealtime;
    ListView list;
    List<Alumnos> datosConsultaAlu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nc_alu = findViewById(R.id.nc_alu);
        nom_alu = findViewById(R.id.nom_alu);
        ape_alu = findViewById(R.id.ape_alu);
        carrera = findViewById(R.id.carrera);

        consultar = findViewById(R.id.consultar);
        insertar = findViewById(R.id.insertar);
        actualizar = findViewById(R.id.actualizar);
        eliminar = findViewById(R.id.eliminar);

        servicioRealtime = FirebaseDatabase.getInstance().getReference();

        insertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertarAlu();
            }
        });

        consultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultarAlu();
            }
        });
    }
    private void insertarAlu(){
        Alumnos alumnos = new Alumnos(nc_alu.getText().toString(),nom_alu.getText().toString(),ape_alu.getText().toString(),carrera.getText().toString());

        servicioRealtime.child("alumnos").push().setValue(alumnos)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this,"EXITO",Toast.LENGTH_SHORT).show();
                        nc_alu.setText("");
                        nom_alu.setText("");
                        ape_alu.setText("");
                        carrera.setText("");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,"ERROR", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void consultarAlu(){
        servicioRealtime.child("alumnos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                datosConsultaAlu = new ArrayList<>();

                servicioRealtime.child("alumnos").child(dataSnapshot.getKey()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot snap : dataSnapshot.getChildren()){ //recupera todos los hijos que tiene
                            Alumnos alumnos = snap.getValue(Alumnos.class);

                            if(alumnos!=null){
                                datosConsultaAlu.add(alumnos);
                            }
                        }
                        crearListView();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {//cuandi no logra la conexion

            }
        });
    }

    private void crearListView(){

    }
}
