package org.jboss.forge.addon.asciidoctor.facets;

import org.jboss.forge.addon.dependencies.Dependency;
import org.jboss.forge.addon.dependencies.builder.CoordinateBuilder;
import org.jboss.forge.addon.dependencies.builder.DependencyBuilder;

/**
 * 
 * @author <a href="mailto:greaumaxime@gmail.com">Maxime Gréau</a>
 */
public interface AsciidoctorDiagramFacet extends AsciidoctorGemFacet
{
   public static final Dependency ASCIIDOCTOR_DIAGRAM =
            DependencyBuilder.create().setGroupId("rubygems").setArtifactId("asciidoctor-diagram").setVersion("1.2.1")
                     .setScopeType("provided").setPackaging("gem")
                     .addExclusion(CoordinateBuilder.create().setGroupId("rubygems").setArtifactId("asciidoctor"));
   
}
