package adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.graduationteam.graduation.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.List;

import entities.Advert;

public class ImageAdapter extends BaseAdapter {
    // Keep all Images in array
    List<Advert> list_;
    String imageLink;
    Bitmap bitmap_;
    ImageLoader imageLoader;

    public Integer[] mThumbIds = {
            R.drawable.iconmainlistnotfoundbigger
    };
    private Context mContext;

    // Constructor
    public ImageAdapter(Context c) {
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(c));
        mContext = c;
    }

    public ImageAdapter(Context c, List<Advert> advertList) {
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(c));
        list_ = advertList;
        mContext = c;
    }

    @Override
    public int getCount() {
        return list_.size();
    }

    @Override
    public Object getItem(int position) {
        return list_.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            //just creating the view if not already present
        } else {
            imageView = (ImageView) convertView;
            //re-using if already here
        }

        if (list_ != null && list_.size() > 0)
            imageLink = list_.get(position).getAdvtImageLink();

        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int imgWidth_ = ((size.x) * 3 / 10);
        int imgHeight = (((size.y) - 60) * 15 / 100);

        imageView.setImageResource(mThumbIds[0]);
        imageView.setAdjustViewBounds(true);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        GridView.LayoutParams params = new GridView.LayoutParams(imgWidth_, imgHeight);
        imageView.setLayoutParams(params);

        if (list_ != null && list_.size() > 0 && !String.valueOf(imageLink).equals("-")) {
            bitmap_ = null;
        } else
            bitmap_ = BitmapFactory.decodeResource(mContext.getResources(),
                    mThumbIds[0]);


        if (bitmap_ != null) {
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap_, imgWidth_, imgHeight, true);
            imageView.setImageBitmap(resizedBitmap);
        } else {
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .considerExifParams(true)
                    .showImageOnLoading(R.drawable.iconmainlistnotfoundbigger)
                    //.displayer(new RoundedBitmapDisplayer(15))
                    .build();
            imageLoader.displayImage(imageLink, imageView, options);
        }
        return imageView;
    }
}
