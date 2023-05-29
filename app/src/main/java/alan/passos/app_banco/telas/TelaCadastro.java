    package alan.passos.app_banco.telas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import alan.passos.app_banco.DAO.UserDAO;
import alan.passos.app_banco.R;
import alan.passos.app_banco.entidades.UserEntity;
import alan.passos.app_banco.utils.BancoDeDados;

    public class TelaCadastro extends AppCompatActivity implements View.OnClickListener {

        /* ATRIBUTOS **************************************************************/
        EditText editNome, editSenha;
        RadioGroup userType_rg;
        RadioButton comun_rdb, tecnico_rdb;
        TextView  btnVoltar;
        Button btnCadastrar;
        BancoDeDados bd;

        /* MÉTODOS DO LIFECYRCLE **************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro);

        //cria uma instancia da conexão com o banco
        bd = BancoDeDados.getInstance(getApplicationContext());

        capturaComponentes(); // 1ª
        vinculaEventos();     // 2ª

    }

        /* MÉTODOS ****************************************************************/
        private void capturaComponentes(){
            editNome  = findViewById(R.id.editNome);
            editSenha  = findViewById(R.id.editSenha);
            btnCadastrar = findViewById(R.id.btnCadastrar);
            btnVoltar = findViewById(R.id.btnVoltar);
            userType_rg =  findViewById(R.id.userType_rg);
            comun_rdb =  findViewById(R.id.comun_rdb);
            tecnico_rdb =  findViewById(R.id.tecnico_rdb);
        }
        private void vinculaEventos(){
            btnVoltar.setOnClickListener(this);
            btnCadastrar.setOnClickListener(this);
        }
        private boolean validarCampos(){
            if(userType_rg.getCheckedRadioButtonId() == -1){
                editNome.setError("Tipo de usuario invalido");
                return false;

            }else if (editNome.getText().length()==0){ // testa campo nome
                editNome.setError("Nome Obrigatório");
                return false;

            }else if (editSenha.getText().length()==0){ // testa campo senha
                editSenha.setError("Senha Obrigatória");
                return false;

            }
            return true;
        }

        private void salvarDadosNoBanco(){


            String nome = editNome.getText().toString();
            String senha = editSenha.getText().toString();

            UserEntity novoUserEntity = new UserEntity();
            novoUserEntity.nome= nome ;

            if(tecnico_rdb.isChecked()){
                //tecnico
                novoUserEntity.typeUser = 1;
            }else{
                //comun
                novoUserEntity.typeUser = 2;
            }
            novoUserEntity.senha=senha;


            Thread thread = new Thread(() -> {
                //criar uma instancia do DAO
                UserDAO userDAO = bd.getUserDao();
                userDAO.insert(novoUserEntity);

            });

            thread.start();

            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        /* MÉTODOS DE INTERFACES DE EVENTOS ******************************************/
        public void onClick(View view) { // tratar todos os toques dos componentes
            // filtro dos componentes tocados
            if(view.getId()==R.id.btnVoltar) {
                finish();
            }else if(view.getId()==R.id.btnCadastrar) {
                if (validarCampos() == true) {
                    salvarDadosNoBanco();
                    Toast.makeText(this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }



            }
        }

