package adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.graduationteam.graduation.CreateAdvertActivity;
import com.graduationteam.graduation.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;

import java.util.List;

import entities.Advert;

public class ImageAdapter extends BaseAdapter {
    List<Advert> list_;
    String imageLink;
    Bitmap bitmap_;
    int imgWidth_ = 0;
    int imgHeight_ = 0;

    ImageLoader imageLoader;
    DisplayImageOptions options;
    public Integer[] mThumbIds = {
            R.drawable.iconmainlistnotfoundbigger
    };
    private Context mContext;

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
        } else {
            imageView = (ImageView) convertView;
        }

        if (list_ != null && list_.size() > 0)
            imageLink = list_.get(position).getAdvtImageLink();

        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        if (size.y > size.x) {
            imgWidth_ = ((size.x) * 28 / 100);
            imgHeight_ = (((size.y) - 60) * 14 / 100);
        } else {
            imgWidth_ = ((size.y) * 28 / 100);
            imgHeight_ = (((size.x) - 60) * 14 / 100);
        }

        imageView.setImageResource(mThumbIds[0]);
        imageView.setAdjustViewBounds(true);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        GridView.LayoutParams params = new GridView.LayoutParams(imgWidth_, imgHeight_);
        imageView.setLayoutParams(params);

        if (list_ != null && list_.size() > 0 && !String.valueOf(imageLink).equals("-")) {
            bitmap_ = null;
        } else
            bitmap_ = BitmapFactory.decodeResource(mContext.getResources(),
                    mThumbIds[0]);

        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, CreateAdvertActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("AdvertWithDetail", list_.get(position));
                mContext.startActivity(i);
            }
        });

        if (bitmap_ != null) {
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap_, imgWidth_, imgHeight_, true);
            imageView.setImageBitmap(resizedBitmap);
        } else {
            options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .considerExifParams(true)
                    .showImageOnLoading(R.drawable.iconmainlistnotfoundbigger)
                    .build();
            imageLoader.displayImage(imageLink, imageView, options);
        }
        return imageView;
    }
}
