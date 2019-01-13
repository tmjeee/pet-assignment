package tmjee.pet.commons;

import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

public class DogCeoService {

    public static final String URL = "https://dog.ceo/api/breeds/image/random";

    private static final String BREEDS = "/breeds/";
    private static final String SLASH = "/";

    public static class DogCeoException extends RuntimeException {
        DogCeoException(String msg) {
            super(msg);
        }
        DogCeoException(String msg, Exception e) {
            super(msg, e);
        }
    }

    public static class DogResponse {
        public String status;
        public String message;
    }

    public static class Dog {
        public final String breedName;
        public final byte[] image;

        Dog(String breedName, byte[] image) {
            this.breedName = breedName;
            this.image = image;
        }
    }



    public Dog requestNewDog() {
        DogResponse dogResponse =
                new RestTemplate()
                        .getForObject(URL, DogResponse.class);

        if ("success".equalsIgnoreCase(dogResponse.status)) {
            int i1 = dogResponse.message.indexOf(BREEDS);
            int i2 = dogResponse.message.lastIndexOf(SLASH);
            String breedName = dogResponse.message.substring(i1+BREEDS.length(),i2);

            byte[] imageInBytes = new RestTemplate()
                    .getForObject(dogResponse.message, byte[].class);

            return new Dog(breedName, imageInBytes);
        } else {
            throw new DogCeoException("Respose failure from Dog CEO");
        }
    }
}
