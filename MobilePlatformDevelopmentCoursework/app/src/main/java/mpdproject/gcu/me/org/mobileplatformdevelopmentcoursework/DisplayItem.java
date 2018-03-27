//Matric No: S1315451
package mpdproject.gcu.me.org.mobileplatformdevelopmentcoursework;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DisplayItem implements Serializable
{
    //Title, description and link storage
    String title;
    String description;
    String link;
    //Strings specific to roadworks only
    String startDate;
    String endDate;
    String works;
    String management;
    String diversion;
    //Latitude + Longitude for Google Maps
    double latitude;
    double longitude;
    //Used to work out the number of days between the start and finish date
    Date stDate;
    Date fnDate;

    public DisplayItem(String title, String description, String link)
    {
        this.title = title;
        this.description = description;
        this.link = link;
    }

    //Work out length of days between two dates
    public long getTimeBetweenDates()
    {
        if(startDate !=null && endDate !=null)
        {
            long diffInMilliseconds = fnDate.getTime() - stDate.getTime();
            return TimeUnit.DAYS.convert(diffInMilliseconds, TimeUnit.MILLISECONDS);
        }
        else
        {
            return 0;
        }
    }

    //Getters
    public String getWorks() {return this.works;}
    public String getManagement() {return this.management;}
    public String getDiversion() {return  this.diversion;}
    public double getLatitude(){return this.latitude;}
    public double getLongitude() {return this.longitude;}

    //Setters
    public void setWorks(String works) {this.works = works;}
    public void setManagement(String management) {this.management = management;}
    public void setDiversion(String diversion) {this.diversion = diversion;}
    public void setLatitude(double latitude) {this.latitude = latitude;}
    public void setLongitude(double longitude){this.longitude = longitude;}
}