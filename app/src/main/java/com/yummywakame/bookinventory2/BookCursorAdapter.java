package com.yummywakame.bookinventory2;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.yummywakame.bookinventory2.data.BookContract.BookEntry;

/**
 * {@link BookCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of book data as its data source. This adapter knows
 * how to create list items for each row of book data in the {@link Cursor}.
 */
public class BookCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link BookCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public BookCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        //Return the list item view.
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the book data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current book can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView nameTextView = view.findViewById(R.id.name);
        TextView weightTextView = view.findViewById(R.id.weight);

        // Extract properties from cursor
        String bookName = cursor.getString(cursor.getColumnIndexOrThrow(BookEntry.COLUMN_BOOK_TITLE));
        int bookWeight = cursor.getInt(cursor.getColumnIndexOrThrow(BookEntry.COLUMN_BOOK_WEIGHT));

        // Populate fields with extracted properties
        nameTextView.setText(bookName);
        // If the book author is empty string or null, then use some default text
        // that says "Unknown author", so the TextView isn't blank.
        if (bookWeight == 0) {
            weightTextView.setText(context.getString(R.string.unknown_weight));
        } else {
            weightTextView.setText(String.valueOf(bookWeight));
        }
    }
}
