package nl.rabobank.mongo.repositories;

import java.util.List;

import nl.rabobank.mongo.entities.PowerOfAttorneyDto;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PowerOfAttorneysRepository extends MongoRepository<PowerOfAttorneyDto,Long> {

    @Query(value = "{ 'grantee' : ?0 }", fields = "{ 'account' : 1, 'authorization' : 1 }")
    List<PowerOfAttorneyDto> findByGrantee(String grantee);

}
