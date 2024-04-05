import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PokemonAPI {

    @Test
    public void testGetPokemonData() throws InterruptedException {
        RestAssured.baseURI = "https://pokeapi.co/api/v2/pokemon/ditto";

        Response jsonResponse = RestAssured.when()
                .get()
                .then()
                .extract().response();

        io.restassured.path.json.JsonPath body = jsonResponse.jsonPath();

        System.out.println("***** Pokemon API *****");

        /*
        * Task 1: Extract and print the names of all the abilities         *
        */
        List<String> names = body.getList("abilities.ability.name");
        System.out.println("Task 1: Names of abilities: " + names);

        /*
        * Task 2: Count and print the number of game indices present in the JSON response         *
        */
        int gamesIndices = body.getList("game_indices").size();
        System.out.println("Task 2: Number of game indices = " + gamesIndices);

        /*
        * Task 3: Find and print the URLs of all the items held by the Pokemon         *
        */
        List<String> heldItems = body.getList("held_items.item.url");
        System.out.println("Task 3: URLs of items held by the Pokemon: ");
        heldItems.forEach(System.out::println);

        /*
        * Task 4: Find and print the names of all the moves learned by the Pokemon     *
        */
        List<String> movesLearned = body.getList("moves.move.name");
        System.out.println("Task 4: Name of moves learned: " + movesLearned);

        /*
        * Task 5: Find and print the URLs of all the versions in which the Pokemon can be encountered
        */
        List<String> versions1 = body.getList("game_indices.version.url");
        List<String> versions2 = body.getList("held_items.version_details.version.url.flatten()");
        List<String> versions = Stream.of(versions1,versions2)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());

        System.out.println("Task 5: URLs of all the versions ");
        versions.forEach(System.out::println);

        /*
        * Bonus: Calculate and print the sum of the rarities for each version in which the Pokemon can hold an item.
        */
        System.out.println(String.format("**** Bonus ****"));
        int x = body.getList("held_items").size();
        for (int i = 0 ; i < x ; i++)
        {
            String nombre = body.getList("held_items.item.name").get(i).toString();
            List<Integer> bonus = (List<Integer>) body.getList("held_items.version_details.rarity").get(i);
            int suma = bonus.stream().mapToInt(Integer::intValue).sum();
            System.out.println(String.format("Item: %1$s  %2$s", nombre, suma));
        }
    }
}
