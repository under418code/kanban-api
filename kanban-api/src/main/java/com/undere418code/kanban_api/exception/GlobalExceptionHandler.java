package com.undere418code.kanban_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Classe centrale de gestion des erreurs pour toute l'application.
 *
 * Grâce à @RestControllerAdvice, Spring intercepte automatiquement
 * toutes les exceptions lancées dans les contrôleurs REST et les redirige ici.
 *
 * L'objectif : renvoyer des réponses JSON propres, cohérentes et adaptées
 * au client (front-end, mobile, Postman, etc.), au lieu de laisser Spring
 * renvoyer des erreurs HTML ou des stacktraces illisibles.
 *
 * C'est une bonne pratique essentielle dans une API professionnelle.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Gestion des erreurs "NotFoundException".
     *
     * Cette exception est généralement lancée lorsqu'une ressource
     * (Board, Task, Column, etc.) n'existe pas en base de données.
     *
     * On renvoie un code HTTP 404 (NOT FOUND) avec un message clair.
     * Le message provient directement de l'exception personnalisée.
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ex.getMessage()));
    }

    /**
     * Gestion des erreurs de validation métier.
     *
     * Cette exception est lancée manuellement dans le code métier
     * lorsqu'une règle de validation n'est pas respectée
     * (ex : nom de board vide, valeur incorrecte, etc.).
     *
     * On renvoie un code HTTP 400 (BAD REQUEST) avec un message explicite.
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(ValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ex.getMessage()));
    }

    /**
     * Gestion des erreurs de validation Spring (@Valid).
     *
     * Cette exception est automatiquement lancée par Spring
     * lorsqu'un DTO annoté avec @Valid ne respecte pas les contraintes
     * (ex : @NotBlank, @Size, @Email, etc.).
     *
     * On récupère le message défini dans l'annotation de validation
     * et on renvoie un code HTTP 400 avec ce message.
     *
     * Exemple :
     * @NotBlank(message = "Le nom est obligatoire")
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleSpringValidation(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldError().getDefaultMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(msg));
    }

        /**
     * Gestion des erreurs d'authentification / autorisation.
     *
     * UnauthorizedException est utilisée lorsqu'un utilisateur tente
     * d'accéder à une ressource sans être authentifié ou sans avoir
     * les permissions nécessaires.
     *
     * On renvoie un code HTTP 401 (UNAUTHORIZED), ce qui indique au client
     * qu'il doit s'authentifier ou renouveler son token.
     *
     * Cette exception sera particulièrement utile lorsque tu ajouteras
     * la sécurité JWT à ton API.
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(ex.getMessage()));
    }

    /**
     * Gestion des erreurs non prévues (fallback).
     *
     * Si une exception non gérée par les handlers précédents survient,
     * cette méthode prend le relais.
     *
     * Cela évite d'exposer une stacktrace au client et garantit
     * une réponse JSON propre, même en cas d'erreur interne.
     *
     * On renvoie un code HTTP 500 (INTERNAL SERVER ERROR).
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleOther(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erreur interne du serveur"));
    }
}
