package com.educa.alan.polibrain;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.Blob;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import katex.hourglass.in.mathlib.MathView;

import static com.firebase.ui.auth.AuthUI.TAG;

public class QuestaoActivity extends AppCompatActivity implements View.OnClickListener {

    //color
    private DrawingView drawView;
    private ImageButton currPaint, drawBtn, eraseBtn, newBtn, saveBtn;
    private float smallBrush, mediumBrush, largeBrush;

    // Dados de layout
    private TextView mTextQuestaoX;
    private Chronometer mChronometer;
    private CardView mCardAltA, mCardAltB, mCardAltC, mCardAltD;
    private MathView mTextEnunciado, mTextAltA, mTextAltB, mTextAltC, mTextAltD;
    private Button mButtonConfirmar;
    private ImageView mButtonDificuldade;

    // Dados da interação com o usuario
    private int alt_pressionada = 0;
    private int alt_correta = 0;
    private int grupo_questao;
    private String id_ideal;
    private int pressed = 0;

    // Dados do banco de questões
    private int camada, nivel;
    private String [] nome_camada = {"mat_bas","fracoes","equacoes"};
    private String [][] nome_nivel = {
            {"soma","subtracao","expressoes1","expressoes2","multiplicacao","divisao"},
            {"mdc", "mmc", "irredutiveis", "somafrac", "multfrac", "divfrac"},
            {"1grau"}
    };

    private int [] mensagem_dificuldade = {
            R.string.g0, R.string.g1, R.string.g2, R.string.g3, R.string.g4, R.string.g5, R.string.g6, R.string.g7, R.string.g8, R.string.g9, R.string.g10
    };

    private int[] cor_dificuldade = {
            R.color.g0, R.color.g1, R.color.g2, R.color.g3, R.color.g4, R.color.g5, R.color.g6, R.color.g7, R.color.g8, R.color.g9, R.color.g10
    };


    // Dados do usuario
    private String email_usuario;

    // Dado dinamicos
    private int questao_atual;
    private float pontuacao;
    private int acertos;
    private int tempo_total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //set content view AFTER ABOVE sequence (to avoid crash)
        this.setContentView(R.layout.activity_questao);

        getSupportActionBar().hide();

        Intent intent = getIntent();
        camada = intent.getExtras().getInt("camada");
        nivel = intent.getExtras().getInt("nivel");
        questao_atual = intent.getExtras().getInt("questao");
        pontuacao = intent.getExtras().getFloat("pontuacao");
        acertos = intent.getExtras().getInt("acertos");
        tempo_total = intent.getExtras().getInt("tempo");

        // Dados de Layout
        LinearLayout layout = (LinearLayout) findViewById(R.id.questao);
        //layout.setBackgroundResource(R.drawable.teste1);

        mTextQuestaoX = (TextView) findViewById(R.id.id_questao_x);
        mChronometer = (Chronometer) findViewById(R.id.id_chronometer);
        mButtonDificuldade = (ImageView) findViewById(R.id.mbut_dificuldade);

        mTextEnunciado = (MathView) findViewById(R.id.mTextEnunciado);
        mTextAltA = (MathView) findViewById(R.id.mTextAltA);
        mTextAltB = (MathView) findViewById(R.id.mTextAltB);
        mTextAltC = (MathView) findViewById(R.id.mTextAltC);
        mTextAltD = (MathView) findViewById(R.id.mTextAltD);

        mCardAltA = (CardView) findViewById(R.id.card_view_A);
        mCardAltB = (CardView) findViewById(R.id.card_view_B);
        mCardAltC = (CardView) findViewById(R.id.card_view_C);
        mCardAltD = (CardView) findViewById(R.id.card_view_D);

        mButtonConfirmar = (Button) findViewById(R.id.mButtonConfirmar);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        email_usuario = user.getEmail();

        pressed = 0;

        mTextQuestaoX.setText("Questão " + String.valueOf(questao_atual));

        FirebaseFirestore db;
        QuestaoClass questao = new QuestaoClass();
        db = FirebaseFirestore.getInstance();
        DocumentReference docRef;

        // Create a reference to the cities collection
        CollectionReference citiesRef = db.collection("questoes_mat").document(nome_camada[camada-1]).collection(nome_nivel[camada-1][nivel-1]);

        // Create a query against the collection.
        Query query = citiesRef.orderBy("grupo");

        final ArrayList<DocumentSnapshot> questoes_doc = new ArrayList<DocumentSnapshot>() ;

        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                questoes_doc.add(document);
                                //tenho que fazer tudo aqui
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                        QuestaoClass util_questao = new QuestaoClass();

                        //double mediaFacilidade = util_questao.calculaMedia(questoes_doc);
                        //double stdFacilidade = util_questao.calculaDesvio(questoes_doc, mediaFacilidade);
                        int grupo_ideal = util_questao.calculaMelhorGrupo(questao_atual, pontuacao);
                        int id = util_questao.calculaMelhorId(questoes_doc, 0, 0, grupo_ideal);
                        DocumentSnapshot questao_ideal = questoes_doc.get(id);
                        id_ideal = questao_ideal.getId();
                        String enunciado = questao_ideal.getString("enunciado");
                        String alt_a = questao_ideal.getString("alt_a");
                        String alt_b = questao_ideal.getString("alt_b");
                        String alt_c = questao_ideal.getString("alt_c");
                        String alt_d = questao_ideal.getString("alt_d");

                        grupo_questao = (int) (long) questao_ideal.getLong("grupo");
                        alt_correta = (int) (long) questao_ideal.getLong("alt_correta");

                        mButtonDificuldade.setImageResource(cor_dificuldade[grupo_questao]);

                        mChronometer.setBase(SystemClock.elapsedRealtime());
                        mChronometer.start();

                        mTextEnunciado.setDisplayText(enunciado);
                        mTextAltA.setDisplayText(alt_a);
                        mTextAltB.setDisplayText(alt_b);
                        mTextAltC.setDisplayText(alt_c);
                        mTextAltD.setDisplayText(alt_d);

                    }
                });

        // Iniciando variáveis para desenho
        TabHost abas = (TabHost) findViewById(R.id.tabhost);
        abas.setup();

        TabHost.TabSpec descritor = abas.newTabSpec("aba1");
        descritor.setContent(R.id.questao);
        descritor.setIndicator(getString(R.string.questao));
        abas.addTab(descritor);

        descritor = abas.newTabSpec("aba2");
        descritor.setContent(R.id.rascunho);
        descritor.setIndicator(getString(R.string.rascunho));
        abas.addTab(descritor);

        drawView = (DrawingView) findViewById(R.id.drawing);
        //LinearLayout paintLayout = (LinearLayout)findViewById(R.id.paint_colors);
        //currPaint = (ImageButton)paintLayout.getChildAt(0);
        //currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));

        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.medium_size);
        largeBrush = getResources().getInteger(R.integer.large_size);

        drawBtn = (ImageButton) findViewById(R.id.draw_btn);
        drawBtn.setOnClickListener(this);

        //drawView.setBrushSize(smallBrush);

        eraseBtn = (ImageButton) findViewById(R.id.erase_btn);
        eraseBtn.setOnClickListener(this);

        newBtn = (ImageButton) findViewById(R.id.new_btn);
        newBtn.setOnClickListener(this);

        saveBtn = (ImageButton) findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(this);

        mTextAltA.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mCardAltA.setBackgroundResource(R.color.colorCardLight);
                mCardAltB.setBackgroundResource(R.color.colorCard);
                mCardAltC.setBackgroundResource(R.color.colorCard);
                mCardAltD.setBackgroundResource(R.color.colorCard);

                alt_pressionada = 1;
                mButtonConfirmar.setVisibility(View.VISIBLE);

                return true;
            }
        });

        mTextAltB.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mCardAltA.setBackgroundResource(R.color.colorCard);
                mCardAltB.setBackgroundResource(R.color.colorCardLight);
                mCardAltC.setBackgroundResource(R.color.colorCard);
                mCardAltD.setBackgroundResource(R.color.colorCard);

                alt_pressionada = 2;
                mButtonConfirmar.setVisibility(View.VISIBLE);

                return true;
            }
        });

        mTextAltC.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mCardAltA.setBackgroundResource(R.color.colorCard);
                mCardAltB.setBackgroundResource(R.color.colorCard);
                mCardAltC.setBackgroundResource(R.color.colorCardLight);
                mCardAltD.setBackgroundResource(R.color.colorCard);

                alt_pressionada = 3;
                mButtonConfirmar.setVisibility(View.VISIBLE);

                return true;
            }
        });

        mTextAltD.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mCardAltA.setBackgroundResource(R.color.colorCard);
                mCardAltB.setBackgroundResource(R.color.colorCard);
                mCardAltC.setBackgroundResource(R.color.colorCard);
                mCardAltD.setBackgroundResource(R.color.colorCardLight);

                alt_pressionada = 4;
                mButtonConfirmar.setVisibility(View.VISIBLE);

                return true;
            }
        });

        mButtonConfirmar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        pressed = pressed + 1;
                        int tempo = (int) (SystemClock.elapsedRealtime() - mChronometer.getBase())/1000;
                        if (alt_pressionada == alt_correta){

                            mButtonConfirmar.setBackgroundColor(Color.GREEN);

                            if (pressed == 1) {
                                pontuacao = calculaPontuacao(pontuacao, 1, tempo, grupo_questao);
                                acertos = acertos + 1;
                                tempo_total = tempo_total + tempo;
                                escreve_usuario_pont_questoes(1, tempo);
                            }

                            if (alt_pressionada == 1){
                                mCardAltA.setCardBackgroundColor(Color.GREEN);
                            }
                            else if (alt_pressionada == 2){
                                mCardAltB.setCardBackgroundColor(Color.GREEN);
                            }
                            else if (alt_pressionada == 3){
                                mCardAltC.setCardBackgroundColor(Color.GREEN);
                            }
                            else if (alt_pressionada == 4){
                                mCardAltD.setCardBackgroundColor(Color.GREEN);
                            }
                        }
                        else{

                            mButtonConfirmar.setBackgroundColor(Color.RED);

                            if (pressed == 1) {
                                pontuacao = calculaPontuacao(pontuacao, 0, tempo, grupo_questao);
                                tempo_total = tempo_total + tempo;
                                escreve_usuario_pont_questoes(0, tempo);
                            }


                            if (alt_pressionada == 1){
                                mCardAltA.setCardBackgroundColor(Color.RED);
                            }
                            else if (alt_pressionada == 2){
                                mCardAltB.setCardBackgroundColor(Color.RED);
                            }
                            else if (alt_pressionada == 3){
                                mCardAltC.setCardBackgroundColor(Color.RED);
                            }
                            else if (alt_pressionada == 4){
                                mCardAltD.setCardBackgroundColor(Color.RED);
                            }
                        }

                        if (questao_atual<=9) {
                            finish();
                            goToNextQuestion();
                        }
                        else if(questao_atual == 10){
                            if (pressed == 1) {
                                escreve_usuario_pont_nivel((int) pontuacao);
                                finish();
                                goToFeedback();
                            }
                        }
                        else{
                            finish();
                            goToNiveis();
                        }

                    }
                }, 1000);   //1 second

                return true;
            }
        });

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Tem certeza que deseja sair? A sua pontuação neste tópico não será computada.")
                .setCancelable(false)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        QuestaoActivity.this.finish();
                        goToNiveis();
                    }
                })
                .setNegativeButton("Não", null)
                .show();
    }


    public void escreve_usuario_pont_questoes(int acerto, int tempo){

        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        DocumentReference docRef;

        Map<String, Object> questao = new HashMap<>();
        questao.put("email_usr", email_usuario);
        questao.put("camada", camada);
        questao.put("nivel", nivel);
        questao.put("id_questao", id_ideal);
        questao.put("acerto", acerto);
        questao.put("tempo", tempo);

        db.collection("usuarios_pont_questoes")
                .add(questao);
    }


    public void escreve_usuario_pont_nivel(int pontuacao){

        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        DocumentReference docRef;

        Map<String, Object> nivel_usuario = new HashMap<>();
        nivel_usuario.put("email_usr", email_usuario);
        nivel_usuario.put("camada", camada);
        nivel_usuario.put("nivel", nivel);
        nivel_usuario.put("pontuacao", pontuacao);

        db.collection("usuarios_pont_niveis")
                .add(nivel_usuario);
    }

    public float calculaPontuacao(float pont, int acerto, int tempo, int grupo){

        if (acerto == 0){
            return pont;
        }

        int bonus;
        if (grupo == 0){
            bonus = 150;
        }
        else{
            bonus = 30*grupo;
        }

        int nova = (int) (bonus + (300 - 5*tempo));
        pont = pont + nova;
        Toast.makeText(getApplicationContext(), "+ " + String.valueOf(nova) + " pontos !", Toast.LENGTH_SHORT).show();

        if (pont <=30){
            return pont + 30;
        }

        return pont;
    }

    public void goToNextQuestion (){
        Intent intent = new Intent (this, QuestaoActivity.class);
        intent.putExtra("camada", camada);
        intent.putExtra("nivel", nivel);
        intent.putExtra("questao", questao_atual + 1);
        intent.putExtra("pontuacao", pontuacao);
        intent.putExtra("acertos", acertos);
        intent.putExtra("tempo", tempo_total);
        startActivity(intent);
    }

    public void goToFeedback (){
        Intent intent = new Intent (this, FeedbackActivity.class);
        intent.putExtra("camada", camada);
        intent.putExtra("nivel", nivel);
        intent.putExtra("pontuacao", pontuacao);
        intent.putExtra("acertos", acertos);
        intent.putExtra("tempo", tempo_total);
        startActivity(intent);
    }

    public void goToNiveis (){
        Intent intent = new Intent (this, NiveisActivity.class);
        intent.putExtra("camada", camada);
        startActivity(intent);
    }


    @Override
    public void onClick(View view) {

        //Clicando em botoes de Drawing
        if (view.getId() == R.id.draw_btn) {
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle("Brush size:");
            brushDialog.setContentView(R.layout.brush_chooser);
            ImageButton smallBtn = (ImageButton) brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(smallBrush);
                    drawView.setLastBrushSize(smallBrush);
                    drawView.setErase(false);
                    brushDialog.dismiss();
                }
            });
            ImageButton mediumBtn = (ImageButton) brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(mediumBrush);
                    drawView.setLastBrushSize(mediumBrush);
                    drawView.setErase(false);
                    brushDialog.dismiss();
                }
            });
            ImageButton largeBtn = (ImageButton) brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(largeBrush);
                    drawView.setLastBrushSize(largeBrush);
                    drawView.setErase(false);
                    brushDialog.dismiss();
                }
            });

            brushDialog.show();
        } else if (view.getId() == R.id.erase_btn) {
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle("Eraser size:");
            brushDialog.setContentView(R.layout.brush_chooser);
            ImageButton smallBtn = (ImageButton) brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(smallBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton mediumBtn = (ImageButton) brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(mediumBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton largeBtn = (ImageButton) brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(largeBrush);
                    brushDialog.dismiss();
                }
            });

            brushDialog.show();
        } else if (view.getId() == R.id.new_btn) {
            AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
            newDialog.setTitle("New drawing");
            newDialog.setMessage("Start new drawing (you will lose the current drawing)?");
            newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    drawView.startNew();
                    dialog.dismiss();
                }
            });
            newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            newDialog.show();
        } else if (view.getId() == R.id.save_btn) {
            AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
            saveDialog.setTitle("Save drawing");
            saveDialog.setMessage("Save drawing to device Gallery?");
            saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    drawView.setDrawingCacheEnabled(true);
                    String imgSaved = MediaStore.Images.Media.insertImage(
                            getContentResolver(), drawView.getDrawingCache(),
                            UUID.randomUUID().toString() + ".png", "drawing");
                    if (imgSaved != null) {
                        Toast savedToast = Toast.makeText(getApplicationContext(),
                                "Drawing saved to Gallery!", Toast.LENGTH_SHORT);
                        savedToast.show();
                    } else {
                        Toast unsavedToast = Toast.makeText(getApplicationContext(),
                                "Oops! Image could not be saved.", Toast.LENGTH_SHORT);
                        unsavedToast.show();
                    }
                    drawView.destroyDrawingCache();
                }
            });
            saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            saveDialog.show();
        }

    }

    public void clicaButDif(View view) {
        Toast.makeText(getApplicationContext(), getResources().getString(mensagem_dificuldade[grupo_questao]), Toast.LENGTH_SHORT).show();
    }
}

class DrawingView extends View
{
    private Path drawPath;
    private Paint drawPaint, canvasPaint;
    private int paintColor = 0xFF660000;
    private Canvas drawCanvas;
    private Bitmap canvasBitmap;
    private float brushSize, lastBrushSize;
    private boolean erase=false;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                drawCanvas.drawPath(drawPath, drawPaint);
                drawPath.reset();
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }

    public DrawingView(Context context, AttributeSet attrs){
        super(context, attrs);
        setupDrawing();
    }

    private void setupDrawing(){
        brushSize = getResources().getInteger(R.integer.small_size);
        lastBrushSize = brushSize;

        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(brushSize);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        canvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    public void setColor(String newColor){
        invalidate();
        paintColor = Color.parseColor(newColor);
        drawPaint.setColor(paintColor);

    }

    public void setBrushSize(float newSize){
        float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, newSize, getResources().getDisplayMetrics());
        brushSize=pixelAmount;
        drawPaint.setStrokeWidth(brushSize);
    }

    public void setLastBrushSize(float lastSize){
        lastBrushSize=lastSize;
    }

    public float getLastBrushSize(){
        return lastBrushSize;
    }

    public void setErase(boolean isErase){
        erase=isErase;
        if(erase) drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        else drawPaint.setXfermode(null);
    }

    public void startNew(){
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }
}