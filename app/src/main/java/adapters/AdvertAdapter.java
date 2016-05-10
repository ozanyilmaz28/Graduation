package adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.graduationteam.graduation.R;

import java.io.InputStream;
import java.util.List;

import entities.Advert;

/**
 * Created by LA-173 on 20.03.2016.
 */
public class AdvertAdapter extends ArrayAdapter<Advert> {
    private static LayoutInflater inflater = null;
    Context context;
    String url_ = "";

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

        if (!String.valueOf(adverts.getAdvtImageLink()).equals("-")) {
            url_ = adverts.getAdvtImageLink().replace("\\", "/");
            if (!(adverts.getAdvtImageLink().indexOf("http") > -1))
                url_ = "http://" + url_;
            new DownloadImageTask((imgButton)).execute(url_);
        }
        return myRow;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            //bmImage.setBackgroundResource(0);
            bmImage.setImageBitmap(result);
        }
    }
}
