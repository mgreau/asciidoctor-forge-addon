package org.jboss.forge.addon.asciidoctor;

import java.util.Map;

public interface Converter
{
   public String getId();

   public String getName();

   public String getBackend();

   public Map<String, String> getAttributes();
   
   public void setAttribute(String name, String value);

}
