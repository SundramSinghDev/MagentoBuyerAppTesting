/*
 *
 *   Copyright/* *
 *             * CedCommerce
 *             *
 *             * NOTICE OF LICENSE
 *             *
 *             * This source file is subject to the End User License Agreement (EULA)
 *             * that is bundled with this package in the file LICENSE.txt.
 *             * It is also available through the world-wide-web at this URL:
 *             * http://cedcommerce.com/license-agreement.txt
 *             *
 *             * @category  Ced
 *             * @package   MultiVendor
 *             * @author    CedCommerce Core Team <connect@cedcommerce.com >
 *             * @copyright Copyright CEDCOMMERCE (http://cedcommerce.com/)
 *             * @license   http://cedcommerce.com/license-agreement.txt
 *
 *
 *
 */

package com.magentotwo.magenativeapp.cad_magenative_activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.magentotwo.magenativeapp.R;
import com.bumptech.glide.Glide;
import com.magentotwo.magenativeapp.databinding.MagenativeBannerLayoutBinding;
import com.magentotwo.magenativeapp.ui.productsection.activity.Ced_NewProductView;
import com.magentotwo.magenativeapp.ui.productsection.activity.Ced_New_Product_Listing;
import com.magentotwo.magenativeapp.ui.websection.Ced_Weblink;

import java.util.ArrayList;
import java.util.HashMap;

public class Ced_bannersAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<HashMap<String, String>> data;
    private LayoutInflater inflater;
    Activity activity;

    public Ced_bannersAdapter(Context context, ArrayList<HashMap<String, String>> img) {
        this.context = context;
        data = img;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final View container, final int position) {

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        MagenativeBannerLayoutBinding bannerLayoutBinding = DataBindingUtil.inflate(inflater,R.layout.magenative_banner_layout, (ViewGroup) container, false);
        final ImageView bannerimage =bannerLayoutBinding.MageNativeBannerimage;
        final TextView banner_link_to = bannerLayoutBinding.linkTo;
        final TextView banner_id = bannerLayoutBinding.id;

        banner_link_to.setText(data.get(position).get("link_to"));
        banner_id.setText(data.get(position).get("id"));
        Glide.with(context)
                .load(data.get(position).get("banner_image"))

                .placeholder(R.drawable.bannerplaceholder)
                .error(R.drawable.bannerplaceholder)
                .into(bannerimage);
        bannerimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (banner_link_to.getText().toString().equals("category")) {
                    Intent intent = new Intent(context, Ced_New_Product_Listing.class);
                    intent.putExtra("ID", banner_id.getText().toString());
                    context.startActivity(intent);

                }
                if (banner_link_to.getText().toString().equals("product")) {
                    Intent prod_link = new Intent(context, Ced_NewProductView.class);
                    prod_link.putExtra("product_id", banner_id.getText().toString());
                    context.startActivity(prod_link);

                }
                if (banner_link_to.getText().toString().equals("website")) {
                    Intent weblink = new Intent(context, Ced_Weblink.class);
                    weblink.putExtra("link", banner_id.getText().toString());
                    context.startActivity(weblink);

                }
            }
        });
        return bannerLayoutBinding.getRoot();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((LinearLayout) object);
    }

    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ((ViewPager) container).removeView((LinearLayout) object);
    }
}
