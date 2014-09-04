package org.jboss.forge.addon.asciidoctor.converters;


public class DocBookConverter extends AbstractConverter
{
  
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


}
