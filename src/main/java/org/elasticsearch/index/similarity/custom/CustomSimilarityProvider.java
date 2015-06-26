package org.elasticsearch.index.similarity.custom;
import org.apache.lucene.search.similarities.Similarity;

import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.similarity.AbstractSimilarityProvider;

/**
 * {@link AbstractSimilarityProvider} for {@link CustomSimilarity}
 *
 */
public class CustomSimilarityProvider extends AbstractSimilarityProvider {

	CustomSimilarity similarity = new CustomSimilarity();

	@Inject
	protected CustomSimilarityProvider(@Assisted String name, @Assisted Settings settings) {
		super(name);

		boolean discountOverlaps = settings.getAsBoolean("discount_overlaps", Boolean.valueOf(true)).booleanValue();
		boolean compressNorms = settings.getAsBoolean("compress_norms", Boolean.valueOf(false)).booleanValue();

		similarity.setDiscountOverlaps(discountOverlaps);
		similarity.setCompressNorms(compressNorms);
	}

	@Override
	public Similarity get() {
		return similarity;
	}
}