package com.educa.alan.polibrain;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.firebase.ui.auth.AuthUI.TAG;

public class QuestaoClass {
    private String enunciado, altA, altB, altC, altD;
    private int tentativas, acertos;


    public double calculaMedia(ArrayList<DocumentSnapshot> questoes) {

        int num_questoes = questoes.size();

        int i;
        DocumentSnapshot questoes_document;
        Double facilidade;
        Double soma_facilidade = 0.0;
        for (i=0; i<num_questoes; i++) {
            questoes_document = questoes.get(i);
            facilidade = questoes_document.getDouble("facilidade");
            soma_facilidade = soma_facilidade + facilidade;
        }

        double media_facilidade = soma_facilidade/num_questoes;
        return media_facilidade;
    }

    public double calculaDesvio(ArrayList<DocumentSnapshot> questoes, double media_facilidade){

        int num_questoes = questoes.size();

        int i;
        DocumentSnapshot questoes_document;
        Double facilidade;
        Double aux = 0.0;
        for (i=0; i<num_questoes; i++) {
            questoes_document = questoes.get(i);
            facilidade = questoes_document.getDouble("facilidade");
            aux = aux + Math.pow(facilidade - media_facilidade, 2);
        }
        double std = Math.sqrt(aux)/num_questoes;

        return std;
    }


//    public JSONObject getQuestaoIdeal(JSONArray questoes, float dificuldade_ideal){
//        // Dadas as questoes aleatorias (Json), escolher a questao ideal para o usuario dada a dificuldade ideal
//    }
//
//    public void setDataQuestaoIdeal(JSONObject questaoIdeal){
//
//        enunciado = kkk;
//        altA = ;
//        altB = ;
//        altC = ;
//        altD = ;
//
//    }

    public String getEnunciado() {
        return enunciado;
    }

    public String getAltA() {
        return altA;
    }

    public String getAltB() {
        return altB;
    }

    public String getAltC() {
        return altC;
    }

    public String getAltD() {
        return altD;
    }
}
