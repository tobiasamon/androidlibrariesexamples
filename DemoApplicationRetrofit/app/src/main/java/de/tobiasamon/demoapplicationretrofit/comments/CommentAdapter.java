package de.tobiasamon.demoapplicationretrofit.comments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import de.tobiasamon.demoapplicationretrofit.R;

/**
 * Created by tobiasamon on 16.11.15.
 */
public class CommentAdapter extends ArrayAdapter<Comment> {

    public CommentAdapter(Context context) {
        super(context, R.layout.comment_item, 0, new ArrayList<Comment>());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Comment comment = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.comment_item, parent, false);
        }

        TextView header = (TextView) convertView.findViewById(R.id.commentHeader);
        TextView body = (TextView) convertView.findViewById(R.id.commentBody);

        header.setText(comment.getName());
        body.setText(comment.getBody());

        return convertView;
    }
}
