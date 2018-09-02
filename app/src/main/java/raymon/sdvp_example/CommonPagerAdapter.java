package raymon.sdvp_example;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CommonPagerAdapter extends PagerAdapter {

    Context mContext;

    int[] mColorArray = {R.color.colorRed, R.color.colorGreen, R.color.colorBlue, R.color.colorCyan, R.color.colorPink};

    public CommonPagerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return 10;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View vChild = LayoutInflater.from(mContext).inflate(R.layout.pager_item, container, false);
        TextView tvTitle = vChild.findViewById(R.id.tvTitle);
        tvTitle.setText(String.format("Position : %s", position));
        vChild.setBackgroundResource(mColorArray[position % mColorArray.length]);
        container.addView(vChild);
        return vChild;
}

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
