package alan.passos.app_banco.utils;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import alan.passos.app_banco.DAO.ProdutoDAO;
import alan.passos.app_banco.DAO.UserDAO;
import alan.passos.app_banco.entidades.ProdutoEntity;
import alan.passos.app_banco.entidades.UserEntity;


@Database(
    entities = {
        UserEntity.class,
        ProdutoEntity.class
    },
    version = 2
)

public abstract class BancoDeDados extends RoomDatabase {

    public abstract UserDAO getUserDao();
    public abstract ProdutoDAO getProdutoDao();

    public static BancoDeDados getInstance(Context context) {
        return Room.databaseBuilder(context, BancoDeDados.class, "banco")
                .addMigrations(MIGRATION_1_2)
                .build();
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE produto ADD COLUMN cliente_id INT");
        }
    };

}
