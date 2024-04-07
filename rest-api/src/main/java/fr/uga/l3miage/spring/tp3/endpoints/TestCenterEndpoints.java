package fr.uga.l3miage.spring.tp3.endpoints;

import fr.uga.l3miage.spring.tp3.request.TestCenterRequest;
import fr.uga.l3miage.spring.tp3.request.SessionCreationRequest;
import fr.uga.l3miage.spring.tp3.request.TestCenterRequest;
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

@Tag(name = "Gestion des test centers")
@RestController
@RequestMapping("/api/centers")
public interface TestCenterEndpoints {

    @Operation(description = "Ajouter à un centre de test une collection d'étudiants")
    @ApiResponse(responseCode = "202",description = "Candidate collection successfully added")
    @ApiResponse(responseCode = "404" ,description = "Test center not found", content = @Content(schema = @Schema(implementation = String.class),mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(responseCode = "400" ,description = "Bad request : candidate collection can't be empty", content = @Content(schema = @Schema(implementation = String.class),mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping("/{centerId}/addCandidates")
    SessionResponse addCandidatesToTestCenter(@RequestBody Set<TestCenterRequest> request);
}