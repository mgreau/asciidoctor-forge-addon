package org.jboss.forge.addon.asciidoctor.attributes;

import org.jboss.forge.addon.convert.Converter;

/**
 * 
 * @author <a href="mailto:greaumaxime@gmail.com">Maxime Gréau</a>
 */
public class AttributeConverter implements Converter<String, Attribute>
{
   private static final String SEPARATOR = "=";

   @Override
   public Attribute convert(String source)
   {
      if (source.contains(SEPARATOR))
      {
         String[] attr = source.split(SEPARATOR);
         return new Attribute(attr[0], attr.length > 1 ? attr[1] : "");
      }
      else
      {
         return new Attribute(source, "");
      }
   }
}