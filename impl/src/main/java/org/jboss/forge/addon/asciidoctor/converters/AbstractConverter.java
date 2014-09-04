package org.jboss.forge.addon.asciidoctor.converters;

import java.util.HashMap;
import java.util.Map;

import org.jboss.forge.addon.asciidoctor.Converter;

import static java.util.Collections.unmodifiableMap;

public abstract class AbstractConverter implements Converter
{
   
   protected final Map<String, String> attributes =  new HashMap<String, String>();

   @Override
   public Map<String, String> getAttributes()
   {
      return unmodifiableMap(attributes);
   }
   
   @Override
   public void setAttribute(String name, String value)
   {
      attributes.put(name, value);
   }


}
