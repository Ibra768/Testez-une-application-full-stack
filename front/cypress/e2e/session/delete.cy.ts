﻿describe('Session delete e2e test', () => {
  const teachers = [
    {
      id: 1,
      lastName: "Doe",
      firstName: "John",
      createdAt: new Date(),
      updatedAt: new Date(),
    },
    {
      id: 2,
      lastName: "Dupont",
      firstName: "Louis",
      createdAt: new Date(),
      updatedAt: new Date(),
    }
  ]
  it('Delete a session', () => {


    //LOGIN

    let sessionUsers: Number[] = [];

    cy.login('yoga@studio.com','test!1234');


    // DELETE


    // Intercepte la requête GET vers '/api/teacher'
    cy.intercept('GET', '/api/teacher', {
      body:
      teachers
    })

    // Intercepte la requête DELETE vers '/api/session/1'
    cy.intercept('DELETE', '/api/session/1', {
      status: 200,

    })

    // Intercepte la requête GET vers '/api/session'
    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session')

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
        users:[],
        createdAt: new Date(),
        updatedAt: new Date()
      }
    ).as('session')

    // Clique sur le bouton pour afficher les détails d'une session
    cy.get('button span').contains("Detail").click()

    // Vérifie que l'utilisateur est redirigé vers la page de détails de la session
    cy.url().should('include', '/sessions/detail/1')

    // Clique sur le bouton pour supprimer une session
    cy.get('button span').contains("Delete").click()

    // Vérifie que l'utilisateur est redirigé vers la page des sessions
    cy.url().should('include', '/sessions')
  })
});
