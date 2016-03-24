package adapters;

import android.content.Context;
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
import android.widget.RelativeLayout;

import com.graduationteam.graduation.R;

public class ImageAdapter extends BaseAdapter {
    // Keep all Images in array
    public Integer[] mThumbIds = {
            R.drawable.iconmainlistnotfoundbigger, R.drawable.iconmainlistnotfoundbigger,
            R.drawable.iconmainlistnotfoundbigger, R.drawable.iconmainlistnotfoundbigger,
            R.drawable.iconmainlistnotfoundbigger, R.drawable.iconmainlistnotfoundbigger,
            R.drawable.iconmainlistnotfoundbigger, R.drawable.iconmainlistnotfoundbigger,
            R.drawable.iconmainlistnotfoundbigger, R.drawable.iconmainlistnotfoundbigger,
            R.drawable.iconmainlistnotfoundbigger, R.drawable.iconmainlistnotfoundbigger,
            R.drawable.iconmainlistnotfoundbigger, R.drawable.iconmainlistnotfoundbigger,
            R.drawable.iconmainlistnotfoundbigger
    };
    private Context mContext;

    // Constructor
    public ImageAdapter(Context c) {
        mContext = c;
    }

    @Override
    public int getCount() {
        return mThumbIds.length;
    }

    @Override
    public Object getItem(int position) {
        return mThumbIds[position];
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
        imageView.setImageResource(mThumbIds[position]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setAdjustViewBounds(true);
        imageView.setLayoutParams(new GridView.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT));

        Bitmap m_d = BitmapFactory.decodeResource(mContext.getResources(),
                mThumbIds[position]);

        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int imgWidth_ = ((size.x) * 3 / 10);
        int imgHeight = (((size.y) - 30) * 15 / 100);


        if (m_d != null) {
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(m_d, imgWidth_, imgHeight, true);
            imageView.setImageBitmap(resizedBitmap);
        }
        return imageView;
    }
}
