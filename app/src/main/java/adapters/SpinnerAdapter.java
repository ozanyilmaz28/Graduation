package adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.graduationteam.graduation.CreateAdvertActivity;
import com.graduationteam.graduation.R;

public class SpinnerAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    String[] result;
    Context context;
    int[] imageId;

    public SpinnerAdapter(Activity mainActivity, String[] prgmNameList, int[] prgmImages) {
        // TODO Auto-generated constructor stub
        result = prgmNameList;
        context = mainActivity;
        imageId = prgmImages;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.custom_spinner_createadvert, null);
        holder.tv = (TextView) rowView.findViewById(R.id.textViewSpinner);
        holder.img = (ImageView) rowView.findViewById(R.id.imageViewSpinner);
        holder.tv.setText(result[position]);
        if (imageId.length == 1)
            holder.img.setImageResource(imageId[0]);
        else
            holder.img.setImageResource(imageId[position]);
        return rowView;
    }

    public class Holder {
        TextView tv;
        ImageView img;
    }

}
