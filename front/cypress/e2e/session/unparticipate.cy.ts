// Début du bloc de tests pour les fonctionnalités utilisateur
describe('Unparticipate to a session e2e test', () => {
  it('Do not participate to a session', () => {
    // Intercepte la requête GET vers '/api/teacher'
    cy.intercept('GET', '/api/teacher', {
      body:
        [
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
    })

    // Intercepte la requête DELETE vers '/api/session/1/participate/1'
    cy.intercept('DELETE', '/api/session/1/participate/1', {
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
        users: [],
        createdAt: new Date(),
        updatedAt: new Date()
      }
    ).as('session')

    // Clique sur le bouton pour ne plus participer à la session
    cy.get('button span').contains("Do not participate").click()
    // Vérifie que le nombre de participants est correct
    cy.get('span[class=ml1]').contains("0 attendees")
  })
});
