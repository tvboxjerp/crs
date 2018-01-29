package crstv.app.com.crstv;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class Adaptador extends BaseAdapter {

    private Context context;
    private List<Datos> list;



    public Adaptador(Context context, List<Datos> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.griditem, null);

            //TextView group = (TextView) convertView.findViewById(R.id.txtGroup);
            ImageView image = (ImageView) convertView.findViewById(R.id.imgChannel);
            TextView name = (TextView) convertView.findViewById(R.id.txtName);
            Picasso.with(convertView.getContext()).load(list.get(position).getLogo().toString()).error(R.drawable.noimage).fit().centerInside().into(image);
            name.setText(list.get(position).getName().toString());

            final View finalConvertView = convertView;
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Datos objDatos = (Datos) list.get(position);
                    Intent step = new Intent(finalConvertView.getContext(), DetailActivity.class);
                    step.putExtra("datos", objDatos);
                    context.startActivity(step);

                }
            });

        return convertView;
    }


}
