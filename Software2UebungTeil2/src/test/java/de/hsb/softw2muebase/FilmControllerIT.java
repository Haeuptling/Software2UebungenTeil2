package de.hsb.softw2muebase;

import org.junit.Assert;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@SpringBootTest
public class FilmControllerIT {

    @Autowired
    MockMvc mockMvc;

  @Autowired
  private FilmController filmController = new FilmController();
    //Testet, ob bei der Url suche nach einem nicht enthaltenen film Stauts 200 und eine leere Liste zurück gegeben wird
    @ParameterizedTest
    @ValueSource(strings={"Scream"})
    public void returnContainsEmptyList(String input) throws Exception {

        mockMvc
                .perform(get("/movies/Scream"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }
    //Testet die to.String Methode
    /*@ParameterizedTest
    @ValueSource(strings={"Who am I"})
    void returnContainsListOfOne(String input) throws Exception {
        Film newFilm = new Film(input);
        filmController.newMovie(newFilm);

        mockMvc
                .perform(get("/movies/Who am I"))
                .andExpect(status().isOk())
                .andExpect(content().string("[{\"id\":"+newFilm.getId()+","+"\"name\""+":"+"\""+input+"\""+"}]"));
    }*/

    //Testet, ob bei einem neu erstellten Film Status Created zurück gegeben wird
    @ParameterizedTest
    @ValueSource(strings={"Matrix"})
    public void returnContainsCreated(String input) throws Exception {
        Film newFilm = new Film(input);
        filmController.newMovie(newFilm);

        ResponseEntity<Object> responseEntity = (ResponseEntity<Object>) filmController.newMovie(newFilm);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);
    }


    //Testet, ob ein anderer Parameter als name funktioniert
    @ParameterizedTest
    @ValueSource(strings = {"abc", "name","numus"})
    void returnIfFormatJson(String input) throws Exception {
        String content="{ \""+input+"\":\"Matrix\"}";
        if(input.equals("name")){
            mockMvc.perform(post("/movies").content(content).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
        }if(! input.equals("name")){
            mockMvc.perform(post("/movies").content(content).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
       }
    }
    //Testet, ob bei einer nicht existierenden ID Eror404 ausgegeben wird und ob der Responsebody leer ist
    @ParameterizedTest
    @ValueSource( strings =  {"/movies/10"})
    void returnNonExistingID(String input) throws Exception {
        String url=input;
        mockMvc.perform(get(url))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());
    }
    //Testet, ob der Film, die ihm entsprechende ID hat
    @ParameterizedTest
    @ValueSource( strings =  {"San Andreas"})
    void returnExistingID(String input) throws Exception {
        Film newFilm= new Film(input);

        Assert.assertNotEquals(newFilm.getId(), filmController.oneFilmById(1));

    }

    //Testet, ob die ID für den Film neu gesetzt wurde
    @ParameterizedTest
    @ValueSource( strings =  {"GhostBusters"})
    void testIdChangesBySetID (String input) throws Exception {
        Film newFilm= new Film(input);
        int oldId =newFilm.getId();
        newFilm.setId(newFilm.getId()+1);
        Assert.assertNotEquals(oldId, newFilm.getId());
    }

    //Testet to string format
    @ParameterizedTest
    @ValueSource (strings = {"Interstellar"})
    void returnContainsJSONString(String input) throws Exception {
        Film newFilm = new Film(input);

        String s = ("{ \n" + "id: 0,\n" + "name: Interstellar\n" + "}");
        Assert.assertEquals(s, newFilm.toString());
    }
}



