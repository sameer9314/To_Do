package com.bridgelabz.todoapplication.noteservice.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import com.bridgelabz.todoapplication.noteservice.model.Notes;

/**
 * Purpose : NoteElasticRepository interface which extends ElasticsearchRepository to store 
 *           the Note into Elasticsearch Database.
 * @author   Sameer Saurabh
 * @version  1.0
 * @Since    31/07/2018
 */
public interface NoteElasticRepository extends ElasticsearchRepository<Notes, String>{
}
