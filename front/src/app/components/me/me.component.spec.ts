import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';
import { expect } from '@jest/globals';

import { MeComponent } from './me.component';
import {UserService} from "../../services/user.service";
import {of} from "rxjs";
import {User} from "../../interfaces/user.interface";
import {Router} from "@angular/router";

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    },
    logOut: jest.fn()
  }
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [{ provide: SessionService, useValue: mockSessionService }],
    })
      .compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('Should call history.back on back', () => {
    // On écoute la méthode back de l'objet window.history
    const spy = jest.spyOn(window.history, 'back');
    // On lance la méthode back du composant
    component.back();
    // On s'attend à ce que la méthode back de l'objet window.history ait été appelée
    expect(spy).toHaveBeenCalled();
  });

  it('Should call userService.getById with the correct id on ngOnInit and return a User object', () => {
    // On injecte le service UserService
    const userService = TestBed.inject(UserService);
    // On crée un mock d'utilisateur
    const mockUser: User = {
      id: 1,
      email: 'test@test.com',
      lastName: 'Test',
      firstName: 'User',
      admin: true,
      password: 'password',
      createdAt: new Date(),
      updatedAt: new Date()
    };
    // On écoute la méthode getById du service UserService et on retourne une Observable contenant le mock d'utilisateur
    const spy = jest.spyOn(userService, 'getById').mockReturnValue(of(mockUser));

    // On lance la méthode
    component.ngOnInit();

    // On s'attend à ce que la méthode getById ait été appelée avec l'id de la session
    expect(spy).toHaveBeenCalledWith(mockSessionService.sessionInformation.id.toString());
    // On s'attend à ce que l'utilisateur du composant soit égal au mock d'utilisateur
    expect(component.user).toEqual(mockUser);
  });

});

