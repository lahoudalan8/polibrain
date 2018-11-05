package com.educa.alan.polibrain;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class NiveisActivity extends AppCompatActivity {

    private static int NumeroNiveis = 6;

    // Dados de layout dos niveis
    private CardView niveisCard[] = new CardView[NumeroNiveis];
    private TextView niveisNome[] = new TextView[NumeroNiveis];
    private ImageButton niveisEx[] = new ImageButton[NumeroNiveis];
    private ImageButton niveisVideo[] = new ImageButton[NumeroNiveis];
    private TextView niveisPont[] = new TextView[NumeroNiveis];

    private int camada;

    // Nome dos níveis:
    String[][] nomes_camadas = new String[][]{
            {"Soma", "Subtração", "Expressões 1", "Expressões 2", "Multiplicação", "Divisão"},
            {"MDC", "MMC", "Frações Irredutíveis"}
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_niveis);

        Intent intent = getIntent();
        camada = intent.getExtras().getInt("camada");

        // Setando os cards
        int temp;
        String[] id_cards = new String[]{"n1_card", "n2_card", "n3_card", "n4_card", "n5_card", "n6_card"};
        for(int i=0; i<nomes_camadas[camada-1].length; i++){
            temp = getResources().getIdentifier(id_cards[i], "id", getPackageName());
            niveisCard[i] = (CardView) findViewById(temp);
            niveisCard[i].setVisibility(View.VISIBLE);
        }

        String[] id_nomes = new String[]{"n1_nome", "n2_nome", "n3_nome", "n4_nome", "n5_nome", "n6_nome"};
        for(int i=0; i<nomes_camadas[camada-1].length; i++){
            temp = getResources().getIdentifier(id_nomes[i], "id", getPackageName());
            niveisNome[i] = (TextView) findViewById(temp);
            niveisNome[i].setText(nomes_camadas[camada-1][i]);
        }

        // Setando os botoes de exercício
        String[] id_exs = new String[]{"n1_ex", "n2_ex", "n3_ex", "n4_ex", "n5_ex", "n6_ex"};
        for(int i=0; i<nomes_camadas[camada-1].length; i++){
            temp = getResources().getIdentifier(id_exs[i], "id", getPackageName());
            niveisEx[i] = (ImageButton) findViewById(temp);
        }

        // Setando os botoes de video
        String[] id_videos = new String[]{"n1_video", "n2_video", "n3_video", "n4_video", "n5_video", "n6_video"};
        for(int i=0; i<nomes_camadas[camada-1].length; i++){
            temp = getResources().getIdentifier(id_videos[i], "id", getPackageName());
            niveisVideo[i] = (ImageButton) findViewById(temp);
        }

        // Setando os textos de pontuação
        String[] id_ponts = new String[]{"n1_pont", "n2_pont", "n3_pont", "n4_pont", "n5_pont", "n6_pont"};
        for(int i=0; i<NumeroNiveis; i++){
            temp = getResources().getIdentifier(id_ponts[i], "id", getPackageName());
            niveisPont[i] = (TextView) findViewById(temp);
        }

    }

    // Clicando nos exercicios dos niveis
    public void clica_n1_ex(View view) {
        Intent intent = new Intent(this, QuestaoActivity.class);
        intent.putExtra("camada", camada);
        intent.putExtra("nivel", 1);
        intent.putExtra("questao", 1);
        startActivity(intent);
    }
    public void clica_n2_ex(View view) {
        Intent intent = new Intent(this, QuestaoActivity.class);
        intent.putExtra("camada", camada);
        intent.putExtra("nivel", 2);
        intent.putExtra("questao", 1);
        startActivity(intent);
    }
    public void clica_n3_ex(View view) {
        Intent intent = new Intent(this, QuestaoActivity.class);
        intent.putExtra("camada", camada);
        intent.putExtra("nivel", 3);
        intent.putExtra("questao", 1);
        startActivity(intent);
    }
    public void clica_n4_ex(View view) {
        Intent intent = new Intent(this, QuestaoActivity.class);
        intent.putExtra("camada", camada);
        intent.putExtra("nivel", 4);
        intent.putExtra("questao", 1);
        startActivity(intent);
    }
    public void clica_n5_ex(View view) {
        Intent intent = new Intent(this, QuestaoActivity.class);
        intent.putExtra("camada", camada);
        intent.putExtra("nivel", 5);
        intent.putExtra("questao", 1);
        startActivity(intent);
    }
    public void clica_n6_ex(View view) {
        Intent intent = new Intent(this, QuestaoActivity.class);
        intent.putExtra("camada", camada);
        intent.putExtra("nivel", 6);
        intent.putExtra("questao", 1);
        startActivity(intent);
    }



    // CLicando nos videos dos níveis
    public void clica_n1_video(View view) {
        Toast.makeText(this, "Não há video no momento", Toast.LENGTH_SHORT);
    }
    public void clica_n2_video(View view) {
        Toast.makeText(this, "Não há video no momento", Toast.LENGTH_SHORT);
    }
    public void clica_n3_video(View view) {
        Toast.makeText(this, "Não há video no momento", Toast.LENGTH_SHORT);
    }
    public void clica_n4_video(View view) {
        Toast.makeText(this, "Não há video no momento", Toast.LENGTH_SHORT);
    }
    public void clica_n5_video(View view) {
        Toast.makeText(this, "Não há video no momento", Toast.LENGTH_SHORT);
    }
    public void clica_n6_video(View view) {
        Toast.makeText(this, "Não há video no momento", Toast.LENGTH_SHORT);
    }

    @Override
    public void onBackPressed() {
        finish();
        goToHall();
    }

    public void goToHall (){
        Intent intent = new Intent (this, HallActivity.class);
        startActivity(intent);
    }
}
