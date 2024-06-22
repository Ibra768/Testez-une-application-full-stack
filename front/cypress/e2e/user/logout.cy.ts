describe('Logout e2e test', () => {
  it('Logout', () => {

    cy.login('yoga@studio.com','test!1234');

    // LOGOUT

    cy.get('span[class=link]').contains("Logout").click()
    // Vérifie que l'utilisateur est redirigé vers la page d'accueil
    cy.url().should('eq', 'http://localhost:4200/')
  })
});
