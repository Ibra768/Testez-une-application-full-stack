describe('Session edit e2e test', () => {
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
  it('Edit a session', () => {

    //LOGIN


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

    // EDIT


    // Intercepte la requête GET vers '/api/teacher'
    cy.intercept('GET', '/api/teacher', {
      body:
      teachers
    })

    // Intercepte la requête POST vers '/api/session'
    cy.intercept('POST', '/api/session', {
      status: 200,

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
          name: "A session name",
          date: new Date(),
          teacher_id: 1,
          description: "A small description",
          users:[],
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
        name: "Test",
        date: new Date(),
        teacher_id: 1,
        description: "A small description",
        users:[],
        createdAt: new Date(),
        updatedAt: new Date()
      }
    ).as('session')

    // Intercepte la requête PUT vers '/api/session/1'
    cy.intercept('PUT', '/api/session/1', {
      status: 200,

    })

    // Clique sur le bouton pour modifier une session
    cy.get('button span').contains("Edit").click()

    // Vérifie que l'utilisateur est redirigé vers la page de modification de session
    cy.url().should('include', '/sessions/update/1')

    // Modifie le nom de la session
    cy.get('input[formControlName=name]').clear()
    cy.get('input[formControlName=name]').type("A session name")
    // Soumet le formulaire
    cy.get('button[type=submit]').click()

    // Vérifie que l'utilisateur est redirigé vers la page des sessions
    cy.url().should('include', '/sessions')

  })
});
