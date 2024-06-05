describe('User e2e test', () => {

  it('Register', () => {
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

  it('Login', () => {
    let sessionUsers: Number[] = [];
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: false
      },
    })

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

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.url().should('include', '/sessions')
  })

  it('Account details', () => {
    let sessionUsers: Number[] = [];

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

    cy.get('span[routerLink=me]').click().then(()=>{
      cy.url().should('include', '/me').then(()=>{
        cy.get('p').contains("Name: John "+("Doe").toUpperCase())
        cy.get('p').contains("Email: yoga@studio.com")
      })
    })
    cy.get('button').first().click()
    cy.url().should('include', '/sessions')

    cy.get('button span').contains("Detail").click()
    cy.url().should('include', '/sessions/detail/1')
  })

  it('Participate to a session', () => {

    let sessionUsers: Number[] = [1];

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

    cy.intercept('POST', '/api/session/1/participate/1', {
      status: 200,

    })

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


    cy.get('h1').contains("Session Name").then(()=>{
      sessionUsers.push(1)
      cy.get('button span').contains("Participate").click().then(()=>{
        cy.wait(500)
        cy.get('button span').contains('Do not participate')
        cy.get('span[class=ml1]').contains("1 attendees")
      })
    })
  })

  it('Do not participate to a session', () => {
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

    cy.intercept('DELETE', '/api/session/1/participate/1', {
      status: 200,
    })

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session')

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

    cy.get('button span').contains("Do not participate").click()
    cy.get('span[class=ml1]').contains("0 attendees")
  })

  it('Logout successful', () => {
    cy.get('span[class=link]').contains("Logout").click()

    cy.url().should('eq', 'http://localhost:4200/')
  })
});
