describe('Register spec', () => {
  it('Register successfull', () => {
    // Visiter la page d'inscription
    cy.visit('/register')

    // Intercepter la requête POST vers '/api/auth/register'
    cy.intercept('POST', '/api/auth/register', {
      statusCode: 200,
      body: {
        firstName: 'firstName',
        lastName: 'lastName',
        email: 'yoga@studio.com',
        password: 'password'
      },
    }).as('registerRequest')

    // Remplir le formulaire d'inscription
    cy.get('input[formControlName=firstName]').type("John")
    cy.get('input[formControlName=lastName]').type("Doe")
    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type("test!1234")

    // Soumettre le formulaire
    cy.get('button[type=submit]').click()

    // Vérifier que la requête a été envoyée avec les bonnes données
    cy.wait('@registerRequest').its('request.body').should('deep.equal', {
      firstName: 'John',
      lastName: 'Doe',
      email: 'yoga@studio.com',
      password: 'test!1234'
    })

    // Vérifier que l'utilisateur est redirigé vers la page de connexion
    cy.url().should('include', '/login')
  })
})
