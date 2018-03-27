//Matric No: S1315451
package mpdproject.gcu.me.org.mobileplatformdevelopmentcoursework;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.xmlpull.v1.XmlPullParser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private Button currentIncidentButton;
    private Button plannedRoadworkButton;

    private enum buttonPressed
    {
        CURRENT_INCIDENTS,
        PLANNED_ROADWORKS
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set-up the main view
        setContentView(R.layout.activity_main);
        //Set-up the buttons and listeners
        currentIncidentButton = (Button)findViewById(R.id.currentIncidents);
        currentIncidentButton.setOnClickListener(this);
        plannedRoadworkButton = (Button)findViewById(R.id.plannedRoadworks);
        plannedRoadworkButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        //Trigger fetchCurrentIncidents() if currentIncidentsButton is pushed
        if(v == currentIncidentButton)
        {
            fetchCurrentIncidents();
        }
        //Trigger fetchPlannedRoadworks() if plannedRoadworksButton is pushed
        else if (v == plannedRoadworkButton)
        {
            fetchPlannedRoadworks();
        }
    }

    public void fetchCurrentIncidents()
    {
        try
        {
            Intent intent = new Intent(getApplicationContext(), MPDXmlParser.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("ButtonPressed", 1);
            startActivity(intent);
        }
        catch (ActivityNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public void fetchPlannedRoadworks()
    {
        try
        {
            Intent intent = new Intent(getApplicationContext(), MPDXmlParser.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("ButtonPressed", 2);
            startActivity(intent);
        }
        catch(ActivityNotFoundException e)
        {
            e.printStackTrace();
        }
    }
}
