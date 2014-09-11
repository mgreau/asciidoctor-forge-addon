package org.jboss.forge.addon.asciidoctor.facets;

import org.jboss.forge.addon.dependencies.Dependency;
import org.jboss.forge.addon.dependencies.builder.CoordinateBuilder;
import org.jboss.forge.addon.dependencies.builder.DependencyBuilder;

public interface AsciidoctorPDFFacet extends AsciidoctorGemFacet
{
   public static final Dependency ASCIIDOCTOR_PDF =
            DependencyBuilder.create().setGroupId("rubygems").setArtifactId("asciidoctor-pdf").setVersion("1.5.0.alpha.4")
                     .setScopeType("provided").setPackaging("gem")
                     .addExclusion(CoordinateBuilder.create().setGroupId("rubygems").setArtifactId("asciidoctor"));

}
