describe('User e2e login test', () => {
  it('Login', () => {
    cy.login('yoga@studio.com','test!1234');
  })
});
