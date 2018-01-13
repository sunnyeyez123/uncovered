package com.jasminelawrence.uncovered;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Jasmine on 1/12/18.
 */

public class ArticleAdapter extends ArrayAdapter<Article> {

    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the list is the data we want
     * to populate into the lists.
     *
     * @param context  The current context. Used to inflate the layout file.
     * @param Articles A List of article objects to display in a list
     */


    public ArticleAdapter(Activity context, ArrayList<Article> Articles) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, Articles);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Get the {@link AndroidFlavor} object located at this position in the list
        Article currentArticle = getItem(position);

        // Find the TextView in the list_item.xml layout with the ID version_name
        TextView titleTextView = (TextView) listItemView.findViewById(R.id.title);


        titleTextView.setText(currentArticle.getTitle());


        // Find the TextView in the list_item.xml layout with the ID version_name
        TextView authorTextView = (TextView) listItemView.findViewById(R.id.author);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView

        authorTextView.setText(currentArticle.getAuthor());


        // Find the TextView in the list_item.xml layout with the ID version_name
        TextView publishedTextView = (TextView) listItemView.findViewById(R.id.date);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView

        String date = currentArticle.getPublished();

        if (date != null && date.length() > 0) {
            String formattedDate[] = date.split("T");
            publishedTextView.setText(formattedDate[0]);

        } else {
            publishedTextView.setText(date);
        }

        // Find the TextView in the list_item.xml layout with the ID version_number
        TextView sectionTextView = (TextView) listItemView.findViewById(R.id.section);
        // Get the version number from the current AndroidFlavor object and
        // set this text on the number TextView


        sectionTextView.setText(currentArticle.getSection());


        return listItemView;

    }


}
