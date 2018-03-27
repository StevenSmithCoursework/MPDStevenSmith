//Matric No: S1315451
package mpdproject.gcu.me.org.mobileplatformdevelopmentcoursework;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MPDXmlParser extends AppCompatActivity
{
    private String URL1 = "https://trafficscotland.org/rss/feeds/currentincidents.aspx";
    private String URL2 = "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    private String result = "";
    private String sURL;

    List<DisplayItem> currentIncidentList = new ArrayList<>();
    List<DisplayItem> plannedRoadWorksList = new ArrayList<>();

    ListItem listItem;
    ListView listView;

    ProgressBar progressBar;
    Toolbar toolbar;
    DatePickerDialog.OnDateSetListener dateSetListener;



    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        listView = (ListView)findViewById(R.id.listView);
        toolbar = (Toolbar)findViewById(R.id.toolbarID);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        Bundle bundle = getIntent().getExtras();
        int buttonPressed = bundle.getInt("ButtonPressed");
        if(buttonPressed == 1)
        {
            startStream(URL1, buttonPressed);
            getSupportActionBar().setTitle("Incidents:");
        }
        else
        {
            startStream(URL2, buttonPressed);
            getSupportActionBar().setTitle("Upcoming Roadworks:");
        }

        dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker picker, int y, int m, int d)
            {
                m = m + 1;

                String dateString = Integer.toString(d) + Integer.toString(m) + Integer.toString(y);

                SimpleDateFormat df = new SimpleDateFormat("ddMyyyy");
                SimpleDateFormat df2 = new SimpleDateFormat("dd MMMM yyyy");

                try
                {
                    Date searchDate = df.parse(dateString);
                    String searchDateString = df2.format(searchDate);

                    List<DisplayItem> newList = new ArrayList<>();

                    for(DisplayItem item: currentIncidentList)
                    {
                      if(item.startDate.equals(searchDateString))
                      {
                          newList.add(item);
                      }
                    }

                    listItem = new ListItem(getApplicationContext(), R.layout.activity_list_item, newList);
                    listView.setAdapter(listItem);
                }
                catch (ParseException e)
                {
                    e.printStackTrace();
                }
            }
        };

        listItem = new ListItem(getApplicationContext(), R.layout.activity_list_item, currentIncidentList);
        listView.setAdapter(listItem);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id)
            {
                DisplayItem item = (DisplayItem)adapter.getItemAtPosition(position);
                OpenMapView(item);
            }
        });
    }

    private void OpenMapView(DisplayItem item)
    {
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        intent.putExtra("DisplayItem", item);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.viewer_menu, menu);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.searchIcon));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query) {return false;}

            @Override
            public boolean onQueryTextChange(String newText)
            {
                listItem.getFilter().filter(newText);

                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.calendarIcon)
        {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dDialog = new DatePickerDialog(
                    this,
                    R.style.Theme_AppCompat_Light_Dialog_MinWidth,
                    dateSetListener,
                    year,month,day);

            dDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

            dDialog.show();
        }
        else if (id == R.id.searchIcon)
        {

        }
        return true;
    }



    public void startStream(String URL, int buttonPressed)
    {
        XMLStream xmlStream = new XMLStream(URL, buttonPressed);
        xmlStream.execute();
    }

    class XMLStream extends AsyncTask<Void, Void, List<DisplayItem>>
    {
        private String aUrl;
        private int aButtonPressed;
        List<DisplayItem> aList;

        public XMLStream(String url, int buttonPressed)
        {
            aUrl = url;
            aButtonPressed = buttonPressed;
        }

        @Override
        protected void onPreExecute()
        {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<DisplayItem> doInBackground(Void... aVoid)
        {
            DisplayItem item = null;
            currentIncidentList = new ArrayList<>();

            URL bgUrl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";

            try
            {
                bgUrl = new URL(aUrl);
                yc = bgUrl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));

                while((inputLine = in.readLine()) != null)
                {
                    result = result + inputLine;
                }

                in.close();

            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            if(result != null)
            {
                try
                {
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser pullparser = factory.newPullParser();
                    pullparser.setInput(new StringReader(result));

                    int eventType = pullparser.getEventType();
                    boolean finished = false;

                    while(eventType != XmlPullParser.END_DOCUMENT && !finished)
                    {
                        switch(eventType)
                        {
                            case XmlPullParser.START_DOCUMENT:
                                break;
                            case XmlPullParser.START_TAG:
                                if(pullparser.getName().equalsIgnoreCase("item"))
                                {
                                    item = new DisplayItem(" ", " ", " ");
                                }
                                else if(item != null)
                                {
                                    if(pullparser.getName().equalsIgnoreCase("title"))
                                    {
                                        item.title = pullparser.nextText().trim();
                                    }
                                    else if (pullparser.getName().equalsIgnoreCase("description"))
                                    {
                                        item.description = pullparser.nextText().trim();
                                        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy");
                                        if (aButtonPressed == 2)
                                        {
                                            String input = item.description;
                                            //Work out the start + end dates
                                            String[] startdate1 = input.split(":");
                                            String[] startdate2 = startdate1[1].split(", ");
                                            String[] startdate3 = startdate2[1].split(" - ");
                                            String startDate = startdate3[0];

                                            String enddate1 = startdate1[3];
                                            String[] enddate2 = enddate1.split(", ");
                                            String[] enddate3 = enddate2[1].split(" - ");
                                            String endDate = enddate3[0];
                                            //Refactor the dates into Date type
                                            Date myStartDate = df.parse(startDate);
                                            Date myEndDate = df.parse(endDate);
                                            //Set dates
                                            item.stDate = myStartDate;
                                            item.fnDate = myEndDate;
                                            item.startDate = df.format(myStartDate);
                                            item.endDate = df.format(myEndDate);
                                            //Set traffic management + diversion information
                                            String trafficman = startdate1[5].replaceAll("Traffic Management", " ");
                                            String diversion = startdate1[6].replaceAll("Diversion Information", " ");
                                            item.setWorks(trafficman);
                                            item.setManagement(diversion);
                                            //Check to see if diversion information is given
                                            if (item.description.contains("Diversion Information"))
                                            {
                                                //If so, get the string and set it
                                                String diversionString = item.description.substring(item.description.indexOf("Diversion Information:"));
                                                diversionString = diversionString.replaceAll("Diversion Information:", " ");
                                                item.setDiversion(diversionString);
                                            }
                                            else
                                            {
                                                item.setDiversion(null);
                                            }
                                        }
                                        else
                                        {
                                            Date current = Calendar.getInstance().getTime();
                                            item.fnDate = current;
                                            item.stDate = current;
                                            item.startDate = df.format(current);
                                            item.endDate = df.format(current);
                                        }
                                    }
                                    else if(pullparser.getName().equalsIgnoreCase("link"))
                                    {
                                        item.link = pullparser.nextText().trim();
                                    }
                                    else if(pullparser.getName().equalsIgnoreCase("point"))
                                    {
                                        String pointString = pullparser.nextText().trim();
                                        String longitude = pointString.substring(pointString.indexOf("-"));
                                        String latitude = pointString.replaceAll(longitude, "");
                                        item.setLatitude(Double.valueOf(latitude));
                                        item.setLongitude(Double.valueOf(longitude));
                                    }
                                }
                                break;
                            case XmlPullParser.END_TAG:
                                if(pullparser.getName().equalsIgnoreCase("item") && item !=null)
                                {
                                    currentIncidentList.add(item);
                                }
                                else if (pullparser.getName().equalsIgnoreCase("channel"))
                                {
                                    finished = true;
                                }
                                break;
                        }
                        eventType = pullparser.next();
                    }
                }
                catch (XmlPullParserException e)
                {
                    e.printStackTrace();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                catch (ParseException e)
                {
                    e.printStackTrace();
                }
            }
            return currentIncidentList;
        }

        @Override
        protected void onPostExecute(List<DisplayItem> result)
        {
            listItem = new ListItem(getApplicationContext(), R.layout.activity_list_item, result);
            listView.setAdapter(listItem);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
