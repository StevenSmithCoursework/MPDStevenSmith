//Matric No: S1315451
package mpdproject.gcu.me.org.mobileplatformdevelopmentcoursework;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filterable;
import android.widget.Filter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ListItem extends BaseAdapter implements Filterable
{
    List<DisplayItem> listOfItems;
    List<DisplayItem> searchList;
    Context context;
    int listNo;
    ValueFilter filter;

    public ListItem(Context context, int listNo, List<DisplayItem> listOfItems)
    {
        this.context = context;
        this.listNo = listNo;
        this.listOfItems = listOfItems;
        searchList = listOfItems;
    }

    @Override
    public View getView(int pos, View viewType, ViewGroup parentView)
    {
        View view = View.inflate(context, listNo, null);
        TextView titleText = (TextView)view.findViewById(R.id.itemTitle);
        TextView startDate = (TextView)view.findViewById(R.id.itemLink);
        TextView days = (TextView)view.findViewById(R.id.itemDays);
        TextView description = (TextView)view.findViewById(R.id.itemDescription);
        titleText.setText(listOfItems.get(pos).title);
        long numDays = listOfItems.get(pos).getTimeBetweenDates();

        if(listOfItems.get(pos).startDate != null)
        {
            startDate.setText("Start date: " + listOfItems.get(pos).startDate);
        }

        if(numDays > 0)
        {
            if(numDays > 1 && numDays < 8)
            {
                days.setText(Long.toString(numDays) + " Days ");
                days.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.greencircle, 0);
            }
            else if(numDays > 15)
            {
                days.setText(Long.toString(numDays) + " Days ");
                days.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.yellowcircle, 0);
            }
            else
            {
                days.setText(Long.toString(numDays) + " Day ");
                days.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.greencircle, 0);
            }
        }
        else
        {
            days.setText("~1 Day ");
            days.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.greencircle, 0);
        }

        //Change item description to aid readability
        if(listOfItems.get(pos).getWorks() != null)
        {
            description.setText("Work Type: " + listOfItems.get(pos).getWorks());

            if(listOfItems.get(pos).getManagement() != null)
            {
                description.setText("Work Type: " + listOfItems.get(pos).getWorks() + "\n" + "Traffic Management: " + listOfItems.get(pos).getManagement());

                if(listOfItems.get(pos).getDiversion() != null)
                {
                    description.setText("Work Type: " + listOfItems.get(pos).getWorks() + "\n" + "Traffic Management: " + listOfItems.get(pos).getManagement() + "\n" + "Diversion: " + listOfItems.get(pos).getDiversion());
                }
            }
        }
        else
        {
            description.setText(listOfItems.get(pos).description);
        }
        view.setTag(listOfItems.get(pos));

        return view;
    }

    public Filter getFilter()
    {
        if(filter == null)
        {
            filter = new ValueFilter();
        }
        return filter;
    }

    @Override
    public int getCount()
    {
        return listOfItems.size();
    }

    @Override
    public DisplayItem getItem(int pos)
    {
        return listOfItems.get(pos);
    }

    @Override
    public long getItemId(int pos)
    {
        return pos;
    }

    public void removeItem(Object item)
    {
        listOfItems.remove(item);
    }

    private class ValueFilter extends Filter
    {
        @Override
        protected FilterResults performFiltering(CharSequence constraint)
        {
            FilterResults results = new FilterResults();
            if(constraint != null && constraint.length() > 0)
            {
                List<DisplayItem> filteredList = new ArrayList<>();

                for(int i = 0; i < searchList.size(); i++)
                {
                    if(searchList.get(i).title.toUpperCase().contains(constraint.toString().toUpperCase()))
                    {
                        DisplayItem item = searchList.get(i);

                        filteredList.add(item);
                    }
                }
                results.count = filteredList.size();
                results.values = filteredList;
            }
            else
            {
                results.count = searchList.size();
                results.values = searchList;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results)
        {
            listOfItems = (ArrayList<DisplayItem>)results.values;
            notifyDataSetChanged();
        }
    }

}

