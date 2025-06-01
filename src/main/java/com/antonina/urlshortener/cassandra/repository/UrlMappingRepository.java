package com.antonina.urlshortener.cassandra.repository;

import com.antonina.urlshortener.cassandra.entity.UrlMapping;
import org.springframework.data.cassandra.repository.CassandraRepository;

public interface UrlMappingRepository extends CassandraRepository<UrlMapping, String> {

}

