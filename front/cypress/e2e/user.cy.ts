// Début du bloc de tests pour les fonctionnalités utilisateur
describe('User e2e test', () => {

  // Test pour l'inscription d'un utilisateur
  it('Register', () => {
    // Visite la page d'inscription
    cy.visit('/register')

    // Intercepte la requête POST vers '/api/auth/register'
    cy.intercept('POST', '/api/auth/register', {
      statusCode: 200,
      body: {
        firstName: 'firstName',
        lastName: 'lastName',
        email: 'yoga@studio.com',
        password: 'password'
      },
    }).as('registerRequest')

    // Remplit le formulaire d'inscription
    cy.get('input[formControlName=firstName]').type("John")
    cy.get('input[formControlName=lastName]').type("Doe")
    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type("test!1234")

    // Soumet le formulaire
    cy.get('button[type=submit]').click()

    // Vérifie que la requête a été envoyée avec les bonnes données
    cy.wait('@registerRequest').its('request.body').should('deep.equal', {
      firstName: 'John',
      lastName: 'Doe',
      email: 'yoga@studio.com',
      password: 'test!1234'
    })

    // Vérifie que l'utilisateur est redirigé vers la page de connexion
    cy.url().should('include', '/login')
  })

  // Test pour la connexion d'un utilisateur
  it('Login', () => {
    let sessionUsers: Number[] = [];
    cy.visit('/login')

    // Intercepte la requête POST vers '/api/auth/login'
    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: false
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
  })

  // Test pour afficher les détails du compte d'un utilisateur
  it('Account details', () => {
    let sessionUsers: Number[] = [];

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

    // Clique sur le bouton pour afficher les détails d'une session
    cy.get('button span').contains("Detail").click()
    // Vérifie que l'utilisateur est redirigé vers la page de détails de la session
    cy.url().should('include', '/sessions/detail/1')
  })

  // Test pour participer à une session
  it('Participate to a session', () => {

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

  // Test pour ne plus participer à une session
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

  // Test pour la déconnexion d'un utilisateur
  it('Logout', () => {
    // Clique sur le lien pour se déconnecter
    cy.get('span[class=link]').contains("Logout").click()

    // Vérifie que l'utilisateur est redirigé vers la page d'accueil
    cy.url().should('eq', 'http://localhost:4200/')
  })
});
