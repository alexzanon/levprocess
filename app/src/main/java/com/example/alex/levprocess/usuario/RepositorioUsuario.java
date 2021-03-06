package com.example.alex.levprocess.usuario;

/**
 * Created by Alex on 30/08/2015.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.example.alex.levprocess.usuario.Usuario.Usuarios;

public class RepositorioUsuario {
    private static final String CATEGORIA = "levProcess";
    // Nome do banco
    private static final String NOME_BANCO = "lev";
    private static final String NOME_BANCO_CPY = "lev";
    private Context myCtx;
    public static String DB_PATH = "/data/data/com.example.alex.levprocess/databases/";

    // Nome da tabela
    public static final String NOME_TABELA = "usuario";
    protected SQLiteDatabase db;

    public void createDataBase() throws IOException{
        boolean dbExist = checkDataBase(NOME_BANCO);
        if(!dbExist){
            try {
                copyDataBase(NOME_BANCO_CPY,NOME_BANCO);
            } catch (Exception e) {
                throw new Error("Erro copiando o banco de dados");
            }
        }
    }

    private boolean checkDataBase(String DB){
        SQLiteDatabase checkDB = null;
        try{
            String myPath = DB_PATH + DB;
            checkDB = SQLiteDatabase.openDatabase(myPath, null,  SQLiteDatabase.OPEN_READONLY);

        }catch(SQLiteException e){}

        if(checkDB != null){

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }

    private void copyDataBase(String assetfile,String DB) {

        InputStream myInput = null;
        OutputStream myOutput = null;
        try {
            myInput = myCtx.getAssets().open(assetfile);

            String outFileName = DB_PATH + DB;

            myOutput = new FileOutputStream(outFileName);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer))>0){
                myOutput.write(buffer, 0, length);
            }

            System.out.println("***************************************");
            System.out.println("####### Data base copied ##############");
            System.out.println("***************************************");

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally{
            try {
                myOutput.flush();
                myOutput.close();
                myInput.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    public RepositorioUsuario(Context ctx) {
        try {
            this.myCtx = ctx;

            String myPath = DB_PATH + NOME_BANCO;
            db = ctx.openOrCreateDatabase(NOME_BANCO, Context.MODE_PRIVATE, null);
            db.setVersion(1);
            Log.e(CATEGORIA, db.getPath());
            db.setLockingEnabled(true);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    protected RepositorioUsuario() {
        // Apenas para criar uma subclasse...
    }
    // Salva o usuario, insere um novo ou atualiza
    public long salvar(Usuario usuario) {
        long id = usuario.id;
        if (id != 0) {
            atualizar(usuario);
        } else {
            // Insere novo
            id = inserir(usuario);
        }
        return id;
    }

    // Insere um novo usuario
    public long inserir(Usuario usuario) {
        ContentValues values = new ContentValues();
        values.put(Usuarios.LOGIN, usuario.login);
        values.put(Usuarios.SENHA, usuario.senha);
        values.put(Usuarios.NOME, usuario.nome);
        values.put(Usuarios.CPF, usuario.cpf);
        values.put(Usuarios.DATANASCIMENTO, usuario.dataNascimento);
        values.put(Usuarios.TELEFONE, usuario.telefone);
        values.put(Usuarios.EMAIL, usuario.email);
        values.put(Usuarios.TIPO, usuario.tipo);
        long id = inserir(values);
        return id;
    }
    // Insere um novo usuario
    public long inserir(ContentValues valores) {
        long id = db.insert(NOME_TABELA, "", valores);
        return id;
    }
    // Atualiza o usuario no banco. O id do usuario e utilizado.
    public int atualizar(Usuario usuario) {
        ContentValues values = new ContentValues();
        values.put(Usuarios.LOGIN, usuario.login);
        values.put(Usuarios.SENHA, usuario.senha);
        values.put(Usuarios.NOME, usuario.nome);
        values.put(Usuarios.CPF, usuario.cpf);
        values.put(Usuarios.DATANASCIMENTO, usuario.dataNascimento);
        values.put(Usuarios.TELEFONE, usuario.telefone);
        values.put(Usuarios.EMAIL, usuario.email);
        values.put(Usuarios.TIPO, usuario.tipo);
        String _id = String.valueOf(usuario.id);
        String where = Usuarios._ID + "=?";
        String[] whereArgs = new String[] { _id };
        int count = atualizar(values, where, whereArgs);
        return count;
    }

    // Atualiza o usuario com os valores abaixo
    // A clausula where e utilizada para identificar o usuario a ser atualizado
    public int atualizar(ContentValues valores, String where, String[] whereArgs) {
        int count = db.update(NOME_TABELA, valores, where, whereArgs);
        Log.i(CATEGORIA, "Atualizou [" + count + "] registros");
        return count;
    }
    // Deleta o usuario com o id fornecido
    public int deletar(long id) {
        String where = Usuarios._ID + "=?";
        String _id = String.valueOf(id);
        String[] whereArgs = new String[] { _id };
        int count = deletar(where, whereArgs);
        return count;
    }
    // Deleta o usuario com os argumentos fornecidos
    public int deletar(String where, String[] whereArgs) {
        int count = db.delete(NOME_TABELA, where, whereArgs);
        Log.i(CATEGORIA, "Deletou [" + count + "] registros");
        return count;
    }

    // Busca o usuario pelo id
    public Usuario buscarUsuario(long id) {
        // select * from usuario where _id=?
        Cursor c = db.query(true, NOME_TABELA, Usuario.colunas, Usuarios._ID + "=" + id,
                null, null, null, null, null);
        if (c.getCount() > 0) {
            // Posicinoa no primeiro elemento do cursor
            c.moveToFirst();
            Usuario usuario = new Usuario();
            // Le os dados
            usuario.id = c.getLong(0);
            usuario.login = c.getString(1);
            usuario.senha = c.getString(2);
            usuario.nome = c.getString(3);
            usuario.cpf = c.getString(4);
            usuario.dataNascimento = c.getString(5);
            usuario.telefone = c.getString(6);
            usuario.email = c.getString(7);
            usuario.tipo = c.getString(8);

            return usuario;
        }
        return null;
    }
    // Retorna um cursor com todos os usuarios
    public Cursor getCursor() {
        try {
            // select * from usuarios
            return db.query(NOME_TABELA, Usuario.colunas, null, null, null, null, null, null);
        } catch (SQLException e) {
            Log.e(CATEGORIA, "Erro ao buscar os usu�rios: " + e.toString());
            return null;
        }
    }

    // Retorna uma lista com todos os usuarios
    public List<Usuario> listarUsuarios() {
        Cursor c = getCursor();
        List<Usuario> usuarios = new ArrayList<Usuario>();
        if (c.moveToFirst()) {
            // Recupera os indices das colunas
            int idxId = c.getColumnIndex(Usuarios._ID);
            int idxLogin = c.getColumnIndex(Usuarios.LOGIN);
            int idxSenha = c.getColumnIndex(Usuarios.SENHA);
            int idxNome = c.getColumnIndex(Usuarios.NOME);
            int idxCpf = c.getColumnIndex(Usuarios.CPF);
            int idxDataNascimento = c.getColumnIndex(Usuarios.DATANASCIMENTO);
            int idxTelefone = c.getColumnIndex(Usuarios.TELEFONE);
            int idxEmail = c.getColumnIndex(Usuarios.EMAIL);
            int idxTipo = c.getColumnIndex(Usuarios.TIPO);
            // Loop ate o final
            do {
                Usuario usuario = new Usuario();
                usuarios.add(usuario);
                // recupera os atributos de usuario
                usuario.id = c.getLong(idxId);
                usuario.login = c.getString(idxLogin);
                usuario.senha = c.getString(idxSenha);
                usuario.nome = c.getString(idxNome);
                usuario.cpf = c.getString(idxCpf);
                usuario.dataNascimento = c.getString(idxDataNascimento);
                usuario.telefone = c.getString(idxTelefone);
                usuario.email = c.getString(idxEmail);
                usuario.tipo = c.getString(idxTipo);
            } while (c.moveToNext());
        }
        return usuarios;
    }
    // Busca o usuario pelo nome "select * from usuario where nome=?"
    public Usuario buscarUsuarioPorLogin(String login) {
        Usuario usuario = null;
        try {
            // Idem a: SELECT _id,login,senha from USUARIO where login = ?
            Cursor c = db.query(NOME_TABELA, Usuario.colunas, Usuarios.LOGIN + "='" + login + "'",
                    null, null, null, null);
            // Se encontrou...
            if (c.moveToNext()) {
                usuario = new Usuario();
                // utiliza os metodos getLong(), getString(), getString(), etc para recuperar os valores
                usuario.id = c.getLong(0);
                usuario.login = c.getString(1);
                usuario.senha = c.getString(2);
                usuario.nome = c.getString(3);
                usuario.cpf = c.getString(4);
                usuario.dataNascimento = c.getString(5);
                usuario.telefone = c.getString(6);
                usuario.email = c.getString(7);
                usuario.tipo = c.getString(8);
            }
        } catch (SQLException e) {
            Log.e(CATEGORIA, "Erro ao buscar o usu�rio pelo login: " + e.toString());
            return null;
        }
        return usuario;
    }

    // Fecha o banco
    public void fechar() {
        // fecha o banco de dados
        if (db != null) {
            db.close();
        }
    }
}