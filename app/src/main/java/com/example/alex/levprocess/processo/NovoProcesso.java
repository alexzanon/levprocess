package com.example.alex.levprocess.processo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alex.levprocess.R;

/**
 * Created by Alex on 28/11/2015.
 */
public class NovoProcesso extends Activity {

    private EditText nome, responsavel, papel, objetivo, condicao, entradas, saidas, roteiro;
    private Long id;
    private RepositorioProcesso repositorio;

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        init();
        setContentView(R.layout.novo_processo);
        nome = (EditText) findViewById(R.id.etNomeProcesso);
        responsavel = (EditText) findViewById(R.id.etResponsavel);
        papel = (EditText) findViewById(R.id.etPapel);
        objetivo = (EditText) findViewById(R.id.etObjetivo);
        condicao = (EditText) findViewById(R.id.etCondicao);
        entradas = (EditText) findViewById(R.id.etEntradas);
        saidas = (EditText) findViewById(R.id.etSaidas);
        id = null;

        Button btSalvar = (Button) findViewById(R.id.btSalvar);
        btSalvar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (nome.getText().toString().equals("")) {
                    Toast.makeText(NovoProcesso.this, "Favor cadastrar ao menos um nome para o processo", Toast.LENGTH_LONG).show();
                } else {
                    salvar();
                }
            }
        });

        Button btCancelar = (Button) findViewById(R.id.btCancelar);
        btCancelar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();// Fecha a tela
            }
        });

    }

    private void init() {
        repositorio = new RepositorioProcesso(this);
    }

    protected void onPause() {
        super.onPause();
        // Cancela para nao ficar nada na tela pendente
        setResult(RESULT_CANCELED);
        // Fecha a tela
        finish();
    }

    public void salvar() {
        Processo processo = new Processo();
        if (id != null) {
            processo.id = id;// E uma atualizacao
        }
        processo.nome = nome.getText().toString();
        processo.responsavel = responsavel.getText().toString();
        processo.papel = papel.getText().toString();
        processo.objetivo = objetivo.getText().toString();
        processo.condicao = condicao.getText().toString();
        processo.entradas = entradas.getText().toString();
        processo.saidas = saidas.getText().toString();
        salvarProcesso(processo);// Salvar

        setResult(RESULT_OK, new Intent());	// OK
        Toast.makeText(NovoProcesso.this, "Processo Gravado com Sucesso", Toast.LENGTH_LONG).show();
        // Fecha a tela
        finish();
    }

    protected void salvarProcesso(Processo processo) {
        repositorio.salvar(processo);
    }
}