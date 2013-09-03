import java.io.*;
import java.util.*;

public class XMLFormat
{
    static String[] images;
    static String dir;
    static StringBuffer icon;
    static StringBuffer drawable;
    
    public static void main(String args[])
    {
        dir = new File(XMLFormat.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent();
        icon = new StringBuffer();
        drawable = new StringBuffer();
        
        System.out.println("\n\nDirectory: " + dir + "\nScanning...");
        images = getFileNames(finder(dir));
        
        System.out.println("Found " + images.length + " images");
        
        writeIconXML();
        writeDrawableXML();
        System.out.print("Finished!");
    }

    public static File[] finder(String dirName){
        File dir = new File(dirName);

        return dir.listFiles(new FilenameFilter()
        { 
             public boolean accept(File dir, String filename)
             {
                 return filename.endsWith(".png") || filename.endsWith(".PNG");
             }
        } );
    }
    
    public static String[] getFileNames(File[] files)
    {
        String[] names = new String[files.length];
        
        for(int x = 0; x < files.length; x++)
            names[x] = files[x].getName().replace(".png", "").replace(".PNG", "");
        
        return names;
    }
    
    public static void writeIconXML()
    {
        icon.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        icon.append("<resources>\n\n");
        icon.append("   <string-array name=\"icon_pack\" translatable=\"true\">\n");
        for(String img : images)
            icon.append("       <item>" + img + "</item>\n");
        icon.append("   </string-array>\n\n");
        icon.append("</resources>");
        
        try
        {
            File iconFile = new File(dir, "icon_pack.xml");
            FileOutputStream f = new FileOutputStream(iconFile);
            f.write(icon.toString().getBytes());
		    f.close();
        }
        catch(Exception e)
        {
            System.out.println("Oops, error occured while writing icon_pack.xml");
        }
    }
    
    public static void writeDrawableXML()
    {
        drawable.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        drawable.append("<resources>\n\n");
        drawable.append("    <version>1</version>\n\n");
        for(String img : images)
            drawable.append("       <item drawable=\"" + img + "\"/>\n");
        drawable.append("\n</resources>");
        
        try
        {
            File drawableFile = new File(dir, "drawable.xml");
            FileOutputStream f = new FileOutputStream(drawableFile);
            f.write(drawable.toString().getBytes());
		    f.close();
        }
        catch(Exception e)
        {
            System.out.println("Oops, error occured while writing drawable.xml");
        }
    }
}
