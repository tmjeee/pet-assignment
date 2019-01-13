package tmjee.pet.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tmjee.pet.commons.DbService;
import tmjee.pet.commons.EndPointResponse;
import tmjee.pet.commons.S3BucketService;

import java.util.List;

@RestController
@RequestMapping("/dog")
public class RemovalEndPoint {

    @Autowired
    private DbService dbService;

    @Autowired
    private S3BucketService s3BucketService;

    @Transactional
    @RequestMapping(value="{id}", method= RequestMethod.DELETE)
    public EndPointResponse<Void> delete(@PathVariable("id")Integer id) {
        DbService.DogRetrievalOutput dogDeleted = dbService.dogRetrieval(id);
        dbService.dogRemoval(id);
        s3BucketService.deleteDogPicture(dogDeleted.s3Key);
        return new EndPointResponse<>(
                true,
                List.of(String.format("Dog with id %s deleted", id)),
                null
        );
    }

}
