package com.example.pr_kalorapp;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.zip.Inflater;
import list.URUN;
import list.urunadepter;
import veritabani.DB;

public class Main3Activity extends AppCompatActivity implements View.OnClickListener {
    DB vt;
    SQLiteDatabase db;
    ListView liste;
    urunadepter adp;
    List<URUN> list;
    ArrayList<Integer> id;
    Button btn;
    EditText urun, kalori;
    AlertDialog.Builder dialog;
    Context contex = this;
    int secilenid;
    URUN u;
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        urun = (EditText) findViewById(R.id.turun);
        kalori = (EditText) findViewById(R.id.tkalori);
        btn = (Button) findViewById(R.id.btninsert);
        btn.setOnClickListener(this);
        layout = new LinearLayout(this);
        id = new ArrayList<Integer>();
        vt = new DB(this);
        liste = (ListView) findViewById(R.id.lurun);
        list = new ArrayList<URUN>();
        adp = new urunadepter(this, list);
        liste.setAdapter(adp);
        datagetir();
        final EditText gunurun = new EditText(this);
        final EditText gunkalori = new EditText(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(gunurun);
        layout.addView(gunkalori);
        liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                u = list.get(i);
                secilenid = id.get(i);
                gunurun.setText(u.urunad);
                gunkalori.setText(u.kalori + "");
                dialog = new AlertDialog.Builder(contex);
                dialog.setView(layout);
                dialog.setTitle("Sil veya Düzenle");
                dialog.setCancelable(true);
                dialog.setPositiveButton("Sil", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sil(secilenid);
                    }
                });
                dialog.setNeutralButton("Düzenle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (gunurun.getText().length() > 0 && gunkalori.getText().length() > 0) {
                            double kal = Double.parseDouble(gunkalori.getText() + "");
                            guncelle(secilenid, gunurun.getText() + "", kal);
                        }
                    }
                });
                dialog.create().show();
            }
        });

    }

    public void baglan() {
        db = vt.getWritableDatabase();
    }

    @Override
    public void onClick(View view) {
        try {
            baglan();
            if (urun.getText().length() > 0 && kalori.getText().length() > 0) {
                double kal = Double.parseDouble(kalori.getText() + "");
                ContentValues par = new ContentValues();
                par.put("Urunad", urun.getText() + "");
                par.put("Kalori", kal);
                long durum = db.insertOrThrow("Urun", null, par);
                urun.setText("");
                kalori.setText("");
                vt.kapat();
                datagetir();
                MainActivity.spinnerdoldur(this);

            }
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void sil(int id) {
        try {
            baglan();
            db.delete("Urun", "id=?", new String[]{id + ""});
            datagetir();
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    public void guncelle(int id, String ad, double kalori) {
        try {
            baglan();
            ContentValues par = new ContentValues();
            par.put("Urunad", ad);
            par.put("Kalori", kalori);
            db.update("Urun", par, "id=?", new String[]{id + ""});
            vt.kapat();
            datagetir();
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void datagetir() {
        try {
            baglan();
            String[] col = {"id", "Urunad", "Kalori"};
            Cursor c = db.query("Urun", col, null, null, null, null, null);
            list.clear();
            adp.notifyDataSetChanged();
            id.clear();
            c.moveToPosition(-1);
            while (c.moveToNext()) {
                URUN d = new URUN();
                d.urunad = c.getString(1);
                d.kalori = c.getDouble(2);
                id.add(c.getInt(0));
                list.add(d);
            }
            c.close();
            vt.kapat();
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}