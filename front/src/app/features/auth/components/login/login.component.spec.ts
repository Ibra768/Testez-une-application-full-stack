import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';
import {throwError} from "rxjs";
import {AuthService} from "../../services/auth.service";

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [SessionService],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule]
    })
      .compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // Unit test
  it('Should form value be of type LoginRequest', () => {
    // Arrange
    const expectedEmail = 'test@test.com';
    const expectedPassword = 'password';

    component.form.controls['email'].setValue(expectedEmail);
    component.form.controls['password'].setValue(expectedPassword);

    // Act
    component.submit();

    // Assert
    const loginRequest = component.form.value;
    expect(loginRequest).toBeTruthy();
    expect(loginRequest.email).toEqual(expectedEmail);
    expect(loginRequest.password).toEqual(expectedPassword);
  });

  // En cas d'erreur, la variable onError doit être à true
  it('Should set onError to true when login fails', () => {

    // On injecte le service AuthService
    const authService = TestBed.inject(AuthService);
    // On mock la méthode login pour qu'elle retourne une erreur
    jest.spyOn(authService, 'login').mockReturnValue(throwError('error'));

    // On lance la méthode submit
    component.submit();

    // On s'attend à ce que la variable onError soit à true
    expect(component.onError).toBe(true);
  });

});
