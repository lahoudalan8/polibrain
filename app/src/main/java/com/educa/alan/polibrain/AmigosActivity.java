package com.educa.alan.polibrain;

import android.app.DownloadManager;
import android.content.DialogInterface;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.firebase.ui.auth.AuthUI.TAG;

public class AmigosActivity extends AppCompatActivity {


    private static int NumeroAmigos = 5;


    private String email_usuario;

    // Dados de layout dos niveis
    private CardView amigosCard[] = new CardView[NumeroAmigos];
    private TextView amigosNome[] = new TextView[NumeroAmigos];
    private ImageView amigosImg[] = new ImageView[NumeroAmigos];
    private ProgressBar amigosC1[] = new ProgressBar[NumeroAmigos];
    private ProgressBar amigosC2[] = new ProgressBar[NumeroAmigos];
    private ProgressBar amigosC3[] = new ProgressBar[NumeroAmigos];
    private TextView amigosPont[] = new TextView[NumeroAmigos];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amigos);

        // Setando os cards
        int temp;
        String[] id_cards = new String[]{"mCard_amigo1", "mCard_amigo2", "mCard_amigo3", "mCard_amigo4", "mCard_amigo5"};
        int flag = 0;
        int i = 0;
        while (flag == 0){
            try {
                temp = getResources().getIdentifier(id_cards[i], "id", getPackageName());
                amigosCard[i] = (CardView) findViewById(temp);
            } catch (Exception e) {
                flag = 1;
            }
            i = i + 1;
        }

        String[] id_nomes_ = new String[]{"mNome_Amigo1", "mNome_Amigo2", "mNome_Amigo3", "mNome_Amigo4", "mNome_Amigo5"};
        flag = 0;
        i = 0;
        while (flag == 0){
            try {
                temp = getResources().getIdentifier(id_nomes_[i], "id", getPackageName());
                amigosNome[i] = (TextView) findViewById(temp);
            } catch (Exception e) {
                flag = 1;
            }
            i = i + 1;
        }


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        email_usuario = user.getEmail();

        verifica_amigos();
    }

    public void clicaAddAmigo(View view) {

        final EditText input = new EditText(AmigosActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);

        new AlertDialog.Builder(this)
                .setMessage("Escreva o email do amigo que deseja adicionar")
                .setCancelable(true)
                .setView(input)
                .setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        enviaPedido(input.getText().toString());
                    }
                })
                .show();

    }

    public void enviaPedido(String email_recebe){

        escreve_envia_pedido(email_recebe);

        Toast toast = Toast.makeText(this, "Pedido de amizade enviado para " + email_recebe, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }

    public void escreve_envia_pedido(String email_recebe){

        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        DocumentReference docRef;

        Map<String, Object> envia_amizade = new HashMap<>();
        envia_amizade.put("email_amigo", email_recebe);
        envia_amizade.put("status", 1);
        db.collection("usuarios_amigos").document(email_usuario).collection(email_usuario).document(email_recebe)
                .set(envia_amizade);

        Map<String, Object> recebe_amizade = new HashMap<>();
        recebe_amizade.put("email_amigo", email_usuario);
        recebe_amizade.put("status", 0);
        db.collection("usuarios_amigos").document(email_recebe).collection(email_recebe).document(email_usuario)
                .set(recebe_amizade);
    }

    public void verifica_amigos(){
        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();

        // Create a reference to the cities collection
        CollectionReference amigoRef = db.collection("usuarios_amigos").document(email_usuario).collection(email_usuario);

        final ArrayList<DocumentSnapshot> amigos_doc = new ArrayList<DocumentSnapshot>() ;

        amigoRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                amigos_doc.add(document);
                                //tenho que fazer tudo aqui
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                        carrega_amigos(amigos_doc);

                    }
                });

    }

    public void carrega_amigos(ArrayList<DocumentSnapshot> amigos){
        if (amigos.size() == 0){
            return;
        }
        else {
            for (int i=0; i<amigos.size(); i++) {
                int amigo_atual = 0;
                DocumentSnapshot amigo = amigos.get(i);
                String email_amigo = amigo.getString("email_amigo");
                int status_amigo = (int)(long)(amigo.getLong("status"));
                if (status_amigo == 0){
                    aceita_pedido(email_amigo);
                }
                else if(status_amigo == 1){
                    continue;
                }
                else{
                    carrega_amigo(email_amigo, amigo_atual);
                    amigo_atual ++;
                }
            }
        }
    }

    public void aceita_pedido(final String email_amigo){
        new AlertDialog.Builder(this)
                .setMessage("Aceita o pedido de amizade de " + email_amigo + " ?")
                .setCancelable(false)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        update_amizade(email_usuario, email_amigo, 2);
                        update_amizade(email_amigo, email_usuario, 2);
                    }
                })
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        update_amizade(email_usuario, email_amigo, 1);
                    }
                })
                .show();
    }

    public void carrega_amigo(String email_usuario, int amigo_atual){
        amigosCard[amigo_atual].setVisibility(View.VISIBLE);
        amigosNome[amigo_atual].setText(email_usuario);
    }

    public void update_amizade(String email_usr, String email_amigo, int status_novo){

        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();

        Map<String, Object> amigo = new HashMap<>();
        amigo.put("email_amigo", email_amigo);
        amigo.put("status", status_novo);

        DocumentReference amigodoc = db.collection("usuarios_amigos").document(email_usr).collection(email_usr).document(email_amigo);

        amigodoc
                .update("status", status_novo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });

    }

}
