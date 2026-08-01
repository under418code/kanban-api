package com.undere418code.kanban_api.exception;

/**
 * Représente la structure standard d'une erreur renvoyée au client.
 *
 * Cette classe est utilisée par le GlobalExceptionHandler pour construire
 * un objet JSON propre, cohérent et lisible par le front-end.
 *
 * Exemple de réponse envoyée au client :
 * {
 *     "message": "Le nom du board est obligatoire"
 * }
 *
 * Avoir une classe dédiée permet :
 * - d'uniformiser toutes les erreurs de l'API
 * - d'éviter les stacktraces ou messages techniques exposés au client
 * - de faciliter l'évolution (ajout d'un timestamp, d'un code, d'un path, etc.)
 *
 */
public class ErrorResponse {

    /**
     * Message explicite décrivant l'erreur.
     * Il provient généralement de l'exception capturée par le GlobalExceptionHandler.
     */
    private String message;

    /**
     * Constructeur permettant d'initialiser le message d'erreur.
     * Le GlobalExceptionHandler l'utilise pour renvoyer une réponse JSON propre.
     */
    public ErrorResponse(String message) {
        this.message = message;
    }

    /**
     * Getter permettant au framework (Jackson) de sérialiser le message
     * dans la réponse JSON envoyée au client.
     */
    public String getMessage() {
        return message;
    }
}
