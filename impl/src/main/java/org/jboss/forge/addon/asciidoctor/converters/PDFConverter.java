package org.jboss.forge.addon.asciidoctor.converters;

import java.util.HashMap;
import java.util.Map;

public class PDFConverter extends AbstractConverter
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
   
  
}
