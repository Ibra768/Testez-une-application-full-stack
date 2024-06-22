import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';

import { RegisterComponent } from './register.component';
import {AuthService} from "../../services/auth.service";
import {throwError} from "rxjs";
import {RegisterRequest} from "../../interfaces/registerRequest.interface";

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // Unit test
  it('Should form value be of type RegisterRequest', () => {

    // On définit les valeurs fictives du formulaire
    const expectedEmail = 'test@test.com';
    const expectedFirstName = 'firstName';
    const expectedLastName = 'lastName';
    const expectedPassword = 'password';

    // On les bind
    component.form.controls['email'].setValue(expectedEmail);
    component.form.controls['firstName'].setValue(expectedFirstName);
    component.form.controls['lastName'].setValue(expectedLastName);
    component.form.controls['password'].setValue(expectedPassword);

    // On lance la méthode submit
    component.submit();

    // On récupère la valeur du formulaire
    const registerRequest = component.form.value;
    // On s'attend a ce que registerRequest ait toutes les propriétés de RegisterRequest
    expect(registerRequest).toEqual({
      email: expectedEmail,
      firstName: expectedFirstName,
      lastName: expectedLastName,
      password: expectedPassword
    });
  });

  // En cas d'erreur, la variable onError doit être à true
  it('Should set onError to true when login fails', () => {

    // On injecte le AuthService
    const authService = TestBed.inject(AuthService);
    // On mock la méthode register pour qu'elle retourne une erreur
    jest.spyOn(authService, 'register').mockReturnValue(throwError('Error'));

    // On lance la méthode submit
    component.submit();

    // On s'attend à ce que la variable onError soit à true
    expect(component.onError).toBe(true);
  });

});
