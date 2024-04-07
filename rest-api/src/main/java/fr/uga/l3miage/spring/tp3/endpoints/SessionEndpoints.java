package fr.uga.l3miage.spring.tp3.endpoints;

import fr.uga.l3miage.spring.tp3.exceptions.SessionNotFoundResponse;
import fr.uga.l3miage.spring.tp3.exceptions.SessionStatusNotUpdatedResponse;
import fr.uga.l3miage.spring.tp3.exceptions.StepNotFoundResponse;
import fr.uga.l3miage.spring.tp3.request.SessionCreationRequest;
import fr.uga.l3miage.spring.tp3.responses.CandidateEvaluationGridResponse;
import fr.uga.l3miage.spring.tp3.responses.SessionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Tag(name = "Gestion des session")
@RestController
@RequestMapping("/api/sessions")
public interface SessionEndpoints {

    @Operation(description = "Créer une session")
    @ApiResponse(responseCode = "201",description = "La session a bien été créée")
    @ApiResponse(responseCode = "400" ,description = "La session n'a pas être créée", content = @Content(schema = @Schema(implementation = String.class),mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create")
    SessionResponse createSession(@RequestBody SessionCreationRequest request);

    // Endpoint pour modifier l'état d'une session d'examen
    @Operation(description = "Terminer une session")
    @ApiResponse(responseCode = "200",description = "Successfully updated session")
    @ApiResponse(responseCode = "404" ,description = "Session not found", content = @Content(schema = @Schema(implementation = SessionNotFoundResponse.class),mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(responseCode = "404" ,description = "No last step found", content = @Content(schema = @Schema(implementation = StepNotFoundResponse.class),mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(responseCode = "409" ,description = "Conflict", content = @Content(schema = @Schema(implementation = SessionStatusNotUpdatedResponse.class),mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{sessionId}/end")
    Set<CandidateEvaluationGridResponse> endSession(@PathVariable Long sessionId);

}
