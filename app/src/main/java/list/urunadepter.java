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

public class urunadepter extends BaseAdapter {
    List<URUN> liste;
    LayoutInflater layout;
    public urunadepter(Activity ac, List<URUN> liste)
    {
        this.liste=liste;
        layout=(LayoutInflater)ac.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public int getCount() {
        return liste.size();
    }

    @Override
    public Object getItem(int i) {
        return liste.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View myview=layout.inflate(R.layout.colum2,null);
        TextView kolon1=(TextView)myview.findViewById(R.id.colum1);
        TextView kolon2=(TextView)myview.findViewById(R.id.colum2);
        URUN u=liste.get(i);
        kolon1.setText(u.urunad);
        kolon2.setText(u.kalori+"");
        return myview;
    }
}
