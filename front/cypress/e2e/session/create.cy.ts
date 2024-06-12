describe('Session create e2e test', () => {
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
  it('create a session', () => {


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

    // CREATE


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
});
