package veritabani;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class DB extends SQLiteOpenHelper {
    public static   int version=1;
    public static   String name="kaloriler.db";

    public DB(Context c)
    {
        super(c,name,null,version);

    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql="create table Kalori(id integer not null primary key autoincrement,Urunid integer not null,Miktar real not null);" ;
        String sql1="create table Urun(id integer not null primary key autoincrement,Urunad text not null,Kalori real not null);";
        sqLiteDatabase.execSQL(sql);
        sqLiteDatabase.execSQL(sql1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists  Kalori");
        sqLiteDatabase.execSQL("drop table  if exists  Urun");
        onCreate(sqLiteDatabase);
    }
    public void kapat(){
        this.close();

    }

}
