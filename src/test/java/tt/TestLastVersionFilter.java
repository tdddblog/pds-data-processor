package tt;

import java.io.File;

import ek.web.LastVersionFilter;


public class TestLastVersionFilter
{

    public static void main(String[] args) throws Exception
    {
        File file = new File("/tmp/pds4-context-refs.txt");
        
        LastVersionFilter filter = new LastVersionFilter();
        filter.setUrlFilter("/investigation/");
        filter.process(file);
    }

    
}
