package tmjee.pet.endpoint;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tmjee.pet.commons.DbService;
import tmjee.pet.commons.DogCeoService;
import tmjee.pet.commons.EndPointResponse;
import tmjee.pet.commons.S3BucketService;
import tmjee.pet.config.PetAppConfig;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List; @RestController
@RequestMapping("/dog")
public class GenerateNewDogEndPoint {


    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response {
        public final Integer id;
        public final String breedName;
        public final String creationDate;
        public final String url;
        Response (Integer id,
                  String breedName,
                  LocalDateTime creationDate,
                  String url) {
            this.id = id;
            this.breedName = breedName;
            this.creationDate = creationDate.format(
                    DateTimeFormatter.ofPattern(PetAppConfig.DATE_TIME_PATTERN));
            this.url =url;
        }
    }


    @Autowired
    private DogCeoService dogCeoService;

    @Autowired
    private S3BucketService s3BucketService;

    @Autowired
    private DbService dbService;


    @Transactional
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public EndPointResponse<Response> get() {

        DogCeoService.Dog dog = this.dogCeoService.requestNewDog();

        S3BucketService.SavedDog savedDog = this.s3BucketService.saveDogPicture(dog.image);

        DbService.DogInsertionOutput dogInsertionResponse =
           dbService.dogInsertion(new DbService.DogInsertionInput(
                dog.breedName,
                savedDog.date,
                savedDog.location,
                savedDog.key
        ));

        return new EndPointResponse<>(
                true,
                List.of(String.format("Dog of breed %s saved (id = %s)", dog.breedName, dogInsertionResponse.id)),
                new Response(
                            dogInsertionResponse.id,
                            dog.breedName,
                            savedDog.date,
                            savedDog.location
                        ));

    }
}
