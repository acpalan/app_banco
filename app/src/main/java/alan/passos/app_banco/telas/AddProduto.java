package alan.passos.app_banco.telas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.room.Room;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import alan.passos.app_banco.R;
import alan.passos.app_banco.entidades.ProdutoEntity;
import alan.passos.app_banco.utils.BancoDeDados;

public class AddProduto extends AppCompatActivity {

    ConstraintLayout toolbar;
    EditText titulo_edt,descricao_edt,link_img_edt;
    Button salvar_produto_click;
    BancoDeDados bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_produto);

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("init", Context.MODE_PRIVATE);
        int userId = sharedPref.getInt("user_id_login", -1);

        toolbar = findViewById(R.id.Mytoolbar);
        titulo_edt = findViewById(R.id.titulo_edt);
        descricao_edt = findViewById(R.id.descricao_edt);
        link_img_edt = findViewById(R.id.link_img_edt);
        salvar_produto_click = findViewById(R.id.salvar_produto_click);

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        salvar_produto_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String titulo = titulo_edt.getText().toString();
                String descricao = descricao_edt.getText().toString();
                String link = link_img_edt.getText().toString();

                if( !titulo.isEmpty()  | !descricao.isEmpty() & !link.isEmpty()){

                    ProdutoEntity pd = new ProdutoEntity();
                    pd.cliente_id = userId;
                    pd.descricao_produto = descricao;
                    pd.titulo_produto = titulo;
                    if(link.isEmpty()){
                        pd.imagem_produto = "https://t4.ftcdn.net/jpg/02/51/95/53/360_F_251955356_FAQH0U1y1TZw3ZcdPGybwUkH90a3VAhb.jpg";
                    }else{
                        pd.imagem_produto = link;
                    }

                    BancoDeDados bd = Room.databaseBuilder(getApplicationContext(), BancoDeDados.class, "banco").allowMainThreadQueries().build();
                    bd.getProdutoDao().insert(pd);

                    Toast.makeText(AddProduto.this, "Salvo", Toast.LENGTH_SHORT).show();
                    finish();

                }

            }
        });





    }
}