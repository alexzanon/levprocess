package com.example.alex.levprocess.usuario;


import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Alex on 30/08/2015.
 */

public class Usuario {

    public static String[] colunas = new String[] { Usuarios._ID, Usuarios.LOGIN, Usuarios.SENHA, Usuarios.NOME, Usuarios.CPF, Usuarios.DATANASCIMENTO, Usuarios.TELEFONE, Usuarios.EMAIL, Usuarios.TIPO  };

    public static final String AUTHORITY = "br.livro.android.provider.usuarios";

    public long id;
    public String login;
    public String senha;
    public String nome;
    public String cpf;
    public String dataNascimento;
    public String telefone;
    public String email;
    public String tipo;

    public Usuario() {

    }

    public Usuario(String login, String senha) {
        super();
        this.login = login;
        this.senha = senha;

    }

    public Usuario(long id, String login, String senha, String nome, String cpf, String dataNascimento, String telefone, String email, String tipo) {
        super();
        this.id = id;
        this.login = login;
        this.senha = senha;
        this.nome = nome;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.telefone = telefone;
        this.email = email;
        this.tipo = tipo;
    }

    public static final class Usuarios implements BaseColumns {

        // Nao pode instanciar esta Classe
        private Usuarios() {
        }

        // content://br.livro.android.provider.usuario/usuarios
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/usuarios");
        // Mime Type para todos os usuarios
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.google.usuarios";
        // Mime Type para um unico usuario
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.google.usuarios";
        // Ordenacao default para inserir no order by
        public static final String DEFAULT_SORT_ORDER = "_id ASC";

        public static final String LOGIN = "login";
        public static final String SENHA = "senha";
        public static final String NOME = "nome";
        public static final String CPF = "cpf";
        public static final String DATANASCIMENTO = "dataNascimento";
        public static final String TELEFONE = "telefone";
        public static final String EMAIL = "email";
        public static final String TIPO = "tipo";

        // Metodo que constroi uma Uri para um Usuario especifico, com o seu id
        // A Uri e no formato "content://br.livro.android.provider.usuario/usuarios/id"
        public static Uri getUriId(long id) {
            // Adiciona o id na URI default do /usuarios
            Uri uriUsuario = ContentUris.withAppendedId(Usuarios.CONTENT_URI, id);
            return uriUsuario;
        }
    }

    @Override
    public String toString() {
        return "Login: " + login + ", Senha: " + senha + ", Nome: " + nome + ", CPF: " + cpf + ", Data de Nascimento: " + dataNascimento + ", Telefone: " + telefone + ", E-mail: " + email + ", Tipo: " + tipo;
    }
}