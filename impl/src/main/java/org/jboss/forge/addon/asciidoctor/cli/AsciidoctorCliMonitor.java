package org.jboss.forge.addon.asciidoctor.cli;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.asciidoctor.cli.AsciidoctorInvoker;
import org.jboss.forge.addon.resource.Resource;
import org.jboss.forge.addon.resource.ResourceFactory;
import org.jboss.forge.addon.resource.events.ResourceEvent;
import org.jboss.forge.addon.resource.events.ResourceModified;
import org.jboss.forge.addon.resource.monitor.ResourceListener;
import org.jboss.forge.addon.resource.monitor.ResourceMonitor;
import org.jboss.forge.furnace.container.cdi.events.Local;
import org.jboss.forge.furnace.event.PreShutdown;

@Singleton
public class AsciidoctorCliMonitor
{
   private static final Logger log = Logger.getLogger(AsciidoctorCliMonitor.class.getName());

   @Inject
   private ResourceFactory resourceFactory;

   private ResourceMonitor monitor;

   private AsciidoctorInvoker cli;

   @PostConstruct
   public void init()
   {
      cli = new AsciidoctorInvoker();
   }

   public void monitorResource(Resource<?> sourceResource, final String... args)
   {
      monitor = resourceFactory.monitor(sourceResource);
      monitor.addResourceListener(new ResourceListener()
      {

         @Override
         public void processEvent(ResourceEvent event)
         {
            if (event instanceof ResourceModified)
            {
               processResource(event.getResource(), args);
            }
         }
      });
   }

   public void processResource(final Resource<?> sourceResource, final String... args)
   {
      cli.invoke(args);
      log.fine("Resource processed by Asciidoctor.");
   }

   void shutdown(@Observes @Local PreShutdown event)
   {
      if (monitor != null)
      {
         // If the monitor is no longer needed, dispose properly by calling cancel
         monitor.cancel();
         log.fine("Cancel the monitor.");
      }
   }

}
