package ek.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;


public class Downloader
{
    private File outDir;
    private boolean ignorePath = true;
    
    
    public Downloader(File outDir)
    {
        this.outDir = outDir;
    }

    
    public void download(File urls) throws Exception
    {
        BufferedReader rd = new BufferedReader(new FileReader(urls));
        
        String line = null;
        while((line = rd.readLine()) != null)
        {
            if(line.length() > 0 && !line.startsWith("#"))
            {
                downloadFile(line);
            }
        }
        
        rd.close();
    }
    
    
    private void downloadFile(String fromUrl) throws Exception
    {
        URL url = new URL(fromUrl);
        String fileName = url.getPath();
        if(ignorePath)
        {
            int idx = fileName.lastIndexOf('/');
            if(idx > 0) fileName = fileName.substring(idx+1);
        }
        
        File outFile = new File(outDir, fileName);
        if(outFile.exists()) return;

        outFile.getParentFile().mkdirs();
        
        // TODO: Add timeout and retry
        InputStream is = url.openStream();
        Files.copy(is, outFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

}
