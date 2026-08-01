package com.undere418code.kanban_api.exception;

/**
 * Exception métier utilisée lorsqu'une ressource demandée
 * (Board, Task, Column, etc.) n'existe pas en base de données.
 *
 * Elle permet de remonter une erreur claire jusqu'au GlobalExceptionHandler,
 * qui renverra un code HTTP 404 au client.
 *
 * C'est une bonne pratique dans une API REST : chaque ressource introuvable
 * doit renvoyer un statut cohérent (NOT FOUND).
 */
public class NotFoundException extends RuntimeException {

    /**
     * Le message passé ici sera directement renvoyé au client
     * via ErrorResponse dans le GlobalExceptionHandler.
     */
    public NotFoundException(String message) {
        super(message);
    }
}
