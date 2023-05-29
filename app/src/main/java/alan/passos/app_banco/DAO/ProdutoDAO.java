package alan.passos.app_banco.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import alan.passos.app_banco.entidades.ProdutoEntity;

@Dao
public interface ProdutoDAO {

    @Insert
    void insert(ProdutoEntity produto);

    @Query("SELECT * FROM produto ")
    List<ProdutoEntity> getProdutoAll();

    @Query("SELECT * FROM produto where id_Produto =:id")
    ProdutoEntity getProdutoId(int id);

    @Update()
    void updateProduto(ProdutoEntity produto);

    @Query("DELETE FROM produto WHERE id_Produto =:id")
    void deleteProduto(int id);

    @Query("SELECT * FROM produto WHERE cliente_id =:cliente_id")
    List<ProdutoEntity> getProdutosFromCliente(int cliente_id);
}
