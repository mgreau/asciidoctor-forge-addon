package org.jboss.forge.addon.asciidoctor.converters;

import java.util.HashMap;
import java.util.Map;

import org.jboss.forge.addon.asciidoctor.Converter;

public class HTML5Converter implements Converter
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
   
   @Override
   public Map<String, String> getAttributes()
   {
      return attributes;
   }
}
