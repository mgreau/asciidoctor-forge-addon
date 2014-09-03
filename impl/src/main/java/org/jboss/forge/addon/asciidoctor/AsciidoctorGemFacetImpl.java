package org.jboss.forge.addon.asciidoctor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import org.jboss.forge.addon.asciidoctor.facets.AsciidoctorGemFacet;
import org.jboss.forge.addon.dependencies.Dependency;
import org.jboss.forge.addon.facets.constraints.FacetConstraint;
import org.jboss.forge.addon.facets.constraints.FacetConstraints;
import org.jboss.forge.addon.maven.projects.MavenPluginFacet;
import org.jboss.forge.addon.projects.dependencies.DependencyInstaller;
import org.jboss.forge.addon.projects.facets.DependencyFacet;
import org.jboss.forge.addon.resource.ResourceFacet;

/**
 * A base facet implementation for Facets which require Asciidoctor library APIs to be installed.
 * 
 * @author <a href="mailto:greaumaxime@gmail.com">Maxime Gréau</a>
 * 
 */
@FacetConstraints({
         @FacetConstraint(MavenPluginFacet.class),
         @FacetConstraint(DependencyFacet.class)
})
public class AsciidoctorGemFacetImpl extends AbstractAsciidoctorFacet implements AsciidoctorGemFacet
{

   private final DependencyInstaller installer;

   @Inject
   public AsciidoctorGemFacetImpl(DependencyInstaller installer)
   {
      this.installer = installer;
   }

   /**
    * Return a {@link Map} where KEY represents a {@link Dependency} to be installed if none of the VALUE
    * {@link Dependency} are installed.
    */
   private Map<Dependency, List<Dependency>> getRequiredDependencyOptions()
   {
      return Collections.singletonMap(ASCIIDOCTOR_DIAGRAM, Arrays.asList(ASCIIDOCTOR_DIAGRAM));
   }

   @Override
   public boolean install()
   {
      addGemRepository();
      addGemPlugin();
      addAsciidoctorDiagramDependency();

      return true;
   }

   @Override
   public boolean isInstalled()
   {
      return dependencyRequirementsMet();
   }

   protected boolean dependencyRequirementsMet()
   {
      DependencyFacet deps = origin.getFacet(DependencyFacet.class);
      for (Entry<Dependency, List<Dependency>> group : getRequiredDependencyOptions().entrySet())
      {
         boolean satisfied = false;
         for (Dependency dependency : group.getValue())
         {
            if (deps.hasEffectiveDependency(dependency))
            {
               satisfied = true;
               break;
            }
         }

         if (!satisfied)
            return false;
      }
      return true;
   }

   protected DependencyInstaller getInstaller()
   {
      return installer;
   }

   private void addAsciidoctorDiagramDependency()
   {
      DependencyFacet deps = origin.getFacet(DependencyFacet.class);
      for (Entry<Dependency, List<Dependency>> group : getRequiredDependencyOptions().entrySet())
      {
         boolean satisfied = false;
         for (Dependency dependency : group.getValue())
         {
            if (deps.hasEffectiveDependency(dependency))
            {
               satisfied = true;
               break;
            }
         }

         if (!satisfied)
         {
            installer.installManaged(origin, ASCIIDOCTOR_DIAGRAM);
            installer.install(origin, group.getKey());
         }
      }
   }

   private void addGemPlugin()
   {
      // TODO Auto-generated method stub

   }

   private void addGemRepository()
   {
      origin.getFacet(DependencyFacet.class).addRepository("RubyGems.org Proxy (Releases)",
               "http://rubygems-proxy.torquebox.org/releases");
   }

}