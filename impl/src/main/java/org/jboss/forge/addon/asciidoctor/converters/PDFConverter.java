package org.jboss.forge.addon.asciidoctor.converters;

import java.util.HashMap;
import java.util.Map;

import org.jboss.forge.addon.asciidoctor.Converter;

public class PDFConverter implements Converter
{
   final Map<String, String> attributes = new HashMap<>();

   @Override
   public String getName()
   {
      return "PDF";
   }

   @Override
   public String getBackend()
   {
      return "pdf";
   }

   @Override
   public String getId()
   {
      return "generate-pdf-doc";
   }
   
   @Override
   public Map<String, String> getAttributes()
   {
      return attributes;
   }
}
