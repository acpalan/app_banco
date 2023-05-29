package alan.passos.app_banco.entidades;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "user")
public class UserEntity {


    @PrimaryKey(autoGenerate = true)
    public int id_user;
    @ColumnInfo
    public String nome;
    @ColumnInfo
    public int typeUser;
    @ColumnInfo
    public String senha;

}
