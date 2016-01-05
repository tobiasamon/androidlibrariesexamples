package de.tobiasamon.demoapplicationglide;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        List<String> urls = Arrays.asList(new String[]{
                "https://pixabay.com/static/uploads/photo/2015/11/03/12/58/dog-1020790_640.jpg",
                "https://pixabay.com/static/uploads/photo/2014/03/05/19/23/dog-280332_640.jpg",
                "https://pixabay.com/static/uploads/photo/2014/05/26/13/30/malinois-354527_640.jpg",
                "https://pixabay.com/static/uploads/photo/2015/01/05/07/50/dog-589002_640.jpg",
                "https://pixabay.com/static/uploads/photo/2015/03/26/09/54/pug-690566_640.jpg",
                "https://pixabay.com/static/uploads/photo/2014/03/14/20/13/dog-287420_640.jpg",
                "https://pixabay.com/static/uploads/photo/2015/08/30/23/07/animals-914811_640.jpg",
                "https://pixabay.com/static/uploads/photo/2014/05/26/13/27/swiss-shepherd-dog-354526_640.jpg"
        });
        ImageArrayAdapter adapter = new ImageArrayAdapter(getApplicationContext(), urls);
        listView.setAdapter(adapter);
    }

    class ImageArrayAdapter extends ArrayAdapter<String> {

        private List<String> urls;

        ImageArrayAdapter(Context context, List<String> items) {
            super(context, R.layout.image_layout, 0, items);
            urls = items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String url = urls.get(position);

            final ImageView imageView;

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.image_layout, parent, false);
            }
            imageView = (ImageView) convertView.findViewById(R.id.imageViewItem);

            Glide.with(getContext()).load(url).centerCrop().placeholder(R.drawable.ic_sync_black_24dp).crossFade().into(imageView);

            return convertView;
        }
    }
}
