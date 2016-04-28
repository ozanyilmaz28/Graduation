package adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.graduationteam.graduation.AdvertListActivity;
import com.graduationteam.graduation.CategoryListActivity;
import com.graduationteam.graduation.R;

import entities.UserInfo;

/**
 * Created by LA-173 on 20.03.2016.
 */
public class CategoryAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    String[] result;
    Context context;
    int[] imageId;
    String[] subCategories;
    String subText;
    Intent intent_;

    public CategoryAdapter(CategoryListActivity mainActivity, String[] prgmNameList, int[] prgmImages) {
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
        rowView = inflater.inflate(R.layout.custom_category_list, null);
        holder.tvMain = (TextView) rowView.findViewById(R.id.textViewMain);
        holder.tvSub = (TextView) rowView.findViewById(R.id.textViewSub);
        holder.imageMain = (ImageView) rowView.findViewById(R.id.imageViewMain);
        holder.tvMain.setText(result[position]);
        subText = "";
        if (position < 1 || position == 5)
            subText = "";
        if (position == 1) {
            subCategories = context.getResources().getStringArray(R.array.SubCategories2);
            for (int i = 1; i < subCategories.length; i++)
                subText += subCategories[i] + "\n";
        }
        if (position == 2) {
            subCategories = context.getResources().getStringArray(R.array.SubCategories3);
            for (int i = 1; i < subCategories.length; i++)
                subText += subCategories[i] + "\n";
        }
        if (position == 3) {
            subCategories = context.getResources().getStringArray(R.array.SubCategories4);
            for (int i = 1; i < subCategories.length; i++)
                subText += subCategories[i] + "\n";
        }
        if (position == 4) {
            subCategories = context.getResources().getStringArray(R.array.SubCategories5);
            for (int i = 1; i < subCategories.length; i++)
                subText += subCategories[i] + "\n";
        }
        holder.tvSub.setText(subText.trim());
        holder.imageMain.setImageResource(imageId[position]);
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent_ = new Intent(context, AdvertListActivity.class);
                UserInfo.SelectedMainCategory = position + 1;
                context.startActivity(intent_);
            }
        });
        return rowView;
    }

    public class Holder {
        TextView tvMain;
        TextView tvSub;
        ImageView imageMain;
    }

}
