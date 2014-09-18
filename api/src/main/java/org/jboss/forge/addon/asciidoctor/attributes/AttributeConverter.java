package org.jboss.forge.addon.asciidoctor.attributes;

import org.jboss.forge.addon.convert.Converter;

/**
 */
public class AttributeConverter implements Converter<String, Attribute>
{
   private static final String COLON = ":";

   @Override
   public Attribute convert(String source)
   {
      if (source.contains(COLON))
      {
         String[] attr = source.split(COLON);
         return new Attribute(attr[0], attr.length > 1 ? attr[1] : "");
      }
      else
      {
         return new Attribute(source, "");
      }
   }
}