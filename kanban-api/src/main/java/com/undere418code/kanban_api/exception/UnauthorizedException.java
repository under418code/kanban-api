package com.undere418code.kanban_api.exception;

/**
 * Exception utilisée lorsqu'un utilisateur tente d'accéder
 * à une ressource sans être authentifié ou autorisé.
 *
 * Elle sera interceptée par le GlobalExceptionHandler pour renvoyer
 * un code HTTP 401 (UNAUTHORIZED).
 *
 * Tu l'utiliseras lorsque tu ajouteras la sécurité JWT à ton API.
 */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }
}
