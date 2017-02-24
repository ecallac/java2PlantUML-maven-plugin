package com.globant.utils.plantuml.classes.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SaveFileHelper {

   private static final String J2PUML = "j2puml";

   public static void save(StringBuilder pumlContent, String path) throws IOException {
      final File file = new File(getPathName(path));
      BufferedWriter bw;
      bw = new BufferedWriter(new FileWriter(file));
      bw.write(pumlContent.toString());

      bw.flush();
      bw.close();
   }

   private static String getPathName(String rout) {
      final StringBuilder path;
      if (rout != null) {
         // TODO: It must be checked what we got in the rout (path) to validate
         // it...
         path = new StringBuilder(rout.concat(J2PUML));
      } else {
         path = new StringBuilder(J2PUML);
      }

      SimpleDateFormat instant = new SimpleDateFormat("ddMMyyyy_HM_mm", Locale.getDefault());
      Date now = new Date();
      StringBuilder fullName = new StringBuilder(instant.format(now));

      path.append(fullName).append(".txt");

      return path.toString();
   }
   
   public static void createDirectories(String path){
   	try {
			File file = new File(path);
			if (file.exists() && file.isFile())
		    {
		        System.out.println("The dir with name could not be created as it is a normal file");
		    }
		    else
		    {
		        try
		        {
		            if (!file.exists())
		            {
		            	System.out.println("The dir " +path + " will be created");
		            	file.mkdirs();
		            	System.out.println("The dir " +path + " was created");
		            }else{
//		            	System.out.println("The path "+path+" exist");
		            }

		        }
		        catch (Exception e)
		        {
		            System.out.println("prompt for error");
		        }
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
   }
   public static void createFile(String pumlContent, String filePath) throws IOException {
	      final File file = new File(filePath);
	      BufferedWriter bw;
	      bw = new BufferedWriter(new FileWriter(file));
	      bw.write(pumlContent);

	      bw.flush();
	      bw.close();
	   }
   public static String[] splitPackages(String packages){
   	return packages.split(",");
   }

}
