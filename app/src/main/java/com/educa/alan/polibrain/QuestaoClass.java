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

    public int calculaMelhorGrupo(int questao_atual, float pontuacao){
        int media = (int) pontuacao/questao_atual;
        return (int) media/40;
        //return (int) (11 * Math.random()) - 1;
    }

    public int calculaMelhorId(ArrayList<DocumentSnapshot> questoes, double media_facilidade, double std_facilidade, int grupo){

        int qde_questoes = questoes.size();

        int[] histograma_grupos = calculaArrayQuestoes(questoes);

        if (histograma_grupos[0] != 0){
            double prob = Math.random();
            if (prob>0.5) {
                return (int) (histograma_grupos[0] * Math.random());
            }
        }

        int g = 0;
        int num_questoes = 0;
        while (g<grupo){
            num_questoes = num_questoes + histograma_grupos[g];
            g++;
        }
        if (num_questoes < qde_questoes - 30 && num_questoes > 30){
            return (int) (30 * Math.random()) + num_questoes - 15;
        }
        else if (num_questoes > qde_questoes - 30){
            return (int) (qde_questoes - 1 - (30 * Math.random()));
        }
        else{
            return (int) (30 * Math.random());
        }

    }


    public int[] calculaArrayQuestoes(ArrayList<DocumentSnapshot> questoes){

        int num_questoes = questoes.size();

        int num0 = 0;
        int num1 = 0;
        int num2 = 0;
        int num3 = 0;
        int num4 = 0;
        int num5 = 0;
        int num6 = 0;
        int num7 = 0;
        int num8 = 0;
        int num9 = 0;
        int num10 = 0;

        int i;
        DocumentSnapshot questoes_document;
        int grupo;
        Double soma_facilidade = 0.0;
        for (i=0; i<num_questoes; i++) {
            questoes_document = questoes.get(i);
            grupo = (int) (long) questoes_document.getLong("grupo");
            if (grupo == 0){
                num0++;
                continue;
            }
            else if (grupo == 1){
                num1++;
                continue;
            }
            else if (grupo == 2){
                num2++;
                continue;
            }
            else if (grupo == 3){
                num3++;
                continue;
            }
            else if (grupo == 4){
                num4++;
                continue;
            }
            else if (grupo == 5){
                num5++;
                continue;
            }
            else if (grupo == 6){
                num6++;
                continue;
            }
            else if (grupo == 7){
                num7++;
                continue;
            }
            else if (grupo == 8){
                num8++;
                continue;
            }
            else if (grupo == 9){
                num1++;
                continue;
            }
            else if (grupo == 10){
                num10++;
                continue;
            }
        }

        int[] quantidade_por_grupo = new int[]{num0, num1, num2, num3, num4, num5, num6, num7, num8, num9, num10};
        return quantidade_por_grupo;
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
