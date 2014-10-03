package org.jboss.forge.addon.asciidoctor.converters;


/**
 * 
 * @author <a href="mailto:greaumaxime@gmail.com">Maxime Gréau</a>
 */
public class HTML5Converter extends AbstractConverter
{
   @Override
   public String getName()
   {
      return "html5";
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

   public void useAsciidoctorDiagram(boolean useDiagram)
   {
      isGemRequired = useDiagram;
      if (useDiagram)
      {
         setConfigurationElement("requires", "<require>asciidoctor-diagram</require>");
      }
   }

}
