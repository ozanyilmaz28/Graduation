package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.graduationteam.graduation.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

import entities.Advert;

/**
 * Created by LA-173 on 20.03.2016.
 */
public class AdvertAdapter extends ArrayAdapter<Advert> {
    private static LayoutInflater inflater = null;
    Context context;
    String url_ = "";

    ImageLoader imageLoader;

    public AdvertAdapter(Context context, int resource,
                         List<Advert> objects) {

        super(context, resource, objects);
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Advert adverts = getItem(position);
        View myRow = convertView;

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
        TextView txtAdvertID = (TextView) myRow.findViewById(R.id.txtAdvertListID);
        TextView txtAdvertPhone = (TextView) myRow.findViewById(R.id.txtAdvertListPhone);
        TextView txtAdvertMail = (TextView) myRow.findViewById(R.id.txtAdvertListMail);
        ImageView imgButton = (ImageView) myRow.findViewById(R.id.imgAdvertList);

        txtAdvertHeader.setText(adverts.getAdvtDescription().substring(0, 19) + "...");
        txtAdvertDate.setText(adverts.getAdvtDateTime().split("T")[0]);
        txtAdvertCategoryCode.setText(adverts.getAdvtCategoryCode());
        txtAdvertID.setText(String.valueOf(adverts.getAdvtID()));
        txtAdvertPhone.setText(adverts.getAdvtPhone());
        txtAdvertMail.setText(adverts.getAdvtMail());

        if (adverts.getAdvtPrice() > 0)
            txtAdvertPrice.setText(String.valueOf(adverts.getAdvtPrice()));
        else
            txtAdvertPrice.setText("");


        if (!String.valueOf(adverts.getAdvtImageLink()).equals("-") && !String.valueOf(adverts.getAdvtImageLink()).equals("")) {
            url_ = adverts.getAdvtImageLink().toString();
            imageLoader.displayImage(url_, imgButton);
        }

        return myRow;
    }
}
