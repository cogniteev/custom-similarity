package org.elasticsearch.plugin.similarity.custom;

import org.elasticsearch.index.similarity.SimilarityModule;
import org.elasticsearch.index.similarity.custom.CustomSimilarityProvider;
import org.elasticsearch.plugins.AbstractPlugin;


public class CustomSimilarityPlugin extends AbstractPlugin {

  @Override public String name() {
    return "custom";
  }


  @Override public String description() {
    return "Custom Similarity";
  }


  public void onModule(SimilarityModule module) {
    module.addSimilarity("custom", CustomSimilarityProvider.class);
  }

}
