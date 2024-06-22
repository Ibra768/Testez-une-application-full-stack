describe('User e2e me test', () => {
  it('Me', () => {

    let sessionUsers: Number[] = [];
    cy.login('yoga@studio.com','test!1234');

    // ME


    // Intercepte la requête GET vers '/api/user/1'
    cy.intercept(
      {
        method: 'GET',
        url: '/api/user/1',
      },
      {
        id: 1,
        username: 'JohnDoe',
        firstName: 'John',
        lastName: 'Doe',
        email: "yoga@studio.com",
        admin: false,
        password: "password",
        createdAt: new Date(),
        updatedAt: new Date()

      },
    ).as('user')

    // Intercepte la requête GET vers '/api/session'
    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      [
        {
          id: 1,
          name: 'Session name',
          date: new Date(),
          teacher_id: 1,
          description: "A small description",
          users: [],
          createdAt: new Date(),
          updatedAt: new Date()
        }
      ]).as('session')

    // Intercepte la requête GET vers '/api/session/1'
    cy.intercept(
      {
        method: 'GET',
        url: '/api/session/1',
      },
      {
        id: 1,
        name: 'Session name',
        date: new Date(),
        teacher_id: 1,
        description: "A small description",
        users: sessionUsers,
        createdAt: new Date(),
        updatedAt: new Date()
      }
    ).as('session')

    // Clique sur le lien pour afficher les détails du compte
    cy.get('span[routerLink=me]').click().then(()=>{
      // Vérifie que l'utilisateur est redirigé vers la page de détails du compte
      cy.url().should('include', '/me').then(()=>{
        // Vérifie que les détails du compte sont corrects
        cy.get('p').contains("Name: John "+("Doe").toUpperCase())
        cy.get('p').contains("Email: yoga@studio.com")
      })
    })
    // Clique sur le premier bouton pour revenir à la page des sessions
    cy.get('button').first().click()
    // Vérifie que l'utilisateur est redirigé vers la page des sessions
    cy.url().should('include', '/sessions')
  })
});
