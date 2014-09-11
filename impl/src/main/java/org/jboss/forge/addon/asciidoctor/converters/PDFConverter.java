package org.jboss.forge.addon.asciidoctor.converters;


public class PDFConverter extends AbstractConverter
{
   public PDFConverter()
   {
      super();
      setConfigurationElement("requires", "<require>asciidoctor-pdf</require>");
   }
   
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
   public void useAsciidoctorDiagram(boolean value)
   {
      // TODO Auto-generated method stub
      
   }


  
}
