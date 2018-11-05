package com.educa.alan.polibrain;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class FeedbackActivity extends AppCompatActivity {

    // Dados das questoes e do usuario
    private int camada, nivel;
    private int pontuacao, acertos, tempo;

    // Dados de Layout
    private TextView mTextPont, mTextAcerto, mTextTempo;
    private RatingBar mRating, mRatingA1, mRatingA2;
    private ImageView mIA1, mIA2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        // Coletando informações de atividades anteriores
        Intent intent = getIntent();
        camada = intent.getExtras().getInt("camada");
        nivel = intent.getExtras().getInt("nivel");
        pontuacao = intent.getExtras().getInt("pontuacao");
        acertos = intent.getExtras().getInt("acertos");
        tempo = intent.getExtras().getInt("tempo");

        // Setando Layout
        mTextPont = (TextView) findViewById(R.id.mtext_pontuacao);
        mTextAcerto = (TextView) findViewById(R.id.mtext_acertos);
        mTextTempo = (TextView) findViewById(R.id.mtext_tempo);

        // Alimentando o Layout
        mTextPont.setText(String.valueOf(pontuacao));
        mTextAcerto.setText(String.valueOf(acertos) + "/10");
        mTextTempo.setText(String.valueOf(tempo) + " s");

    }

    public String[] pega_dois_amigos(){
        return new String[]{"", ""};
    }

    public int calcula_media_nivel(){
        return 0;
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Já analisou o seu desempenho?")
                .setCancelable(false)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FeedbackActivity.this.finish();
                        goToNiveis();
                    }
                })
                .setNegativeButton("Não", null)
                .show();
    }

    public void goToNiveis (){
        Intent intent = new Intent (this, NiveisActivity.class);
        intent.putExtra("camada", camada);
        startActivity(intent);
    }

}
