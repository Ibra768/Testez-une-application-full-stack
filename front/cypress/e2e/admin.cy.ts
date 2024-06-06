// Début du bloc de tests pour les fonctionnalités administrateur
describe('Admin user e2e tests', () => {

  // Liste des enseignants pour les tests
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

  // Test pour la connexion d'un administrateur
  it('Login', () => {
    // Visite la page de connexion
    cy.visit('/login')

    // Intercepte la requête POST vers '/api/auth/login'
    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'Doe',
        firstName: 'John',
        lastName: 'JohnDoe',
        admin: true
      },
    })

    // Intercepte la requête GET vers '/api/session'
    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session')

    // Remplit le formulaire de connexion
    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    // Vérifie que l'utilisateur est redirigé vers la page des sessions
    cy.url().should('include', '/sessions')
  })

  // Test pour créer une session
  it('create a session', () => {
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
          name: "A session Name",
          date: new Date(),
          teacher_id: 1,
          description: "A small description",
          createdAt: new Date(),
          updatedAt: new Date()
        }
      ]).as('session')

    // Clique sur le bouton pour créer une session
    cy.get('button[routerlink="create"]').click()

    // Vérifie que l'utilisateur est redirigé vers la page de création de session
    cy.url().should('include', '/sessions/create')

    // Remplit le formulaire de création de session
    cy.get('input[formControlName=name]').type("A session name")
    cy.get('input[formControlName=date]').type("2023-07-14")
    cy.get('mat-select[formControlName=teacher_id]').click().then(() => {
      cy.get(`.cdk-overlay-container .mat-select-panel .mat-option-text`).should('contain', teachers[0].firstName);
      cy.get(`.cdk-overlay-container .mat-select-panel .mat-option-text:contains(${teachers[0].firstName})`).first().click().then(() => {
        cy.get(`[formcontrolname=teacher_id]`).contains(teachers[0].firstName);})
    })
    cy.get('textarea[formControlName=description]').type("A small description")

    // Soumet le formulaire
    cy.get('button[type=submit]').click()

    // Vérifie que l'utilisateur est redirigé vers la page des sessions
    cy.url().should('include', '/sessions')
  })

  // Test pour modifier une session
  it('Edit a session', () => {
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

  // Test pour supprimer une session
  it('Delete a session', () => {
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

  // Test pour la déconnexion d'un administrateur
  it('Logout admin', () => {
    // Clique sur le lien pour se déconnecter
    cy.get('span[class=link]').contains("Logout").click()

    // Vérifie que l'utilisateur est redirigé vers la page d'accueil
    cy.url().should('eq', 'http://localhost:4200/')
  })

});
