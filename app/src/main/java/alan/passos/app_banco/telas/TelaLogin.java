package alan.passos.app_banco.telas;

import static alan.passos.app_banco.R.layout.activity_tela_login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import alan.passos.app_banco.R;
import alan.passos.app_banco.entidades.UserEntity;
import alan.passos.app_banco.utils.BancoDeDados;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Dispatchers;

public class TelaLogin extends AppCompatActivity implements View.OnClickListener {

    /* ATRIBUTOS **************************************************************/
    EditText editLogin, editSenha;
    Button btnLogin;
    TextView txtCadastrar;
    BancoDeDados bd;
    UserEntity userEntity;


    /* MÉTODOS DO LIFECYRCLE **************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bd = BancoDeDados.getInstance(getApplicationContext());
        setContentView(activity_tela_login);

        capturaComponentes(); // 1ª
        vinculaEventos();     // 2ª
        //cria uma instancia da conexão com o banco
    }

    /* MÉTODOS ****************************************************************/
    private void capturaComponentes() {
        editLogin = findViewById(R.id.edit_Login);
        editSenha = findViewById(R.id.edit_Senha);
        btnLogin = findViewById(R.id.btnLogin);
        txtCadastrar = findViewById(R.id.txtCadastrar);
    }

    private void vinculaEventos() {
        txtCadastrar.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    private boolean validarCampos() {
        if (editLogin.getText() == null ||
                editLogin.getText().length() == 0) { // validação do campo Login
            editLogin.setError("Login obrigatório!");
            return false;

        } else if (editSenha.getText() == null || editSenha.getText().length() == 0) { // validação do campo senha
            editSenha.setError("Senha obrigatória!");
            return false;


        }
        return true;
    }

    private void fazerLogin() {
        String nome = editLogin.getText().toString().trim();
        String senha = editSenha.getText().toString().trim();

        Thread thread = new Thread(() -> {
            userEntity = bd.getUserDao().getUser(nome, senha);
        });

        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (userEntity == null) { //não encontrou os dados no banco
            Toast.makeText(this, "Usuário inexistente", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();

            SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("init", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("user_id_login", userEntity.id_user);
            editor.commit();

            Intent intent = new Intent(getApplicationContext(), TelaMenu.class);

            editLogin.setText("");
            editSenha.setText("");
            startActivity(intent);
        }
    }

    /* MÉTODOS DE INTERFACES DE EVENTOS ******************************************/
    @Override
    public void onClick(View view) { // tratar todos os toques dos componentes
        // filtro dos componentes tocados
        if (view.getId() == R.id.txtCadastrar) {
            Intent intent = new Intent(getApplicationContext(), TelaCadastro.class);
            startActivity(intent);
        } else if (view.getId() == R.id.btnLogin) {
            if (validarCampos()) {
                fazerLogin();
            }
        }
    }
}

