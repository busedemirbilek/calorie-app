package list;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.pr_kalorapp.R;

import java.util.List;

import kaloriler.Kalori;

public class listadepter extends BaseAdapter {
    private  List<Kalori> list;
    private  LayoutInflater layout;

    public listadepter(Activity activity, List<Kalori> list){
        this.list=list;
        layout=(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View nesne;
        nesne=layout.inflate(R.layout.colum,null);
        TextView t1=(TextView)nesne.findViewById(R.id.col1);
        TextView t2=(TextView)nesne.findViewById(R.id.col2);
        TextView t3=(TextView)nesne.findViewById(R.id.col3);
        TextView t4=(TextView)nesne.findViewById(R.id.col4);
        Kalori k=list.get(i);
        t1.setText(k.urunad+"");
        t2.setText(k.miktar+"");
        t3.setText(k.kalori+"");
        t4.setText(k.toplam+"");
        return nesne;
    }
}
