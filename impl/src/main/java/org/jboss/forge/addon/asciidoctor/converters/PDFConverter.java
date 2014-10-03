package org.jboss.forge.addon.asciidoctor.converters;

/**
 * 
 * @author <a href="mailto:greaumaxime@gmail.com">Maxime Gréau</a>
 */
public class PDFConverter extends AbstractConverter
{
   public PDFConverter()
   {
      super();
      isGemRequired = true;
      setConfigurationElement("requires", "<require>asciidoctor-pdf</require>");
   }
   
   @Override
   public String getName()
   {
      return "pdf";
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
