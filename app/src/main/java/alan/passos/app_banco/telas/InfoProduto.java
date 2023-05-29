package alan.passos.app_banco.telas;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.room.Room;

import com.bumptech.glide.Glide;

import alan.passos.app_banco.R;
import alan.passos.app_banco.entidades.ProdutoEntity;
import alan.passos.app_banco.utils.BancoDeDados;

public class InfoProduto extends AppCompatActivity {

    TextView descricao_produto_info, avaliacao_tecnico_info,titulo_produto_info;
    EditText sendText_tecnico_info;
    ImageView image_produto_info;
    ConstraintLayout toolbar;
    ImageButton  send_btn_click;

    LinearLayout container_tecnico_sendInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_produtos_activity);

        toolbar = findViewById(R.id.Mytoolbar);
        descricao_produto_info  = findViewById(R.id.descricao_produto_info);
        avaliacao_tecnico_info  = findViewById(R.id.avaliacao_tecnico_info);
        image_produto_info  = findViewById(R.id.image_produto_info);
        titulo_produto_info  = findViewById(R.id.titulo_produto_info);
        sendText_tecnico_info  = findViewById(R.id.sendText_tecnico_info);
        send_btn_click  = findViewById(R.id.send_btn_click);
        container_tecnico_sendInfo  = findViewById(R.id.container_tecnico_sendInfo);

        int id_produto = Integer.parseInt((String) getIntent().getExtras().get("id_produto"));
        BancoDeDados bd = Room.databaseBuilder(getApplicationContext(), BancoDeDados.class, "banco").allowMainThreadQueries().build();
        ProdutoEntity produto = bd.getProdutoDao().getProdutoId(id_produto);

        Glide.with(getApplicationContext()).load(produto.imagem_produto).into(image_produto_info);
        titulo_produto_info.setText(produto.titulo_produto);
        descricao_produto_info.setText(produto.descricao_produto);


        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("init",Context.MODE_PRIVATE);
        int user_id_login = sharedPref.getInt("user_id_login",0);
        int type = bd.getUserDao().getTypeUser(user_id_login);

        if(type == 1){
            //tecnico
            container_tecnico_sendInfo.setVisibility(View.VISIBLE);
            sendText_tecnico_info.setText(produto.descricao_tecnico);
        }else{
            //comun
            if(produto.descricao_tecnico!=null && !produto.descricao_tecnico.isEmpty()){
                avaliacao_tecnico_info.setVisibility(View.VISIBLE);
                avaliacao_tecnico_info.setText(produto.descricao_tecnico+"");
            }else{
                avaliacao_tecnico_info.setVisibility(View.VISIBLE);
                avaliacao_tecnico_info.setText("Sem Resposta, aguarda...");
            }

        }

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        send_btn_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!sendText_tecnico_info.getText().toString().isEmpty()){
                    produto.descricao_tecnico = sendText_tecnico_info.getText()+"";
                    BancoDeDados bd = Room.databaseBuilder(getApplicationContext(), BancoDeDados.class, "banco").allowMainThreadQueries().build();
                    bd.getProdutoDao().updateProduto(produto);
                    Toast.makeText(InfoProduto.this, "Menssagem enviada", Toast.LENGTH_SHORT).show();
                    finish();

                }
            }
        });

    }

}
