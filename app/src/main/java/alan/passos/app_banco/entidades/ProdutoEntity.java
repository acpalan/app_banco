package alan.passos.app_banco.entidades;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "produto")
public class ProdutoEntity {

    @PrimaryKey(autoGenerate = true)
    public int id_Produto;

    @ColumnInfo
    public String titulo_produto;

    @ColumnInfo
    public String descricao_produto;

    @ColumnInfo
    public String descricao_tecnico;

    @ColumnInfo
    public String imagem_produto;

    @ColumnInfo
    public Integer cliente_id;
}
