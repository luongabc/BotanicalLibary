package com.example.botanicallibrary.en.Rss;

import java.util.List;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name = "channel", strict = false)
public class RssChannel
{
    @Element(required = false)
    private String title;


    @Element(required = false)
    private String link;

    @Element(required = false)
    private String generator;

    @Override
    public String toString() {
        return "RssChannel{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", generator='" + generator + '\'' +
                ", item=" + item +
                '}';
    }


    @ElementList(inline = true)
    public List<RssItem> item;

}