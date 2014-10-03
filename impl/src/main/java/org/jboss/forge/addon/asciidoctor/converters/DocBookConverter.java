package org.jboss.forge.addon.asciidoctor.converters;

/**
 * 
 * @author <a href="mailto:greaumaxime@gmail.com">Maxime Gréau</a>
 */
public class DocBookConverter extends AbstractConverter
{

   public DocBookConverter()
   {
      super();
      attributes.put("doctype", "book");
   }

   @Override
   public String getName()
   {
      return "docbook";
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
   public void useAsciidoctorDiagram(boolean value)
   {
      // TODO Auto-generated method stub
      
   }

}
