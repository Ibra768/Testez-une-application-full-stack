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

  it('Should form value be of type RegisterRequest', () => {

    // Arrange
    const expectedEmail = 'test@test.com';
    const expectedFirstName = 'firstName';
    const expectedLastName = 'lastName';
    const expectedPassword = 'password';

    component.form.controls['email'].setValue(expectedEmail);
    component.form.controls['firstName'].setValue(expectedFirstName);
    component.form.controls['lastName'].setValue(expectedLastName);
    component.form.controls['password'].setValue(expectedPassword);

    // Act
    component.submit();

    // Assert
    const registerRequest = component.form.value;
    expect(registerRequest).toBeTruthy();
    expect(registerRequest.email).toEqual(expectedEmail);
    expect(registerRequest.firstName).toEqual(expectedFirstName);
    expect(registerRequest.lastName).toEqual(expectedLastName);
    expect(registerRequest.password).toEqual(expectedPassword);

  });

  // En cas d'erreur, la variable onError doit être à true
  it('Should set onError to true when login fails', () => {

    // Arrange
    const authService = TestBed.inject(AuthService);
    jest.spyOn(authService, 'register').mockReturnValue(throwError('error'));

    // Act
    component.submit();

    // Assert
    expect(component.onError).toBe(true);
  });

});
