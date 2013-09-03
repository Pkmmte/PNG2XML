import java.io.*;
import java.util.*;

public class PNG2XML
{
    private static File[] images;
    private static String dir;
    private static StringBuilder icon;
    private static StringBuilder drawable;
    
    public static void main(String args[])
    {
        // Get .jar directory
        dir = new File(PNG2XML.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent();
        // Replace %20 with real spaces
        dir = dir.replace("%20", " ");
        
        // Show progress & Scan
        System.out.println("\nDirectory: " + dir + "\nScanning...");
        images = Scan4Images();
        
        // Write images if found, else return error message
        if (images.length > 0)
        {
            System.out.println("Found " + images.length + " images");
            
            writeXML();
            saveFiles();
            System.out.print("Finished!");
        }
        else
            System.out.print("Could not find any .png image files. :(");
    }

    private static File[] Scan4Images()
    {
        File directory = new File(dir);
        
        return directory.listFiles(new FilenameFilter()
        { 
             public boolean accept(File dir, String filename)
             {
                 return filename.endsWith(".png") || filename.endsWith(".PNG");
             }
        } );
    }
    
    private static void writeXML()
    {
        // Initialize String
        icon = new StringBuilder();
        drawable = new StringBuilder();
        
        // Write headers
        icon.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        icon.append("<resources>\n\n");
        icon.append("   <string-array name=\"icon_pack\" translatable=\"true\">\n");
        drawable.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        drawable.append("<resources>\n\n");
        drawable.append("    <version>1</version>\n\n");
        
        // Loop through all files and assign a value for each
        for(File img : images)
        {
            icon.append("       <item>" + img.getName().replace(".png", "").replace(".PNG", "") + "</item>\n");
            drawable.append("       <item drawable=\"" + img.getName().replace(".png", "").replace(".PNG", "") + "\"/>\n");
        }
        
        // Write footers
        icon.append("   </string-array>\n\n");
        icon.append("</resources>");
        drawable.append("\n</resources>");
    }
    
    private static void saveFiles()
    {
        // Save icon_pack.xml
        try
        {
            File iconFile = new File(dir, "icon_pack.xml");
            FileOutputStream f = new FileOutputStream(iconFile);
            f.write(icon.toString().getBytes());
            f.close();
        }
        catch(Exception e)
        {
            System.out.println("Oops, an error occured while writing icon_pack.xml");
        }
        
        // Save drawable.xml
        try
        {
            File drawableFile = new File(dir, "drawable.xml");
            FileOutputStream f = new FileOutputStream(drawableFile);
            f.write(drawable.toString().getBytes());
            f.close();
        }
        catch(Exception e)
        {
            System.out.println("Oops, an error occured while writing drawable.xml");
        }
    }
}
