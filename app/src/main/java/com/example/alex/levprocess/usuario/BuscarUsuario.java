package com.example.alex.levprocess.usuario;

/**
 * Created by Alex on 30/08/2015.
 */

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.alex.levprocess.R;

/**
 * Buscar o Usuario.
 *
 */
public class BuscarUsuario extends Activity implements View.OnClickListener {
    private RepositorioUsuario repositorio;

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        init();
        setContentView(R.layout.form_buscar_usuario);
        ImageButton btBuscar = (ImageButton) findViewById(R.id.btBuscar);
        btBuscar.setOnClickListener(this);
    }

    public void init() {
        repositorio = new RepositorioUsuario(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        // Cancela para nao ficar nada pendente na tela
        setResult(RESULT_CANCELED);
        // Fecha a tela
        finish();
    }
    public void onClick(View view) {
        EditText login = (EditText) findViewById(R.id.etUsuario);
        EditText senha = (EditText) findViewById(R.id.etSenha);

        // Recupera o nome do usuario
        String loginUsuario = login.getText().toString();
        // Busca o usuario pelo nome
        Usuario u = buscarLogin(loginUsuario);
        if (u != null) {
            // Atualiza os campos com o resultado
            login.setText(u.login);
            senha.setText(u.senha);
        } else {
            // Limpa os campos
            login.setText("");
            senha.setText("");

            Toast.makeText(BuscarUsuario.this, "Nenhum usuario encontrado", Toast.LENGTH_SHORT).show();
        }
    }
    // Busca um usuario pelo nome
    protected Usuario buscarLogin(String loginUsuario) {
        Usuario u = repositorio.buscarUsuarioPorLogin(loginUsuario);
        return u;
    }
}