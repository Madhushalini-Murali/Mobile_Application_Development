package com.example.androidnotes;

import android.util.JsonWriter;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;

public class AndroidNotes implements Serializable
{
    private String title;
    private String noteText;
    private String dateTime;
    public String getNote_Title()
    {
        return title;
    }
    public String getNote_Text()
    {
        return noteText;
    }
    public String getNote_DateTime()
    {
        return dateTime;
    }

    public void setNote_Title(String title)
    {
        this.title = title;
    }

    public void setNote_DateTime(String dateTime)
    {
        this.dateTime = dateTime;
    }

    public void setNote_Text(String noteText)
    {
        this.noteText = noteText;
    }

    public AndroidNotes(String note_title, String note_dateTime, String note_description)
    {
        this.title=note_title;
        this.dateTime=note_dateTime;
        this.noteText=note_description;
    }
    public String toString()
    {
        try{
            StringWriter sw=new StringWriter();
            JsonWriter jsonWriter=new JsonWriter(sw);
            jsonWriter.setIndent(" ");
            jsonWriter.beginObject();
            jsonWriter.name("Note_Title").value(getNote_Title());
            jsonWriter.name("Note_dateTime").value(getNote_DateTime());
            jsonWriter.name("Note_Desc").value(getNote_Text());
            jsonWriter.endObject();
            jsonWriter.close();
            return sw.toString();
        }catch (IOException e){
            e.printStackTrace();
        }
        return "Android Notes{" +
                "Title='" + title + '\'' +
                ", Date Time='" + dateTime + '\'' +
                ", Description=" + noteText +
                '}';
        //return "";
    }
}
