package ek.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class LastVersionFilter
{
    private String urlFilter;
    
    private String lastUrl;
    private String lastName;
    private float lastVersion;
    
    
    public LastVersionFilter()
    {
    }

    
    public void setUrlFilter(String filter)
    {
        this.urlFilter = filter;
    }
    
    
    public void process(File file) throws Exception
    {
        BufferedReader rd = new BufferedReader(new FileReader(file));        

        String line = null;
        String prevLine = null;
        while((line = rd.readLine()) != null)
        {
            // Skip duplicate urls (e.g., in pds4 context pages)
            if(line.equals(prevLine)) continue;
            if(urlFilter != null && line.contains(urlFilter))
            {
                processUrl(line);
                prevLine = line;
            }
        }
        
        rd.close();
        
        writeLastUrl();
    }


    private void processUrl(String url)
    {
        // Extract full file name e.g., mission.lunar_prospector_1.0.xml
        int idx = url.lastIndexOf('/');
        String fullName = url.substring(idx+1);

        // Version
        int idxVer = fullName.lastIndexOf('_');
        // Extension
        int idxExt = fullName.lastIndexOf('.');
        
        String name = fullName.substring(0, idxVer);
        String strVer = fullName.substring(idxVer+1, idxExt);
        float version = Float.parseFloat(strVer);

        if(name.equals(lastName))
        {
            if(version > lastVersion)
            {
                lastVersion = version;
                lastUrl = url;
            }
        }
        else
        {
            writeLastUrl();
            lastUrl = url;
            lastName = name;
            lastVersion = version;
        }
    }

    
    private void writeLastUrl()
    {
        if(lastUrl == null) return;
        
        System.out.println(lastUrl);
    }
}
