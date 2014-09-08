package org.jboss.forge.addon.asciidoctor.converters;

import java.util.HashMap;
import java.util.Map;

public class HTML5Converter extends AbstractConverter
{
   final Map<String, String> attributes = new HashMap<>();

   @Override
   public String getName()
   {
      return "HTML5";
   }

   @Override
   public String getBackend()
   {
      return "html5";
   }

   @Override
   public String getId()
   {
      return "generate-html5-doc";
   }
   
  
}
