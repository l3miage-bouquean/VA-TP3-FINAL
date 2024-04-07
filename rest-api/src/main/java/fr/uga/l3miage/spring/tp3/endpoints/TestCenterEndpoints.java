package fr.uga.l3miage.spring.tp3.endpoints;

import fr.uga.l3miage.spring.tp3.exceptions.CandidatNotFoundResponse;
import fr.uga.l3miage.spring.tp3.request.TestCenterRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Tag(name="Gestion testCenter", description = "Tous les endpoints de gestion d'un testCenter")
@RestController
@RequestMapping("/api/testCenter")
public interface TestCenterEndpoints {

    @Operation(description = "Ajouter une collection d'étudiants à un centre de test")
    @ApiResponse(responseCode = "202", description = "Les étudiants ont été ajoutés avec succès")
    @ApiResponse(responseCode = "404", description = "Le centre de test ou l'étudiant n'a pas été trouvé", content = @Content(schema = @Schema(implementation =  CandidatNotFoundResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(responseCode = "400", description = "Requête incorrecte, vérification métier non passée", content = @Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping("/{centerId}/add-students")
    boolean addStudentsToTestCenter(@RequestBody TestCenterRequest testCenterAddStudentsRequest);

}
