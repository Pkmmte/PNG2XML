import java.io.*;
import java.util.*;

public class PNG2XML
{
    private static File[] images;
    private static String dir;
    private static StringBuilder icon;
    private static StringBuilder drawable;
    private static StringBuilder appfilter;
    private static StringBuilder iconlist;
    private static StringBuilder appmap;
    private static StringBuilder theme_resources;
    
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
        appfilter = new StringBuilder();
        iconlist=new StringBuilder();
        appmap=new StringBuilder();
        theme_resources=new StringBuilder();
        // Write headers
        appfilter.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        appfilter.append("<resources>\n\n");    
        appfilter.append("\t<!-- Icon Back -->\n");
        appfilter.append("\t<iconback img1=\"iconback1\" img2=\"iconback2\" img3=\"iconback3\" img4=\"iconback4\" img5=\"iconback5\"/>\n");
        appfilter.append("\t<!-- You can add or remove iconbacks by editing above line using the given format  -->\n\n");
        appfilter.append("\t<!-- Icon Upon -->\n");
        appfilter.append("\t<iconupon img1=\"iconupon1\" img2=\"iconupon2\" img3=\"iconupon3\"/>\n");
        appfilter.append("\t<!-- You can add or remove iconupons by editing above line using the given format  -->\n\n");
        appfilter.append("\t<!-- Icon Mask -->\n");
        appfilter.append("\t<iconmask img1=\"iconmask1\" img2=\"iconmask2\" img3=\"iconmask3\"/>\n");
        appfilter.append("\t<!-- You can add or remove iconmasks by editing above line using the given format  -->\n\n");
        appfilter.append("\t<!-- Below line scales the images to fit into your iconback -->\n");
        appfilter.append("\t<scale factor=\"0.8\"/>\n\n");
        appfilter.append("\t<!-- Icons Starts From Here  -->\n");
        appmap.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        appmap.append("<appmap>\n\n");      
        drawable.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        drawable.append("<resources>\n\n");
        drawable.append("\t<version>1</version>\n\n");
        icon.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        icon.append("<resources>\n\n");
        icon.append("\t<string-array name=\"icon_pack\" translatable=\"true\">\n\n");        
        iconlist.append("This file is just for your reference\n");      
        iconlist.append(images.length+" icons\n\n");        
        iconlist.append("Icon List:\n\n");
        theme_resources.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        theme_resources.append("<Theme version=\"1\">\n\n");     
        theme_resources.append("\t<Label value=\"Your Icon Pack Name\"/>\n");
        theme_resources.append("\t<Wallpaper image=\"lg_homescreen_wallpaper\"/>\n");
        theme_resources.append("\t<LockScreenWallpaper image=\"lockscreen_wallpaper\"/>\n");
        theme_resources.append("\t<ThemePreview image=\"lg_homescreen_preview\"/>\n");
        theme_resources.append("\t<ThemePreviewWork image=\"lg_homescreen_preview_work\"/>\n");
        theme_resources.append("\t<ThemePreviewMenu image=\"lg_homescreen_preview_menu\"/>\n");
        theme_resources.append("\t<DockMenuAppIcon selector=\"lg_dockmenu_all_apps_button_icon\"/>\n\n");
        
        // Loop through all files and assign a value for each
        for(File img : images)
        {
            appmap.append("\t<!-- "+img.getName().replace("_", " ").replace(".PNG", "").replace(".png", "").toUpperCase()+" -->\n");
            appmap.append("\t<item class=\"\" name=\""+img.getName().replace(".png", "").replace(".PNG", "")+"\"/>\n");
            appfilter.append("\t<!-- "+img.getName().replace("_", " ").replace(".PNG", "").replace(".png", "").toUpperCase()+" -->\n");
            appfilter.append("\t<item component=\"ComponentInfo{}\" drawable=\"" + img.getName().replace(".png", "").replace(".PNG", "") + "\"/>\n");            
            drawable.append("\t<item drawable=\"" + img.getName().replace(".png", "").replace(".PNG", "") + "\"/>\n");            
            icon.append("\t\t<item>" + img.getName().replace(".png", "").replace(".PNG", "") + "</item>\n");
            iconlist.append("-"+img.getName().replace(".png", "").replace(".PNG", "")+"\n");
            theme_resources.append("\t<!-- "+img.getName().replace("_", " ").replace(".PNG", "").replace(".png", "").toUpperCase()+" -->\n");
            theme_resources.append("\t<AppIcon name=\"\" image=\""+img.getName().replace(".png", "").replace(".PNG", "")+"\"/>\n");
        }
        
        // Write footers
        appmap.append("\n</appmap>");        
        appfilter.append("\n</resources>");
        drawable.append("\n</resources>");        
        icon.append("\n\t</string-array>\n\n");
        icon.append("</resources>");
        theme_resources.append("\n</Theme>");

    }
    
    private static void saveFiles()
    {
       //Save appmap.xml
        try
        {
            File appmapFile = new File(dir, "appmap.xml");
            FileOutputStream f = new FileOutputStream(appmapFile);
            f.write(appmap.toString().getBytes());
            f.close();
       }
       catch (Exception e)
       {
            System.out.println("Oops, an error occured while writing appmap.xml");
       }      
        //Save appfilter.xml
        try
        {
            File appfilterFile = new File(dir, "appfilter.xml");
            FileOutputStream f = new FileOutputStream(appfilterFile);
            f.write(appfilter.toString().getBytes());
            f.close();
       }
       catch (Exception e)
       {
            System.out.println("Oops, an error occured while writing appfilter.xml");
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
       //Save iconlist.txt (just for reference)
        try
        {
            File iconlistFile = new File(dir, "iconlist.txt");
            FileOutputStream f = new FileOutputStream(iconlistFile);
            f.write(iconlist.toString().getBytes());
            f.close();
       }
       catch (Exception e)
       {
            System.out.println("Oops, an error occured while writing iconlist.txt");
       }          
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
        // Save theme_resources.xml
        try
        {
            File theme_resourcesFile = new File(dir, "theme_resources.xml");
            FileOutputStream f = new FileOutputStream(theme_resourcesFile);
            f.write(theme_resources.toString().getBytes());
            f.close();
        }
        catch(Exception e)
        {
            System.out.println("Oops, an error occured while writing theme_resources.xml");
        }
              
    }
}
