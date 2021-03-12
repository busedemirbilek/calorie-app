package com.example.pr_kalorapp;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import kaloriler.Kalori;
import list.listadepter;
import veritabani.DB;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemClickListener {
    Intent frm;
    static   DB katman;
    static  SQLiteDatabase db;
    static   Spinner  curun,gunurun,cins;
    ListView list;
    EditText miktar;
    TextView genelt,topkalori;
    List<Kalori> liste;
    static ArrayList<String> clist,gunlist,cinsiyet;
    static  ArrayList<Integer> urunid,id,gunurun_id;
    static ArrayAdapter<String> cadp,gunadp,cinsadp;
    public  Button btn;
    Context contex;
    int[] btnid={R.id.btnekle,R.id.btnurun,R.id.btn_kalori};
    int[] text={R.id.tboy,R.id.tyas,R.id.tkilo};
    EditText[] jtext;
    EditText jtext_gun;
    double[] degerler;
    listadepter adp;
    AlertDialog.Builder dialog;
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        katman=new DB(this);
        contex=this;
        jtext=new EditText[text.length];
        degerler=new double[jtext.length];
        topkalori=(TextView)findViewById(R.id.genelkalori);
        for(int i=0;i<btnid.length;i++)
        {
            btn=(Button)findViewById(btnid[i]);
            btn.setOnClickListener(this);
        }
        for(int i=0;i< text.length;i++)
        {
            jtext[i]=(EditText)findViewById(text[i]);
            jtext[i].setText(i+1+"");
        }

        miktar=(EditText)findViewById(R.id.tmiktar);
        list=(ListView)findViewById(R.id.lis);
        curun=(Spinner)findViewById(R.id.curun);
        urunid=new ArrayList<Integer>();
        id=new ArrayList<Integer>();
        clist=new ArrayList<String>();
        cins=(Spinner)findViewById(R.id.cins);
        cinsiyet=new ArrayList<String>();
        cinsadp=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,cinsiyet);
        cins.setAdapter(cinsadp);
        cinsadp.add("Erkek");
        cinsadp.add("Kadın");

        genelt=(TextView)findViewById(R.id.toplam);
        cadp=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,clist);
        liste=new ArrayList<Kalori>();
        adp=new listadepter(this,liste);
        list.setAdapter(adp);
        curun.setAdapter(cadp);
        spinnerdoldur(contex);
        datagetir();
        toplam();
        list.setOnItemClickListener(this);
    }
    public void toplam(){
        double gtoplam=0.0;
        for(int i=0;i<adp.getCount();i++)
        {
            Kalori k=liste.get(i);
            gtoplam+=k.toplam;
        }
        gtoplam=virgul(gtoplam);
        genelt.setText("Toplam="+gtoplam);
    }
    public static void baglan(){
        db=katman.getWritableDatabase();
    }
    public boolean kontrolet()
    {
        int sayac=0;
        for(int i=0;i<jtext.length;i++) {
            if (jtext[i].getText().length() > 0) {
                try {
                    degerler[i] =Double.parseDouble(jtext[i].getText()+"");
                } catch (Exception ex) {
                    Toast.makeText(this,"Lütfen değerleri doğru giriniz!!!",Toast.LENGTH_LONG).show();
                    break;
                }
                sayac++;
            }
            else
                break;
        }
        if(sayac==jtext.length)
            return true;
        return false;
    }
    public void ekle()
    {
        try {
            baglan();
            long secilen=curun.getSelectedItemId();
            int id=urunid.get((int)secilen);
            if(secilen>-1&&miktar.getText().length()>0&&kontrolet()){
                double kmiktar=Double.parseDouble(miktar.getText()+"");
                ContentValues par=new ContentValues();
                par.put("Urunid",id);
                par.put("Miktar",kmiktar);
                db.insertOrThrow("Kalori",null,par);
                katman.close();
                datagetir();

            }
        }catch(Exception ex){
            Toast.makeText(this,ex.getMessage(),Toast.LENGTH_LONG).show();

        }
    }

    public void sil(int id)
    {
        try {
            baglan();
            db.delete("Kalori","id=?",new String[]{id+""});
            katman.kapat();
            datagetir();
        }catch(Exception ex){
            Toast.makeText(this,ex.getMessage(),Toast.LENGTH_LONG).show();

        }

    }
    public void duzenle(int id,double miktar,int urun_id)
    {
        try {
            baglan();
            ContentValues par=new ContentValues();
            par.put("Urunid",urun_id);
            par.put("Miktar",miktar);
            db.update("Kalori",par,"id=?",new String[]{id+""});
            katman.kapat();
            datagetir();
        }catch(Exception ex){
            Toast.makeText(this,ex.getMessage(),Toast.LENGTH_LONG).show();

        }
    }

    public static void spinnerdoldur(Context context)
    {
        try{
            baglan();
            Cursor c=db.query("Urun",new String[]{"id","Urunad"},null,null,null,null,null);
            clist.clear();
            cadp.notifyDataSetChanged();
            urunid.clear();
            c.moveToPosition(-1);
            while(c.moveToNext())
            {
                cadp.add(c.getString(1));
                urunid.add(c.getInt(0));

            }
            c.close();
            katman.close();
        }catch(Exception ex){
            Toast.makeText(context,ex.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
    public void datagetir(){
        try {
            baglan();
            Cursor isaretci=db.rawQuery("select k.id,u.Urunad,k.Miktar,u.Kalori,(u.Kalori*k.Miktar) from Kalori k inner join Urun u on u.id=k.Urunid ",null);
            liste.clear();
            adp.notifyDataSetChanged();
            id.clear();
            isaretci.moveToPosition(-1);
            while(isaretci.moveToNext())
            {
                id.add(isaretci.getInt(0));
                Kalori k=new Kalori();
                k.urunad=isaretci.getString(1);
                k.miktar=isaretci.getDouble(2);
                k.kalori=isaretci.getDouble(3);
                k.toplam=virgul(isaretci.getDouble(4));
                liste.add(k);
            }
            isaretci.close();
            katman.close();
            toplam();
        }catch(Exception ex){
            Toast.makeText(this,ex.getMessage(),Toast.LENGTH_LONG).show();
        }

    }
    public void frm2()
    {
        frm=new Intent(this, Main3Activity.class);
        startActivity(frm);

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnekle:ekle();break;
            case R.id.btnurun:frm2();break;
            case R.id.btn_kalori:kalorihesapla();break;
        }
    }
    public double virgul(double sayi)
    {
        int sayi2=(int)(sayi*100);
        sayi=sayi2/100.0;
        return sayi;
    }
    public void kalorihesapla()
    {
        if(kontrolet())
        {
            double[] erkek= {66,6.8,13.7,5};
            double[] kadin= {655,4.7,9.6,1.8};
            double sonuc=0.0;
            double[] oranlar=kadin;
            double[] degerler=new double[4];
            degerler[0]=1;
            for(int i=0;i<jtext.length;i++)
                degerler[i+1]=Double.parseDouble(jtext[i].getText()+"");

            if(cins.getSelectedItemPosition()==0)//jrd radiobuuton secilimi değilmi
                oranlar=erkek;
            sonuc=(oranlar[0]*degerler[0]+oranlar[1]*degerler[1]+oranlar[2]*degerler[2])-(oranlar[3]*degerler[3]);
            sonuc=virgul(sonuc);
            topkalori.setText("Gün Kalori="+sonuc);
        }else{

            Toast.makeText(contex, "Bilgileri doğru giriniz!", Toast.LENGTH_LONG).show();
        }

    }
    int secileni;
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        secileni = i;
        jtext_gun = new EditText(this);
        gunlist = new ArrayList<String>();
        for (int a = 0; a < cadp.getCount(); a++)
            gunlist.add(cadp.getItem(a));
        gunadp = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, gunlist);
        gunurun = new Spinner(this);
        gunurun.setAdapter(gunadp);
        gunurun_id = new ArrayList<Integer>();
        for (int a = 0; a < urunid.size(); a++)
            gunurun_id.add(urunid.get(a));
        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        dialog = new AlertDialog.Builder(this);
        layout.addView(gunurun);
        layout.addView(jtext_gun);
        dialog.setView(layout);
        Kalori k = liste.get(i);
        jtext_gun.setText(k.miktar + "");
        dialog.setCancelable(true);
        dialog.setTitle("Sil veya Düzenle");
        dialog.setPositiveButton("Sil", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sil(id.get(secileni));
            }
        });
        dialog.setNeutralButton("Düzenle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (secileni > -1 && jtext_gun.getText().length() > 0) {
                    try {
                        double miktar = Double.parseDouble(jtext_gun.getText() + "");
                        int urun_id = gunurun_id.get(gunurun.getSelectedItemPosition());
                        duzenle(id.get(secileni), miktar, urun_id);
                    } catch (Exception ex) {
                        Toast.makeText(contex, "Bilgileri doğru giriniz!", Toast.LENGTH_LONG).show();
                    }

                } else
                    Toast.makeText(contex, "Bilgileri doğru giriniz!", Toast.LENGTH_LONG).show();
            }
        });
        dialog.create().show();
    }
}
