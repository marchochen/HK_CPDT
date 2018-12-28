package com.cw.wizbank.util;

import java.io.*;
import java.util.*;

//
// IniFile by Steven Primrose-Smith Copyright 1998
//
// This class allows you to access the values in an INI file.
// Values must be in the format:
// KEY=VALUE
// Valueless keys are permitted
// Use a # at the beginning of a line to add a comment
// Blank lines are also permitted
// Values can be set and written back to the file
// If values are written back, all comments will be lost
//

public class cwIniFile extends File 
{
   Hashtable values;


   // open the file and read all the key/value pairs

   public cwIniFile(String filename) throws IOException
   {
      super(filename);

      if (!exists()) create();

      values = new Hashtable();

      BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(this)));

      for (;;)
      {
         String line = in.readLine();
         if (line == null) break;
         if (line.trim().compareTo("") == 0) continue;
         if (line.charAt(0) == '#') continue;

         StringTokenizer token = new StringTokenizer(line, "=", false);

         String key = "";
         String value = "";

         if (token.hasMoreElements()) key = (String)token.nextElement();
         
         value = line.substring(key.length() + 1);
         //if (token.hasMoreElements()) value = (String)token.nextElement();

         if (key.compareTo("") != 0) values.put(key.trim(), value.trim());
      }

      in.close();
   }


   // get a value

   public String getValue(String key)
   {
      if (keyExists(key) == false) return null;
      return (String)values.get(key);
   }

   public Hashtable getAll() { return values; }

   // set a value

   public void setValue(String key, String value) { values.put(key, value); }


   // check for existence of a value

   public boolean valueExists(String key)
   {
      if (keyExists(key) == false) return false;

      String value = (String)values.get(key);
      if (value.compareTo("") == 0) return false;

      return true;
   }

   // check for existence of a key

   public boolean keyExists(String key) { return values.containsKey(key); }


   // show all values on standard output

   public void dumpValues()
   {
      Enumeration enumeration = values.keys();

      while (enumeration.hasMoreElements())
      {
         String key = (String)enumeration.nextElement();
         String value = (String)values.get(key);

      }

   }


   // save all keys/values

   public void saveAllValues() throws IOException
   {
      PrintStream ps = new PrintStream(new FileOutputStream(this));

      Enumeration enumeration = values.keys();

      while (enumeration.hasMoreElements())
      {
         String key = (String)enumeration.nextElement();
         String value = (String)values.get(key);
         ps.println(key + "=" + value);
      }

      ps.close();
   }

   // if the ini file doesn't exist, make it

   private void create()
   {
      try
      {
         PrintStream ps = new PrintStream(new FileOutputStream(this));
         ps.println();
         ps.close();
      }
      catch (IOException e) { }
   }
}
