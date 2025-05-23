package com.photoChallenger.tripture.global.elasticSearch.challengeSearch;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChallengeSearchRepository extends ElasticsearchRepository<ChallengeDocument, Long> {

    @Query("{\"match\": {\"postChallengeName\": {\"query\": \"?0\"}}}")
    List<ChallengeDocument> findAllByPostChallengeName(String challengeName);
}
