import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';
import {SessionInformation} from "../interfaces/sessionInformation.interface";

describe('SessionService', () => {
  let service: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should be initialized with isLogged as false', () => {
    expect(service.isLogged).toBe(false);
  });

  // On login sur la base d'un objet SessionInformation, on s'attend a ce que isLogged passe à true
  // et que sessionInformation soit égale à l'objet passé initialement
  it('should set isLogged to true and update sessionInformation on logIn', () => {
    const mockSessionInformation: SessionInformation = {
      token: 'string',
      type: 'string',
      id: 1,
      username: 'John',
      firstName: 'John',
      lastName: 'Doe',
      admin: false
    };
    service.logIn(mockSessionInformation);
    expect(service.isLogged).toBe(true);
    expect(service.sessionInformation).toEqual(mockSessionInformation);
  });

  // On se déconnecte, isLogged doit passer à false et sessionInformation doit être undefined
  it('should set isLogged to false and reset sessionInformation on logOut', () => {
    service.logOut();
    expect(service.isLogged).toBe(false);
    expect(service.sessionInformation).toBeUndefined();
  });

  // On s'assure que la méthode next() met à jour le subject isLoggedSubject
  it('should return an Observable that emits the correct value of isLogged', (done) => {
    service.$isLogged().subscribe(isLogged => {
      expect(isLogged).toBe(service.isLogged);
      done();
    });
  });

});
