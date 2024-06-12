describe('Partipate to a session e2e test', () => {
  it('Participate to a session', () => {


    //LOGIN

    let sessionUsers: Number[] = [];
    cy.visit('/login')

    // Intercepte la requête POST vers '/api/auth/login'
    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    })

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
          description: "Test description",
          users: [],
          createdAt: new Date(),
          updatedAt: new Date()
        }
      ]).as('session')
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
        description: "Test description",
        users: sessionUsers,
        createdAt: new Date(),
        updatedAt: new Date()
      }
    ).as('session')

    // Remplit le formulaire de connexion
    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    // Vérifie que l'utilisateur est redirigé vers la page des sessions
    cy.url().should('include', '/sessions')


    // PARTICIPATE

    let sessionUsers: Number[] = [1];

    // Intercepte la requête GET vers '/api/teacher/1'
    cy.intercept('GET', '/api/teacher/1', {
      body:
        {
          id: 1,
          lastName: "Doe",
          firstName: "John",
          createdAt: new Date(),
          updatedAt: new Date(),
        },
    })

    // Intercepte la requête POST vers '/api/session/1/participate/1'
    cy.intercept('POST', '/api/session/1/participate/1', {
      status: 200,

    })

    // Intercepte la requête GET vers '/api/session/1'
    cy.intercept(
      {
        method: 'GET',
        url: '/api/session/1',
      },
      {
        id: 1,
        name: 'A session name',
        date: new Date(),
        teacher_id: 1,
        description: "A small description",
        users: sessionUsers,
        createdAt: new Date(),
        updatedAt: new Date()
      }
    ).as('session')

    // Vérifie que le nom de la session est correct
    cy.get('h1').contains("Session Name").then(()=>{
      // Ajoute l'utilisateur à la liste des participants
      sessionUsers.push(1)
      // Clique sur le bouton pour participer à la session
      cy.get('button span').contains("Participate").click().then(()=>{
        cy.wait(500)
        // Vérifie que le bouton a changé pour indiquer que l'utilisateur participe à la session
        cy.get('button span').contains('Do not participate')
        // Vérifie que le nombre de participants est correct
        cy.get('span[class=ml1]').contains("1 attendees")
      })
    })
  })
});
