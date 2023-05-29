package alan.passos.app_banco.telas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import alan.passos.app_banco.R;
import alan.passos.app_banco.entidades.ProdutoEntity;
import alan.passos.app_banco.utils.BancoDeDados;

public class ProdutoEdit extends AppCompatActivity {

    EditText titulo_edt_pd,descricao_edt_pd,link_img_edt_pd;
    Button salvar_produto_click_pd;
    ConstraintLayout toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produto_edit);

        toolbar = findViewById(R.id.Mytoolbar);
        titulo_edt_pd = findViewById(R.id.titulo_edt_pd);
        descricao_edt_pd = findViewById(R.id.descricao_edt_pd);
        link_img_edt_pd = findViewById(R.id.link_img_edt_pd);
        salvar_produto_click_pd = findViewById(R.id.salvar_produto_click_pd);

        int id_produto = Integer.parseInt((String) getIntent().getExtras().get("id_produto"));

        BancoDeDados bd = Room.databaseBuilder(getApplicationContext(), BancoDeDados.class, "banco").allowMainThreadQueries().build();
        ProdutoEntity valor_produto = bd.getProdutoDao().getProdutoId(id_produto);

        titulo_edt_pd.setText(valor_produto.titulo_produto);
        descricao_edt_pd.setText(valor_produto.descricao_produto);
        link_img_edt_pd.setText(valor_produto.imagem_produto);

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        salvar_produto_click_pd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String titulo = titulo_edt_pd.getText().toString();
                String descricao = descricao_edt_pd.getText().toString();
                String link = link_img_edt_pd.getText().toString();

                if( !titulo.isEmpty()  | !descricao.isEmpty() & !link.isEmpty()){

                    valor_produto.descricao_produto = descricao;
                    valor_produto.titulo_produto = titulo;
                    valor_produto.imagem_produto = link;

                    try{
                        BancoDeDados bd = Room.databaseBuilder(getApplicationContext(), BancoDeDados.class, "banco").allowMainThreadQueries().build();
                        bd.getProdutoDao().updateProduto(valor_produto);

                        finish();
                        Toast.makeText(ProdutoEdit.this, "Salvo", Toast.LENGTH_SHORT).show();

                    }catch (Exception ae){

                        Toast.makeText(ProdutoEdit.this, "Erro ao salvar", Toast.LENGTH_SHORT).show();

                    }

                }

            }
        });


        salvar_produto_click_pd.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                Toast.makeText(ProdutoEdit.this, "Voce segurou o botão", Toast.LENGTH_SHORT).show();

                return false;
            }
        });


    }
}