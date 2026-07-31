package com.undere418code.kanban_api.exception;

/**
 * Exception métier utilisée lorsqu'une règle de validation interne
 * n'est pas respectée dans l'application.
 *
 * Elle permet de signaler clairement une erreur de validation
 * depuis la couche service ou la logique métier.
 *
 * Exemple d'utilisation :
 * - Nom de board vide
 * - Valeur incorrecte dans un DTO
 * - Contrainte métier non respectée
 *
 * Cette exception sera interceptée par le GlobalExceptionHandler,
 * qui renverra automatiquement un code HTTP 400 (BAD REQUEST)
 * avec un message explicite pour le client.
 *
 * Hérite de RuntimeException pour permettre un throw sans obligation
 * de déclaration dans la signature des méthodes.
 */
public class ValidationException extends RuntimeException {

    /**
     * Constructeur permettant de transmettre un message explicite
     * décrivant la raison de l'erreur de validation.
     *
     * Ce message sera renvoyé au client via ErrorResponse.
     */
    public ValidationException(String message) {
        super(message);
    }
}
