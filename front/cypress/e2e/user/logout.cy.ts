describe('Logout e2e test', () => {
  it('Logout', () => {
    // Todo : rajouter la command login
    cy.get('span[class=link]').contains("Logout").click()
    // Vérifie que l'utilisateur est redirigé vers la page d'accueil
    cy.url().should('eq', 'http://localhost:4200/')
  })
});
