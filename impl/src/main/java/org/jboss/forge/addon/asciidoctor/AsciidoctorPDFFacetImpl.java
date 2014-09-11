package org.jboss.forge.addon.asciidoctor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jboss.forge.addon.asciidoctor.facets.AsciidoctorPDFFacet;
import org.jboss.forge.addon.dependencies.Dependency;
import org.jboss.forge.addon.facets.constraints.FacetConstraint;
import org.jboss.forge.addon.facets.constraints.FacetConstraints;
import org.jboss.forge.addon.maven.projects.MavenPluginFacet;
import org.jboss.forge.addon.projects.facets.DependencyFacet;

/**
 * A base facet implementation for Facets which require Asciidoctor Gem libraries APIs to be installed.
 * 
 * @author <a href="mailto:greaumaxime@gmail.com">Maxime Gréau</a>
 * 
 */
@FacetConstraints({
         @FacetConstraint(MavenPluginFacet.class),
         @FacetConstraint(DependencyFacet.class)
})
public class AsciidoctorPDFFacetImpl extends AbstractAsciidoctorGemFacet implements AsciidoctorPDFFacet
{
   protected Map<Dependency, List<Dependency>> getRequiredDependencyOptions()
   {
      return Collections.singletonMap(ASCIIDOCTOR_PDF, Arrays.asList(ASCIIDOCTOR_PDF));
   }

   protected void addRequiredDependency()
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
            origin.getFacet(DependencyFacet.class).addDirectDependency(ASCIIDOCTOR_PDF);
         }
      }
   }


   


}