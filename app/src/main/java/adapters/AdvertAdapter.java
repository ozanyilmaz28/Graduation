package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.graduationteam.graduation.R;

import java.util.List;

import entities.Advert;

/**
 * Created by LA-173 on 20.03.2016.
 */
public class AdvertAdapter extends ArrayAdapter<Advert> {
    private static LayoutInflater inflater = null;
    Context context;

    public AdvertAdapter(Context context, int resource,
                         List<Advert> objects) {
        super(context, resource, objects);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Advert adverts = getItem(position);
        View myRow = null;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            myRow = inflater.inflate(R.layout.custom_advert_list, null);
        } else {
            myRow = convertView;
        }

        TextView txtAdvertHeader = (TextView) myRow.findViewById(R.id.txtAdvertListHeader);
        TextView txtAdvertDate = (TextView) myRow.findViewById(R.id.txtAdvertListDate);
        TextView txtAdvertCategoryCode = (TextView) myRow.findViewById(R.id.txtAdvertListCategory);
        TextView txtAdvertPrice = (TextView) myRow.findViewById(R.id.txtAdvertListPrice);
        ImageView imgButton = (ImageView) myRow.findViewById(R.id.imgAdvertList);

        txtAdvertHeader.setText(adverts.getAdvtDescription().substring(0, 29) + "...");
        txtAdvertDate.setText(adverts.getAdvtDateTime());
        txtAdvertCategoryCode.setText(adverts.getAdvtCategoryCode());
        txtAdvertPrice.setText(String.valueOf(adverts.getAdvtPrice()) + "\nTL");

        return myRow;
    }
}
