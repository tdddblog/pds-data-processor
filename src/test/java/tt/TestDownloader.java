package tt;

import java.io.File;

import ek.web.Downloader;

public class TestDownloader
{

    public static void main(String[] args) throws Exception
    {
        Downloader dl = new Downloader(new File("/tmp/pds4/ctx"));
        dl.download(new File("/tmp/pds4-investigation-refs.txt"));
    }

}
