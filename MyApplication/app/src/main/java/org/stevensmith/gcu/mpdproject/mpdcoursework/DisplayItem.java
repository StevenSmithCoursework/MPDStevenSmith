package org.stevensmith.gcu.mpdproject.mpdcoursework;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Steven on 24-Mar-18.
 */

public class DisplayItem implements Serializable
{
    //Title, description and link storage
    String title;
    String description;
    String link;
    String publishedDate;
    //Strings specific to roadworks only
    String startDate;
    String endDate;
    String works;
    String management;
    String diversion;
    //Used to work out the number of days between the start and finish date
    Date stDate;
    Date fnDate;

    public DisplayItem(String title, String description, String link, String publishedDate)
    {
        this.title = title;
        this.description = description;
        this.link = link;
        this.publishedDate = publishedDate;
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

    //Setters
    public void setWorks(String works) {this.works = works;}
    public void setManagement(String management) {this.management = management;}
    public void setDiversion(String diversion) {this.diversion = diversion;}
}
