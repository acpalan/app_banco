package alan.passos.app_banco.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import alan.passos.app_banco.entidades.UserEntity;

@Dao
public interface UserDAO {

    @Insert
    public void insert(UserEntity userEntity);

    @Query("SELECT * FROM user WHERE nome=:user AND senha=:senha")
    UserEntity getUser(String user, String senha);

    @Query("SELECT typeUser FROM user WHERE id_user =:id ")
    int getTypeUser(int id);

    @Query("SELECT * FROM user WHERE id_user =:id ")
    UserEntity getUserById(int id);

    @Query("UPDATE user SET senha = :novaSenha WHERE id_user = :id")
    void updatePassword(int id, String novaSenha);

    @Query("SELECT * FROM user WHERE typeUser = 2")
    List<UserEntity> getAllClients();
}
