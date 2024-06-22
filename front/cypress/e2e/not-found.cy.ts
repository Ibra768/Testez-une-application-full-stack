describe('Test the not found component', () => {
  it('should redirect to the not found page', () => {
    cy.visit('/not-found');

    cy.contains('Page not found').should('be.visible');
  });
});