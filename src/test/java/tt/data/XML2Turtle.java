package tt.data;

import ek.solr.SolrDocParser;

public class XML2Turtle
{
    private static class Planet
    {
        String lid;
        String name;
        String type;
        String diameter;
        String axis;
    }
    
    
    private static class PlanetCB implements SolrDocParser.Callback
    {
        private Planet planet;
        
        @Override
        public void onDocStart()
        {
            planet = new Planet();
        }

        @Override
        public boolean onDocEnd()
        {
            System.out.println("<" + planet.lid + ">");
            System.out.println("  <pds:name> \"" + planet.name + "\";");
            System.out.println("  rdf:type <pds:" + planet.type + ">;");
            System.out.println("  <pds:diameter_km> \"" + planet.diameter + "\"^^xsd:float;");
            System.out.println("  <pds:semi_major_axis_au> \"" + planet.axis + "\"^^xsd:float.");
            
            return true;
        }

        @Override
        public void onField(String name, String value)
        {
            switch(name)
            {
            case "lid":
                planet.lid = value;
                break;
            case "title":
                planet.name = value;
                break;
            case "target_type":
                planet.type = value;
                break;
            case "diameter_km":
                planet.diameter = value;
                break;
            case "semi_major_axis_au":
                planet.axis = value;
                break;
                                
            }
        }
    }
    
    
    public static void main(String[] args) throws Exception
    {
        SolrDocParser parser = new SolrDocParser(new PlanetCB());
        parser.parse("/ws/data/curated/planets.xml");
    }

}
