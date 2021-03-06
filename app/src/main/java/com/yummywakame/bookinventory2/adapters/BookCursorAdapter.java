package com.yummywakame.bookinventory2.adapters;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.yummywakame.bookinventory2.HelperClass;
import com.yummywakame.bookinventory2.R;
import com.yummywakame.bookinventory2.data.BookContract;
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
     * list item layout. For example, the title for the current book can be set on the title TextView
     * in the list item layout.
     *
     * @param view          Existing view, returned earlier by newView() method
     * @param context       app context
     * @param cursorData    The cursor from which to get the data. The cursor is already moved to the
     *                      correct row.
     */
    @Override
    public void bindView(View view, final Context context, final Cursor cursorData) {
        // Find saleButton
        Button saleButton = view.findViewById(R.id.sale_button);

        // Find fields to populate in inflated template
        TextView titleTextView = view.findViewById(R.id.title);
        TextView authorTextView = view.findViewById(R.id.author);
        TextView quantityTextView = view.findViewById(R.id.quantity);
        TextView priceTextView = view.findViewById(R.id.price);

        // Extract properties from cursor
        final String bookTitle = cursorData.getString(cursorData.getColumnIndexOrThrow(BookEntry.COLUMN_BOOK_TITLE));
        final String bookAuthor = cursorData.getString(cursorData.getColumnIndexOrThrow(BookEntry.COLUMN_BOOK_AUTHOR));
        final int bookQuantity = cursorData.getInt(cursorData.getColumnIndexOrThrow(BookEntry.COLUMN_BOOK_QUANTITY));
        final double bookPrice = cursorData.getDouble(cursorData.getColumnIndexOrThrow(BookEntry.COLUMN_BOOK_PRICE));

        // Populate fields with extracted properties
        titleTextView.setText(bookTitle);
        authorTextView.setText(bookAuthor);

        // If the book quantity is empty string or null, then set it to zero.
        if (bookQuantity == 0) {
            quantityTextView.setText(context.getString(R.string.unknown_quantity));
            // Set colour of sale button and quantity text view to disabled
            saleButton.setBackgroundResource(R.drawable.sale_button_disabled);
            quantityTextView.setBackgroundResource(R.drawable.rounded_corners_disabled);
            // Disable button click
            saleButton.setEnabled(false);
        } else {
            quantityTextView.setText(String.valueOf(bookQuantity));
            // Set colour of sale button and quantity text view to disabled
            saleButton.setBackgroundResource(R.drawable.sale_button_states);
            quantityTextView.setBackgroundResource(R.drawable.rounded_corners);
            // Enable button click
            saleButton.setEnabled(true);
        }

        // If the book price is empty string or null, then set it to zero.
        if (bookPrice == 0) {
            priceTextView.setText(context.getString(R.string.unknown_price));
        } else {
            priceTextView.setText(String.valueOf(HelperClass.formatPrice(context, bookPrice, false, false)));
        }

        // OnClickListener for Sale button
        // When clicked it reduces the number in stock by 1.
        final String id = cursorData.getString(cursorData.getColumnIndex(BookContract.BookEntry._ID));

        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bookQuantity > 0) {
                    Uri currentBookUri = ContentUris.withAppendedId(BookContract.BookEntry.CONTENT_URI, Long.parseLong(id));
                    ContentValues values = new ContentValues();
                    values.put(BookEntry.COLUMN_BOOK_QUANTITY, bookQuantity - 1);
                    context.getContentResolver().update(currentBookUri, values, null, null);
                    swapCursor(cursorData);
                    // Check if out of stock to display toast
                    if (bookQuantity == 1) {
                        Toast.makeText(context, R.string.toast_out_of_stock, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

}
