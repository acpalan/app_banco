package alan.passos.app_banco.telas;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.List;
import java.util.Objects;

import alan.passos.app_banco.R;
import alan.passos.app_banco.adapter.AdapterLP;
import alan.passos.app_banco.entidades.ProdutoEntity;
import alan.passos.app_banco.entidades.UserEntity;
import alan.passos.app_banco.utils.BancoDeDados;

public class TelaMenu extends AppCompatActivity {

    RecyclerView lp_recycler;
    ImageView add_produto_click, btnLogout, btnAlterarSenha, btnRelatorio;
    BancoDeDados bd;
    TextView nomeUsuario, tipoUsuario;
    UserEntity userEntity;
    List<ProdutoEntity> lista;

    @Override
    protected void onResume() {
        super.onResume();
        setList_recyclerView();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_menu);

        bd = BancoDeDados.getInstance(getApplicationContext());
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("init", Context.MODE_PRIVATE);
        int userId = sharedPref.getInt("user_id_login", -1);
        btnRelatorio = findViewById(R.id.btnRelatorio);


        if (userId != -1){
            Thread thread = new Thread(() -> {
                userEntity = bd.getUserDao().getUserById(userId);
            });

            thread.start();

            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            nomeUsuario = findViewById(R.id.nome);
            tipoUsuario = findViewById(R.id.usuarioTipo);

            nomeUsuario.setText(userEntity.nome);
            if (userEntity.typeUser == 2){
                tipoUsuario.setText("Cliente");
            } else {
                tipoUsuario.setText("Técnico");
                btnRelatorio.setVisibility(View.VISIBLE);
            }
        }

        btnAlterarSenha = findViewById(R.id.btnAlterarSenha);
        btnLogout = findViewById(R.id.btnLogout);
        lp_recycler = findViewById(R.id.lp_recycler);
        add_produto_click = findViewById(R.id.add_produto_click);
        lp_recycler.setLayoutManager( new LinearLayoutManager(getApplicationContext()));

        btnAlterarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TelaMenu.this);
                builder.setTitle("Alterar senha");

                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_alterar_senha, null);
                builder.setView(dialogView);

                builder.setPositiveButton("Alterar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText senhaAtual = dialogView.findViewById(R.id.senhaAtual);
                        EditText novaSenha = dialogView.findViewById(R.id.novaSenha);

                        if (Objects.equals(userEntity.senha, senhaAtual.getText().toString())){

                            Toast.makeText(getApplicationContext(), "Senha alterada com sucesso, deslogando...", Toast.LENGTH_LONG).show();

                            Thread thread = new Thread(() -> {
                                bd.getUserDao().updatePassword(userId, novaSenha.getText().toString().trim());
                            });

                            thread.start();

                            try {
                                thread.join();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }

                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Senha atual digitada errado", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                builder.setNegativeButton("Cancelar", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        btnRelatorio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TelaMenu.this);

                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_relatorio, null);
                builder.setView(dialogView);


                TextView qtdClientes = dialogView.findViewById(R.id.clientes);
                TextView qtdChamados = dialogView.findViewById(R.id.chamados);
                TextView resolvidos = dialogView.findViewById(R.id.resolvidos);


                Thread thread = new Thread(() -> {
                    List<UserEntity> clientes = bd.getUserDao().getAllClients();

                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            qtdClientes.setText(String.valueOf(clientes.size()));
                            qtdChamados.setText(String.valueOf(lista.size()));

                            int contador = 0;

                            for (ProdutoEntity chamado : lista) {
                                if (chamado.descricao_tecnico != null) {
                                    contador++;
                                }
                            }

                            resolvidos.setText(String.valueOf(contador));
                        }
                    });
                });

                thread.start();

                try {
                    thread.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        add_produto_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (getApplicationContext(), AddProduto.class );
                startActivity(intent);
            }
        });

        setList_recyclerView();
    }

    public void setList_recyclerView(){
        BancoDeDados bd = BancoDeDados.getInstance(getApplicationContext());


        Thread thread = new Thread(() -> {
            if (userEntity != null && userEntity.typeUser == 1){
                lista = bd.getProdutoDao().getProdutoAll();
            } else if(userEntity != null && userEntity.typeUser == 2) {
                lista = bd.getProdutoDao().getProdutosFromCliente(userEntity.id_user);
            }
        });

        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if(lista != null ){
            lp_recycler.setAdapter( new AdapterLP( getApplicationContext(), lista ));
        }
    }
}
