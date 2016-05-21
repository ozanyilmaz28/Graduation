package adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.graduationteam.graduation.CreateAdvertActivity;
import com.graduationteam.graduation.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.List;

import entities.Advert;
import entities.KeyCodes;
import entities.UserInfo;

/**
 * Created by LA-173 on 20.03.2016.
 */
public class AdvertAdapter extends ArrayAdapter<Advert> {
    private static LayoutInflater inflater = null;
    private Context mContext;
    String url_ = "";
    DisplayImageOptions options;
    ImageLoader imageLoader;

    public AdvertAdapter(Context context, int resource,
                         List<Advert> objects) {

        super(context, resource, objects);
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .considerExifParams(true)
                .showImageOnLoading(R.drawable.iconmainlistnotfoundbigger)
                        //.displayer(new RoundedBitmapDisplayer(15))
                .build();
        mContext = context;
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

        RelativeLayout coloredLayout = (RelativeLayout) myRow.findViewById(R.id.advertListRelativeLayout);
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

        if (UserInfo.SelectedPage == KeyCodes.MainToMyPage) {
            if (adverts.getAdvtIsOpen())
                coloredLayout.setBackgroundColor(Color.parseColor("#A9F5BC"));
            else
                coloredLayout.setBackgroundColor(Color.parseColor("#F78181"));
        }

        if (adverts.getAdvtPrice() > 0)
            txtAdvertPrice.setText(String.valueOf(adverts.getAdvtPrice()));
        else
            txtAdvertPrice.setText("");


        if (!String.valueOf(adverts.getAdvtImageLink()).equals("-") && !String.valueOf(adverts.getAdvtImageLink()).equals("")) {
            url_ = adverts.getAdvtImageLink().toString();
            imageLoader.displayImage(url_, imgButton, options);
        }

        myRow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, CreateAdvertActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("AdvertWithDetail", adverts);
                mContext.startActivity(i);
            }
        });

        return myRow;
    }
}
