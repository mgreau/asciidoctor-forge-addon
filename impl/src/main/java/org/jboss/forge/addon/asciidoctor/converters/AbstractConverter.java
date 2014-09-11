package org.jboss.forge.addon.asciidoctor.converters;

import java.util.HashMap;
import java.util.Map;

import org.jboss.forge.addon.asciidoctor.Converter;

import static java.util.Collections.unmodifiableMap;

public abstract class AbstractConverter implements Converter
{
   protected final Map<String, String> attributes =  new HashMap<String, String>();
   
   protected final Map<String, String> configuration =  new HashMap<String, String>();
   
   protected boolean isGemRequired = false;
   
   public AbstractConverter()
   {
      setConfigurationElement("backend", getBackend());
   }
   
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
   
   @Override
   public Map<String, String> getConfiguration()
   {
      return unmodifiableMap(configuration);
   }

   @Override
   public void setConfigurationElement(String name, String value)
   {
      configuration.put(name, value);
   }

   @Override
   public boolean isGemRequired()
   {
      return isGemRequired;
   }

}
