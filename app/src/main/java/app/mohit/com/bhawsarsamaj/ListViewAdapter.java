package app.mohit.com.bhawsarsamaj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;

import app.mohit.com.bhawsarsamaj.model.User;
import app.mohit.com.bhawsarsamaj.util.ServerUtil;

public class ListViewAdapter extends BaseAdapter {

	// Declare Variables
	Context mContext;
	LayoutInflater inflater;
	private List<User> userlist = null;
	private ArrayList<User> arraylist;

	public ListViewAdapter(Context context,
			List<User> worldpopulationlist) {
		mContext = context;
		this.userlist = worldpopulationlist;
		inflater = LayoutInflater.from(mContext);
		this.arraylist = new ArrayList<User>();
		this.arraylist.addAll(worldpopulationlist);
	}

	public class ViewHolder {
		TextView rank;
		TextView country;
		TextView population;
		ImageView flag;
	}

	@Override
	public int getCount() {
		return userlist.size();
	}

	@Override
	public User getItem(int position) {
		return userlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.listview_item, null);
			// Locate the TextViews in listview_item.xml
			holder.rank = (TextView) view.findViewById(R.id.rank);
			holder.country = (TextView) view.findViewById(R.id.country);
			holder.population = (TextView) view.findViewById(R.id.population);
			// Locate the ImageView in listview_item.xml
			holder.flag = (ImageView) view.findViewById(R.id.flag);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		// Set the results into TextViews
		holder.rank.setText(userlist.get(position).getUsername());
		holder.country.setText(userlist.get(position).getContactno());
		holder.population.setText(userlist.get(position)
				.getAddress());
		// Set the results into ImageView
	 //    holder.flag.setImageResource(userlist.get(position).getFlag());

	/*	HashMap map = new HashMap();
		map.put("method","downloadImage");
		map.put("imageid",userlist.get(position).getProfileid());

		Long start = System.currentTimeMillis();
		Bitmap bitmap =  ServerUtil.getImageFromServer(map);

			Long end = System.currentTimeMillis();

		Log.e("start #########",    ((end-start)/1000)+ " sec.");

*/

		holder.flag.setImageBitmap(userlist.get(position).getBitmap());

		// Listen for ListView Item Click
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// Send single item click data to SingleItemView Class
				Intent intent = new Intent(mContext, SingleItemView.class);
				// Pass all data rank
				intent.putExtra("name",
						(userlist.get(position).getUsername()));
				// Pass all data country
				intent.putExtra("contactno",
						(userlist.get(position).getContactno()));
				// Pass all data population
				intent.putExtra("address",
						(userlist.get(position).getAddress()));
				// Pass all data flag
	 			intent.putExtra("flag",(userlist.get(position).getProfileid()));
				// Start SingleItemView Class
				mContext.startActivity(intent);
			}
		});

		return view;
	}

	// Filter Class
	public void filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		userlist.clear();
		if (charText.length() == 0) {
			userlist.addAll(arraylist);
		} else {
			for (User wp : arraylist) {
				if (wp.getUsername().toLowerCase(Locale.getDefault())
						.contains(charText)) {
					userlist.add(wp);
				}
			}
		}
		notifyDataSetChanged();
	}

}
