//Matric No: S1315451
package mpdproject.gcu.me.org.mobileplatformdevelopmentcoursework;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback
{
    private TextView itemTitle;
    private TextView itemDescription;
    private TextView itemLink;
    private TextView itemStDate;
    private TextView itemEdDate;

    private GoogleMap gMap;
    DisplayItem item;
    LatLng latlong;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        itemTitle = (TextView)findViewById(R.id.itemTitle);
        itemDescription = (TextView)findViewById(R.id.itemDescription);
        itemLink = (TextView)findViewById(R.id.itemLink);
        itemStDate = (TextView)findViewById(R.id.itemStartDate);
        itemEdDate = (TextView)findViewById(R.id.itemEndDate);
        item = (DisplayItem) getIntent().getSerializableExtra("DisplayItem");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if(item != null)
        {
            latlong = new LatLng(item.getLatitude(), item.getLongitude());
            itemTitle.setText(item.title);
            itemLink.setText(item.link);

            if(item.startDate != null)
            {
                itemStDate.setText(item.startDate + "  -");

                if(item.endDate != null)
                {
                    itemEdDate.setText(item.endDate);
                }
            }

            if(item.getWorks() != null)
            {
                itemDescription.setText("Works: " + item.getWorks());

                if(item.getManagement() != null)
                {
                    itemDescription.setText("Works: " + item.getWorks() + "\n" + "Traffic Management: " + item.getManagement());

                    if(item.getDiversion() != null)
                    {
                        itemDescription.setText("Works: " + item.getWorks() + "\n" + "Traffic Management: " + item.getManagement() + "\n" + "Diversion: " + item.getDiversion());
                    }
                }
            }
            else
            {
                itemDescription.setText("Description: " + item.description);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        gMap = googleMap;
        LatLng pos = latlong;
        gMap.addMarker(new MarkerOptions().position(pos).title(item.title));
        gMap.moveCamera(CameraUpdateFactory.newLatLng(pos));
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos,12.5f));
    }

}
