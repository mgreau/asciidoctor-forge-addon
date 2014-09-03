package org.jboss.forge.addon.asciidoctor.converters;

import java.util.HashMap;
import java.util.Map;

import org.jboss.forge.addon.asciidoctor.Converter;

public class DocBookConverter implements Converter
{
   final Map<String, String> attributes = new HashMap<>();
   
   public DocBookConverter(){
      attributes.put("doctype", "book");
   }

   @Override
   public String getName()
   {
      return "DocBook";
   }

   @Override
   public String getBackend()
   {
      return "docbook";
   }

   @Override
   public String getId()
   {
      return "generate-docbook";
   }

   @Override
   public Map<String, String> getAttributes()
   {
      return attributes;
   }

}
