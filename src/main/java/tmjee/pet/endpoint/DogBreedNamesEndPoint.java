package tmjee.pet.endpoint;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tmjee.pet.commons.DbService;
import tmjee.pet.commons.EndPointResponse;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dog")
public class DogBreedNamesEndPoint {


    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response {
       public final List<String> breeds;
       Response(List<String> breeds) {
           this.breeds = breeds;
       }
    }

    @Autowired
    private DbService dbService;

    @Transactional
    @RequestMapping(value="breeds", method = RequestMethod.GET)
    public EndPointResponse<Response> breeds() {

        List<DbService.DogRetrievalOutput> dogs = dbService.allDogs();

        return new EndPointResponse<>(
                true,
                List.of("success"),
                new Response(dogs
                        .stream()
                        .map((DbService.DogRetrievalOutput d)->d.breedName)
                        .collect(Collectors.toList()))
        );
    }
}
